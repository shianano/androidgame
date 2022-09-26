package com.example.androidgame

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.androidgame.databinding.FragmentSettingBinding
import com.example.androidgame.databinding.FragmentTutorialBinding
import java.util.*

class tutorial : Fragment() {
    private var _binding: FragmentTutorialBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTutorialBinding.inflate(inflater, container, false)

        //var volume = AudioManager.STREAM_MUSIC
        binding.backbtn.setOnClickListener{

        }
        binding.nextbtn.setOnClickListener{

        }

        binding.titlebtn2.setOnClickListener{
            val intent = Intent(activity, StaActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    fun volume(){
    }
}