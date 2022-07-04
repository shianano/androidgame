package com.example.androidgame

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidgame.databinding.ActivityGameoverBinding

class Gameover : AppCompatActivity() {
    lateinit var binding:ActivityGameoverBinding
    lateinit var gameover: MediaPlayer //out
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameoverBinding.inflate(layoutInflater)
        binding.titlebtn.setOnClickListener { start_menu() }
        gameover = MediaPlayer.create(this,R.raw.gameoverbgm)
        gameover.isLooping = true
        gameover.start()
        setContentView(binding.root)
    }

    fun start_menu(){
        gameover.stop()
        val intent = Intent(this,StaActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        gameover.release()
    }
}