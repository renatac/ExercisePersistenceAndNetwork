package com.example.persistenceapp.Utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences {

    companion object{

        lateinit var prefs: SharedPreferences

        fun initSharedPreferences(context: Context, name: String){
            prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }

        fun saveInSharedPreferences(key : String, value: String) {
            val editor = prefs?.edit()
            editor?.apply {
                putString(key, value)
                apply()
            }
        }

        fun getOfSharedPreferences(key: String) =
            prefs?.getString(key,"").toString()
    }
}
