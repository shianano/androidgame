package com.example.androidgame

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.androidgame.databinding.ActivityMainBinding
import com.example.androidgame.databinding.FragmentSettingBinding
import com.example.androidgame.databinding.FragmentSoubiBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader


class soubiFragment : Fragment() {
    private var _binding: FragmentSoubiBinding? = null
    private val binding get() = _binding!!

    //
    lateinit var slist: Spinner
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
    var my_soubi = "0,1,2,3,4,5,6,7,8,9"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSoubiBinding.inflate(inflater, container, false)
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var txt3 = pref.getInt("pl_hppl_max_hp",0).toString()

        //テキスト更新
        lateinit var txt: TextView

        lateinit var ber: ProgressBar
        txt3 = pref.getInt("pl_max_hp",0).toString()
        binding.maxHPS.text = txt3

        val a: Int = txt3.toInt()
        binding.HPBar2S.max = a

        txt3 = pref.getInt("pl_hp",0).toString()
        binding.HP2S.text = txt3

        val b: Int = txt3.toInt()
        binding.HPBar2S.progress = b



        txt3 = pref.getInt("pl_max_mp",0).toString()
        binding.maxMPS.text = txt3

        val c: Int = txt3.toInt()
        binding.MPBar2S.max = c

        txt3 = pref.getInt("pl_mp",0).toString()
        binding.MP2S.text = txt3

        val d: Int = txt3.toInt()
        binding.MPBar2S.progress=d

        txt3 = pref.getInt("pl_atk",0).toString()
        binding.attackPointS.text = txt3

        txt3 = pref.getInt("pl_def",0).toString()
        binding.defensePointS.text = txt3

        binding.magicPowerPointS.text = "100"


        binding.weaponS.text = id_search_json_weapondate(pref.getInt("pl_atk_weapon",0))
        binding.shieldS.text = id_search_json_weapondate(pref.getInt("pl_shield_weapon",1))
        binding.headS.text = id_search_json_weapondate(pref.getInt("pl_head_weapon",2))
        binding.body.text = id_search_json_weapondate(pref.getInt("pl_chest_weapon",3))


        txt3 = pref.getInt("pl_level",0).toString()
        binding.level2S.text = txt3



