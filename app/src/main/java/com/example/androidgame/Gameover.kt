package com.example.androidgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidgame.databinding.ActivityGameoverBinding
import com.example.androidgame.databinding.ActivityStartBinding

class Gameover : AppCompatActivity() {
    lateinit var binding:ActivityGameoverBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameoverBinding.inflate(layoutInflater)
        binding.titlebtn.setOnClickListener { start_menu() }
        setContentView(binding.root)
    }

    fun start_menu(){
        val intent = Intent(this,StartActivity::class.java)
        StartActivity(intent)
    }
}