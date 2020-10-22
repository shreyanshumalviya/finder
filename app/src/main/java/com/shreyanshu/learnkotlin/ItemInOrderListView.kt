package com.shreyanshu.learnkotlin

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.layout_item_in_order_list_view.view.*

@Suppress("PrivatePropertyName")
class ItemInOrderListView : RelativeLayout {
    private lateinit var v_iconCollected: View
    private lateinit var tv_title: TextView
    private lateinit var tv_quantity: TextView
    private lateinit var iv_image : ImageView
    private var itemId:String="honey"
    private var quantity:Int=-1
    private var collected:Boolean=false

    constructor(context: Context) : this(context, null){inflate()}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0){inflate()}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){inflate()}

    private fun inflate() {
        val service = Context.LAYOUT_INFLATER_SERVICE
        val layoutInflater = context.getSystemService(service) as LayoutInflater
        layoutInflater.inflate(R.layout.layout_item_in_order_list_view, this, true)
        tv_title = findViewById(R.id.ic_title)
        tv_quantity = findViewById(R.id.ic_quantity)
        iv_image= findViewById(R.id.ic_image)
        v_iconCollected=findViewById(R.id.ic_icon_done)
    }

    fun setItemId(itemId:String){
        this.itemId=itemId
    }

    fun set_title(title:String){
        tv_title.text = title
    }

    fun set_quantity(quantity:Int){
        this.quantity=quantity
        tv_quantity.text = quantity.toString()
    }

    fun set_image(bitmapImg: Bitmap){
        iv_image.setImageBitmap(bitmapImg)
    }

    fun collected(){
        this.collected=true
        ic_icon_done.foreground=AppCompatResources.getDrawable(context,R.drawable.ic_baseline_check_circle_outline_24)
    }

    fun isCollected(): Boolean {
        return this.collected
    }
}