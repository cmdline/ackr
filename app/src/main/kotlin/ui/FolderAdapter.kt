package org.cmdline.ackr.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.folder_list_item.view.*
import org.cmdline.ackr.Email

import org.cmdline.ackr.Folder
import org.cmdline.ackr.R

class FolderAdapter(private val inflater: LayoutInflater) : BaseAdapter() {
    var folders: List<Folder> = listOf()
    var emails: List<Email> = listOf()

    override fun getCount(): Int = folders.size
    override fun getItem(position: Int): Any = folders[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val r: Int
        val folder = folders[position]


        // FIXME this isn't how Recycling works
        if (folder.open) {
            r = R.layout.folder_list_item
            view = inflater.inflate(r, parent, false)
        } else {
            r = R.layout.folder_list_item
            view = inflater.inflate(r, parent, false)
        }

        val vh = ViewHolder(view)
        vh.folder_name.text = folder.name



        if (folder.open) {
            val emailAdapter = EmailAdapter(inflater)
            vh.email_list.adapter = emailAdapter

            vh.email_list.setOnItemClickListener { _, _, position, _ ->
                val touched = (emailAdapter.getItem(position) as Email)
                val plsclose: Boolean = (touched.open == true)
                emailAdapter.emails.forEach { it.open = false }

                if (plsclose) {
                    touched.read = true
                } else {
                    touched.open = true
                }
                emailAdapter.notifyDataSetChanged()

                if (plsclose) {
                    vh.email_list.smoothScrollToPosition(position)
                }
            }
            emailAdapter.emails = emails
            emailAdapter.notifyDataSetChanged()
            vh.email_list.visibility = View.VISIBLE
            vh.folder_status.visibility = View.VISIBLE
        } else {
            vh.folder_status.visibility = View.GONE
            vh.email_list.visibility = View.GONE
        }


        // todo move this

        vh.email_refresh.setOnRefreshListener {
            // @robinlinden will fix this
            //            requireActivity().getSharedPreferences("ackr", Context.MODE_PRIVATE).run {
//                if (vm.isRefreshing) return@setOnRefreshListener
//
//                val server = getString("server", "")!!
//                val email = getString("email_address", "")!!
//                val password = getString("password", "")!!
//                if (listOf(server, email, password).any { it.isEmpty() }) {
//                    return@setOnRefreshListener
//                }
//
//                GlobalScope.launch {
//                    vm.syncMail(server, email, password)
//                    withContext(Dispatchers.Main) {
//                        vh.email_refresh.isRefreshing = false
//                    }
//                }
//            }
        }


        view.tag = vh
        return view
    }

    private class ViewHolder(view: View) {
        val folder_name: TextView = view.folder_name
        val folder_status: TextView = view.folder_status
        val email_list: ListView = view.email_list
        val email_refresh: androidx.swiperefreshlayout.widget.SwipeRefreshLayout = view.email_refresh
    }

}
