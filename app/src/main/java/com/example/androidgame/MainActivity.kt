package com.example.androidgame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SPEECH
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AppLaunchChecker
import androidx.core.content.edit
import androidx.media.AudioAttributesCompat.CONTENT_TYPE_SPEECH
import com.example.androidgame.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.jar.Attributes
import kotlin.concurrent.thread
import kotlin.math.log


//import androidx.preference

class MainActivity : AppCompatActivity() {
    //定義
    private lateinit var binding: ActivityMainBinding

    //サウンド
    lateinit var soundPool: SoundPool
    var atk_sound = 0
    var daisu_sound = 0
    var dark_bgm = 0
    var skl_heal = 0
    var skl_soud = 0
    var drunk_po = 0
    var bossbgm = 0
    //MediaPlayer
    lateinit var mp0:MediaPlayer //start
    lateinit var btl_sound:MediaPlayer//buttle

    //自分ステータス
    var hp = 0
    var max_hp = 0
    var atk = 0
    var def = 0
    var po = 0
    var mp = 0
    var level = 1
    var exp_all = 0
    var my_skil = "0,2,3,4"
    var my_ult_set: Array<Int> = arrayOf()
    var my_ult_set_atk: Array<Int> = arrayOf()
    var my_ult_atk_name: Array<String> = arrayOf()
    var my_ult_set_heal: Array<Int> = arrayOf()
    var my_ult_heal_name: Array<String> = arrayOf()
    var my_atk_weapon = 0
    var my_shield_weapon = 1
    var my_head_weapon = 2
    var my_chest_weapon = 3
    //
    //

    //マス定義
    var max_height = 30// = 0
    //test input
    var masu_event: Array<Int> = arrayOf()//var masu_event = IntArray(max_height)
    var masu_result_num: Array<Int> = arrayOf()//var masu_result_num = arrayof()

    //人間ステータス定義
    //var human_type_height = 6
    //var human_type_weight = 6
    //test input
    var human_hp: Array<Int> = arrayOf()
    var human_atk: Array<Int> = arrayOf()
    var human_def: Array<Int> = arrayOf()
    var human_mp: Array<Int> = arrayOf()
    var human_ult: Array<String> = arrayOf()
    //set
    var human_ult_set: Array<Int> = arrayOf()
    //人間と戦闘時ステータスを入れて戦闘

