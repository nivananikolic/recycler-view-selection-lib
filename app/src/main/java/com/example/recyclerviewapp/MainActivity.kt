package com.example.recyclerviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var listOfItems = ArrayList<Item>()
    private var selectionTracker: SelectionTracker<Long>? = null
    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rvMainRecyclerView)
//        actionMode = startSupportActionMode(ActionModeCallback())

        for (i in 0..32) {
            listOfItems.add(Item(i, "Item $i"))
        }

        val recyclerViewAdapter = RecyclerViewAdapter(listOfItems)
        recyclerView.adapter = recyclerViewAdapter

        selectionTracker = SelectionTracker.Builder<Long>(
            "Itemselection",
            recyclerView,
            MyItemKeyProvider(recyclerView),
            MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).build()

        selectionTracker?.addObserver(
            object: SelectionTracker.SelectionObserver<Long>() {

                override fun onItemStateChanged(key: Long, selected: Boolean) {
                    super.onItemStateChanged(key, selected)
                    Log.d("MainActivity", "onItemsStateChanged")
                }

                override fun onSelectionRefresh() {
                    super.onSelectionRefresh()
                    Log.d("MainActivity", "onSelectionRefresh")
                }

                override fun onSelectionChanged() {
                    super.onSelectionChanged()

                    if (actionMode == null) {
                        actionMode = startSupportActionMode(recyclerViewAdapter)
                    }

                    val itemsSelected = selectionTracker?.selection?.size()

                    if (itemsSelected!! > 0) {
                        actionMode?.title = "$itemsSelected items selected"
                    } else {
                        actionMode?.finish()
                        actionMode = null
                    }
                    Log.d("MainActivity", "$itemsSelected items selected")
                }

                override fun onSelectionRestored() {
                    super.onSelectionRestored()
                    Log.d("MainActivity", "onSelectionRestored")
                }
            }
        )

        recyclerViewAdapter.tracker = selectionTracker
    }
}