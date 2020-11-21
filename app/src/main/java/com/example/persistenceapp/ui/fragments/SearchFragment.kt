package com.example.persistenceapp.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.persistenceapp.ui.adapters.SearchAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//OpenWeatherMap API
//https://openweathermap.org/api

//Adapter - é para adaptar um objeto em outro objeto - é uma extensão do RecyclerView. O android
//deixa você adaptar seus dados através do bind

class SearchFragment : Fragment(), View.OnClickListener, TextWatcher {
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

        recyclerView.adapter = SearchAdapter(mutableListOf(), this::onFavoriteItemClickListener)

        et_search.addTextChangedListener(this)
    }

    private fun onFavoriteItemClickListener(idNumber : Long) {

            progressBar.visibility = View.VISIBLE

            val service = OpenWeatherManager().getOpenWeatherService()

            val call = service.getCityWeather(idNumber)
            call.enqueue(object : Callback<City> {
                override fun onResponse(call: Call<City>, response: Response<City>) {
                    progressBar.visibility = View.GONE
                    when (response.isSuccessful) {
                        true -> {
                            val city = response.body()
                            Log.d("HSS", "Returned city: $city")

                            if(context != null) {
                                val db = MyWeatherAppDatabase.getInstance(context!!)
                                val cityDatabase =
                                    CityDatabase(city!!.id, city!!.name, city.sys.country,
                                        city.weather[0].main, city.weather[0].description, city.weather[0].icon)
                                db?.cityDatabaseDao()?.save(cityDatabase)
                            }
                        }
                        false -> {
                            val a = response
                            tv_error_feedback.text = getString(R.string.txt_favorite_error_feedback)
                        }
                    }
                }

                override fun onFailure(call: Call<City>, t: Throwable) {
                    tv_error_feedback.text = getString(R.string.txt_favorite_error_feedback)
                }
            })

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

            //Glide é um framework pra loading de forma assíncrona.
            val city = et_search.text.toString()

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

                            (recyclerView.adapter as SearchAdapter).addItems(elements)
                            recyclerView.layoutManager = LinearLayoutManager(context)
                            recyclerView.addItemDecoration(SearchAdapter.MyItemDecoration(30))
                        }

                        false -> {
                            tv_error_feedback.text = getString(R.string.txt_error_feedback)
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

    override fun afterTextChanged(s: Editable?) {
        tv_error_feedback.text = ""
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}