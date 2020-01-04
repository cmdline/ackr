package org.cmdline.ackr.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_tools.view.*
import org.cmdline.ackr.R

class PreferenceListener(
    private val activity: Activity, private val preference: String
) : View.OnFocusChangeListener {
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        require(v is EditText)

        if (hasFocus) return
        activity.getSharedPreferences("ackr", Context.MODE_PRIVATE).edit()
            .putString(preference, v.text.toString())
            .apply()
    }
}

class ToolsFragment : Fragment() {

    private lateinit var toolsViewModel: ToolsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        toolsViewModel =
            ViewModelProviders.of(this).get(ToolsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tools, container, false)

        requireActivity().getSharedPreferences("ackr", Context.MODE_PRIVATE).run {
            root.server.setText(getString("server", ""))
            root.email_address.setText(getString("email_address", ""))
            root.password.setText(getString("password", "")) // FIXME: Password storage?
        }

        root.server.onFocusChangeListener = PreferenceListener(requireActivity(), "server")
        root.email_address.onFocusChangeListener = PreferenceListener(requireActivity(), "email_address")
        root.password.onFocusChangeListener = PreferenceListener(requireActivity(), "password")

        return root
    }
}
