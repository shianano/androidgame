package com.example.androidgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.androidgame.databinding.ActivityClearBinding

class ClearActivity : AppCompatActivity() {

    lateinit var binding: ActivityClearBinding

    var comment_list :Array<String> = arrayOf(
        " だが、お前が私を操作したことが \n 周りにしれれば私の威厳にかかわってしまう。",
        " よって、お前の記憶を消す。いいな。 \n 素晴らしい武勲であったぞ。。。"
    )
    var cnt = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClearBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.maouCommentBaclcolor.setOnClickListener {
            comment()
        }
        binding.BackTop.setOnClickListener {
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            pref.edit().clear().commit()
            val intent = Intent(this,StaActivity::class.java)
            startActivity(intent)
        }
    }
    fun comment(){
        if(cnt<3){
            while(cnt<3){
                binding.maouComment.text = comment_list[cnt]
                cnt++
            }
        }
        else if(cnt>=3){
            binding.BackTop.visibility
        }
    }
}