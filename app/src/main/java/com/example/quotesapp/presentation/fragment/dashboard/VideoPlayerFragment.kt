package com.example.quotesapp.presentation.fragment.dashboard

import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.quotesapp.R
import com.example.quotesapp.databinding.BottomSheetFragmentVideoPlayerBinding
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.util.GlideLoader


class VideoPlayerFragment : BaseFragment<BottomSheetFragmentVideoPlayerBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> BottomSheetFragmentVideoPlayerBinding =
        BottomSheetFragmentVideoPlayerBinding::inflate

    private  val args by navArgs<VideoPlayerFragmentArgs>()
    private var isVideoPlaying  = false
    private var handler: Handler = Handler()
    private var runnable: Runnable? = null


    override fun setup() {
        GlideLoader(requireContext()).loadUserPicture(if (args.isExchange) "http://143.110.247.128:8008/exchange/video/"  else "http://143.110.247.128:8008/user/video/"+args.videofile,binding?.videoPlayerImg)

        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }



        val videoUri =
            if (args.isExchange)
            Uri.parse("http://143.110.247.128:8008/exchange/video/"+args.videofile)
        else
            Uri.parse("http://143.110.247.128:8008/user/video/"+args.videofile)

        binding?.videoPlayer?.setVideoURI(videoUri)

        // Set up SeekBar
        binding?.videoPlayer?.setOnPreparedListener { mediaPlayer ->
            val duration = mediaPlayer.duration
            binding?.seekBar?.max = duration
            val totalTime = formatTime(duration)
            binding?.leftTime?.text = totalTime
            binding?.progressBar?.isVisible = false
            binding?.videoPlayerImg?.isVisible = false
        }

        binding?.videoPlayer?.setOnCompletionListener {
            isVideoPlaying = false
            binding?.btnPlay?.setImageResource(R.drawable.baseline_play_arrow_24)
        }

        // Play button click listener
        binding?.btnPlay?.setOnClickListener {
            if (!isVideoPlaying) {
                binding?.videoPlayer?.start()
                isVideoPlaying = true
                binding?.btnPlay?.setImageResource(R.drawable.baseline_pause_24)
                updateSeekBarAndTime()
            } else {
                binding?.videoPlayer?.pause()
                isVideoPlaying = false
                binding?.btnPlay?.setImageResource(R.drawable.baseline_play_arrow_24)
                stopSeekBarAndTimeUpdate()
            }
        }

        // Forward button click listener
        binding?.btnForward?.setOnClickListener {
            val currentPosition = binding?.videoPlayer?.currentPosition
            if (currentPosition != null) {
                binding?.videoPlayer?.seekTo(currentPosition + 10000)
            } // Forward 10 seconds
        }

        // Rewind button click listener
        binding?.btnRewind?.setOnClickListener {
            val currentPosition = binding?.videoPlayer?.currentPosition
            if (currentPosition != null) {
                binding?.videoPlayer?.seekTo(currentPosition - 10000)
            } // Rewind 10 seconds
        }

        binding?.seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding?.videoPlayer?.seekTo(progress)
                    val currentTime = formatTime(progress)
                    binding?.startTime?.text = currentTime

                    val totalDuration = binding?.videoPlayer?.duration
                    val remainingTime = formatTime(totalDuration?.minus(progress) ?:0 )
                    binding?.leftTime?.text = remainingTime
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onStop() {
        super.onStop()
        if (!isVideoPlaying) {
            binding?.videoPlayer?.start()
            isVideoPlaying = true
            binding?.btnPlay?.setImageResource(R.drawable.baseline_pause_24)
            updateSeekBarAndTime()
        } else {
            binding?.videoPlayer?.pause()
            isVideoPlaying = false
            binding?.btnPlay?.setImageResource(R.drawable.baseline_play_arrow_24)
            stopSeekBarAndTimeUpdate()
        }
    }

    override fun onResume() {
        super.onResume()
            val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
            view.visibility = View.GONE
        if (!isVideoPlaying) {
            binding?.videoPlayer?.start()
            isVideoPlaying = true
            binding?.btnPlay?.setImageResource(R.drawable.baseline_pause_24)
            updateSeekBarAndTime()
        } else {
            binding?.videoPlayer?.pause()
            isVideoPlaying = false
            binding?.btnPlay?.setImageResource(R.drawable.baseline_play_arrow_24)
            stopSeekBarAndTimeUpdate()
        }
    }

    private fun updateSeekBarAndTime() {
        runnable = Runnable {
            binding?.seekBar?.progress = binding?.videoPlayer?.currentPosition?:0
            val currentTime = formatTime(binding?.videoPlayer?.currentPosition?:0)
            binding?.startTime?.text = currentTime

            val totalDuration = binding?.videoPlayer?.duration
            val remainingTime = formatTime(totalDuration?.minus(binding?.videoPlayer?.currentPosition?:0) ?:0 )
            binding?.leftTime?.text = remainingTime

            runnable?.let { handler.postDelayed(it, 1000) } // Update every second
        }
        handler.postDelayed(runnable!!, 0)
    }

    private fun stopSeekBarAndTimeUpdate() {
        runnable?.let { handler.removeCallbacks(it) }
    }

    private fun formatTime(timeInMillis: Int): String {
        val seconds = (timeInMillis / 1000) % 60
        val minutes = (timeInMillis / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


}
