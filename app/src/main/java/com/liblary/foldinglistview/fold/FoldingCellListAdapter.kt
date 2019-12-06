package com.liblary.foldinglistview.fold

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.liblary.foldinglistview.R
import java.util.HashSet

class FoldingCellListAdapter(context: Context, objects: List<ClipData.Item>) : ArrayAdapter<ClipData.Item>(context, 0, objects) {

    private val unfoldedIndexes = HashSet<Int>()//펼쳐진 부분 index를 HashSet형태로 저장 - 중복 불가, 순서 없음, 값만 저장되는 형태.
    var defaultRequestBtnClickListener: View.OnClickListener? = null
    lateinit var cell : FoldingCell




    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // get item for selected view
        val item : ViewItem = getItem(position) as ViewItem//getItem : ArrayAdapter상속 받아 사용 가능. 해당 배열의 position에 해당하는 item 반환. 이를 ViewItem타입으로 변환하여 item이라는 변수에 저장.
        // if cell is exists - reuse it, if not - create the new one from resource
        val viewHolder: ViewHolder//viewHolder는 adapter를 arraylist의 개수만큼 여러번 findViewById 할 필요 없도록 해줌
        if(convertView !=null){//현재 화면에 그려져있는 뷰가 convertView(이를 재사용하여 불필요한 뷰 그리는 작업을 줄인다)
            cell = convertView as FoldingCell
            if (unfoldedIndexes.contains(position)) {//해당 뷰가 펼쳐져있다면
                cell.unfold(true)//펼쳐버린다
            } else {//접혀있다면
                cell.fold(true)//접는다
            }
            viewHolder = cell.tag as ViewHolder // viewHolder는 현재 존재하는 뷰 태그에 저장된 객체(Viewholder타입)
        }else{//그려져있는 뷰가 없다면
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


        // bind data from selected element to view through view holder
        viewHolder.price!!.setText(item.price)
        viewHolder.time!!.setText(item.time)
        viewHolder.date!!.setText(item.date)
        viewHolder.fromAddress!!.setText(item.fromAddress)
        viewHolder.toAddress!!.setText(item.toAddress)
        viewHolder.requestsCount!!.setText((item.requestsCount).toString())
        viewHolder.pledgePrice!!.setText(item.pledgePrice)

        // set custom btn handler for list item from that item
        if (item.requestBtnClickListener != null) {
            viewHolder.contentRequestBtn!!.setOnClickListener(item.requestBtnClickListener)
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.contentRequestBtn!!.setOnClickListener(defaultRequestBtnClickListener)
        }

        return cell
    }//리스트의 아이템 하나를 그리며, 바인딩하여 텍스트 및 이벤트 등록하는 부분

    // simple methods for register cell state changes
    fun registerToggle(position: Int) {//어떤 아이템이 눌렸는지 해당 포지션이 넘어옴.
        if (unfoldedIndexes.contains(position))//펼쳐져있는 아이템이었다면
            registerFold(position)//접도록 하는 이벤트 요청
        else//접혀있는 아이템이었다면
            registerUnfold(position)//펼치도록 이벤트 요청
    }

    fun registerFold(position: Int) {
        unfoldedIndexes.remove(position)
    }//펼쳐진것들 HashSet에서 접힌 포지션 제거

    fun registerUnfold(position: Int) {
        unfoldedIndexes.add(position)
    }//펼쳐진것들 HashSet에 해당 포지션 등록

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
