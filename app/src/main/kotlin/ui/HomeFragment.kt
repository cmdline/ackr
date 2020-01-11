package org.cmdline.ackr.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.cmdline.ackr.Email
import org.cmdline.ackr.R

class HomeFragment : Fragment() {
    private lateinit var vm: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = ViewModelProviders.of(this).get(HomeViewModel::class.java).apply {
            ctx = requireContext()
        }

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val emailAdapter = EmailAdapter(inflater)
        root.email_list.adapter = emailAdapter
        vm.mail.observe(viewLifecycleOwner, Observer {
            emailAdapter.emails = it
            emailAdapter.notifyDataSetChanged()
        })

        root.email_list.setOnItemClickListener { _, _, position, _ ->
            val touched = (emailAdapter.getItem(position) as Email)
            val plsclose: Boolean = (touched.open == true)
            emailAdapter.emails.forEach { it.open = false }


            if (plsclose) {
                touched.read = true
            } else {
                touched.open = true
            }
            emailAdapter.notifyDataSetChanged()
        }

        root.email_swipe_refresh.setOnRefreshListener {
            requireActivity().getSharedPreferences("ackr", Context.MODE_PRIVATE).run {
                if (vm.isRefreshing) return@setOnRefreshListener

                val server = getString("server", "")!!
                val email = getString("email_address", "")!!
                val password = getString("password", "")!!
                if (listOf(server, email, password).any { it.isEmpty() }) {
                    return@setOnRefreshListener
                }

                GlobalScope.launch {
                    vm.syncMail(server, email, password)
                    withContext(Dispatchers.Main) {
                        root.email_swipe_refresh.isRefreshing = false
                    }
                }
            }
        }

        return root
    }
}
