package com.example.carparkbooking.Adapter

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carparkbooking.databinding.ItemOrderViewBinding
import com.example.carparkbooking.models.Order
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderItemAdapter(private val items:ArrayList<Order>)
    : RecyclerView.Adapter<OrderItemAdapter.TheViewHolder>(){

    private var onClickListener: OnClickListener? = null

        class TheViewHolder(binding:ItemOrderViewBinding)
            : RecyclerView.ViewHolder(binding.root) {
            val date= binding.tvDate
            val time=binding.tvBeginTime
            val hour=binding.tvHours
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheViewHolder {
        return TheViewHolder(ItemOrderViewBinding.inflate
            (LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TheViewHolder, position: Int) {
        val model=items[position]
        val hour=((model.endTime)-(model.startTime))/60
        val hTime=model.startTime/60
        val mintime=model.startTime%60
        var time:String=""
        if(hTime>10&&mintime>10){
        time="$hTime : $mintime"}else if(hTime>10&&mintime<10){
            time="$hTime : 0$mintime"
        }else if(hTime<10&&mintime>10){
            time="0$hTime : $mintime"
        }else if(hTime<10&&mintime<10){
            time="0$hTime : 0$mintime"
        }

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val selectedDate = simpleDateFormat.format(Date(model.date))

        holder.date.text=selectedDate.toString()
        holder.time.text=time
        holder.hour.text=hour.toString()

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, model)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

        interface OnClickListener {
            fun onClick(position: Int, model: Order)
        }

    fun setOnClickListener(onClickListener:OnClickListener) {
        this.onClickListener = onClickListener
    }
}