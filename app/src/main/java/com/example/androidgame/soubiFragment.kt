package com.example.androidgame

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [soubiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class soubiFragment : Fragment() {
    // TODO: Rename and change types of parameters
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_soubi, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment soubiFragment.
         */
        // TODO: Rename and change types and number of parameters

        private const val KEY_TEST = "test"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                soubiFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    // Viewの生成が完了した後に呼ばれる
    // UIパーツの設定などを行う
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var txt3 = pref.getInt("pl_hppl_max_hp",0).toString()

        //テキスト更新
        lateinit var txt: TextView

        lateinit var ber: ProgressBar

        txt3 = pref.getInt("pl_max_hp",0).toString()
        System.out.println(txt3)
        txt = view.findViewById<TextView>(R.id.MaxHP2)
        txt.text = txt3

        ber = view.findViewById<ProgressBar>(R.id.HPBar2)
        val a: Int = txt3.toInt()
        ber.max=a

        txt3 = pref.getInt("pl_hp",0).toString()
        txt = view.findViewById<TextView>(R.id.HP2)
        txt.text= txt3

        val b: Int = txt3.toInt()
        ber.progress=b

        txt3 = pref.getInt("pl_max_mp",0).toString()
        txt = view.findViewById<TextView>(R.id.MaxMP2)
        txt.text= txt3

        ber = view.findViewById<ProgressBar>(R.id.MPBar2)
        val c: Int = txt3.toInt()
        ber.max=c

        txt3 = pref.getInt("pl_mp",0).toString()
        txt = view.findViewById<TextView>(R.id.MP2)
        txt.text= txt3
        val d: Int = txt3.toInt()
        ber.max=d

        txt3 = pref.getInt("pl_atk",0).toString()
        txt = view.findViewById<TextView>(R.id.AttackPoint2)
        txt.text= txt3

        txt3 = pref.getInt("pl_def",0).toString()
        txt = view.findViewById<TextView>(R.id.DefensePoint2)
        txt.text= txt3

        txt = view.findViewById<TextView>(R.id.MagicPowerPoint2)
        txt.text= "100"

        txt = view.findViewById<TextView>(R.id.Weapon1)
        txt.text= "剣"
        txt = view.findViewById<TextView>(R.id.Shield1)
        txt.text= "盾"
        txt = view.findViewById<TextView>(R.id.Head1)
        txt.text= "頭"
        txt = view.findViewById<TextView>(R.id.Body1)
        txt.text= "鎧"

        txt3 = pref.getInt("pl_level",0).toString()
        txt = view.findViewById<TextView>(R.id.Level2)
        txt.text= txt3

    }
}