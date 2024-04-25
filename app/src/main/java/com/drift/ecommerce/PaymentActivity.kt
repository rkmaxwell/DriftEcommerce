package com.drift.ecommerce

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drift.ecommerce.adapter.PayAdapter
import com.drift.ecommerce.model.PayList
import com.drift.ecommerce.model.Product
import com.drift.ecommerce.service.RestApiBuilder
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: PayAdapter? = null
    private val coordinatorLayout: CoordinatorLayout? = null
    private var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var textViewCart: TextView? = null
    private var relativeLayoutCart: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        Hawk.init(this).build()
        shimmerFrameLayout = findViewById<View>(R.id.shimmer) as ShimmerFrameLayout
        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        textViewCart = findViewById(R.id.cartCount)
        relativeLayoutCart = findViewById(R.id.cart_area)
        val layoutManager = GridLayoutManager(applicationContext, 2)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = layoutManager
        //check network connectivity
        if (!isNetworkAvailable) {
            val snackbar = Snackbar
                .make(coordinatorLayout!!, "Failed to connect to internet", Snackbar.LENGTH_LONG)
                .setAction("RETRY") { fetchData() }
            snackbar.show()
        } else {
            fetchData()
        }
    }

    private fun prepareData(prodList: PayList?) {
        if (!prodList!!.items.isEmpty()) {
            shimmerFrameLayout!!.visibility = View.GONE
            adapter = PayAdapter(prodList.items)
            recyclerView!!.adapter = adapter
        }
        adapter!!.setOnClickListener { position: Int, model: Product? ->
            Toast.makeText(this, "Thank you", Toast.LENGTH_SHORT).show()
            finish()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun fetchData() {
        val apiService = RestApiBuilder().service
        val payListCall = apiService.payList
        payListCall.enqueue(object : Callback<PayList?> {
            override fun onResponse(call: Call<PayList?>, response: Response<PayList?>) {
                if (response.isSuccessful) {
                    assert(response.body() != null)
                    val payList = response.body()
                    prepareData(payList)
                } else {
                    Toast.makeText(
                        this@PaymentActivity,
                        "Request failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PayList?>, t: Throwable) {
                Log.e("err2", t.message!!)
                Toast.makeText(
                    this@PaymentActivity,
                    "Request failed. Check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
}