package com.example.androidgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AlertDialog
import com.example.androidgame.databinding.ActivityStartBinding

class StaActivity : AppCompatActivity() {
    lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        binding.startbtn.setOnClickListener { game_start() }
        //
        binding.resetbtn.setOnClickListener {
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                    .setTitle("データ削除")
                    .setMessage("本当に削除しますか？")
                    .setPositiveButton("いいえ", { dialog, which ->

                    })
                    .setNegativeButton("はい", { dialog, which ->
                        pref.edit().clear().commit()
                    })
                    .show()
        }

    }
    fun game_start(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}