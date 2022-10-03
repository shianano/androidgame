package com.example.androidgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidgame.databinding.ActivityTutorialBinding

class TutorialActivity : AppCompatActivity() {
    lateinit var binding: ActivityTutorialBinding
    var no=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView5.setImageResource(R.drawable.tutorial0)
        binding.back2btn.setOnClickListener{
            if (no==0){

            }else if(no>0){
                no--
            }
            setimage(no)
        }
        binding.next2btn.setOnClickListener{
            if (no==8){

            }else if(no<8){
                no++
            }
            setimage(no)
        }

        binding.titlebtn3.setOnClickListener{
            val intent = Intent(this, StaActivity::class.java)
            startActivity(intent)
        }

    }
    fun setimage(no:Int){
        System.out.println(no.toString())
        var text_image = "tutorial" + no.toString()
        val resId = resources.getIdentifier(text_image, "drawable", packageName)
        binding.imageView5.setImageResource(resId)
    }
}