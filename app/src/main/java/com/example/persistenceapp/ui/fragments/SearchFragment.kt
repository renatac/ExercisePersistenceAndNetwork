package com.example.persistenceapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.persistenceapp.R
import com.example.persistenceapp.Utils.Constants.Companion.MODEL_ELEMENT
import com.example.persistenceapp.Utils.Constants.Companion.TYPED_CITY
import com.example.persistenceapp.Utils.SharedPreferences.Companion.getOfSharedPreferences
import com.example.persistenceapp.Utils.SharedPreferences.Companion.saveInSharedPreferences
import com.example.persistenceapp.database.MyWeatherAppDatabase
import com.example.persistenceapp.manager.OpenWeatherManager
import com.example.persistenceapp.model.*
import com.example.persistenceapp.ui.activities.DetailsActivity
import com.example.persistenceapp.ui.adapters.SearchAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//OpenWeatherMap API
//https://openweathermap.org/api

class SearchFragment : Fragment(), View.OnClickListener, TextWatcher {

    private var typedCity = ""

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var elements: MutableList<Element>

    private var db: MyWeatherAppDatabase? = null
    private lateinit var list: MutableList<CitySearchDatabase>

    private var isRecreated: Boolean = false

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

        isRecreated = true
        et_search.addTextChangedListener(this)

        //Uso do synthetic
        btn_search.setOnClickListener(this)

        searchAdapter = SearchAdapter(
            mutableListOf(),
            this::onFavoriteItemClickListener,
            this::viewDetailcallback
        )
        recyclerview.adapter = searchAdapter
        recyclerview.addItemDecoration(SearchAdapter.MyItemDecoration(30))

        //Recupera a string digitada anteriormente no EditText da busca da cidade
        typedCity = getOfSharedPreferences(TYPED_CITY)
        et_search.setText(typedCity)

        db = context?.let { MyWeatherAppDatabase.getInstance(it) }

        //Recupera a Lista de CitySearchDatabase que foi salva
        list = db?.cityDatabaseDao()?.getAllSearchDatabase() as MutableList<CitySearchDatabase>

        elements = mutableListOf()

        if (!list.isNullOrEmpty()) {
            list.forEach {
                elements.add(
                    Element(
                        it.id, it.name, it.dt,
                        mutableListOf(
                            Weather(
                                it.weatherId,
                                it.weatherMain,
                                it.weatherDescription,
                                it.weatherIcon
                            )
                        )
                    )
                )
            }
            (recyclerview.adapter as SearchAdapter).addItems(elements)
            recyclerview.layoutManager = LinearLayoutManager(context)
        } else {
            setVisibility(search_group, View.GONE)
        }
    }

    override fun onStart() {
        super.onStart()
        isRecreated = false
    }

    private fun viewDetailcallback(element: Element) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra(MODEL_ELEMENT, element)
        startActivity(intent)
    }

    private fun onFavoriteItemClickListener(idNumber: Long) {

        val service = OpenWeatherManager().getOpenWeatherService()

        val call = service.getCityWeather(idNumber)
        call.enqueue(object : Callback<City> {
            override fun onResponse(call: Call<City>, response: Response<City>) {
                when (response.isSuccessful) {
                    true -> {
                        val city = response.body()
                        Toast.makeText(
                            context, getString(R.string.city_added_to_favorites, city?.name),
                            Toast.LENGTH_LONG
                        ).show()

                        if (context != null) {

                            val cityDatabase =
                                CityDatabase(
                                    city!!.id,
                                    city.name,
                                    city.sys.country,
                                    city.weather[0].main,
                                    city.weather[0].description,
                                    city.weather[0].icon
                                )
                            db?.cityDatabaseDao()?.save(cityDatabase)
                        }
                    }
                    false -> {
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

                val city = et_search.text.toString()

                if (city.isBlank()) {
                    tv_error_feedback.text = getString(R.string.empty_city_feedback)
                } else {

                    val service = OpenWeatherManager().getOpenWeatherService()

                    val callFindTemperature = service.findTemperatures(city)

                    setVisibility(progressBar, View.VISIBLE)
                    setVisibility(search_group, View.GONE)

                    callFindTemperature.enqueue(object : Callback<Root> {
                        override fun onResponse(call: Call<Root>, response: Response<Root>) {

                            when (response.isSuccessful()) {
                                true -> {
                                    val root = response.body()

                                    typedCity = et_search.text.toString()
                                    saveInSharedPreferences(TYPED_CITY, typedCity)
                                    deleteAllSearchDatabase()

                                    elements = mutableListOf()
                                    val citySearchDatabaseList = mutableListOf<CitySearchDatabase>()
                                    root?.list?.forEach { element ->
                                        elements?.add(element)
                                        citySearchDatabaseList.add(
                                            CitySearchDatabase(
                                                element.id,
                                                element.name,
                                                element.dt,
                                                element.weather[0].id,
                                                element.weather[0].main,
                                                element.weather[0].description,
                                                element.weather[0].icon
                                            )
                                        )
                                    }
                                    setVisibility(progressBar, View.GONE)
                                    //Eu encontrei casos em que a api retornou sucesso mas a lista veio vazia.
                                    //Um exemplo é eu digitar "renata" e apertar no botão search
                                    if (root?.list?.isEmpty() ?: false) {
                                        tv_error_feedback.text =
                                            getString(R.string.txt_error_feedback)
                                    } else {
                                        db?.cityDatabaseDao()?.saveSearch(citySearchDatabaseList)
                                        setVisibility(search_group, View.VISIBLE)
                                    }
                                    (recyclerview.adapter as SearchAdapter).addItems(elements)
                                    recyclerview.layoutManager = LinearLayoutManager(context)
                                }

                                false -> {
                                    tv_error_feedback.text = getString(R.string.txt_error_feedback)
                                }
                            }
                            setVisibility(progressBar, View.GONE)
                        }

                        override fun onFailure(call: Call<Root>, t: Throwable) {
                            tv_error_feedback.text = getString(R.string.txt_error_feedback)
                            setVisibility(progressBar, View.GONE)
                        }

                    })
                }
            }
            false -> {
                setVisibility(progressBar, View.GONE)
                Toast.makeText(view?.context, getText(R.string.offline), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }

    override fun afterTextChanged(s: Editable?) {
        if (!isRecreated) {
            typedCity = ""
            tv_error_feedback.text = ""
            setVisibility(search_group, View.GONE)
            deleteAllSearchDatabase()
            saveInSharedPreferences(TYPED_CITY, "")
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    private fun deleteAllSearchDatabase() {
        //Deleta item a item da Lista de CitySearchDatabase
        list.forEach { citySearchDatabase ->
            db?.cityDatabaseDao()?.deleteAllSearchDatabase(citySearchDatabase)
        }
    }
}