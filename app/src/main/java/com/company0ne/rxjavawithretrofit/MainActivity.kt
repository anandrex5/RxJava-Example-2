package com.company0ne.rxjavawithretrofit


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.btnClick)
        button.clicks()
            .throttleFirst(1500, TimeUnit.MILLISECONDS)
            .subscribe {
                Log.d("TAG", "Button Clicked")
            }
        implementNetworkCall()
    }

    private fun implementNetworkCall() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        val productService = retrofit.create(ProductService::class.java)
        productService.getProducts()
            //executes on IoThread
            .subscribeOn(Schedulers.io())
            //executes on IoThread
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                Log.d("TAG", it.toString())
            }
    }
}