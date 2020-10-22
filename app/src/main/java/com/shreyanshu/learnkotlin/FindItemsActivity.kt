package com.shreyanshu.learnkotlin

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class FindItemsActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var mScannerView: ZXingScannerView
    private lateinit var llItemCollectionView: LinearLayout
    private lateinit var buttonSubmit: Button
    private var cameraOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_items)
        llItemCollectionView = findViewById<LinearLayout>(R.id.fi_item_collection_layout)
        val orderId: String = intent.getStringExtra("orderId")!!
        Orders.getOrderDetails(this@FindItemsActivity, orderId, llItemCollectionView)

        buttonSubmit = findViewById(R.id.fi_scan)

        mScannerView = ZXingScannerView(this)
        mScannerView.setAutoFocus(true)
    }

    fun enterManually(view:View){
        val dialog=Dialog(this@FindItemsActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_manual_input_layout)
        dialog.findViewById<Button>(R.id.mil_enter).setOnClickListener {
            llItemCollectionView.removeAllViews()
            val code:String= dialog.findViewById<EditText>(R.id.mil_barCode).text.toString()
            dialog.dismiss()
            checkCode(code)
        }
        dialog.findViewById<Button>(R.id.mil_cancel).setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun scan(view: View) {
        cameraOpen = true
        mScannerView.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView.startCamera() // Start camera on resume
        llItemCollectionView.removeAllViews()
        setContentView(mScannerView)
        //startActivity(Intent(this@FindItemsActivity,ScanActivity::class.java))
    }

    private fun showList() {
        var allDone = true
        val iterator = Orders.currItemsList.iterator()
        while (iterator.hasNext()) {
            val view = iterator.next()
            if (!view.isCollected()) {
                llItemCollectionView.addView(view)
                if (allDone)
                allDone = false
            }
        }

        val iterator2 = Orders.currItemsList.iterator()
        while (iterator2.hasNext()) {
            val view = iterator2.next()
            if (view.isCollected())
                llItemCollectionView.addView(view)
        }
        if (allDone) {
            onAllDone()
        }
    }

    private fun completeCollection(index: Int) {
        llItemCollectionView.removeAllViews()
        //todo show dialog
        // in that dialog we must check if already collected and set collected
        val dialog = Dialog(this@FindItemsActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.complete_collection_layout)
        val linearLayout = dialog.findViewById<LinearLayout>(R.id.frame_for_Item)
        linearLayout.removeAllViews()
        linearLayout.addView(Orders.currItemsList[index])
        dialog.findViewById<Button>(R.id.ccl_cancel).setOnClickListener {
            linearLayout.removeAllViews()
            Toast.makeText(
                this@FindItemsActivity,
                "Canceled that item collection",
                Toast.LENGTH_SHORT
            ).show()
            showList()
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.ccl_confirm).setOnClickListener {
            linearLayout.removeAllViews()
            Orders.currItemsList[index].collected()
            dialog.dismiss()
            showList()
        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun onAllDone() {
        buttonSubmit.text = "Submit"
        buttonSubmit.setOnClickListener {
            startActivity(Intent(this@FindItemsActivity, MainActivity::class.java))
            //todo send data to server
        }
       // llItemCollectionView.removeAllViews()
        val dialog = Dialog(this@FindItemsActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.complete_collection_layout)
        val buttonRecheck = dialog.findViewById<Button>(R.id.ccl_cancel)
        buttonRecheck.text = "Recheck"
        buttonRecheck.setOnClickListener {
            Toast.makeText(
                this@FindItemsActivity,
                "Click on Submit to continue",
                Toast.LENGTH_SHORT
            ).show()
            //showList()
            dialog.dismiss()
        }
        val buttonContinue = dialog.findViewById<Button>(R.id.ccl_confirm)
        buttonContinue.text = "Continue"
        buttonContinue.setOnClickListener {
            startActivity(Intent(this@FindItemsActivity, MainActivity::class.java))
            //todo send data to server
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun checkCode(code:String?){
        setContentView(R.layout.activity_find_items)
        llItemCollectionView = findViewById<LinearLayout>(R.id.fi_item_collection_layout)
        buttonSubmit = findViewById(R.id.fi_scan)

        val index = Orders.currItemIdList.indexOf(code)
        if (index != -1) {
            completeCollection(index)
        } else {
            Toast.makeText(
                this@FindItemsActivity,
                "Item not detected try scanning again or check list",
                Toast.LENGTH_LONG
            ).show()
            showList()
        }
    }

    override fun handleResult(rawResult: Result?) {
        cameraOpen=false
        mScannerView.stopCamera()
        Log.d("TAG", "handleResult: " + rawResult?.text)
        checkCode(rawResult?.text)
    }

    override fun onResume() {
        super.onResume()
        if (cameraOpen) {
            mScannerView.setResultHandler(this) // Register ourselves as a handler for scan results.
            mScannerView.startCamera() // Start camera on resume
        }
    }

    override fun onPause() {
        super.onPause()
        if (cameraOpen) mScannerView.stopCamera() // Stop camera on pause
    }


    override fun onBackPressed() {
        //todo show dialog to confirm exit or not whatever

        if(cameraOpen){
            llItemCollectionView.removeAllViews()
            setContentView(R.layout.activity_find_items)
            llItemCollectionView = findViewById<LinearLayout>(R.id.fi_item_collection_layout)
            buttonSubmit = findViewById(R.id.fi_scan)
            showList()
        }
        //super.onBackPressed()
    }
}