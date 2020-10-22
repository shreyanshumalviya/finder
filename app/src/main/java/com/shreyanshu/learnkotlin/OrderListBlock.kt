package com.shreyanshu.learnkotlin

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import com.shreyanshu.learnkotlin.FindItemsActivity
import com.shreyanshu.learnkotlin.R

class OrderListBlock : RelativeLayout {
    private lateinit var tvOrderId: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvNumberOfItems: TextView


    constructor(context: Context) : super(context) {
        inflate(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inflate(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        inflate(context)
    }


    private fun inflate(context: Context) {
        val service = Context.LAYOUT_INFLATER_SERVICE
        val layoutInflater = context.getSystemService(service) as LayoutInflater
        layoutInflater.inflate(R.layout.layout_order_list_block, this, true)
        tvOrderId = findViewById(R.id.olb_orderId)
        tvUserName = findViewById(R.id.olb_userName)
        tvAddress = findViewById(R.id.olb_address)
        tvNumberOfItems = findViewById(R.id.olb_numberOfItems)
    }

    fun setOrderId(orderId: String) {
        tvOrderId.text = orderId
        setOnClickListener { context.startActivity(Intent(context, FindItemsActivity::class.java).putExtra("orderId",orderId)) }
    }

    fun setUserName(userName: String) {
        tvUserName.text = userName
    }

    fun setAddress(address: String) {
        tvAddress.text = address
    }

    fun setNumberOfItems(numberOfItems: String) {
        tvNumberOfItems.text = numberOfItems
    }


}