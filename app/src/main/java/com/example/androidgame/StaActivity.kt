package com.example.androidgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidgame.databinding.ActivityStartBinding

class StaActivity : AppCompatActivity() {
    lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.startbtn.setOnClickListener { game_start() }
    }
    fun game_start(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}