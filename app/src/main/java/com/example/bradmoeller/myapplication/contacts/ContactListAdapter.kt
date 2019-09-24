package com.example.bradmoeller.myapplication.contacts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.bradmoeller.myapplication.R

/**
 * Created by bradmoeller on 1/17/18.
 */
class ContactListAdapter(val items: List<ContactsActivity.ContactItem>, val listener: (ContactsActivity.ContactItem) -> Unit)
    : RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var textView: TextView? = null;

        init {
            textView = itemView as TextView;
        }

        fun bind(item: ContactsActivity.ContactItem, listener: (ContactsActivity.ContactItem) -> Unit) {
            var tv: TextView = itemView as TextView
            tv.text = item.name + "\n" + item.phone;
            tv.setOnClickListener { listener(item) }
        }
    }
}