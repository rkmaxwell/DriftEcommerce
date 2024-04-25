package com.drift.ecommerce

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drift.ecommerce.adapter.CheckoutAdapter
import com.drift.ecommerce.model.Product
import com.facebook.shimmer.ShimmerFrameLayout
import com.orhanobut.hawk.Hawk

class CheckoutActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: CheckoutAdapter? = null
    private val coordinatorLayout: CoordinatorLayout? = null
    private var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var textViewCart: TextView? = null
    private var appCompatButton: AppCompatButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_checkout)
        Hawk.init(this).build()
        shimmerFrameLayout = findViewById<View>(R.id.shimmer) as ShimmerFrameLayout
        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        textViewCart = findViewById(R.id.cartCount)
        appCompatButton = findViewById(R.id.addCartCheck)
        appCompatButton?.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    PaymentActivity::class.java
                )
            )
        })
        try {
            val all = Hawk.get<List<Product>>("cart")
            shimmerFrameLayout!!.visibility = View.GONE
            adapter = CheckoutAdapter(all)
            recyclerView!!.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = layoutManager
    }

    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
}