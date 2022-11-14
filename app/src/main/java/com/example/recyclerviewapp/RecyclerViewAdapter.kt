package com.example.recyclerviewapp

import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    val dataSet: ArrayList<Item>
): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(), ActionMode.Callback {

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.tvRecyclerViewItemTitle)
        }

        fun getItemDetails(): ItemDetails<Long> {
            return object : ItemDetails<Long>() {
                override fun getPosition(): Int {
                    return bindingAdapterPosition
                }

                override fun getSelectionKey(): Long {
                    return itemId
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
            .from(parent.context).inflate(R.layout.recycler_view_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataSet[position]

        holder.textView.text = currentItem.value
        holder.itemView.isActivated = tracker!!.isSelected(getItemId(position))
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id.toLong()
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.action_mode_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        Log.d("MainActivity", "onPrepareActionMode triggered")
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.iDelete -> {
                Log.d("MainActivity", "DELETE BUTTON CLICKED")
                tracker!!.selection.forEach {
                    dataSet.remove(dataSet.single { s ->
                        s.id == it.toInt()
                    })
                }
                notifyDataSetChanged()
                tracker!!.clearSelection()
                return true
            }
        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        Log.d("MainActivity", "Action mode destroyed")
        if (tracker!!.hasSelection()) {
            tracker!!.clearSelection()
        }
    }
}