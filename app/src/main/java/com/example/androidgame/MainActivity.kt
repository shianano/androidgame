package com.example.androidgame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AppLaunchChecker
import androidx.core.content.edit
import com.example.androidgame.databinding.ActivityMainBinding
import org.json.JSONArray

//import androidx.preference

class MainActivity : AppCompatActivity() {
    //定義
    private lateinit var binding:ActivityMainBinding

    //自分ステータス
    var hp = 0
    var atk = 0
    var def = 0
    var po = 0

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
    //人間と戦闘時ステータスを入れて戦闘

    //技(ult) ultの番号・ヒール系,攻撃系　判定変数・判定変数による値(例：ヒール系の値(100)なら100回復)
    var ult_height = 3
    var ult_weight = 3
    var ult = Array(ult_weight){
        arrayOfNulls<Int>(ult_height)
    }
    //技名
    var ult_name = arrayOf("ヒール", "暗黒斬り")
    //
    var masu_num = 0
    var all_masu = 0
    //
    var type = 0
    var human_daisu = 0
    var devil_daisu = 0
    var round = 0
    var enemy_no = 0
    var enemy_hp = 0
    var enemy_atk = 0
    var enemy_def = 0
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.daisu.setOnClickListener { daisu_start() }
        binding.usepo.setOnClickListener { use_portion() }
        binding.menu.setOnClickListener { menu_change() }
        binding.save.setOnClickListener { save() }
        //
        create_array()
        //save chack
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        if (pref.getInt("pl_check",0)==0){
            status()
            System.out.println("ok")
        }
        else{
            load_status()
            System.out.println("no")
        }

