package com.example.carparkbooking.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carparkbooking.Adapter.OrderItemAdapter
import com.example.carparkbooking.Firestore.FirestoreClass
import com.example.carparkbooking.R
import com.example.carparkbooking.databinding.ActivityOrdersBinding
import com.example.carparkbooking.models.Order
import com.example.carparkbooking.utils.Constants

class OrdersActivity : BaseActivity() {
    private var binding:ActivityOrdersBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityOrdersBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        FirestoreClass().getOrderList(this)
        binding?.toolbar?.setNavigationIcon(R.drawable.ic_back_24dp)
        binding?.toolbar?.setNavigationOnClickListener { onBackPressed() }
    }

    fun populateToUI(list: ArrayList<Order>){
        binding?.tvNoList?.visibility=View.GONE
        binding?.rvOrders?.visibility=View.VISIBLE
        val adapter=OrderItemAdapter(list)
        binding?.rvOrders?.layoutManager=LinearLayoutManager(this)
        binding?.rvOrders?.adapter=adapter
        adapter.setOnClickListener(object:OrderItemAdapter.OnClickListener{
            override fun onClick(position: Int, model: Order) {
                val intent= Intent(this@OrdersActivity, OrderDetailActivity::class.java)
                intent.putExtra(Constants.ORDERS, model)
                intent.putExtra(Constants.POSITION,position)
                startActivityForResult(intent, ORDER_REQUSER_CODE)
            }
        })
    }

    fun setUpTextView(){
        binding?.rvOrders?.visibility=View.GONE
        binding?.tvNoList?.visibility=View.VISIBLE
    }

    companion object{
        const val ORDER_REQUSER_CODE=34
    }
}