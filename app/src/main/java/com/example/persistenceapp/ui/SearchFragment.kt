package com.example.persistenceapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.persistenceapp.R
import kotlinx.android.synthetic.main.fragment_search.*

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
        //uso do synthetic
        btn_search.setOnClickListener(this)
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
            }
            false -> {
                Toast.makeText(view?.context, getText(R.string.offline), Toast.LENGTH_LONG).show()
            }
        }
    }
}