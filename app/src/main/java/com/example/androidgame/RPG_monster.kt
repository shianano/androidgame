package com.example.androidgame

import android.content.res.Resources
//import kotlinx.serialization.Serializable
import java.io.BufferedReader
import java.io.InputStreamReader

//@Serializable
class RPG_monster (
        val id: Int,
        val mon_name: String,
        val atk: Int,
        val def: Int,
        val mp: Int,
        val ult: String
        )
/*
fun getdata(id: Int?,resources: Resources) : String{
        val assetManager = resources.assets
        val open_json = assetManager.open("RPG_Data.json")
        val br = BufferedReader(InputStreamReader(open_json))
        val str: String = br.readText()
        return str
}
 */