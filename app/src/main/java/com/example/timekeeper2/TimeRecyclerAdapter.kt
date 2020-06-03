package com.example.timekeeper2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimeRecyclerAdapter (private  val context: Context, private  val timeDataList: List<timeDataStructure>,var  clickListener: OnTimeItemClickedListener) :
    RecyclerView.Adapter<TimeRecyclerAdapter.ViewHolder>(){


        private  val layoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_edit_data, parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = timeDataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeData = timeDataList[position]
        /*holder.timeDataDateText?.text = timeData.date
        holder.timeDataTimeText?.text = timeData.timeAdded
        holder.timeDataText?.text = timeData.timeString*/
        holder.initialize(timeData, clickListener)
    }
    class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeDataDateText = itemView.findViewById<TextView?>(R.id.timeLoggedDate)
        val timeDataTimeText = itemView.findViewById<TextView?>(R.id.timeLoggedTime)
        val timeDataText = itemView.findViewById<TextView?>(R.id.timeLoggedData)

        fun initialize(timeData : timeDataStructure, action : OnTimeItemClickedListener){
            timeDataDateText?.text = timeData.date
            timeDataTimeText?.text = timeData.timeAdded
            timeDataText?.text = timeData.timeString

            itemView.setOnClickListener(){
                action.onItemClick(timeData)
            }
        }

    }

    interface  OnTimeItemClickedListener{
        fun onItemClick(timeData : timeDataStructure)
    }
}