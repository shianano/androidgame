package com.example.androidgame

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.androidgame.databinding.FragmentSettingBinding
import java.util.*


class SettingFragment : Fragment() {


    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        var volume = AudioManager.STREAM_MUSIC
        binding.ringVolSeekBarr.setProgress(0)
        binding.ringVolSeekBarr.setMax(100)
        binding.pervolume.text = 0.toString() + "%"

        binding.titleButton.setOnClickListener{
            AlertDialog.Builder(requireContext())
                    .setTitle("title")
                    .setMessage("message")
                    .setPositiveButton("いいえ", { dialog, which ->
                    })
                    .setNegativeButton("はい", { dialog, which ->
                        val intent = Intent(activity, StaActivity::class.java)
                        startActivity(intent)
                    })
                    .show()
        }
        binding.ringVolSeekBarr.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    //ツマミがドラッグされると呼ばれる
                    override fun onProgressChanged(
                            seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        // 68 % のようにフォーマト
                        val str: String = String.format(Locale.US, "%d %%",progress);
                        binding.pervolume.text = str
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        // ツマミがタッチされた時に呼ばれる
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        // ツマミがリリースされた時に呼ばれる
                    }

                }
        )
        return binding.root
    }

    fun volume(){
    }
}