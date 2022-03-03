package com.example.carparkbooking.Activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.carparkbooking.Firestore.FirestoreClass
import com.example.carparkbooking.R
import com.example.carparkbooking.databinding.ActivityMainBinding
import com.example.carparkbooking.models.Order
import com.example.carparkbooking.models.User
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {
    private var binding:ActivityMainBinding?=null
    lateinit var mUser:User
    private var mSelectedDueDateMilliSeconds:Long=0
    private var startTime:Long=0
    private var endTime:Long=0
    var bookingList=ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        getUser()
        setupPickers()
        binding?.btnSubmit?.setOnClickListener { createOrder() }

        binding?.datepicker?.setOnClickListener { showDataPicker() }

        binding?.tbMain?.inflateMenu(R.menu.booking_page)
        binding?.tbMain?.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.go_booking_page -> {
                    val intent=Intent(this, OrdersActivity::class.java)
                startActivity(intent)}
            }
                false
        }
    }

    private fun getUser(){
        showProgressDialog("Please Wait")
        FirestoreClass().loadUserData(this)
    }

    private fun createOrder(){
        if(binding?.datepicker?.text=="Pick the Date"){
            Toast.makeText(this,"Please pick a date",Toast.LENGTH_SHORT).show()
        }else {
            bookingList.clear()
            showProgressDialog("Please Wait")
            val hour=binding?.timePicker?.hour?.toInt()
            val minutes=binding?.timePicker?.minute?.toInt()
            startTime= (hour?.times(60))?.plus(minutes!!)!!.toLong()
            endTime=(binding?.numberPicker?.value?.times(60))!!+ startTime
            var order=Order(
                mUser.id,
                mSelectedDueDateMilliSeconds,
                startTime, endTime,1
            )
            FirestoreClass().processDate(this,order)
        }
    }

    private fun setupPickers(){
        binding?.numberPicker?.minValue=1
        binding?.numberPicker?.maxValue=24
        binding?.numberPicker?.value=1

        binding?.timePicker?.setIs24HourView(true)
    }

    private fun showDataPicker() {

        val c = Calendar.getInstance()
        val year =
            c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val sMonthOfYear =
                    if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"

                val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
                binding?.datepicker?.text= selectedDate

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                val theDate = sdf.parse(selectedDate)

                mSelectedDueDateMilliSeconds = theDate!!.time

            },
            year,
            month,
            day
        )
        dpd.show()
    }

    fun orderCreatedSuccess(){
        hideProgressDialog()
        val intent= Intent(this, OrdersActivity::class.java)
        startActivity(intent)
    }




}
