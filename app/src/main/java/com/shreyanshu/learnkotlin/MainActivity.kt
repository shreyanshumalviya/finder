package com.shreyanshu.learnkotlin

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //scanner= ZXingScannerView(this)
        setContentView(R.layout.activity_main)
        initViews()
        Orders.getOrders(this@MainActivity,ll_orderListCollectionLayout)
    }

    private lateinit var ll_orderListCollectionLayout: LinearLayout

    private fun initViews() {
        ll_orderListCollectionLayout = findViewById(R.id.am_order_list_collection_layout)
    }




}