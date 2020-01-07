package org.cmdline.ackr.ui

import android.graphics.Typeface
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
        } else {
            r = R.layout.email_list_item
            view = inflater.inflate(r, parent, false)
        }

        val vh = ViewHolderCommon(view, email)
        vh.from.text = email.from
        vh.subject.text = email.subject
        vh.body?.text = email.body
        vh.recv_date.text = email.recv_date

        view.tag = vh

        return view
    }

    private class ViewHolderCommon(view: View, email: Email) {
        val subject: TextView = view.subject
        val from: TextView = view.from
        val recv_date: TextView = view.recv_date
        val body: TextView? = view.body

        init {
            if (!email.read) {
                from.setTypeface(null, Typeface.BOLD);
                subject.setTypeface(null, Typeface.BOLD);
            }
        }
    }

}
