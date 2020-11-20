package com.example.persistenceapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.persistenceapp.R
import com.example.persistenceapp.database.MyWeatherAppDatabase
import com.example.persistenceapp.ui.adapters.FavoritesAdapter
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //false - quer dizer que o container não estará atachado ao layout root
        val favoritesContainer =
            inflater.inflate(R.layout.fragment_favorites, container, false)
        return favoritesContainer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = context?.let { MyWeatherAppDatabase.getInstance(it) }

        //Recupera a Lista de CityDataBase que foram salvar nos favoritos
        val list = db?.cityDatabaseDao()?.getAllCityDatabase()
        if (list.isNullOrEmpty()) {
            txt_empty_favorites_list.visibility = View.VISIBLE
        } else {
            txt_empty_favorites_list.visibility = View.GONE
        }

        favoriteRecyclerView.adapter = FavoritesAdapter(list)

        favoriteRecyclerView.layoutManager = LinearLayoutManager(context)
        favoriteRecyclerView.addItemDecoration(FavoritesAdapter.FavoritesItemDecoration(25))
    }
}