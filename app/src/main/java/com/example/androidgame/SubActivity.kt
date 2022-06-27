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
        setContentView(R.layout.activity_sub)

        binding.settingButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, SettingFragment())
                addToBackStack(null)
                setContentView(R.layout.fragment_setting)
                commit()
            }
        }
    }

    private fun setContentView(root: ConstraintLayout) {

    }
}
