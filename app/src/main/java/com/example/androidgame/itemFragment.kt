package com.example.androidgame

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.androidgame.databinding.FragmentItemBinding
import com.example.androidgame.databinding.FragmentSettingBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class itemFragment : Fragment() {
    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!

    var item_list:Array<String> = arrayOf()
    var item_id:Array<Int> = arrayOf()
    var item_name:Array<String> = arrayOf()
    var item_num:Array<Int> = arrayOf()
    var visible_text:Array<String> = arrayOf()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        var item_text = pref.getString("item_list","0.1").toString()
        item_list = item_text.split(",").toTypedArray()
        item_id = Array(item_list.size){0}
        item_name = Array(item_list.size){""}
        item_num = Array(item_list.size){0}
        visible_text = Array(item_list.size){""}
        //System.out.println(item_list.size)
        var i = 0
        while (i<item_list.size){
            var a:Array<Int> = item_list[i].split(".").map(String::toInt).toTypedArray()
            item_id[i] = a[0]
            item_num[i] = a[1]
            i++
        }
        list_create()
        val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                visible_text
        )
        binding.itemlist.adapter = adapter

        binding.itemlist.setOnItemClickListener {parent, view, position, id ->
            //System.out.println(parent)
            //System.out.println(view)
            //System.out.println(position)
            System.out.println(id)
        }

        return binding.root
    }
    fun list_create(){
        var name = ""
        val assetManager = resources.assets
        val inputStream = assetManager.open("item.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText()
        val jsonObject = JSONObject(str)
        val jsonArray_item = jsonObject.getJSONArray("item")
        var i = 0
        System.out.println(item_id.size)
        while (i<item_id.size){
            val jsonData = jsonArray_item.getJSONObject(item_id[i])
            name = jsonData.getString("name")
            item_name[i] = name
            visible_text[i] = item_name[i]
            i++
        }

        inputStream.close()
        bufferedReader.close()
    }
}