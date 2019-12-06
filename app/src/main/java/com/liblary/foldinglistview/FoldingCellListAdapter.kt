package com.liblary.foldinglistview

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.HashSet

class FoldingCellListAdapter(context: Context, objects: List<ClipData.Item>) : ArrayAdapter<ClipData.Item>(context, 0, objects) {

    private val unfoldedIndexes = HashSet<Int>()
    var defaultRequestBtnClickListener: View.OnClickListener? = null
    lateinit var cell : FoldingCell

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // get item for selected view
        val item : Item = getItem(position) as Item
        // if cell is exists - reuse it, if not - create the new one from resource
        val viewHolder: ViewHolder
        if(convertView !=null){
            cell = convertView!! as FoldingCell
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true)
            } else {
                cell.fold(true)
            }
            viewHolder = cell.tag as ViewHolder
        }else{
            viewHolder = ViewHolder()
            val vi = LayoutInflater.from(context)
            cell = vi.inflate(R.layout.cell, parent, false) as FoldingCell
            // binding view parts to view holder
            viewHolder.price = cell.findViewById(R.id.title_price)
            viewHolder.time = cell.findViewById(R.id.title_time_label)
            viewHolder.date = cell.findViewById(R.id.title_date_label)
            viewHolder.fromAddress = cell.findViewById(R.id.title_from_address)
            viewHolder.toAddress = cell.findViewById(R.id.title_to_address)
            viewHolder.requestsCount = cell.findViewById(R.id.title_requests_count)
            viewHolder.pledgePrice = cell.findViewById(R.id.title_pledge)
            viewHolder.contentRequestBtn = cell.findViewById(R.id.content_request_btn)
            cell.tag = viewHolder
        }


        if (null == item)
            return cell

        // bind data from selected element to view through view holder
        viewHolder.price!!.setText(item!!.price)
        viewHolder.time!!.setText(item!!.time)
        viewHolder.date!!.setText(item!!.date)
        viewHolder.fromAddress!!.setText(item!!.fromAddress)
        viewHolder.toAddress!!.setText(item!!.toAddress)
        viewHolder.requestsCount!!.setText((item!!.requestsCount).toString())
        viewHolder.pledgePrice!!.setText(item!!.pledgePrice)

        // set custom btn handler for list item from that item
        if (item!!.requestBtnClickListener != null) {
            viewHolder.contentRequestBtn!!.setOnClickListener(item!!.requestBtnClickListener)
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.contentRequestBtn!!.setOnClickListener(defaultRequestBtnClickListener)
        }

        return cell
    }

    // simple methods for register cell state changes
    fun registerToggle(position: Int) {
        if (unfoldedIndexes.contains(position))
            registerFold(position)
        else
            registerUnfold(position)
    }

    fun registerFold(position: Int) {
        unfoldedIndexes.remove(position)
    }

    fun registerUnfold(position: Int) {
        unfoldedIndexes.add(position)
    }

    // View lookup cache
    private class ViewHolder {
        internal var price: TextView? = null
        internal var contentRequestBtn: TextView? = null
        internal var pledgePrice: TextView? = null
        internal var fromAddress: TextView? = null
        internal var toAddress: TextView? = null
        internal var requestsCount: TextView? = null
        internal var date: TextView? = null
        internal var time: TextView? = null
    }
}
