package com.example.persistenceapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.persistenceapp.R

class FavoritesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //false - quer dizer que o container não estará atachado ao layout root
        val favoritesContainer = inflater.inflate(R.layout.fragment_favorites, container, false)
        val textView = favoritesContainer.findViewById<TextView>(R.id.text_favorites)
        textView.text = getString(R.string.title_favorites)
        return favoritesContainer
    }
}