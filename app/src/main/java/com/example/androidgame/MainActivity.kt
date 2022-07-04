package com.example.androidgame

import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SPEECH
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AppLaunchChecker
import androidx.core.content.edit
import androidx.media.AudioAttributesCompat.CONTENT_TYPE_SPEECH
import com.example.androidgame.databinding.ActivityMainBinding
import org.json.JSONArray
import java.util.jar.Attributes

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
    var atk = 0
    var def = 0
    var po = 0
    var mp = 0

    //マス定義
    var max_height = 30
    //test input
    var masu_event = IntArray(max_height)
    var masu_result_num = IntArray(max_height)

    //人間ステータス定義
    //var human_type_height = 6
    //var human_type_weight = 6
    //test input
    val human_hp: Array<Int> = arrayOf(100,90,80,70,60)
    val human_atk: Array<Int> = arrayOf(10,10,10,10,10)
    val human_def: Array<Int> = arrayOf(10,9,8,7,6)
    val human_mp: Array<Int> = arrayOf(5,5,5,5,5)
    val human_ult: Array<String> = arrayOf("1","1","1","1","1")
    //set
    var human_ult_set: Array<Int> = arrayOf()

    //人間名定義
    val human_name = arrayOf("ファグラ","トートリ","アゲイン","エクレー","オフェリア")
    //人間と戦闘時ステータスを入れて戦闘

    //技(ult) ultの番号・[0]ヒール系,[1]攻撃系　判定変数・判定変数による値(例：ヒール系の値(100)なら100回復)
    val ult_type: Array<Int> = arrayOf(0,1)
    val ult_result_num: Array<Int> = arrayOf(20,15)
    val ult_use_mp: Array<Int> = arrayOf(5,5)
    val ult_name: Array<String> = arrayOf("ヒール", "暗黒斬り")
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
        //dark_bgm = soundPool.load(this,R.raw.deathworld,0)
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
                soundPool.play(atk_sound,1.0f,100f,0,0,1.0f)
            }
            else if(type==0){
                soundPool.play(daisu_sound,1.0f,100f,0,0,1.0f)
                if(bgm_ch==0){
                    bgm_ch=1
                }
            }
            daisu_start()
        }
        binding.usepo.setOnClickListener { use_portion() }
        binding.menu.setOnClickListener { menu_change() }
        binding.save.setOnClickListener { save() }
        binding.atkButton.setOnClickListener { my_skl(1) }
        binding.healButton.setOnClickListener { my_skl(0) }
        create_array()
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
        else{
            devil_daisu = (Math.random()*6).toInt()+1
        }
        if(all_masu >= max_height && type == 0){
            binding.enemyImage.setImageResource(R.drawable.clear)
            binding.masucount.text="0"
        }
        else if(type==0){
            binding.masucount.text = (Integer.parseInt(binding.masucount.text.toString())-devil_daisu).toString()
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
        mp=pref.getInt("pl_mp",0)
        masu_num=pref.getInt("pl_masu",0)
        binding.hp.text = hp.toString()
        binding.atk.text = atk.toString()
        binding.def.text = def.toString()
        binding.mp.text = mp.toString()
        binding.masucount.text = (30-masu_num).toString()
    }
    //2回目以降（途中で止めたデータ）
    fun load_status(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        hp=pref.getInt("pl_hp", 0)
        atk=pref.getInt("pl_atk", 0)
        def=pref.getInt("pl_def", 0)
        po=pref.getInt("pl_po", 0)
        mp=pref.getInt("pl_mp",0)
        all_masu=pref.getInt("pl_masu",0)
        binding.hp.text = hp.toString()
        binding.atk.text = atk.toString()
        binding.def.text = def.toString()
        binding.mp.text = mp.toString()
        binding.masucount.text = (30-all_masu).toString()
    }
    //save
    fun save(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit{
            putInt("pl_hp", Integer.parseInt(binding.hp.text.toString()))
            putInt("pl_atk", Integer.parseInt(binding.atk.text.toString()))
            putInt("pl_def", Integer.parseInt(binding.def.text.toString()))
            putInt("pl_mp", Integer.parseInt(binding.mp.text.toString()))
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
        if(masu_event[num]==1){
            //戦闘
            binding.result.text = "戦闘！！"
            binding.no.text = masu_result_num[num].toString()
            enemy_no = Integer.parseInt(binding.no.text.toString())
            set_enemy(enemy_no)
            open_enemy_status(enemy_no)
            type = 1
            mp0.stop()
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
        else{
            binding.result.text = "なし！！"
            binding.enemyImage.setImageResource(R.drawable.notevent3)
        }
        if(all_masu>=10&&all_masu<20){
            bgm_ch=2
            binding.imageView3.setImageResource(R.drawable.backimagerightcave)
        }
        else if(all_masu>=20&&all_masu<=30){
            binding.imageView3.setImageResource(R.drawable.backimagelast)
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
            soundPool.play(drunk_po,1.0f,500f,0,0,1.0f)
            var ing_hp = Integer.parseInt(binding.hp.text.toString())
            ing_hp = ing_hp + 20
            if(ing_hp>100){
                ing_hp = 100
            }
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
        var text = ""
        var text2 = ""
        me_atk_dmg = (atk*devil_daisu) - human_def
        System.out.println(me_atk_dmg)
        if (me_atk_dmg<=0){
            me_atk_dmg = 0
        }
        var result_human_hp = human_hp - me_atk_dmg
        if(result_human_hp<=0){
            soud_ch = 0
            //soundPool.stop(dark_bgm)
            binding.result.text = "倒した！"
            binding.meResult.text = ""
            binding.enemyImage.setImageResource(R.drawable.taosita)
            invisible_enemy_status()
            type = 0
            btl_sound.stop()
            mp0.start()
        }
        else{
            binding.hpnum.text = result_human_hp.toString()
            text = "相手："+ me_atk_dmg + "ダメージ"
            enemy_daisu()
            var ult_check = (Math.random()*100).toInt()
            //
            if(ult_check<=80){
                enemy_skl()
                binding.hp.text = hp.toString()
                binding.result.text = text
            }
            else {
                human_atk_dmg = human_daisu * human_atk - def
                if (human_atk_dmg <= 0) {
                    human_atk_dmg = 0
                }
                hp = hp - human_atk_dmg
                binding.hpnum.text = result_human_hp.toString()
                text2 = "自分：" + human_atk_dmg + "ダメージ"
                //
                binding.hp.text = hp.toString()
                binding.result.text = text
                binding.meResult.text = text2
            }
            if(hp<=0){
                btl_sound.stop()
                game_over()
            }
        }
        round++
    }
    //enemy_turn_atk
    fun enemy_turn_atk(parcent:Int){

    }

    //自身のスキル
    fun my_skl(typ:Int){
        //
        var human_hp = Integer.parseInt(binding.hpnum.text.toString())
        var human_def = Integer.parseInt(binding.defnum.text.toString())
        var me_atk_dmg = 0
        var me_heal = 0
        //
        //typ=0 -> heal
        if(typ==0){
            if(type==1){
                System.out.println("heal")
                if(mp>=5){
                    var rnd_num = (Math.random()*6).toInt()+1
                    me_heal = ult_result_num[typ]*rnd_num
                    soundPool.play(skl_heal,1.0f,100f,0,0,1.0f)
                    if(me_heal+hp>100){
                        hp = 100
                        binding.hp.text = hp.toString()
                        binding.meResult.text = "自身の" + ult_name[typ] + ult_name[typ] + "により全回復"
                    }
                    else{
                        binding.meResult.text = "自身の" + ult_name[typ] + ult_name[typ] + "により" + me_heal.toString() + "回復"
                    }
                    mp = mp - ult_use_mp[typ]
                    binding.mp.text = mp.toString()
                }
                else{
                    binding.result.text = "mpがたりない"
                }
            }
        }
        //typ=1 -> atk
        else if(typ==1){
            if(type==1){
                if(mp>=5){
                    var rnd_num = (Math.random()*6).toInt()+1
                    me_atk_dmg = ult_result_num[typ]*rnd_num
                    soundPool.play(skl_soud,1.0f,100f,0,0,0.8f)
                    if((me_atk_dmg-human_def)>0){
                        var me_atk = me_atk_dmg-human_def
                        human_hp = human_hp - me_atk
                        binding.result.text = ""
                        binding.meResult.text = "自身の" + ult_name[typ] + "により相手に" + me_atk + "ダメージ"
                        if(human_hp<=0){
                            soud_ch = 0
                            //soundPool.stop(dark_bgm)
                            binding.result.text = "倒した！"
                            binding.enemyImage.setImageResource(R.drawable.taosita)
                            invisible_enemy_status()
                            type = 0
                            btl_sound.stop()
                            mp0.start()
                        }
                        else{
                            binding.hpnum.text = human_hp.toString()
                        }
                    }
                    else{
                        binding.result.text = "相手の防御を抜けなかった..."
                    }
                    mp = mp - ult_use_mp[typ]
                    binding.mp.text = mp.toString()
                }
                else{
                    binding.result.text = "mpがたりない"
                }
            }
        }
        else{
            binding.result.text = "error"
        }
    }
    //敵のスキル
    fun enemy_skl(){
        var human_hp = Integer.parseInt(binding.hpnum.text.toString())
        var human_atk = Integer.parseInt(binding.atknum.text.toString())
        var me_atk_dmg = 0
        var human_atk_dmg = 0
        var text2 = ""
        var use_ult = (Math.random()*human_ult_set.size).toInt()
        //
        //攻撃
        if(ult_type[human_ult_set[use_ult]]==1){
            if(enemy_mp>=ult_use_mp[human_ult_set[use_ult]]){
                enemy_mp = enemy_mp - ult_use_mp[human_ult_set[use_ult]]
                binding.mpnum.text = enemy_mp.toString()
                human_atk_dmg = human_daisu + ult_result_num[human_ult_set[use_ult]] - def
                if (human_atk_dmg <= 0) {
                    human_atk_dmg = 0
                }
                hp = hp - human_atk_dmg
                text2 = "相手の" + ult_name[human_ult_set[use_ult]] + me_atk_dmg + "ダメージ"
            }
            else{
                human_atk_dmg = human_daisu * human_atk - def
                if (human_atk_dmg <= 0) {
                    human_atk_dmg = 0
                }
                hp = hp - human_atk_dmg
                text2 = "相手から" + human_atk_dmg + "ダメージ"
            }
        }//回復スキル
        else if(ult_type[human_ult_set[use_ult]]==0){
            if(enemy_mp>=ult_use_mp[human_ult_set[use_ult]]) {
                enemy_mp = enemy_mp - ult_use_mp[human_ult_set[use_ult]]
                binding.mpnum.text = enemy_mp.toString()
                human_hp = human_hp + ult_result_num[human_ult_set[use_ult]]
                binding.hpnum.text = human_hp.toString()
                var heal = ult_result_num[human_ult_set[use_ult]]
                text2 = "相手の" + ult_name[human_ult_set[use_ult]] + ":" + heal + "回復"
            }
            else{
                human_atk_dmg = human_daisu * human_atk - def
                if (human_atk_dmg <= 0) {
                    human_atk_dmg = 0
                }
                hp = hp - human_atk_dmg
                text2 = "相手から" + human_atk_dmg + "ダメージ"
            }
        }
        //
        binding.meResult.text = text2
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
        binding.atkButton.setVisibility(View.VISIBLE)
        binding.healButton.setVisibility(View.VISIBLE)
        set_enemy_status(no)
    }
    //相手のステータス・スキルボタンを非表示
    fun invisible_enemy_status(){
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
        System.out.println(human_ult_set[0])
    }
    //マス、相手のステータス、技などを作る
    fun create_array() {
        //masu
        var height = 0
        var weight = 0
        var enemy = 0
        var re_daisu = 4
        //
        height = 0
        enemy = 0
        System.out.println(masu_event.size)
        System.out.println(masu_result_num.size)
        while (height < max_height) {
            val m_type = (Math.random() * 3).toInt()
            //0->なし　1->戦闘　2->アイテム　masu[][1]
            if (m_type == 1 && enemy < 5) {
                masu_event[height] = m_type
                masu_result_num[height] = enemy
                enemy++
            } else if (m_type == 1 && enemy >= 5) {
                masu_event[height] = m_type
                masu_result_num[height] = (Math.random() * 5).toInt()
            } else {
                masu_event[height] = m_type
                masu_result_num[height] = (Math.random() * 2).toInt() + 1
            }
            height++
        }
    }

    //

    override fun onPause() {
        super.onPause()
        soundPool.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        mp0.release()
    }
}