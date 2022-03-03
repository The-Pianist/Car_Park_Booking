package com.example.carparkbooking.Firestore

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.carparkbooking.Activities.MainActivity
import com.example.carparkbooking.Activities.OrdersActivity
import com.example.carparkbooking.Activities.SignInActivity
import com.example.carparkbooking.Activities.SignUpActivity
import com.example.carparkbooking.models.Order
import com.example.carparkbooking.models.User
import com.example.carparkbooking.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun registerUser(activity: SignUpActivity, userInfo: User){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.registerSuccess()
            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"error message", e)
                Toast.makeText(activity, "Something got wrong in register", Toast.LENGTH_LONG).show()
            }
    }

    fun loadUserData(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {document->
                val loggedInUser=document.toObject(User::class.java)!!
                when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                    is MainActivity->{
                        activity.mUser=loggedInUser
                        activity.hideProgressDialog()
                    }
            }
            }.addOnFailureListener {e->
                when(activity){
                    is SignInActivity->{
                        activity.hideProgressDialog()
                    }
                    is MainActivity->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,"Error message",e)
            }
    }

    fun createOrder(activity: MainActivity, order: Order){
        mFireStore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName,"Booking created")
                activity.orderCreatedSuccess()
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName,"something got wrong",e)
                activity.hideProgressDialog()
                Toast.makeText(activity,"Something got wrong",Toast.LENGTH_LONG).show()
            }
    }

    fun getOrderList(content:OrdersActivity){
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.CREATED_BY, getCurrentUserID())
            .get()
            .addOnSuccessListener {document->
                Log.e(content.javaClass.simpleName,document.documents.toString())
                val orderList=ArrayList<Order>()
                for(i in document.documents){
                    var order=i.toObject(Order::class.java)
                    order!!.documentId=i.id
                    orderList.add(order)
                }
                if(orderList.size>0){
                content.populateToUI(orderList)}else{
                    content.setUpTextView()
                }
            }.addOnFailureListener { e->
                Log.e(content.javaClass.simpleName, "Error Message", e)
                Toast.makeText(content, "Error in getting data", Toast.LENGTH_SHORT).show()
            }
    }

    fun processDate(activity:MainActivity, booking:Order){
        val dateInMin=fetchDate()
        mFireStore.collection(Constants.ORDERS)
            .whereGreaterThan(Constants.DATE,dateInMin)
            .get()
            .addOnSuccessListener { document->
                for (i in document.documents){
                    val obj=i.toObject(Order::class.java)
                    if (obj != null) {
                        if(obj.startTime>=booking.startTime&&obj.startTime<=booking.endTime&&obj.date==booking.date){
                            activity.bookingList.add(obj)
                        }else if(obj.date==booking.date&&obj.endTime>=booking.startTime&&obj.endTime<=booking.endTime){
                            activity.bookingList.add(obj)
                        }
                    }
                }
                Toast.makeText(activity,"The number of booking: ${activity.bookingList.size}",Toast.LENGTH_LONG).show()
                if(activity.bookingList.size<29&&activity.bookingList.isNotEmpty()){
                    booking.parkNum=activity.bookingList.size+1
                    FirestoreClass().createOrder(activity, booking)
                }else if(activity.bookingList.isNullOrEmpty()){
                    booking.parkNum=1
                    FirestoreClass().createOrder(activity,booking)
                } else{
                    Toast.makeText(activity, "Sorry, booking is Full",Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e->
                Toast.makeText(activity,"Something got wrong getting data",Toast.LENGTH_LONG).show()
                Log.e(activity.javaClass.simpleName, "error message", e)
            }
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun fetchDate(): Long {
        val date = Calendar.getInstance().time
        val dateInString = date.toString("dd/MM/yyyy")
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateMin = sdf.parse(dateInString)
        return dateMin!!.time
    }



}