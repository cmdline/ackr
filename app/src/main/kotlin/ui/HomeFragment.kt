package org.cmdline.ackr.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.cmdline.ackr.Email
import org.cmdline.ackr.R

class HomeFragment : Fragment() {
    private lateinit var vm: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val emailAdapter = EmailAdapter(inflater)
        root.email_list.adapter = emailAdapter
        vm.mail.observe(viewLifecycleOwner, Observer {
            emailAdapter.emails = it
            emailAdapter.notifyDataSetChanged()
        })

        root.email_list.setOnItemClickListener { _, _, position, _ ->
            emailAdapter.emails.forEach { it.open = false }
            (emailAdapter.getItem(position) as Email).open = true
            emailAdapter.notifyDataSetChanged()
        }

        root.fetch.setOnClickListener {
            val host = root.host.text.toString()
            val user = root.user.text.toString()
            val password = root.password.text.toString()
            if (listOf(host, user, password).any { it.isEmpty() }) {
                return@setOnClickListener
            }

            vm.fetchMail(host, user, password)
        }

        return root
    }
}