    //技(ult) ultの番号・[0]ヒール系,[1]攻撃系　判定変数・判定変数による値(例：ヒール系の値(100)なら100回復)
    var ult_type: Array<Int> = arrayOf()
    var ult_result_num: Array<Int> = arrayOf()
    var ult_use_mp: Array<Int> = arrayOf()
    var ult_name: Array<String> = arrayOf()
    var ult_select_name = ""
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
    var enemy_mp = 0
    var soud_ch = 0
    var bgm_ch = 0
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //
        //
        ult_list_set()
        //
        val assetManager = resources.assets
        val inputStream = assetManager.open("RPG_Data.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText()
        val jsonObject = JSONObject(str)
        val jsonArray = jsonObject.getJSONArray("enemy")
        //
        //
        human_hp = Array(jsonArray.length()){0}
        human_atk = Array(jsonArray.length()){0}
        human_def = Array(jsonArray.length()){0}
        human_mp = Array(jsonArray.length()){0}
        human_ult = Array(jsonArray.length()){"0"}
        //
        //
        try {
            for (i in 0 until jsonArray.length()) {
                val jsonData = jsonArray.getJSONObject(i)
                human_hp[i] = jsonData.getString("HP").toInt()
                human_atk[i] = jsonData.getString("ATK").toInt()
                human_def[i] = jsonData.getString("DEF").toInt()
                human_mp[i] = jsonData.getString("MP").toInt()
                human_ult[i] = jsonData.getString("ult")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        //
        val jsonArray_masu = jsonObject.getJSONArray("masu_data")
        masu_event = Array(jsonArray_masu.length()){0}
        masu_result_num = Array(jsonArray_masu.length()){0}
        max_height = jsonArray_masu.length()
        try {
            for (i in 0 until jsonArray_masu.length()) {
                val jsonData = jsonArray_masu.getJSONObject(i)
                masu_event[i] = jsonData.getString("masu_event").toInt()
                masu_result_num[i] = jsonData.getString("masu_result_num").toInt()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        inputStream.close()
        bufferedReader.close()
        //
        //
        mp0 = MediaPlayer.create(this,R.raw.firstbgm)
        mp0.isLooping = true
        btl_sound = MediaPlayer.create(this,R.raw.normalbattlebgm)
        btl_sound.isLooping = true
        //
        soundPool = SoundPool.Builder().run {
            val audioAttributes = AudioAttributes.Builder().run {
                setUsage(AudioAttributes.USAGE_MEDIA)
                build()
            }
            setMaxStreams(5)
            setAudioAttributes(audioAttributes)
            build()
        }
        atk_sound = soundPool.load(this,R.raw.sordattack2,1)
        daisu_sound = soundPool.load(this,R.raw.daisu,2)
        skl_heal = soundPool.load(this,R.raw.sklheal,0)
        skl_soud = soundPool.load(this,R.raw.sklsoud,0)
        drunk_po = soundPool.load(this,R.raw.drunks,0)
        bossbgm = soundPool.load(this,R.raw.bossbgm,0)
        //
        binding.daisu.setOnClickListener {
            if (type==1){
                if(soud_ch==0){
                    //soundPool.play(dark_bgm,1.0f,100f,0,-1,0.5f)
                    soud_ch = 1
                }
                soundPool.play(atk_sound,10f,10f,0,0,1.0f)
            }
            else if(type==0){
                soundPool.play(daisu_sound,10f,10f,0,0,1.0f)
                if(bgm_ch==0){
                    bgm_ch=1
                }
            }
            daisu_start()
        }
        binding.usepo.setOnClickListener { use_portion() }
        binding.menu.setOnClickListener { menu_change() }
        binding.atkButton.setOnClickListener { my_skl(1) }
        binding.healButton.setOnClickListener { my_skl(0) }
        //save chack
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        if (pref.getInt("pl_check",0)==0){
            status()
            System.out.println("data_set")
        }
        else{
            load_status()
            System.out.println("load_data")
        }
        //
        mp0.start()
        //
        //
        setContentView(binding.root)
    }
    //
    //
    fun daisu_start(){
        if (type==0){
            devil_daisu = (Math.random()*6).toInt()+1
            //devil_daisu=1
            //val pref = PreferenceManager.getDefaultSharedPreferences(this)
            all_masu = all_masu + devil_daisu
        }
        else{
            devil_daisu = (Math.random()*6).toInt()+1
        }
        if(all_masu >= max_height && type == 0){
            mp0.stop()
            btl_sound.stop()
            val intent = Intent(this, ClearActivity::class.java)
            startActivity(intent)
        }
        else if(type==0){
            binding.masucount.text = (Integer.parseInt(binding.masucount.text.toString())-devil_daisu).toString()
            set_daisu_image(devil_daisu)
            masu_checker(all_masu)
        }
        else if (type==1){
            set_daisu_image(devil_daisu)
            btl()
        }
        save()
    }

    //初回ステータス保存
    fun status(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        // 保存
        pref.edit{
            putInt("pl_hp", 100)
            putInt("pl_max_hp",100)
            putInt("pl_atk", 5)
            putInt("pl_def", 10)
            putInt("pl_mp", 20)
            putInt("pl_max_mp",20)
            putInt("pl_exp",0)
            putInt("pl_po", 0)
            putInt("pl_all_masu",0)
            putInt("pl_check",1)
            putInt("pl_level",1)
            putInt("pl_now_masu",0)
            putInt("pl_atk_weapon",0)
            putString("pl_atk_weapon_list","0")
            putInt("pl_shield_weapon",1)
            putString("pl_shield_weapon_list","1")
            putInt("pl_head_weapon",2)
            putString("pl_head_weapon_list","2")
            putInt("pl_chest_weapon",3)
            putString("pl_chest_weapon_list","3")
        }
        hp=pref.getInt("pl_hp", 0)
        max_hp=pref.getInt("pl_max_hp",0)
        atk=pref.getInt("pl_atk", 0)
        def=pref.getInt("pl_def", 0)
        po=pref.getInt("pl_po", 0)
        mp=pref.getInt("pl_mp",0)
        all_masu=pref.getInt("pl_all_masu",0)
        level=pref.getInt("pl_level",0)
        exp_all=pref.getInt("pl_exp",0)
        devil_daisu=pref.getInt("pl_now_daisu",0)
        my_atk_weapon=pref.getInt("pl_atk_weapon",0)
        my_shield_weapon=pref.getInt("pl_shield_weapon",1)
        my_head_weapon=pref.getInt("pl_head_weapon",2)
        my_chest_weapon=pref.getInt("pl_chest_weapon",3)
        weapon_status_plus()
        binding.hp.text = hp.toString()
        binding.atk.text = atk.toString()
        binding.def.text = def.toString()
        binding.mp.text = mp.toString()
        binding.levelMain.text = level.toString()
        binding.masucount.text = (max_height-masu_num).toString()
    }
    //2回目以降（途中で止めたデータ）
    fun load_status(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        hp=pref.getInt("pl_hp", 0)
        max_hp=pref.getInt("pl_max_hp",0)
        atk=pref.getInt("pl_atk", 0)
        def=pref.getInt("pl_def", 0)
        po=pref.getInt("pl_po", 0)
        mp=pref.getInt("pl_mp",0)
        level=pref.getInt("pl_level",0)
        all_masu=pref.getInt("pl_all_masu",0)
        devil_daisu=pref.getInt("pl_now_daisu",0)
        my_atk_weapon=pref.getInt("pl_atk_weapon",0)
        my_shield_weapon=pref.getInt("pl_shield_weapon",1)
        my_head_weapon=pref.getInt("pl_head_weapon",2)
        my_chest_weapon=pref.getInt("pl_chest_weapon",3)
        weapon_status_plus()
        binding.hp.text = hp.toString()
        binding.atk.text = atk.toString()
        binding.def.text = def.toString()
        binding.mp.text = mp.toString()
        binding.levelMain.text = level.toString()
        binding.masucount.text = (max_height-all_masu).toString()
        masu_checker(all_masu)
    }
    //save
    fun save(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit{
            putInt("pl_hp", Integer.parseInt(binding.hp.text.toString()))
            putInt("pl_atk", Integer.parseInt(binding.atk.text.toString()))
            putInt("pl_def", Integer.parseInt(binding.def.text.toString()))
            putInt("pl_mp", Integer.parseInt(binding.mp.text.toString()))
            putInt("pl_level",Integer.parseInt(binding.levelMain.text.toString()))
            putInt("pl_exp",exp_all)
            putInt("pl_po", Integer.parseInt(binding.portionNum.text.toString()))
            putInt("pl_all_masu",max_height-Integer.parseInt(binding.masucount.text.toString()))
            putInt("pl_level",Integer.parseInt(binding.levelMain.text.toString()))
            putInt("pl_max_hp",(Integer.parseInt(binding.levelMain.text.toString())-1)*10 + 100)
            putInt("pl_now_daisu",devil_daisu)
            putInt("pl_check",1)
        }
        max_hp = pref.getInt("pl_max_hp",100)
    }
    //マス保存
    //
    //ボスマス移動
    fun move(){
        //val intent = Intent(this,res2::class.java)
        //startActivity(intent)
        binding.result.text="到達!!"
    }
    //ゲームオーバー移動
    fun game_over(){
        mp0.stop()
        btl_sound.stop()
        val intent = Intent(this, Gameover::class.java)
        startActivity(intent)
        //binding.result.text = "gameover"
    }
    //メニュー移動
    fun menu_change(){
        val intent = Intent(this,SubActivity::class.java)
        startActivity(intent)
        //setContentView(R.layout.activity_sub)
    }
    //マス確認
    fun masu_checker(num: Int){
        if(num==0){
            //説明欄
        }
        else if(masu_event[num]==1){
            //戦闘
            binding.result.text = "戦闘！！"
            binding.no.text = masu_result_num[num].toString()
            enemy_no = Integer.parseInt(binding.no.text.toString())
            set_enemy(enemy_no)
            open_enemy_status(enemy_no)
            type = 1
            mp0.pause()
            btl_sound.seekTo(0)
            btl_sound.setVolume(0.3f,0.3f)
            btl_sound.start()
        }
        else if(masu_event[num]==2){
            //アイテムゲット
            binding.result.text = "アイテムゲット！！"
            binding.meResult.text = ""
            binding.enemyImage.setImageResource(R.drawable.po)
            po = po + masu_result_num[num]
            binding.portionNum.text = po.toString()
        }
        else if(masu_event[num]==3){
            weapon_get(masu_result_num[num])
        }
        else{
            binding.result.text = "なし！！"
            binding.enemyImage.setImageResource(R.drawable.notevent3)
        }
        if(all_masu>=max_height/4&&all_masu<max_height/2){
            bgm_ch=2
            binding.imageView3.setImageResource(R.drawable.backimagerightcave)
        }
        else if(all_masu>=max_height/2&&all_masu<=max_height){
            binding.imageView3.setImageResource(R.drawable.backimagelast)
        }
    }
    //イメージセット
    fun set_enemy(no: Int){
        var text_image = "image" + no.toString()
        val resId = resources.getIdentifier(text_image, "drawable", packageName)
        binding.enemyImage.setImageResource(resId)
    }
    //サイコロイメージセット
    fun set_daisu_image(no:Int){
        var text_image = "daisu" + no.toString()
        val resId = resources.getIdentifier(text_image, "drawable", packageName)
        binding.daisuimage.setImageResource(resId)
    }
    //敵
    fun enemy_set_daisu_image(no:Int){
        var text_image = "daisu" + no.toString()
        val resId = resources.getIdentifier(text_image, "drawable", packageName)
        binding.enemyDaisu.setImageResource(resId)
    }
    //ポーション使用
    fun use_portion(){
        var text1 = ""
        var text2 = ""
        if (po>0){
            soundPool.play(drunk_po,50f,50f,0,0,1.0f)
            var ing_hp = Integer.parseInt(binding.hp.text.toString())
            ing_hp = ing_hp + 20
            if(ing_hp>max_hp){
                ing_hp = max_hp
            }
            binding.hp.text = ing_hp.toString()
            text1 = "ポーション使用"
            text2 = "20回復した"
            po--
            binding.portionNum.text = po.toString()
            comment_in(text1,text2)
        }
        else if (po==0){
            binding.portionNum.text = po.toString()
        }
        hp = Integer.parseInt(binding.hp.text.toString())
        save()
    }
    //戦闘モード
    fun btl(){
        mp0.pause()
        //敵のHP
        var human_hp = Integer.parseInt(binding.hpnum.text.toString())
        var human_atk = Integer.parseInt(binding.atknum.text.toString())
        var human_def = Integer.parseInt(binding.defnum.text.toString())
        var me_atk_dmg = 0
        var human_atk_dmg = 0
        var text1 = ""
        var text2 = ""
        me_atk_dmg = (atk*devil_daisu) - human_def
        System.out.println(text1 + text2)
        if (me_atk_dmg<=0){
            me_atk_dmg = 0
        }
        text1 = "通常攻撃"
        text2 = "相手に" + me_atk_dmg.toString() + "ダメージ"
        comment_in(text1,text2)
        var result_human_hp = human_hp - me_atk_dmg
        thread {
            Thread.sleep(1500L)
            if(result_human_hp<=0){
                soud_ch = 0
                //soundPool.stop(dark_bgm)
                text1 = "倒した！"
                text2 = ""
                runOnUiThread{
                    comment_in(text1,text2)
                    binding.enemyImage.setImageResource(R.drawable.taosita)
                    exp_check()
                }
                invisible_enemy_status()
                type = 0
                btl_sound.pause()
                mp0.seekTo(0)
                mp0.start()
            }
            else{
                runOnUiThread {
                    binding.hpnum.text = result_human_hp.toString()
                }
                enemy_daisu()
                var ult_check = (Math.random()*100).toInt()
                //
                if(ult_check<=80){
                    enemy_skl()
                    runOnUiThread {
                        binding.hp.text = hp.toString()
                    }
                }
                else {
                    human_atk_dmg = human_daisu * human_atk - def
                    if (human_atk_dmg <= 0) {
                        human_atk_dmg = 0
                    }
                    hp = hp - human_atk_dmg
                    runOnUiThread {
                        enemy_set_daisu_image(human_daisu)
                        binding.hpnum.text = result_human_hp.toString()
                    }
                    text1 = "相手の通常攻撃"
                    text2 = human_atk_dmg.toString() + "ダメージ"
                    //
                    runOnUiThread {
                        binding.hp.text = hp.toString()
                    }
                    runOnUiThread {
                        comment_in(text1,text2)
                    }
                }
                if(hp<=0){
                    btl_sound.pause()
                    game_over()
                }
            }
        }
        round++
    }
    //自身のスキルセット
    fun my_skil_set(){
        my_ult_set = my_skil.split(",").map(String::toInt).toTypedArray()
        //System.out.println(my_ult_set.size)
        var sta = 0
        var sta2 = 0
        var sta3 = 0
        my_ult_set_heal = Array(my_ult_set.size){1}
        my_ult_set_atk = Array(my_ult_set.size){1}
        my_ult_heal_name = Array(my_ult_set.size){""}
        my_ult_atk_name = Array(my_ult_set.size){""}
        while(sta<my_ult_set.size){
            //heal
            if(ult_type[my_ult_set[sta]]==0){
                //System.out.println(my_ult_set[sta].toString())
                my_ult_set_heal[sta2] = my_ult_set[sta]
                my_ult_heal_name[sta2] = ult_name[my_ult_set[sta]] + "-mp消費" + ult_use_mp[my_ult_set[sta]]
                sta2++
            }
            //atk
            else if(ult_type[my_ult_set[sta]]==1){
                my_ult_set_atk[sta3] = my_ult_set[sta]
                my_ult_atk_name[sta3] = ult_name[my_ult_set[sta]] + "-mp消費" + ult_use_mp[my_ult_set[sta]]
                sta3++
            }
            sta++
        }
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,my_ult_atk_name)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.atkList.adapter = adapter
        val adapter2 = ArrayAdapter(this,android.R.layout.simple_spinner_item,my_ult_heal_name)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.healList.adapter = adapter2
    }
    //選択スキル取得
    fun get_select_skil(no:Int):Int{
        var number = 0
        if(no==0){
            val spinner = binding.healList
            ult_select_name = spinner.selectedItem.toString()
            while (number<my_ult_set.size){
                if(my_ult_heal_name[number]==ult_select_name){
                    return number
                }
                number++
            }
        }
        else if(no==1){
            val spinner = binding.atkList
            ult_select_name = spinner.selectedItem.toString()
            while (number<my_ult_set.size){
                if(my_ult_atk_name[number]==ult_select_name){
                    return number
                }
                number++
            }
        }
        return 0
    }
    //自身のスキル
    fun my_skl(typ:Int){
        //
        var human_hp = Integer.parseInt(binding.hpnum.text.toString())
        var human_def = Integer.parseInt(binding.defnum.text.toString())
        mp = Integer.parseInt(binding.mp.text.toString())
        var me_atk_dmg = 0
        var me_heal = 0
        var text1 = ""
        var text2 = ""
        //
        //typ=0 -> heal
        if(typ==0){
            if(type==1) {
                //
                var select_no = get_select_skil(0)
                System.out.println(select_no.toString())
                //
                if(ult_use_mp[my_ult_set_heal[select_no]]<=mp){
                    //var rnd_num = (Math.random()*6).toInt()+1
                    me_heal = ult_result_num[my_ult_set_heal[select_no]]
                    soundPool.play(skl_heal,1.0f,100f,0,0,1.0f)
                    if(me_heal+hp>max_hp){
                        text1 = "自身の回復スキル[" + ult_name[my_ult_set_heal[select_no]] + "]"
                        text2 = "全回復"
                        hp = max_hp
                        binding.hp.text = hp.toString()
                    }
                    else{
                        text1 = "自身の回復スキル[" + ult_name[my_ult_set_heal[select_no]] + "]"
                        text2 = me_heal.toString() + "回復"
                        hp = Integer.parseInt(binding.hp.text.toString())
                        binding.hp.text = (hp + me_heal).toString()
                    }
                    mp = mp - ult_use_mp[my_ult_set_heal[select_no]]
                    binding.mp.text = mp.toString()
                }
                else{
                    text2 = "mpがたりない"
                }
                comment_in(text1,text2)
            }
        }
        //typ=1 -> atk
        else if(typ==1){
            if(type==1){
                //
                var select_no = get_select_skil(1)
                System.out.println(select_no.toString())
                //
                if(ult_use_mp[my_ult_set_atk[select_no]]<=mp){
                    var rnd_num = (Math.random()*6).toInt()+1
                    me_atk_dmg = ult_result_num[my_ult_set_atk[select_no]]*rnd_num
                    soundPool.play(skl_soud,1.0f,100f,0,0,0.8f)
                    if((me_atk_dmg-human_def)>0){
                        var me_atk = me_atk_dmg-human_def
                        human_hp = human_hp - me_atk
                        text1 = "自身の攻撃スキル[" + ult_name[my_ult_set_atk[select_no]] + "]"
                        text2 = "相手に" + me_atk + "ダメージ"
                        if(human_hp<=0){
                            soud_ch = 0
                            //soundPool.stop(dark_bgm)
                            text1 = "倒した！"
                            binding.enemyImage.setImageResource(R.drawable.taosita)
                            exp_check()
                            invisible_enemy_status()
                            type = 0
                            btl_sound.pause()
                            mp0.seekTo(0)
                            mp0.start()
                        }
                        else{
                            binding.hpnum.text = human_hp.toString()
                        }
                    }
                    else{
                        text1 = "相手の防御を抜けなかった..."
                    }
                    mp = mp - ult_use_mp[my_ult_set_atk[select_no]]
                    binding.mp.text = mp.toString()
                }
                else{
                    text1 = "mpがたりない"
                }
                comment_in(text1,text2)
                save()
            }
        }
        else{
            text1 = "error"
        }
    }
    //敵のスキル
    fun enemy_skl(){
        var human_hp = Integer.parseInt(binding.hpnum.text.toString())
        var human_atk = Integer.parseInt(binding.atknum.text.toString())
        hp = Integer.parseInt(binding.hp.text.toString())
        var me_atk_dmg = 0
        var human_atk_dmg = 0
        var text1 = ""
        var text2 = ""
        var use_ult = (Math.random()*human_ult_set.size).toInt()
        //
        //攻撃
        if(ult_type[human_ult_set[use_ult]]==1){
            if(enemy_mp>=ult_use_mp[human_ult_set[use_ult]]){
                enemy_mp = enemy_mp - ult_use_mp[human_ult_set[use_ult]]
                runOnUiThread {
                    binding.mpnum.text = enemy_mp.toString()
                }
                human_atk_dmg = human_daisu * ult_result_num[human_ult_set[use_ult]] - def
                enemy_set_daisu_image(human_daisu)
                if (human_atk_dmg <= 0) {
                    human_atk_dmg = 0
                }
                hp = hp - human_atk_dmg
                text1 = "相手の攻撃スキル[" + ult_name[human_ult_set[use_ult]] + "]"
                text2 = human_atk_dmg.toString() + "ダメージ"
                runOnUiThread {
                    binding.hp.text = hp.toString()
                }
            }
            else{
                human_atk_dmg = human_daisu * human_atk - def
                if (human_atk_dmg <= 0) {
                    human_atk_dmg = 0
                }
                hp = hp - human_atk_dmg
                text1 = "相手の通常攻撃"
                text2 = "相手から" + human_atk_dmg + "ダメージ"
                runOnUiThread {
                    enemy_set_daisu_image(human_daisu)
                    binding.hp.text = hp.toString()
                }
            }
        }//回復スキル
        else if(ult_type[human_ult_set[use_ult]]==0){
            if(enemy_mp>=ult_use_mp[human_ult_set[use_ult]]) {
                enemy_mp = enemy_mp - ult_use_mp[human_ult_set[use_ult]]
                human_hp = human_hp + ult_result_num[human_ult_set[use_ult]]
                runOnUiThread {
                    binding.mpnum.text = enemy_mp.toString()
                    binding.hpnum.text = human_hp.toString()
                }
                var heal = ult_result_num[human_ult_set[use_ult]]
                text1 = "相手の回復スキル[" + ult_name[human_ult_set[use_ult]] + "]"
                text2 = heal.toString() + "回復"
            }
            else{
                human_atk_dmg = human_daisu * human_atk - def
                if (human_atk_dmg <= 0) {
                    human_atk_dmg = 0
                }
                hp = hp - human_atk_dmg
                text1 = "相手の通常攻撃"
                text2 = "相手から" + human_atk_dmg + "ダメージ"
                runOnUiThread {
                    enemy_set_daisu_image(human_daisu)
                    binding.hp.text = hp.toString()
                }
            }
        }
        //
        comment_in(text1,text2)
    }

    //コメント表示
    fun comment_in(text1:String,text2:String){
        runOnUiThread {
            binding.result.text = text1
            binding.meResult.text = text2
        }
    }

    //相手攻撃
    fun enemy_daisu(){
        human_daisu = (Math.random()*6).toInt()+1
    }
    //相手のステータス・スキルボタンを表示
    fun open_enemy_status(no: Int){
        binding.hptext.setVisibility(View.VISIBLE)
        binding.hpnum.setVisibility(View.VISIBLE)
        binding.atktext.setVisibility(View.VISIBLE)
        binding.atknum.setVisibility(View.VISIBLE)
        binding.deftext.setVisibility(View.VISIBLE)
        binding.defnum.setVisibility(View.VISIBLE)
        binding.mptext.setVisibility(View.VISIBLE)
        binding.mpnum.setVisibility(View.VISIBLE)
        //スキルボタン
        my_skil_set()
        binding.atkButton.setVisibility(View.VISIBLE)
        binding.healButton.setVisibility(View.VISIBLE)
        binding.healList.setVisibility(View.VISIBLE)
        binding.atkList.setVisibility(View.VISIBLE)
        set_enemy_status(no)
    }
    //相手のステータス・スキルボタンを非表示
    fun invisible_enemy_status(){
        runOnUiThread {
            binding.hptext.setVisibility(View.INVISIBLE)
            binding.hpnum.setVisibility(View.INVISIBLE)
            binding.atktext.setVisibility(View.INVISIBLE)
            binding.atknum.setVisibility(View.INVISIBLE)
            binding.deftext.setVisibility(View.INVISIBLE)
            binding.defnum.setVisibility(View.INVISIBLE)
            binding.mptext.setVisibility(View.INVISIBLE)
            binding.mpnum.setVisibility(View.INVISIBLE)
            //スキルボタン
            binding.atkButton.setVisibility(View.INVISIBLE)
            binding.healButton.setVisibility(View.INVISIBLE)
            binding.healList.setVisibility(View.INVISIBLE)
            binding.atkList.setVisibility(View.INVISIBLE)
        }
    }
    //相手のステータスの数値を表示
    fun set_enemy_status(no: Int){
        binding.hpnum.text=human_hp[no].toString()
        binding.atknum.text=human_atk[no].toString()
        binding.defnum.text=human_def[no].toString()
        binding.mpnum.text=human_mp[no].toString()
        enemy_hp = Integer.parseInt(binding.hpnum.text.toString())
        enemy_atk = Integer.parseInt(binding.atknum.text.toString())
        enemy_def = Integer.parseInt(binding.defnum.text.toString())
        enemy_mp = Integer.parseInt(binding.mpnum.text.toString())
        var i = human_ult[no]
        human_ult_set = i.split(",").map(String::toInt).toTypedArray()
    }

    //経験値レベルアップ判定
    fun exp_check(){
        level = Integer.parseInt(binding.levelMain.text.toString())
        exp_all += 100
        if(exp_all>=level*100){
            runOnUiThread{
                binding.levelMain.text = (level+1).toString()
            }
            exp_all = 0
        }
    }
    //装備入手一覧にいれる
    fun weapon_get(no:Int){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val assetManager = resources.assets
        val inputStream = assetManager.open("weapon_list.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText()
        val jsonObject = JSONObject(str)
        val jsonArray_weapon = jsonObject.getJSONArray("weapon")
        val jsonData = jsonArray_weapon.getJSONObject(no)
        var weapon_type = jsonData.getInt("weapon_type")
        var weapon_name = jsonData.getString("weapon_name")
        var weapon_list_txt = ""
        //武器
        if(weapon_type==0){
            weapon_list_txt = pref.getString("pl_atk_weapon_list","0") + "," + no.toString()
            pref.edit(){
                putString("pl_atk_weapon_list",weapon_list_txt)
            }
            binding.result.text = "「" + weapon_name + "」" + "を手に入れた！"
            binding.enemyImage.setImageResource(R.drawable.buki)
        }
        //盾
        else if(weapon_type==1){
            weapon_list_txt = pref.getString("pl_shield_weapon_list","1") + "," + no.toString()
            pref.edit(){
                putString("pl_shield_weapon_list",weapon_list_txt)
            }
            binding.result.text = "「" + weapon_name + "」" + "を手に入れた！"
            binding.enemyImage.setImageResource(R.drawable.tate)
        }
        //頭
        else if(weapon_type==2){
            weapon_list_txt = pref.getString("pl_head_weapon_list","2") + "," + no.toString()
            pref.edit(){
                putString("pl_head_weapon_list",weapon_list_txt)
            }
            binding.result.text = "「" + weapon_name + "」" + "を手に入れた！"
            binding.enemyImage.setImageResource(R.drawable.atama)
        }
        //胸
        else if(weapon_type==3){
            weapon_list_txt = pref.getString("pl_chest_weapon_list","3") + "," + no.toString()
            pref.edit(){
                putString("pl_chest_weapon_list",weapon_list_txt)
            }
            binding.result.text = "「" + weapon_name + "」" + "を手に入れた！"
            binding.enemyImage.setImageResource(R.drawable.karada)
        }
        inputStream.close()
        bufferedReader.close()
    }
    //装備ステータス反映
    fun weapon_status_plus() {
        val assetManager = resources.assets
        val inputStream = assetManager.open("weapon_list.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText()
        val jsonObject = JSONObject(str)
        val jsonArray_weapon = jsonObject.getJSONArray("weapon")
        val atk_date = jsonArray_weapon.getJSONObject(my_atk_weapon)
        atk = atk + atk_date.getInt("plus_atk")
        def = def + atk_date.getInt("plus_def")
        val shield_date = jsonArray_weapon.getJSONObject(my_shield_weapon)
        atk = atk + shield_date.getInt("plus_atk")
        def = def + shield_date.getInt("plus_def")
        val head_date = jsonArray_weapon.getJSONObject(my_head_weapon)
        atk = atk + head_date.getInt("plus_atk")
        def = def + head_date.getInt("plus_def")
        val chest_date = jsonArray_weapon.getJSONObject(my_chest_weapon)
        atk = atk + chest_date.getInt("plus_atk")
        def = def + chest_date.getInt("plus_def")
        inputStream.close()
        bufferedReader.close()
    }
    //
    fun ult_list_set(){
        val assetManager = resources.assets
        val inputStream = assetManager.open("ult_list.json")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText()
        val jsonObject = JSONObject(str)
        val jsonArray_ult = jsonObject.getJSONArray("ult")
        ult_type = Array(jsonArray_ult.length()){0}
        ult_result_num = Array(jsonArray_ult.length()){0}
        ult_use_mp = Array(jsonArray_ult.length()){0}
        ult_name = Array(jsonArray_ult.length()){""}
        var i = 0
        while(i < jsonArray_ult.length()){
            val ult_date = jsonArray_ult.getJSONObject(i)
            ult_type[i]=ult_date.getInt("ult_type")
            ult_result_num[i]=ult_date.getInt("ult_result_num")
            ult_use_mp[i]=ult_date.getInt("ult_use_mp")
            ult_name[i]=ult_date.getString("ult_name")
            i++
        }
        System.out.println(ult_name[jsonArray_ult.length()-1])
        inputStream.close()
        bufferedReader.close()
    }
    //
    override fun onPause() {
        super.onPause()
        soundPool.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        mp0.stop()
        btl_sound.stop()
        soundPool.release()
        mp0.release()
        btl_sound.release()
    }

    override fun onResume() {
        super.onResume()
        if(type==1){
            btl_sound.start()
        }
        else if(type==0){
            mp0.start()
            load_status()
            weapon_status_plus()
            System.out.println("weapon_status_plus")
        }
    }

    override fun onStop() {
        super.onStop()
        if(type==1){
            btl_sound.pause()
        }
        else if(type==0){
            mp0.pause()
        }
    }
}