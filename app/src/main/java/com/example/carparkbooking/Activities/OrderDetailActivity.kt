package com.example.carparkbooking.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.carparkbooking.R
import com.example.carparkbooking.databinding.ActivityOrderDetailBinding
import com.example.carparkbooking.models.Order
import com.example.carparkbooking.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailActivity : AppCompatActivity() {
    private var binding:ActivityOrderDetailBinding?=null
    private var mOrderDetail: Order?=null
    private var mPosition:Int=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityOrderDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        if(intent.hasExtra(Constants.ORDERS)){
            mOrderDetail=intent.getParcelableExtra(Constants.ORDERS)
        }
        if (intent.hasExtra(Constants.POSITION)){
            mPosition=intent.getIntExtra(Constants.POSITION,-1)
        }
        if(mOrderDetail!=null){ setUpUI() }
        binding?.toolbar?.setNavigationIcon(R.drawable.ic_back_24dp)
        binding?.toolbar?.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setUpUI(){
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val selectedDate = simpleDateFormat.format(Date(mOrderDetail!!.date))

        val hour=((mOrderDetail!!.endTime)-(mOrderDetail!!.startTime))/60
        val hTime=mOrderDetail!!.startTime/60
        val mintime=mOrderDetail!!.startTime%60
        var sTime:String=""
        if(hTime>10&&mintime>10){
            sTime="$hTime : $mintime"}else if(hTime>10&&mintime<10){
            sTime="$hTime : 0$mintime"
        }else if(hTime<10&&mintime>10){
            sTime="0$hTime : $mintime"
        }else if(hTime<10&&mintime<10){
            sTime="0$hTime : 0$mintime"
        }

        val eHour=mOrderDetail!!.endTime/60
        val eMin=mOrderDetail!!.endTime%60
        var eTime:String=""
        if(eHour>10&&mintime>10){
            eTime="$eHour : $eMin"}else if(eHour>10&&eMin<10){
            eTime="$eHour : 0$eMin"
        }else if(eHour<10&&eMin>10){
            eTime="0$eHour : $eMin"
        }else if(eHour<10&&eMin<10){
            eTime="0$eHour : 0$eMin"
        }


        binding?.tvDate?.text=selectedDate.toString()
        binding?.tvHorus?.text=hour.toString()
        binding?.tvStartTime?.text=sTime
        binding?.tvEndTime?.text=eTime
        binding?.tvHorus?.text=hour.toString()
        binding?.tvParkNo?.text=mOrderDetail!!.parkNum.toString()

    }


}