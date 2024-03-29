package com.example.androidgame

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.androidgame.databinding.FragmentSoubiBinding
import com.example.androidgame.databinding.FragmentStatusBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [statusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class statusFragment : Fragment() {

    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!

    private var param1: String? = null
    private var param2: String? = null

    private var testStr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }



    var my_atk_weapon = 0
    var my_shield_weapon = 1
    var my_head_weapon = 2
    var my_chest_weapon = 3
    // Viewの生成が完了した後に呼ばれる
    // UIパーツの設定などを行う
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        val assetManager = resources.assets
        val inputStream = assetManager.open("weapon_list.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText()
        val jsonObject = JSONObject(str)
        val jsonArray_weapon = jsonObject.getJSONArray("weapon")

        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var txt3 = pref.getInt("pl_hppl_max_hp", 0).toString()

        //テキスト更新
        lateinit var txt: TextView

        my_atk_weapon=pref.getInt("pl_atk_weapon", 0)
        my_shield_weapon=pref.getInt("pl_shield_weapon", 1)
        my_head_weapon=pref.getInt("pl_head_weapon", 2)
        my_chest_weapon=pref.getInt("pl_chest_weapon", 3)
        val atk_date = jsonArray_weapon.getJSONObject(my_atk_weapon)
        val shield_date = jsonArray_weapon.getJSONObject(my_shield_weapon)
        val head_date = jsonArray_weapon.getJSONObject(my_head_weapon)
        val chest_date = jsonArray_weapon.getJSONObject(my_chest_weapon)

        var txt1 = pref.getInt("pl_atk", 0)+atk_date.getInt("plus_atk")+shield_date.getInt("plus_atk")+head_date.getInt("plus_atk")+chest_date.getInt("plus_atk")
        binding.AttackPoint.text = txt1.toString()
        txt1 = pref.getInt("pl_def", 0)+atk_date.getInt("plus_def")+shield_date.getInt("plus_def")+head_date.getInt("plus_def")+chest_date.getInt("plus_def")
        binding.DefensePoint.text = txt1.toString()


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var txt3 = ""

        //テキスト更新
        lateinit var txt: TextView

        lateinit var ber: ProgressBar

        txt3 = pref.getInt("pl_max_hp",0).toString()
        System.out.println(txt3)
        txt = view.findViewById<TextView>(R.id.MaxHP)
        txt.text = txt3
        ber = view.findViewById<ProgressBar>(R.id.HPBar)
        val a: Int = txt3.toInt()
        ber.max=a

        txt3 = pref.getInt("pl_hp",0).toString()
        txt = view.findViewById<TextView>(R.id.HP)
        txt.text= txt3
        val b: Int = txt3.toInt()
        ber.progress=b


        txt3 = pref.getInt("pl_max_mp",0).toString()
        txt = view.findViewById<TextView>(R.id.MaxMP)
        txt.text= txt3
        ber = view.findViewById<ProgressBar>(R.id.MPBar)
        val c: Int = txt3.toInt()
        ber.max=c

        txt3 = pref.getInt("pl_mp",0).toString()
        txt = view.findViewById<TextView>(R.id.MP)
        txt.text= txt3
        val d: Int = txt3.toInt()
        ber.progress=d


        txt = view.findViewById<TextView>(R.id.MagicPowerPoint)
        txt.text= "100"

        txt = view.findViewById<TextView>(R.id.Weapon)
        txt.text= id_search_json_weapondate(pref.getInt("pl_atk_weapon",0))
        txt = view.findViewById<TextView>(R.id.Shield)
        txt.text= id_search_json_weapondate(pref.getInt("pl_shield_weapon",1))
        txt = view.findViewById<TextView>(R.id.Head)
        txt.text= id_search_json_weapondate(pref.getInt("pl_head_weapon",2))
        txt = view.findViewById<TextView>(R.id.Body)
        txt.text= id_search_json_weapondate(pref.getInt("pl_chest_weapon",3))

        var txt4 = pref.getInt("pl_level",0)
        txt = view.findViewById<TextView>(R.id.Level)
        txt.text= txt4.toString()

        txt3 = (txt4*100-pref.getInt("pl_exp",0)).toString()
        txt = view.findViewById<TextView>(R.id.NextLevel)
        txt.text = txt3

        var date = pref.getString("pl_ult_list","0,1,3,4").toString()
        var list:Array<Int> = date.split(",").map(String::toInt).toTypedArray()
        var i=0
        var ult_list_text = ""
        while(i<list.size){
            ult_list_text = ult_list_text + id_search_json_ult(list[i]) + "\n"+"\n"
            i++
        }
        txt = view.findViewById<TextView>(R.id.ult_text)
        txt.text = ult_list_text
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
    fun id_search_json_ult(no:Int):String{
        var name = ""
        val assetManager = resources.assets
        val inputStream = assetManager.open("ult_list.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText()
        val jsonObject = JSONObject(str)
        val jsonArray_weapon = jsonObject.getJSONArray("ult")
        val jsonData = jsonArray_weapon.getJSONObject(no)
        name = jsonData.getString("ult_name")
        inputStream.close()
        bufferedReader.close()
        return name
    }
}