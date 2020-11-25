package com.example.persistenceapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.persistenceapp.R
import com.example.persistenceapp.database.MyWeatherAppDatabase
import com.example.persistenceapp.model.CityDatabase
import com.example.persistenceapp.ui.adapters.FavoritesAdapter
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {

    private lateinit var favoriteAdapter: FavoritesAdapter
    private var db: MyWeatherAppDatabase? = null
    private lateinit var list: MutableList<CityDatabase>

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

        db = context?.let { MyWeatherAppDatabase.getInstance(it) }

        //Recupera a Lista de CityDataBase que foram salvas nos favoritos
        list = db?.cityDatabaseDao()?.getAllCityDatabase() as MutableList<CityDatabase>
        if (list.isNullOrEmpty()) {
            txt_empty_favorites_list.visibility = View.VISIBLE
            txt_favorites_title.visibility = View.GONE
            txt_explanation_favorites.visibility = View.GONE
        } else {
            txt_empty_favorites_list.visibility = View.GONE
            txt_favorites_title.visibility = View.VISIBLE
            txt_explanation_favorites.visibility = View.VISIBLE
        }

        favoriteAdapter = FavoritesAdapter(list)
        favoriteRecyclerView.adapter = favoriteAdapter
        favoriteRecyclerView.layoutManager = LinearLayoutManager(context)
        favoriteRecyclerView.addItemDecoration(FavoritesAdapter.FavoritesItemDecoration(25))
        val helper =
            androidx.recyclerview.widget.ItemTouchHelper(
                ItemTouchHandler(
                    androidx.recyclerview.widget.ItemTouchHelper.UP
                            or androidx.recyclerview.widget.ItemTouchHelper.DOWN,
                    androidx.recyclerview.widget.ItemTouchHelper.LEFT
                )
            ) // E deslize para esquerda
        helper.attachToRecyclerView(favoriteRecyclerView)
    }

    // Reordenando os itens de célula
    inner class ItemTouchHandler(dragDirs: Int, swipeDirs: Int) :
        androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //Deleta o item da Lista de CityDataBase que foi arrastado para a esquerda
            list.forEachIndexed { index, cityDatabase ->
                if (index == viewHolder.adapterPosition) {
                    db?.cityDatabaseDao()?.deleteCityDatabaseItem(list.get(index))
                }
            }

            favoriteAdapter.list?.removeAt(viewHolder.adapterPosition)
            favoriteAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            favoriteAdapter.notifyItemRangeChanged(
                viewHolder.adapterPosition,
                favoriteAdapter.list!!.size
            );
            favoriteAdapter.notifyDataSetChanged()

            if (favoriteAdapter.list!!.isEmpty()) {
                txt_empty_favorites_list.visibility = View.VISIBLE
                txt_favorites_title.visibility = View.GONE
                txt_explanation_favorites.visibility = View.GONE
            }
        }
    }
}