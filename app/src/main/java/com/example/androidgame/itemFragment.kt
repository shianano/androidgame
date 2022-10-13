package com.example.androidgame

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
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
    var item_effect_num = 0
    var item_size = 0
    var max_mp = 0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        var item_text = pref.getString("item_list","0.1").toString()
        max_mp = pref.getInt("pl_max_mp",20)
        if(item_text==""){
            //なし
        }
        else{
            item_list = item_text.split(",").toTypedArray()
            item_id = Array(item_list.size){0}
            item_name = Array(item_list.size){""}
            item_num = Array(item_list.size){0}
            visible_text = Array(item_list.size){""}
            System.out.println(item_text)
            item_size = item_list.size
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
                select_item(id.toInt())
                //System.out.println(parent)
                //System.out.println(view)
                //System.out.println(position)
                //System.out.println(id)
            }
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
        //System.out.println(item_id.size)
        while (i<item_id.size){
            val jsonData = jsonArray_item.getJSONObject(item_id[i])
            name = jsonData.getString("name")
            item_name[i] = name
            visible_text[i] = item_id[i].toString() + "." + item_name[i]
            i++
        }

        inputStream.close()
        bufferedReader.close()
    }
    fun select_item(num:Int){
        var item_name = visible_text[num]
        //System.out.println(visible_text[num])
        var list_no_name = item_name.split(".")
        var no = list_no_name[0].toInt()
        var detail = ""
        val assetManager = resources.assets
        val inputStream = assetManager.open("item.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText()
        val jsonObject = JSONObject(str)
        val jsonArray_item = jsonObject.getJSONArray("item")
        val jsonData = jsonArray_item.getJSONObject(item_id[num])
        detail = jsonData.getString("detail")
        //System.out.println(item_num[no])
        AlertDialog.Builder(requireContext())
                .setTitle(item_name + ":個数" + item_num[num].toString())
                .setMessage("詳細:" + detail)
                .setPositiveButton("使う", { dialog, which ->
                    //read
                    use_item(num)
                })
                .show()
        inputStream.close()
        bufferedReader.close()
    }
    fun use_item(no:Int){
        item_num[no] = item_num[no] - 1
        //無くなった時
        if(item_num[no]==0){
            System.out.println("０個になりました")
            if(item_list.size==1){
                System.out.println("無くなりました！")
                item_list = arrayOf("empty")
            }
            else{
                list_reset(no)
            }
        }
        //まだあるとき
        else{
            //
            list_renum(no,item_num[no])
        }
        item_type(no)
        re_list()
        now_item_list_save()
    }
    fun list_reset(no:Int){
        if(item_list.size == 1){
            item_list = arrayOf()
        }
        else{
            var no = no
            while (no<item_size-1){
                item_list[no] = item_list[no+1]
                no++
            }
            val copy_list = item_list.dropLast(1)
            item_list = copy_list.toTypedArray()
        }
    }
    fun list_renum(no:Int,num:Int){
        var no = no
        item_list[no] = no.toString() + "." + num.toString()
    }
    fun re_list(){
        if(item_list[0]=="empty"){
            //
            visible_text = arrayOf()
            val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    visible_text
            )
            binding.itemlist.adapter = adapter
        }
        else{
            item_id = Array(item_list.size){0}
            item_name = Array(item_list.size){""}
            item_num = Array(item_list.size){0}
            visible_text = Array(item_list.size){""}
            //System.out.println(item_list.size)
            item_size = item_list.size
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
        }
    }
    fun now_item_list_save(){
        var i = 1
        if (item_list[0]=="empty"){
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            pref.edit {
                putString("item_list","")
            }
        }
        else{
            var item_text = item_list[0]
            while (i<item_list.size){
                item_text = item_text + "," + item_list[i]
                i++
            }
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            pref.edit {
                putString("item_list",item_text)
            }
        }
    }
    fun item_type(no:Int){
        var item_id_no = item_id[no]
        val assetManager = resources.assets
        val inputStream = assetManager.open("item.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText()
        val jsonObject = JSONObject(str)
        val jsonArray_item = jsonObject.getJSONArray("item")
        val jsonData = jsonArray_item.getJSONObject(item_id_no)
        var type = jsonData.getInt("type")
        item_effect_num = jsonData.getInt("num")
        item_type_effect(no,type)
        inputStream.close()
        bufferedReader.close()
    }
    fun item_type_effect(no:Int,type:Int){
        if(type==0){
            //回復
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            var num = pref.getInt("pl_mp",0)
            if(num==max_mp){
                //最大値ｍｐ　->　使えない
                AlertDialog.Builder(requireContext())
                        .setTitle("使用")
                        .setMessage("MPが最大なので使用できません！")
                        .setPositiveButton("OK", { dialog, which ->
                            item_num[no] = item_num[no] + 1
                        })
                        .show()
            }
            else{
                //一応は減っている
                if(num+item_effect_num>=max_mp){
                    //最大値をこえる　->　ｍｐを最大値にする
                    pref.edit {
                        putInt("pl_mp",max_mp)
                    }
                }
                else if(num+item_effect_num<max_mp){
                    //飲んでもｍｐの最大値に届かない
                    pref.edit {
                        putInt("pl_mp",num+item_effect_num)
                    }
                }
            }
        }
        else if(type==1){
            //攻撃力　UP
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            var num = pref.getInt("pl_atk",0) + item_effect_num
            pref.edit {
                putInt("pl_atk",num)
            }
        }
    }
}