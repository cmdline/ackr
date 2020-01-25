package org.cmdline.ackr.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.cmdline.ackr.Email
import org.cmdline.ackr.Folder
import org.cmdline.ackr.R

class HomeFragment : Fragment() {
    private lateinit var vm: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
    ): View? {
        vm = ViewModelProviders.of(this).get(HomeViewModel::class.java).apply {
            ctx = requireContext()
        }

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val folderAdapter = FolderAdapter(inflater)
        root.folder_list.adapter = folderAdapter

//        ViewCompat.setNestedScrollingEnabled(root.folder_list, true);
        vm.folder.observe(viewLifecycleOwner, Observer {
            folderAdapter.folders = it
            folderAdapter.notifyDataSetChanged()
        })

        root.folder_list.setOnItemClickListener { _, _, position, _ ->
            val touched = (folderAdapter.getItem(position) as Folder)
            val plsclose: Boolean = (touched.open == true)
            folderAdapter.folders.forEach { it.open = false }

            if (plsclose) {
                vm.search = ""
            } else {
                touched.open = true
                vm.search = touched.name
            }
            folderAdapter.notifyDataSetChanged()

            val filtered = vm.get_search(touched.name)
            if (filtered != null) {
                folderAdapter.emails = filtered
            }

            if (plsclose) {
                root.folder_list.smoothScrollToPosition(position)
            }
        }


        return root
    }
}
