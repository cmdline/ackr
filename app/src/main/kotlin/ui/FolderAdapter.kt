package org.cmdline.ackr.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.folder_list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.cmdline.ackr.Email

import org.cmdline.ackr.Folder
import org.cmdline.ackr.R
import org.cmdline.ackr.db.EmailRepository

class FolderAdapter(private val inflater: LayoutInflater, private val ctx: Context): BaseAdapter() {
    var folders: List<Folder> = listOf()
    var emails: List<Email> = listOf()
    val eadapter: EmailAdapter = EmailAdapter(inflater)
    var isRefreshing = false
    val er = EmailRepository(ctx)
    var server: String = ""
    var email: String = ""
    var passwd: String = ""

    init {
        update_creds()
    }

    fun update_creds() {
        ctx.getSharedPreferences("ackr", Context.MODE_PRIVATE).run {
            server = getString("server", "")!!
            email = getString("email_address", "")!!
            passwd = getString("password", "")!!
        }
    }

    fun emailClickListener(pos: Int): Boolean {
        val touched = (eadapter.getItem(pos) as Email)
        val plsclose: Boolean = (touched.open == true)
        eadapter.emails.forEach { it.open = false }

        if (plsclose) {
            touched.read = true
        } else {
            touched.open = true
        }
        eadapter.notifyDataSetChanged()
        return plsclose

    }

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
            vh.email_list.adapter = eadapter

            vh.email_list.setOnItemClickListener { _, _, p, _ ->
                if (emailClickListener(p)) {
                    vh.email_list.smoothScrollToPosition(p)
                }
            }
            eadapter.emails = emails
            eadapter.notifyDataSetChanged()

            vh.email_refresh.visibility = View.VISIBLE

            vh.email_list.layoutParams.height = 1800
            vh.email_refresh.layoutParams.height = 1800
        } else {
            vh.email_refresh.visibility = View.GONE
        }


        // todo move this

        vh.email_refresh.setOnRefreshListener {
            if (isRefreshing) return@setOnRefreshListener
            if (listOf(server, email, passwd).any { it.isEmpty() }) {
                update_creds()
            }

            GlobalScope.launch {
                er.sync_email(server, email, passwd)
                withContext(Dispatchers.Main) {
                    vh.email_refresh.isRefreshing = false
                }
            }
        }


        view.tag = vh
        return view
    }

    private class ViewHolder(view: View) {
        val folder_name: TextView = view.folder_name
        val email_list: ListView = view.email_list
        val email_refresh: SwipeRefreshLayout = view.email_refresh
    }

}