        setContentView(binding.root)
    }
    //
    //
    fun daisu_start(){
        if (type==0){
            devil_daisu = (Math.random()*6).toInt()+1
            //val pref = PreferenceManager.getDefaultSharedPreferences(this)
            all_masu = all_masu + devil_daisu
        }
        if(all_masu >= 30 && type == 0){
            binding.enemyImage.setImageResource(R.drawable.clear)
            binding.masucount.text="0"
        }
        else if(type==0){
            binding.masucount.text = (30-all_masu).toString()
            masu_checker(all_masu)
        }
        else if (type==1){
            btl()
        }
    }

    //初回ステータス保存
    fun status(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        // 保存
        pref.edit{
            putInt("pl_hp", 100)
            putInt("pl_atk", 5)
            putInt("pl_def", 10)
            putInt("pl_mp", 20)
            putInt("pl_po", 0)
            putInt("pl_masu",0)
            putInt("pl_check",1)
        }
        hp=pref.getInt("pl_hp", 0)
        atk=pref.getInt("pl_atk", 0)
        def=pref.getInt("pl_def", 0)
        po=pref.getInt("pl_po", 0)
        masu_num=pref.getInt("pl_masu",0)
        binding.hp.text = hp.toString()
        binding.atk.text = atk.toString()
        binding.def.text = def.toString()
        binding.masucount.text = (30-masu_num).toString()
    }
    //2回目以降（途中で止めたデータ）
    fun load_status(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        hp=pref.getInt("pl_hp", 0)
        atk=pref.getInt("pl_atk", 0)
        def=pref.getInt("pl_def", 0)
        po=pref.getInt("pl_po", 0)
        masu_num=pref.getInt("pl_masu",0)
        binding.hp.text = hp.toString()
        binding.atk.text = atk.toString()
        binding.def.text = def.toString()
        binding.masucount.text = (30-masu_num).toString()
    }
    //save
    fun save(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit{
            putInt("pl_hp", Integer.parseInt(binding.hp.text.toString()))
            putInt("pl_atk", Integer.parseInt(binding.atk.text.toString()))
            putInt("pl_def", Integer.parseInt(binding.def.text.toString()))
            putInt("pl_mp", 20)
            putInt("pl_po", Integer.parseInt(binding.portionNum.text.toString()))
            putInt("pl_masu",30-Integer.parseInt(binding.masucount.text.toString()))
            putInt("pl_check",1)
        }
    }
    //
    //
    //ボスマス移動
    fun move(){
        //val intent = Intent(this,res2::class.java)
        //startActivity(intent)
        binding.result.text="到達!!"
    }
    //ゲームオーバー移動
    fun game_over(){
        val intent = Intent(this, Gameover::class.java)
        startActivity(intent)
    }
    //メニュー移動
    fun menu_change(){
        //val intent = Intent(this,SubActivity::class.java)
        //startActivity(intent)
        setContentView(R.layout.activity_sub)
    }
    //マス確認
    fun masu_checker(num: Int){
        if(masu[num][1]==1){
            //戦闘
            binding.result.text = "戦闘！！"
            binding.no.text = masu[num][2].toString()
            enemy_no = Integer.parseInt(binding.no.text.toString())
            set_enemy(enemy_no)
            open_enemy_status(enemy_no)
            type = 1
        }
        else if(masu[num][1]==2){
            //アイテムゲット
            binding.result.text = "アイテムゲット！！"
            binding.enemyImage.setImageResource(R.drawable.po)
            po = po + 1
            binding.portionNum.text = po.toString()
        }
        else{
            binding.result.text = "なし！！"
            binding.enemyImage.setImageResource(R.drawable.notevent3)
        }
    }
    //イメージセット
    fun set_enemy(no: Int){
        enemy_no = no
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
    //ポーション使用
    fun use_portion(){
        if (po>0){
            var ing_hp = Integer.parseInt(binding.hp.text.toString())
            ing_hp = ing_hp + 20
            binding.hp.text = ing_hp.toString()
            binding.result.text = ""
            binding.meResult.text = "20回復した"
            po--
            binding.portionNum.text = po.toString()
        }
        else if (po==0){
            binding.portionNum.text = po.toString()
        }
    }
    //戦闘モード
    fun btl(){
        //敵のHP
        var human_hp = Integer.parseInt(binding.hpnum.text.toString())
        var human_atk = Integer.parseInt(binding.atknum.text.toString())
        var human_def = Integer.parseInt(binding.defnum.text.toString())
        var me_atk_dmg = 0
        var human_atk_dmg = 0
        me_atk_dmg = human_def - (atk*devil_daisu)
        if (me_atk_dmg<=0){
            me_atk_dmg = 0
        }
        var result_human_hp = human_hp - me_atk_dmg
        if(result_human_hp<=0){
            binding.result.text = "倒した！"
            binding.meResult.text = ""
            binding.enemyImage.setImageResource(R.drawable.taosita)
            invisible_enemy_status()
            type = 0
        }
        else{
            enemy_daisu()
            human_atk_dmg = def - human_daisu*human_atk
            if (human_atk_dmg<=0){
                human_atk_dmg = 0
            }
            hp = hp - human_atk_dmg
            var text = "相手："+ me_atk_dmg + "ダメージ"
            binding.hpnum.text = human_hp.toString()
            binding.result.text = text
            text = "自分：" + human_atk_dmg + "ダメージ"
            //
            if (hp>0){
                binding.hp.text = hp.toString()
                binding.meResult.text = text
            }
            else{
                game_over()
            }
        }
        round++
    }
    //相手攻撃
    fun enemy_daisu(){
        human_daisu = (Math.random()*6).toInt()+1
    }
    //相手のステータスを表示
    fun open_enemy_status(no: Int){
        binding.hptext.setVisibility(View.VISIBLE)
        binding.hpnum.setVisibility(View.VISIBLE)
        binding.atktext.setVisibility(View.VISIBLE)
        binding.atknum.setVisibility(View.VISIBLE)
        binding.deftext.setVisibility(View.VISIBLE)
        binding.defnum.setVisibility(View.VISIBLE)
        set_enemy_status(no)
    }
    //相手のステータスを非表示
    fun invisible_enemy_status(){
        binding.hptext.setVisibility(View.INVISIBLE)
        binding.hpnum.setVisibility(View.INVISIBLE)
        binding.atktext.setVisibility(View.INVISIBLE)
        binding.atknum.setVisibility(View.INVISIBLE)
        binding.deftext.setVisibility(View.INVISIBLE)
        binding.defnum.setVisibility(View.INVISIBLE)
    }
    //相手のステータスの数値を表示
    fun set_enemy_status(no: Int){
        binding.hpnum.text=human[no][1].toString()
        binding.atknum.text=human[no][2].toString()
        binding.defnum.text=human[no][3].toString()
        enemy_hp = Integer.parseInt(binding.hpnum.text.toString())
        enemy_atk = Integer.parseInt(binding.atknum.text.toString())
        enemy_def = Integer.parseInt(binding.defnum.text.toString())
    }
    //マス、相手のステータス、技などを作る
    fun create_array(){
        //masu
        var height = 0
        var weight = 0
        var enemy = 0
        var re_daisu = 4
        //
        while(height<max_height){
            weight = 0
            while (weight<max_weight){
                //マス番号1-30　masu[][0]
                masu[height][weight] = weight+1
                weight++
                val m_type = (Math.random()*3).toInt()
                //0->なし　1->戦闘　2->アイテム　masu[][1]
                masu[height][weight] = m_type
                weight++
                if (m_type == 1 && enemy<5){
                    //masu[][2]　戦闘相手の番号
                    masu[height][weight] = enemy
                    enemy++
                }
                else if(m_type == 1 && enemy>=5){
                    while (re_daisu==2||re_daisu==0){
                        val re_m_type = (Math.random()*3).toInt()
                        re_daisu =re_m_type
                    }
                    masu[height][weight - 1] = re_daisu
                }
                enemy++
                weight++
            }
            height++
        }
        //monster
        height = 0
        //human[][0]-> 相手の番号
        while (height<human_type_height){
            human[height][0] = height+1
            height++
        }
        //hp
        human[0][1] = 100
        human[1][1] = 90
        human[2][1] = 80
        human[3][1] = 70
        human[4][1] = 60
        //atk
        human[0][2] = 2
        human[1][2] = 3
        human[2][2] = 3
        human[3][2] = 4
        human[4][2] = 3
        //def
        human[0][3] = 10
        human[1][3] = 9
        human[2][3] = 8
        human[3][3] = 7
        human[4][3] = 6
        //mp
        human[0][4] = 1
        human[1][4] = 1
        human[2][4] = 1
        human[3][4] = 1
        human[4][4] = 1
        //ult
        human[0][5] = 1
        human[1][5] = 1
        human[2][5] = 1
        human[3][5] = 1
        human[4][5] = 1

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