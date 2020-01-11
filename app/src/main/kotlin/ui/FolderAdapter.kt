package org.cmdline.ackr.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.folder_list_item.view.*

import org.cmdline.ackr.Folder
import org.cmdline.ackr.R

class FolderAdapter(private val inflater: LayoutInflater) : BaseAdapter() {
    var folders: List<Folder> = listOf()

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

        view.tag = vh

        return view
    }

    private class ViewHolder(view: View) {
        val folder_name: TextView = view.folder_name
    }

}
