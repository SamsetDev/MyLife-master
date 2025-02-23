package com.sam.mylife.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sam.mylife.R
import com.sam.mylife.core.model.HomeResponse
import com.sam.mylife.core.model.MonthItemModel
import com.sam.mylife.databinding.HomeRecordItemBinding
import com.sam.mylife.databinding.ParentRecordItemBinding
import com.sam.mylife.utils.listeners.OnClickItemListeners

class HomeRecordAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>()   {
   private lateinit var listeners: OnClickItemListeners
    private  var listdata = ArrayList<HomeResponse>()
    private val ITEM_VIEW_TYPE_HEADER = 1
    private val ITEM_VIEW_TYPE_ITEM = 0


    fun setListeners(onclick: OnClickItemListeners){
        this.listeners=onclick
    }

    fun addData(data:ArrayList<HomeResponse>){
        this.listdata=data
        notifyDataSetChanged()
    }

    class RecordViewHolder(val bind: HomeRecordItemBinding) : RecyclerView.ViewHolder(bind.root){

        @SuppressLint("SetTextI18n")
        fun setupdata(listners:OnClickItemListeners, ctx:Context, listdata:MonthItemModel){
            bind.tvDate.text=listdata.date
            bind.tvItemName.text=listdata.item
            bind.llItemview.setOnClickListener {
                listners.onItemClick(listdata)
            }
        }
    }
    class ParentViewHolder(val bind: ParentRecordItemBinding) : RecyclerView.ViewHolder(bind.root){

        @SuppressLint("SetTextI18n")
        fun setupdata(listners:OnClickItemListeners,listdata: HomeResponse){
            bind.tvItemParent.text=listdata.month
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_HEADER ->{
                val view=ParentRecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ParentViewHolder(view)
            }
            ITEM_VIEW_TYPE_ITEM ->{
                val view=HomeRecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RecordViewHolder(view)
            }
            else -> throw RuntimeException()

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var dataList=listdata.get(position)
        when(holder)
        {
          is  ParentViewHolder -> {
                holder.setupdata(listeners,dataList)
               holder.bind.llParent.setOnClickListener {
                   expandOrCollapseParentItem(dataList,position)
               }

            }
           is RecordViewHolder -> {

                holder.setupdata(listeners,context, dataList.list.first())
            }

        }

    }

    private fun expandOrCollapseParentItem(singleBoarding: HomeResponse,position: Int) {

        if (singleBoarding.isExpanded) {
            collapseParentRow(position)
        } else {
            expandParentRow(position)
        }
    }

    private fun expandParentRow(position: Int){
        val currentBoardingRow = listdata[position]
        val services = currentBoardingRow.list
        currentBoardingRow.isExpanded = true
        var nextPosition = position
        if(currentBoardingRow.type==1){

            services.forEach { service ->
                val parentModel =  HomeResponse()
                parentModel.type = 0
                val subList : ArrayList<MonthItemModel> = ArrayList()
                subList.add(service)
                parentModel.list=subList
                listdata.add(++nextPosition,parentModel)
            }
            notifyDataSetChanged()
        }
    }
    private fun collapseParentRow(position: Int){
        val currentBoardingRow = listdata[position]
        val services = currentBoardingRow.list
        listdata[position].isExpanded = false
        if(listdata[position].type==1){
            services.forEach { _ ->
                listdata.removeAt(position + 1)
            }
            notifyDataSetChanged()
        }
    }


    override fun getItemViewType(position: Int): Int = listdata[position].type
    override fun getItemId(position: Int): Long = position.toLong()

    /*override fun getItemViewType(position: Int): Int {
         return if (listdata.get(position).type==1) {
             ITEM_VIEW_TYPE_HEADER
        } else {
            ITEM_VIEW_TYPE_ITEM
        }
    }*/


    override fun getItemCount(): Int = listdata?.size ?: 0

    fun clear() {
        listdata.clear()
        notifyDataSetChanged()
    }
}