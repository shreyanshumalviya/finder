package com.shreyanshu.learnkotlin

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.LinearLayout
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Orders {
    companion object {
        private const val HOST = "https://e-grocery-hbd.herokuapp.com/"
        private const val ORDERS_ADDRESS = "finderOrderList"
        private const val ORDER_DETAIL_ADDRESS = "orderDetails"
        private const val FINDER_ID_KEY = "finderId"

        private val orderList = ArrayList<String>()

        public val currItemsList=ArrayList<ItemInOrderListView>()

        val currItemIdList=ArrayList<String>()

        fun getOrders(activity: Activity, linearLayout: LinearLayout) {
            val url = URL("http://192.168.43.165:8080/" + ORDERS_ADDRESS)

            val thread = Thread {

                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                // todo after finder data class is formed replace below "1" with finder id
                connection.addRequestProperty(FINDER_ID_KEY, "1")
                //headerFields.put("d","ds")
                Log.d(
                    "TAG",
                    "getOrders: Sent 'GET' request to URL : $url; Response Code : $connection.responseCode\""
                )
                connection.inputStream.bufferedReader().use {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        val responseBuilder = StringBuilder()
                        it.lines().forEach { line ->
                            //println(line)
                            responseBuilder.append(line)
                            Log.d("TAG", "getOrders: $line")
                        }
                        val array = JSONArray(responseBuilder.toString())
                        for (i in 0 until array.length()) {
                            val jsonObject = array.getJSONObject(i)
                            orderList.add(jsonObject.getString("OrderId"))
                            val orderListBlock = OrderListBlock(activity)
                            orderListBlock.setOrderId(jsonObject.getString("OrderId"))
                            orderListBlock.setNumberOfItems(jsonObject.getInt("Length").toString())
                            orderListBlock.setAddress(jsonObject.getString("Address"))
                            orderListBlock.setUserName(jsonObject.getString("UserName"))
                            activity.runOnUiThread {
                                linearLayout.addView(orderListBlock)
                            }
                        }
                    }
                }
            }
            thread.start()
        }

        fun getOrderDetails(
            activity: Activity,
            orderId: String,
            ll_itemsCollectionLayout: LinearLayout
        ) {
            var connection: HttpURLConnection? = null

            val thread = Thread {
                try {
                    Log.d("TAG", "downloadList: 1")
                    val host = "http://192.168.43.165:8080/" + ORDER_DETAIL_ADDRESS
//                    val host = "https://e-grocery-hbd.herokuapp.com/homeitems"
                    val url = URL(host)
                    connection = url.openConnection() as HttpURLConnection
                    connection?.addRequestProperty("orderId", orderId)
                    //connection?.addRequestProperty("userID", "1")
                    val inputStream = connection?.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    var line: String? = reader.readLine()
                    val data: StringBuilder = StringBuilder()
                    Log.d("TAG", "downloadList: 2")
                    while (line != null) {
                        data.append(line)
                        line = reader.readLine()
                    }
                    clearCurrData()
                    Log.d("TAG", "downloadList: 3")

                    activity.runOnUiThread {
                        Log.d("TAG", "downloadList: 4")
                        val jsonArray = JSONArray(data.toString())
                        for (i in 0 until jsonArray.length()) {
                            Log.d("TAG", "downloadList: fd")
                            val jsonObject = jsonArray.getJSONObject(i)
                            val imageInByteArray = Base64.decode(
                                jsonObject["Image"].toString(),
                                Base64.DEFAULT
                            )
                            val bitmap = BitmapFactory.decodeByteArray(
                                imageInByteArray,
                                0,
                                imageInByteArray.size
                            )
                            val block = ItemInOrderListView(activity)
                            block.set_title(jsonObject.getString("ItemName"))
                            currItemIdList.add(i.toString())
                            block.setItemId(i.toString())
                            //currItemIdList.add(jsonObject.getString("ItemId"))
                            //block.set_title(jsonObject.getString("name"))
                            block.set_image(bitmap)
                            block.set_quantity(jsonObject.getInt("Quantity"))
                            currItemsList.add(block)
                            //block.set_quantity(jsonObject.getInt("amount"))
                            ll_itemsCollectionLayout.addView(block)

                            Log.d("TAG", "downloadList: 5")
                        }
                    }
                } /*catch (e: IOException) {
                e.printStackTrace()
            }*/ catch (e: JSONException) {
                    e.printStackTrace()
                } finally {
                    connection?.disconnect()
                    Log.d("TAG", "run: downloaded")
                }
            }
            thread.start()
        }

        private fun clearCurrData() {
            currItemsList.clear()
            currItemIdList.clear()
        }
    }
}