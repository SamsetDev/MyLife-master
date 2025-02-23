package com.sam.mylife.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sam.mylife.databinding.AutoSuggestItemBinding
import com.sam.mylife.utils.listeners.OnClickItemListeners
import java.util.*
import kotlin.collections.ArrayList

class AutoSuggestAdapter(var listdata:List<String>) : RecyclerView.Adapter<AutoSuggestAdapter.RecordViewHolder>()   {
   private lateinit var listeners: OnClickItemListeners
    //private  var listdata =ArrayList<String>()


    fun setListeners(onclick: OnClickItemListeners){
        this.listeners=onclick
    }
    public fun filterList(list:ArrayList<String>){
        this.listdata = list
        notifyDataSetChanged();
    }


    class RecordViewHolder(val bind: AutoSuggestItemBinding) : RecyclerView.ViewHolder(bind.root){

        fun setupdata(listners:OnClickItemListeners,listdata:String){
            bind.tvItemSearch.text=listdata
            Log.e(" tag","  item is  "+listdata)
            bind.tvItemSearch.setOnClickListener {
                listners.onItemClick(listdata)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view=AutoSuggestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
            holder.setupdata(listeners,listdata.get(position))
    }

    override fun getItemCount(): Int = listdata?.size ?: 0
}