        binding.bukiButton.setOnClickListener {
            binding.bukiList.setVisibility(View.VISIBLE)
            System.out.println("aaa")
            my_weapon_set(0)
        }
        binding.tateButton.setOnClickListener {
            binding.tateList.setVisibility(View.VISIBLE)
            my_weapon_set(1)
        }
        binding.kabutoButton.setOnClickListener {
            binding.atamaList.setVisibility(View.VISIBLE)
            my_weapon_set(2)
        }
        binding.yoroiButton.setOnClickListener {
            binding.yoroiList.setVisibility(View.VISIBLE)
            my_weapon_set(3)
        }
        return binding.root
    }

    fun id_search_json_weapondate(no:Int):String{
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

    // Viewの生成が完了した後に呼ばれる
    // UIパーツの設定などを行う
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    //装備の選択
    fun my_weapon_set(no: Int):Int{
        val pref = PreferenceManager.getDefaultSharedPreferences(activity)

        if(no==0){
            var txttt = pref.getString("pl_atk_weapon_list", "0").toString()
            my_soubi_set = txttt.split(",").map(String::toInt).toTypedArray()

            my_soubi_set_buki = Array(my_soubi_set.size){1}
            my_soubi_buki_name = Array(my_soubi_set.size){""}

            var i = 0
            while (i<my_soubi_set.size){
                System.out.println(my_soubi_set[i].toString())
                my_soubi_set_buki[i] = my_soubi_set[i]
                //my_soubi_buki_name[i] = soubi_name[my_soubi_set[i]] + "攻撃力" + plus_atk[my_soubi_set[i]] + "防御力" + plus_def[my_soubi_set[i]]
                my_soubi_buki_name[i] = id_search_json_weapondate( my_soubi_set_buki[i])
                i++
            }
            //var weapon_list_txt = id_search_json_weapondate( )

            val adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,my_soubi_buki_name)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.bukiList.adapter = adapter

        }else if(no==1){
            var txttt = pref.getString("pl_shield_weapon_list", "1").toString()
            my_soubi_set = txttt.split(",").map(String::toInt).toTypedArray()

            my_soubi_set_tate = Array(my_soubi_set.size){1}
            my_soubi_tate_name = Array(my_soubi_set.size){""}

            var i = 0
            while (i<my_soubi_set.size){
                my_soubi_set_tate[i] = my_soubi_set[i]
                //my_soubi_buki_name[i] = soubi_name[my_soubi_set[i]] + "攻撃力" + plus_atk[my_soubi_set[i]] + "防御力" + plus_def[my_soubi_set[i]]
                my_soubi_tate_name[i] = id_search_json_weapondate( my_soubi_set_tate[i])
                i++
            }
            //var weapon_list_txt = id_search_json_weapondate( )

            val adapter2 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,my_soubi_tate_name)
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.tateList.adapter = adapter2

        }else if(no==2){
            var txttt = pref.getString("pl_head_weapon_list", "2").toString()
            my_soubi_set = txttt.split(",").map(String::toInt).toTypedArray()

            my_soubi_set_atama = Array(my_soubi_set.size){1}
            my_soubi_atama_name = Array(my_soubi_set.size){""}

            var i = 0
            while (i<my_soubi_set.size){
                my_soubi_set_atama[i] = my_soubi_set[i]
                my_soubi_atama_name[i] = id_search_json_weapondate( my_soubi_set_atama[i])
                i++
            }

            val adapter3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,my_soubi_atama_name)
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.atamaList.adapter = adapter3
        }else if(no==3){
            var txttt = pref.getString("pl_chest_weapon_list", "3").toString()
            my_soubi_set = txttt.split(",").map(String::toInt).toTypedArray()

            my_soubi_set_yoroi = Array(my_soubi_set.size){1}
            my_soubi_yoroi_name = Array(my_soubi_set.size){""}

            var i = 0
            while (i<my_soubi_set.size){
                my_soubi_set_yoroi[i] = my_soubi_set[i]
                my_soubi_yoroi_name[i] = id_search_json_weapondate( my_soubi_set_yoroi[i])
                i++
            }

            val adapter4 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,my_soubi_yoroi_name)
            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.yoroiList.adapter = adapter4
        }

        /*
        val assetManager = resources.assets
        val inputStream = assetManager.open("weapon_list.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText()
        val jsonObject = JSONObject(str)
        val jsonArray_weapon = jsonObject.getJSONArray("weapon")
        val jsonData = jsonArray_weapon.getJSONObject(1)

        var weapon_type = jsonData.getInt("weapon_type")
        var weapon_name = jsonData.getString("weapon_name")
        */


        var sta = 0
        var sta2 = 0
        var sta3 = 0
        var sta4 = 0
        var sta5 = 0


        my_soubi_set_atama = Array(my_soubi_set.size){1}
        my_soubi_set_yoroi = Array(my_soubi_set.size){1}


        my_soubi_atama_name = Array(my_soubi_set.size){""}
        my_soubi_yoroi_name = Array(my_soubi_set.size){""}



        /*while(sta<my_soubi_set.size){
            if(soubi_type[my_soubi_set[sta]]==0){

                sta2++
            }
            else if(soubi_type[my_soubi_set[sta]]==1){
                my_soubi_set_tate[sta3] = my_soubi_set[sta]
                my_soubi_tate_name[sta3] = soubi_name[my_soubi_set[sta]] + "攻撃力" + plus_atk[my_soubi_set[sta]] + "防御力" + plus_def[my_soubi_set[sta]]
                sta3++
            }
            else if(soubi_type[my_soubi_set[sta]]==2){
                my_soubi_set_atama[sta4] = my_soubi_set[sta]
                my_soubi_atama_name[sta4] = soubi_name[my_soubi_set[sta]] + "攻撃力" + plus_atk[my_soubi_set[sta]] + "防御力" + plus_def[my_soubi_set[sta]]
                sta4++
            }
            else if(soubi_type[my_soubi_set[sta]]==3){
                my_soubi_set_yoroi[sta5] = my_soubi_set[sta]
                my_soubi_yoroi_name[sta5] = soubi_name[my_soubi_set[sta]] + "攻撃力" + plus_atk[my_soubi_set[sta]] + "防御力" + plus_def[my_soubi_set[sta]]
                sta5++
            }
            sta++
        }*/


        return 0
    }
}
