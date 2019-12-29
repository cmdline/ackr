package org.cmdline.ackr.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.email_list_item.view.*
import org.cmdline.ackr.Email
import org.cmdline.ackr.R

class EmailAdapter(private val inflater: LayoutInflater) : BaseAdapter() {
    var emails: List<Email> = listOf()

    override fun getCount(): Int = emails.size
    override fun getItem(position: Int): Any = emails[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val vh: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.email_list_item, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }

        val email = emails[position]
        vh.from.text = email.from
        vh.subject.text = email.subject

        return view
    }

    private class ViewHolder(row: View) {
        val from: TextView = row.from
        val subject: TextView = row.subject
    }
}
