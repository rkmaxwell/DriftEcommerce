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
import com.drift.ecommerce.adapter.ProdAdapter
import com.drift.ecommerce.model.Product
import com.drift.ecommerce.model.ProductList
import com.drift.ecommerce.service.RestApiBuilder
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: ProdAdapter? = null
    private val coordinatorLayout: CoordinatorLayout? = null
    private var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var textViewCart: TextView? = null
    private var relativeLayoutCart: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Hawk.init(this).build()
        shimmerFrameLayout = findViewById<View>(R.id.shimmer) as ShimmerFrameLayout
        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        textViewCart = findViewById(R.id.cartCount)
        relativeLayoutCart = findViewById(R.id.cart_area)
        relativeLayoutCart?.setOnClickListener(View.OnClickListener setOnClickListener@{ v: View? ->
            try {
                if (Hawk.get<Any?>("cart") == null) {
                    Toast.makeText(this, "Please add items to cart", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                startActivity(Intent(this, CheckoutActivity::class.java))
            } catch (e: Exception) {
                Toast.makeText(this, "Please add items to cart", Toast.LENGTH_SHORT).show()
            }
        })
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

    override fun onStart() {
        super.onStart()
        try {
            val all = Hawk.get<List<Product>>("cart")
            textViewCart!!.text = all.size.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun prepareData(prodList: ProductList?) {
        if (!prodList!!.items.isEmpty()) {
            shimmerFrameLayout!!.visibility = View.GONE
            adapter = ProdAdapter(prodList.items)
            recyclerView!!.adapter = adapter
        }
        adapter!!.setOnClickListener { position: Int, model: Product ->

            //Log.e("click","click");
            try {
                val existing = Hawk.get<MutableList<Product>>("cart")
                if (existing != null) {
                    existing.add(model)
                    Hawk.put<List<Product>>("cart", existing)
                    Toast.makeText(this, "Added to cart exist", Toast.LENGTH_SHORT).show()
                } else {
                    val selected: MutableList<Product> = ArrayList()
                    selected.add(model)
                    Hawk.put<List<Product>>("cart", selected)
                    Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
                }
                val all = Hawk.get<List<Product>>("cart")
                textViewCart!!.text = all.size.toString()
            } catch (e: Exception) {
                val selected: MutableList<Product> = ArrayList()
                selected.add(model)
                Hawk.put<List<Product>>("cart", selected)
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchData() {
        val apiService = RestApiBuilder().service
        val userListCall = apiService.productList
        userListCall.enqueue(object : Callback<ProductList?> {


            override fun onResponse(call: Call<ProductList?>, response: Response<ProductList?>) {
                if (response.isSuccessful) {
                    assert(response.body() != null)
                    val productList = response.body()
                    prepareData(productList)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Request failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ProductList?>, t: Throwable) {
                Log.e("err2", t.message!!)
                Toast.makeText(
                    this@MainActivity,
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