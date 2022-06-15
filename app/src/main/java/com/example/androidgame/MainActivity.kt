package com.example.androidgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.androidgame.databinding.ActivityMainBinding
//import androidx.preference

class MainActivity : AppCompatActivity() {
    //定義
    private lateinit var binding:ActivityMainBinding

    //マス定義
    var max_height = 30
    var max_weight = 3
    var masu = Array(max_height){
        arrayOfNulls<Int>(max_weight)
    }

    //人間ステータス定義
    var human_type_height = 6
    var human_type_weight = 6
    var human = Array(human_type_weight){
        arrayOfNulls<Int>(human_type_height)
    }
    //人間名定義
    //var human_name = arrayOf("ファグラ","トートリ","アゲイン","エクレー","オフェリア")
    //var human_image = arrayOf("R.drawable.image1","R.drawable.image2","R.drawable.image3","R.drawable.image4","R.drawable.image5")

    //技(ult) ultの番号・ヒール系,攻撃系　判定変数・判定変数による値(例：ヒール系の値(100)なら100回復)
    var ult_height = 3
    var ult_weight = 3
    var ult = Array(ult_weight){
        arrayOfNulls<Int>(ult_height)
    }
    //技名
    var ult_name = arrayOf("ヒール","暗黒斬り")
    //
    var masu_num = 0
    var all_masu = 0
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        create_array()
        start_status()
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.daisu.setOnClickListener { daisu_start() }
        setContentView(binding.root)
    }
    //
    //
    //
    fun daisu_start(){
        val rnd_num = (Math.random()*6).toInt()+1
        //val pref = PreferenceManager.getDefaultSharedPreferences(this)
        all_masu = all_masu + rnd_num
        if(all_masu>=30){
            move()
        }
        else{
            binding.masucount.text = (30-all_masu).toString()
            masu_checker(all_masu)
        }
    }

    fun start_status(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putInt("HP",100).putInt("ATK",20).putInt("ult",0)
    }

    fun move(){
        //val intent = Intent(this,res2::class.java)
        //startActivity(intent)
        binding.result.text="到達!!"
    }

    fun masu_checker(num:Int){
        //val pref = PreferenceManager.getDefaultSharedPreferences(this)
        //val editor = pref.edit()
        //editor.putInt("num",num)
        if(masu[num][1]==1){
            //戦闘
            binding.result.text = "戦闘！！"
            var no = masu[num][2]
            set_enemy(no)
        }
        else if(masu[num][1]==2){
            //アイテムゲット
            binding.result.text = "アイテムゲット！！"
            binding.enemyImage.setImageResource(R.drawable.po)
        }
        else{
            binding.result.text = "なし！！"
            binding.enemyImage.setImageResource(R.drawable.notevent3)
        }
    }

    //img_set
    fun set_enemy(no:Int?){
        if (no==0){
            binding.enemyImage.setImageResource(R.drawable.image1)
        }
        else if (no==1){
            binding.enemyImage.setImageResource(R.drawable.image2)
        }
        else if (no==2){
            binding.enemyImage.setImageResource(R.drawable.image3)
        }
        else if (no==3){
            binding.enemyImage.setImageResource(R.drawable.image4)
        }
        else if (no==4){
            binding.enemyImage.setImageResource(R.drawable.image5)
        }
    }

    //enemy_atack
    fun enemy_daisu(){

    }

    //
    fun create_array(){
        //masu
        var height = 0
        var weight = 0
        //
        while(height<max_height){
            weight = 0
            while (weight<max_weight){
                masu[height][weight] = weight+1
                weight++
                val type = (Math.random()*3).toInt()
                masu[height][weight] = type
                weight++
                if (type==1){
                    val enemy = (Math.random()*human_type_height).toInt()
                    masu[height][weight] = enemy
                }
                else{
                    masu[height][weight] = 99
                }
                weight++
            }
            height++
        }
        //monster
        height = 0
        while (height<human_type_height){
            human[height][0] = height+1
            height++
        }
        //atk
        human[0][1] = 100
        human[1][1] = 100
        human[2][1] = 100
        human[3][1] = 100
        human[4][1] = 100
        //def
        human[0][2] = 100
        human[1][2] = 100
        human[2][2] = 100
        human[3][2] = 100
        human[4][2] = 100
        //mp
        human[0][3] = 10
        human[1][3] = 10
        human[2][3] = 10
        human[3][3] = 10
        human[4][3] = 10
        //ult
        human[0][4] = 1
        human[1][4] = 1
        human[2][4] = 1
        human[3][4] = 1
        human[4][4] = 1
        //exp
        human[0][5] = 20
        human[1][5] = 20
        human[2][5] = 20
        human[3][5] = 20
        human[4][5] = 20

        //ult

        height = 0
        while (height<ult_height){
            ult[height][0] = height+1
            height++
        }
        //0->ヒール　・　1->攻撃
        ult[0][1] = 0
        ult[1][1] = 1
        //num
        ult[0][2] = 100
        ult[1][2] = 100
    }
}