package com.example.androidgame

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.edit
import com.example.androidgame.databinding.FragmentSoubiBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader


class soubiFragment : Fragment() {
    private var _binding: FragmentSoubiBinding? = null
    private val binding get() = _binding!!

    var plus_atk: Array<Int> = arrayOf()
    var plus_def: Array<Int> = arrayOf()
    var soubi_name: Array<String> = arrayOf()
    var soubi_type: Array<Int> = arrayOf()
    var my_soubi_set: Array<Int> = arrayOf()
    var my_soubi_set_buki: Array<Int> = arrayOf()
    var my_soubi_buki_name: Array<String> = arrayOf()
    var my_soubi_set_tate: Array<Int> = arrayOf()
    var my_soubi_tate_name: Array<String> = arrayOf()
    var my_soubi_set_atama: Array<Int> = arrayOf()
    var my_soubi_atama_name: Array<String> = arrayOf()
    var my_soubi_set_yoroi: Array<Int> = arrayOf()
    var my_soubi_yoroi_name: Array<String> = arrayOf()
    var weapon_list:Array<String> = arrayOf()
    var visible_text:Array<String> = arrayOf()

    var my_soubi_buki_id: Array<Int> = arrayOf()
    var my_soubi_tate_id: Array<Int> = arrayOf()
    var my_soubi_atama_id: Array<Int> = arrayOf()
    var my_soubi_yoroi_id: Array<Int> = arrayOf()

    var select_weapon_name = ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSoubiBinding.inflate(inflater, container, false)
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var txt3 = pref.getInt("pl_hppl_max_hp", 0).toString()

        //テキスト更新
        lateinit var txt: TextView

        lateinit var ber: ProgressBar
        txt3 = pref.getInt("pl_max_hp", 0).toString()
        binding.maxHPS.text = txt3

        val a: Int = txt3.toInt()
        binding.HPBar2S.max = a

        txt3 = pref.getInt("pl_hp", 0).toString()
        binding.HP2S.text = txt3

        val b: Int = txt3.toInt()
        binding.HPBar2S.progress = b



        txt3 = pref.getInt("pl_max_mp", 0).toString()
        binding.maxMPS.text = txt3

        val c: Int = txt3.toInt()
        binding.MPBar2S.max = c

        txt3 = pref.getInt("pl_mp", 0).toString()
        binding.MP2S.text = txt3

        val d: Int = txt3.toInt()
        binding.MPBar2S.progress = d

        txt3 = pref.getInt("pl_atk", 0).toString()
        binding.attackPointS.text = txt3

        txt3 = pref.getInt("pl_def", 0).toString()
        binding.defensePointS.text = txt3

        binding.magicPowerPointS.text = "100"


        binding.weaponS.text = id_search_json_weapondate(pref.getInt("pl_atk_weapon", 0))
        binding.shieldS.text = id_search_json_weapondate(pref.getInt("pl_shield_weapon", 1))
        binding.headS.text = id_search_json_weapondate(pref.getInt("pl_head_weapon", 2))
        binding.body.text = id_search_json_weapondate(pref.getInt("pl_chest_weapon", 3))


        txt3 = pref.getInt("pl_level", 0).toString()
        binding.level2S.text = txt3


        //装備の変更ボタン
        var buki = 0
        var tate = 0
        var atama = 0
        var yoroi = 0

