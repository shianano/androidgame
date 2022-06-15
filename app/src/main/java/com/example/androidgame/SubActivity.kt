package com.example.androidgame

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.androidgame.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingButton.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, SettingFragment())
                addToBackStack(null)
                commit()
            }
        }
    }
}