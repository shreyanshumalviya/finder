package com.shreyanshu.learnkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanActivity : AppCompatActivity(),ZXingScannerView.ResultHandler {

    private lateinit var mScannerView:ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        mScannerView= ZXingScannerView(this)
        mScannerView.setAutoFocus(true)
        setContentView(mScannerView)
    }


    override fun handleResult(rawResult: Result?) {
        Log.d("TAG", "handleResult: " + rawResult?.text)
        if(Orders.currItemIdList.contains(rawResult?.text)){
            // todo do some function
        }else{
            Toast.makeText(
                this@ScanActivity,
                "Item not detected try scanning again or check list",
                Toast.LENGTH_LONG
            ).show()
        }
        super.onBackPressed()
    }


    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView.startCamera() // Start camera on resume
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera() // Stop camera on pause
    }


}