        my_weapon_set(0)
        my_weapon_set(1)
        my_weapon_set(2)
        my_weapon_set(3)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, my_soubi_buki_name)
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.bukiList.adapter = adapter
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, my_soubi_tate_name)
        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tateList.adapter = adapter2
        val adapter3 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, my_soubi_atama_name)
        //adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.atamaList.adapter = adapter3
        val adapter4 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, my_soubi_yoroi_name)
        //adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.yoroiList.adapter = adapter4

        binding.bukiButton.setOnClickListener {
            if (buki == 0) {
                buki = 1
                binding.bukiList.setVisibility(View.VISIBLE)
                binding.tateList.setVisibility(View.INVISIBLE)
                binding.atamaList.setVisibility(View.INVISIBLE)
                binding.yoroiList.setVisibility(View.INVISIBLE)
                System.out.println("aaa")
            } else if (buki == 1) {
                buki = 0
                binding.bukiList.setVisibility(View.INVISIBLE)
            }
        }
        binding.tateButton.setOnClickListener {
            if (tate == 0) {
                tate = 1
                binding.bukiList.setVisibility(View.INVISIBLE)
                binding.tateList.setVisibility(View.VISIBLE)
                binding.atamaList.setVisibility(View.INVISIBLE)
                binding.yoroiList.setVisibility(View.INVISIBLE)
            } else if (tate == 1) {
                tate = 0
                binding.tateList.setVisibility(View.INVISIBLE)
            }
        }
        binding.kabutoButton.setOnClickListener {
            if (atama == 0) {
                atama = 1
                binding.bukiList.setVisibility(View.INVISIBLE)
                binding.tateList.setVisibility(View.INVISIBLE)
                binding.atamaList.setVisibility(View.VISIBLE)
                binding.yoroiList.setVisibility(View.INVISIBLE)
            } else if (atama == 1) {
                atama = 0
                binding.atamaList.setVisibility(View.INVISIBLE)
            }
        }
        binding.yoroiButton.setOnClickListener {
            if (yoroi == 0) {
                yoroi = 1
                binding.bukiList.setVisibility(View.INVISIBLE)
                binding.tateList.setVisibility(View.INVISIBLE)
                binding.atamaList.setVisibility(View.INVISIBLE)
                binding.yoroiList.setVisibility(View.VISIBLE)
            } else if (yoroi == 1) {
                yoroi = 0
                binding.yoroiList.setVisibility(View.INVISIBLE)
            }
        }

        //イベント
        binding.bukiList.setOnItemClickListener { parent, view, position, id ->
            System.out.println(my_soubi_buki_name[position])
        }
        binding.tateList.setOnItemClickListener { parent, view, position, id ->
            System.out.println(my_soubi_tate_name[position])
        }
        binding.atamaList.setOnItemClickListener { parent, view, position, id ->
            System.out.println(my_soubi_atama_name[position])
        }
        binding.yoroiList.setOnItemClickListener { parent, view, position, id ->
            System.out.println(my_soubi_yoroi_name[position])
        }

        return binding.root
    }

    fun id_search_json_weapondate(no: Int): String {
        var name = ""
        val assetManager = resources.assets
        val inputStream = assetManager.open("weapon_list.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText()
        val jsonObject = JSONObject(str)
        val jsonArray_weapon = jsonObject.getJSONArray("weapon")
        val jsonData = jsonArray_weapon.getJSONObject(no)
        name = jsonData.getString("weapon_name")
        inputStream.close()
        bufferedReader.close()
        return name
    }
    //入手した装備を表示
    fun my_weapon_set(no: Int){
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)

        if (no == 0) {
            var txttt = pref.getString("pl_atk_weapon_list", "0").toString()
            my_soubi_buki_id = txttt.split(",").map(String::toInt).toTypedArray()

            my_soubi_set_buki = Array(my_soubi_buki_id.size) { 1 }
            my_soubi_buki_name = Array(my_soubi_buki_id.size) { "" }

            var i = 0
            while (i < my_soubi_buki_id.size) {
                System.out.println(my_soubi_buki_id[i].toString())
                my_soubi_set_buki[i] = my_soubi_buki_id[i]
                //my_soubi_buki_name[i] = soubi_name[my_soubi_set[i]] + "攻撃力" + plus_atk[my_soubi_set[i]] + "防御力" + plus_def[my_soubi_set[i]]
                my_soubi_buki_name[i] = id_search_json_weapondate(my_soubi_set_buki[i])
                i++
            }
            //var weapon_list_txt = id_search_json_weapondate( )

        } else if (no == 1) {
            var txttt = pref.getString("pl_shield_weapon_list", "1").toString()
            my_soubi_tate_id = txttt.split(",").map(String::toInt).toTypedArray()

            my_soubi_set_tate = Array(my_soubi_tate_id.size) { 1 }
            my_soubi_tate_name = Array(my_soubi_tate_id.size) { "" }

            var i = 0
            while (i < my_soubi_tate_id.size) {
                my_soubi_set_tate[i] = my_soubi_tate_id[i]
                my_soubi_tate_name[i] = id_search_json_weapondate(my_soubi_set_tate[i])
                i++
            }
            //var weapon_list_txt = id_search_json_weapondate( )

        } else if (no == 2) {
            var txttt = pref.getString("pl_head_weapon_list", "2").toString()
            my_soubi_atama_id = txttt.split(",").map(String::toInt).toTypedArray()

            my_soubi_set_atama = Array(my_soubi_atama_id.size) { 1 }
            my_soubi_atama_name = Array(my_soubi_atama_id.size) { "" }

            var i = 0
            while (i < my_soubi_atama_id.size) {
                my_soubi_set_atama[i] = my_soubi_atama_id[i]
                my_soubi_atama_name[i] = id_search_json_weapondate(my_soubi_set_atama[i])
                i++
            }
        } else if (no == 3) {
            var txttt = pref.getString("pl_chest_weapon_list", "3").toString()
            my_soubi_yoroi_id = txttt.split(",").map(String::toInt).toTypedArray()

            my_soubi_set_yoroi = Array(my_soubi_yoroi_id.size) { 1 }
            my_soubi_yoroi_name = Array(my_soubi_yoroi_id.size) { "" }

            var i = 0
            while (i < my_soubi_yoroi_id.size) {
                my_soubi_set_yoroi[i] = my_soubi_yoroi_id[i]
                my_soubi_yoroi_name[i] = id_search_json_weapondate(my_soubi_set_yoroi[i])
                i++
            }
        }
        var sta = 0
        var sta2 = 0
        var sta3 = 0
        var sta4 = 0
        var sta5 = 0
    }
}