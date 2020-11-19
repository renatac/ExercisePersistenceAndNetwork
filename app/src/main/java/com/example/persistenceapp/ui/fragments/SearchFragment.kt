package com.example.persistenceapp.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.persistenceapp.R
import com.example.persistenceapp.database.MyWeatherAppDatabase
import com.example.persistenceapp.manager.OpenWeatherManager
import com.example.persistenceapp.model.City
import com.example.persistenceapp.model.CityDatabase
import com.example.persistenceapp.model.Element
import com.example.persistenceapp.model.Root
import com.example.persistenceapp.ui.adapters.MyAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//OpenWeatherMap API
//https://openweathermap.org/api

//Adapter - é para adaptar um objeto em outro objeto - é uma extensão do RecyclerView. O android
//deixa você adaptar seus dados através do bind

class SearchFragment : Fragment(), View.OnClickListener {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //false - quer dizer que o container não estará atachado ao layout root
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Uso do synthetic
        btn_search.setOnClickListener(this)
        recyclerView.adapter = MyAdapter(mutableListOf())

        floatingActionButton.setOnClickListener {
            val city = et_search.text.toString()
            Log.d("HSS", "Seaching city: $city")

            progressBar.visibility = View.VISIBLE

            val service = OpenWeatherManager().getOpenWeatherService()

            val call = service.getCityWeather(city)
            call.enqueue(object : Callback<City> {
                override fun onResponse(call: Call<City>, response: Response<City>) {
                    when (response.isSuccessful) {
                        true -> {
                            val city = response.body()
                            Log.d("HSS", "Returned city: $city")

                            progressBar.visibility = View.GONE

                            if(context != null) {
                                val db = MyWeatherAppDatabase.getInstance(context!!)
                                val cityDatabase =
                                    CityDatabase(city!!.id, city!!.name, city.sys.country,
                                        city.weather[0].main, city.weather[0].description)
                                db?.cityDatabaseDao()?.save(cityDatabase)
                            }
                        }
                        false -> {
                            Log.e("HSS", "Response is not sucess")
                        }
                    }
                }

                override fun onFailure(call: Call<City>, t: Throwable) {
                    Log.e("HSS", "There is an error: ${t.message}")
                }
            })
        }
    }

@Suppress("DEPRECATION")
fun isConnectivityAvailable(context: Context): Boolean {
    var result = false

    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
    } else {
        cm.activeNetworkInfo?.run {
            if (type == ConnectivityManager.TYPE_WIFI) {
                result = true
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                result = true
            }
        }
    }
    return result
}

override fun onClick(v: View?) {
    when (view?.context?.let { isConnectivityAvailable(it) }) {
        true -> {

            Toast.makeText(view?.context, getText(R.string.online), Toast.LENGTH_LONG).show()

            // testando a funcition if(et_search.text.toString().isTrimEmpty())

            //Glide é um framework pra loading de forma assíncrona.
            val city = et_search.text.toString()
            Log.d("HSS", "Seaching city: $city")

            val service = OpenWeatherManager().getOpenWeatherService()

            val callFindTemperature = service.findTemperatures(city)
            callFindTemperature.enqueue(object : Callback<Root> {
                override fun onResponse(call: Call<Root>, response: Response<Root>) {
                    when (response.isSuccessful()) {
                        true -> {
                            val root = response.body()
                            Log.d("HSS", "Returned root element: $root")

                            val elements = mutableListOf<Element>()
                            root?.list?.forEach {
                                elements.add(it)
                            }

                            (recyclerView.adapter as MyAdapter).addItems(elements)
                            recyclerView.layoutManager = LinearLayoutManager(context)
                            recyclerView.addItemDecoration(MyAdapter.MyItemDecoration(30))
                        }

                        false -> {
                            Log.e("HSS", "Response is not sucess")
                        }
                    }
                }

                override fun onFailure(call: Call<Root>, t: Throwable) {
                    Log.e("HSS", "There is an error: ${t.message}")
                }
            })
        }
        false -> {
            Toast.makeText(view?.context, getText(R.string.offline), Toast.LENGTH_LONG).show()
        }
    }
}

//            when (view?.context?.let { isConnectivityAvailable(it) }) {
//                true -> {
//
//                    progressBar.visibility = View.VISIBLE
//
//                    Toast.makeText(view?.context, getText(R.string.online), Toast.LENGTH_LONG).show()
//
//                    // testando a funcition if(et_search.text.toString().isTrimEmpty())
//
//                    //Glide é um framework pra loading de forma assíncrona.
//                    val city = et_search.text.toString()
//                    Log.d("HSS", "Seaching city: $city")
//
//                    val service = OpenWeatherManager().getOpenWeatherService()
//
//                    val call = service.getCityWeather(city)
//                    call.enqueue(object : Callback<City>{
//                        override fun onResponse(call: Call<City>, response: Response<City>) {
//                            when(response.isSuccessful){
//                                true -> {
//                                    val city = response.body()
//                                    Log.d("HSS", "Returned city: $city")
//
//                                    progressBar.visibility = View.GONE
//
//                                    tv_id.text = city?.id.toString()
//                                    tv_name.text = city?.name
//                                }
//                                false -> {
//                                    Log.e("HSS", "Response is not sucess")
//                                }
//                            }
//                        }
//                        override fun onFailure(call: Call<City>, t: Throwable) {
//                            Log.e("HSS", "There is an error: ${t.message}")
//                        }
//                    })
//                }
//                false -> {
//                    Toast.makeText(view?.context, getText(R.string.offline), Toast.LENGTH_LONG).show()
//                }
//            }
//        }

}