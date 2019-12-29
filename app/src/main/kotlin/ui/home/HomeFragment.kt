package org.cmdline.ackr.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.cmdline.ackr.Email
import org.cmdline.ackr.R
import org.cmdline.ackr.ui.EmailAdapter

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })

        root.email_list.adapter = EmailAdapter(inflater).apply {
            emails = listOf(
                Email("fake@email.ackr", "Save on your student loans NOW!"),
                Email("DevBot", "Top 10 ways C is better, number 6 will STUN you!"),
                Email("e0f", "Free newsletters about top 10 lists, subscribe today!")
            )
        }

        return root
    }
}
