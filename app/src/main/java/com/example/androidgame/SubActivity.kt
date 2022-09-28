package com.example.androidgame

import android.os.Bundle
import android.text.TextUtils.replace
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.androidgame.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ステータスボタン
        binding.suteButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, statusFragment())
                addToBackStack(null)
                commit()
            }
        }
        //アイテムボタン
        binding.aitemButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, itemFragment())
                addToBackStack(null)
                commit()
            }
        }
        //設定ボタン
        binding.settingButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, SettingFragment())
                addToBackStack(null)
                commit()
            }
        }
        //装備ボタン
        binding.soubiButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, soubiFragment())
                addToBackStack(null)
                commit()
            }
        }
        //✖ボタン
        binding.batuButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                finish()
            }
        }
    }
}
