package org.cmdline.ackr.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.cmdline.ackr.Email
import org.cmdline.ackr.Folder
import org.cmdline.ackr.R

class HomeFragment : Fragment() {
    private lateinit var vm: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        vm = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val folderAdapter = FolderAdapter(inflater)
        root.folder_list.adapter = folderAdapter

        vm.folders.observe(viewLifecycleOwner, Observer {
            folderAdapter.folders = it

            it.forEach {
                it.emails.observe(viewLifecycleOwner, Observer {
                    folderAdapter.notifyDataSetChanged()
                })
            }

            folderAdapter.notifyDataSetChanged()
        })

        vm.connstate.observe(viewLifecycleOwner, Observer {
            val sb: Snackbar = Snackbar.make(root, "", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
            if (it == 0) {
                sb.setText("Connection failed")
            } else if (it == 1) {
                sb.setText("Server connected!")
            }
            sb.show()
        })


        root.folder_list.setOnItemClickListener { view, _, position, _ ->
            folderAdapter.folders.forEach { it.open = false }
            val folder = folderAdapter.getItem(position) as Folder
            folder.open = true
            
            vm.fetchMail(folder.name)
            folderAdapter.notifyDataSetChanged()
        }


        root.fetch.setOnClickListener {
            requireActivity().getSharedPreferences("ackr", Context.MODE_PRIVATE).run {
                val server = getString("server", "")!!
                val email = getString("email_address", "")!!
                val password = getString("password", "")!!
                if (listOf(server, email, password).any { it.isEmpty() }) {
                    return@setOnClickListener
                }


                vm.testServer(server, email, password)
                vm.fetchFolders(server, email, password)
            }
        }

        return root
    }

}
