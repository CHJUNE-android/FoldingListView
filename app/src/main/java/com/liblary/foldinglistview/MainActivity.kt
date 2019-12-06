package com.liblary.foldinglistview

import android.content.ClipData
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.liblary.foldinglistview.fold.FoldingCell
import com.liblary.foldinglistview.fold.FoldingCellListAdapter
import com.liblary.foldinglistview.fold.ViewItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init(){
        // prepare elements to display
        val items = ViewItem.testingList
        //아이템 역할 -

        // add custom btn handler to first list item
        items.get(0).requestBtnClickListener = View.OnClickListener {  Toast.makeText(
            applicationContext,
            "CUSTOM HANDLER FOR FIRST BUTTON",
            Toast.LENGTH_SHORT
        ).show() }

        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        val adapter = FoldingCellListAdapter(
            this,
            items as List<ClipData.Item>
        )

        // add default btn handler for each request btn on each item if custom handler not found
        adapter.defaultRequestBtnClickListener = View.OnClickListener {
            Toast.makeText(
            applicationContext,
            "DEFAULT HANDLER FOR ALL BUTTONS",
            Toast.LENGTH_SHORT
        ).show() }

        // set elements to adapter
        mainListView.setAdapter(adapter)

        // set on click event listener to list view
        mainListView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, position, l ->
            // toggle clicked cell state
            (view as FoldingCell).toggle(false)
            // register in adapter that state for selected cell is toggled
            adapter.registerToggle(position)//해당 아이템이 접혔는지 펼쳐졌는지 adapter가 알 수 있도록 처리
        })
    }
}

