package org.cmdline.ackr.ui

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.text.parseAsHtml
import kotlinx.android.synthetic.main.email_list_item.view.*
import org.cmdline.ackr.Email
import org.cmdline.ackr.R

class EmailAdapter(private val inflater: LayoutInflater) : BaseAdapter() {
    var emails: List<Email> = listOf()

    override fun getCount(): Int = emails.size
    override fun getItem(position: Int): Any = emails[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // FIXME this isn't how Recycling works
        val r = R.layout.email_list_item
        val view = inflater.inflate(r, parent, false)

        val vh = ViewHolderCommon(view)
        val email = emails[position]
        vh.from.text = email.from
        vh.subject.text = email.subject
        vh.body?.text = email.body.parseAsHtml().toString()
        vh.recv_date.text = email.recv_date.toString()

        view.tag = vh

        view.body.visibility = if (email.open) {
            View.VISIBLE
        } else {
            View.GONE
        }

        if (email.read) {
            view.from.setTypeface(null, Typeface.NORMAL)
            view.subject.setTypeface(null, Typeface.NORMAL)
        } else {
            view.from.setTypeface(null, Typeface.BOLD)
            view.subject.setTypeface(null, Typeface.BOLD)
        }

        return view
    }

    private class ViewHolderCommon(view: View) {
        val subject: TextView = view.subject
        val from: TextView = view.from
        val recv_date: TextView = view.recv_date
        val body: TextView? = view.body
    }
}
