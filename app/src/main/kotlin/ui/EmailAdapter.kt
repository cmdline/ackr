package org.cmdline.ackr.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.email_list_item_open.view.*

import org.cmdline.ackr.Email
import org.cmdline.ackr.R

class EmailAdapter(private val inflater: LayoutInflater) : BaseAdapter() {
    var emails: List<Email> = listOf()

    override fun getCount(): Int = emails.size
    override fun getItem(position: Int): Any = emails[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val r: Int
        val email = emails[position]


        // FIXME this isn't how Recycling works
        if (email.open) {
            r = R.layout.email_list_item_open
            view = inflater.inflate(r, parent, false)
            val vh = ViewHolderOpen(view)
            vh.from.text = email.from
            vh.subject.text = email.subject
            vh.body.text = email.body

            view.tag = vh
        } else {
            r = R.layout.email_list_item
            view = inflater.inflate(r, parent, false)
            val vh = ViewHolderClosed(view)
            vh.from.text = email.from
            vh.subject.text = email.subject

            view.tag = vh
        }

        return view
    }

    private class ViewHolderClosed(row: View) {
        val from: TextView = row.from
        val subject: TextView = row.subject

    }
    private class ViewHolderOpen(row: View) {
        val from: TextView = row.from
        val subject: TextView = row.subject
        val body: TextView = row.body
    }
}
