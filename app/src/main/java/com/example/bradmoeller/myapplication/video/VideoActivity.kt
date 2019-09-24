package com.example.bradmoeller.myapplication.video

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.bradmoeller.myapplication.R
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_video.*


class VideoActivity : AppCompatActivity(), Player.EventListener {

    private val tag: String = "VIDEO"
    private val readRequestCode: Int = 205
    private lateinit var player: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        play_text.setOnClickListener {
            playVideo()
        }

    }

    private fun requestAppPermissions() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), readRequestCode) // your request code
    }

    private fun hasReadPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(baseContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasWritePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(baseContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }



    private fun playVideo() {

        if (!hasReadPermissions()) {
            requestAppPermissions()
            return
        }

        var stringPath = "sdcard/DCIM/Camera/assist-3-0.mp4"//Environment.getExternalStorageDirectory().path + "/DCIM/Camera/assist-3-0.mp4"
        //stringPath = android.net.Uri.parse("file://$stringPath").path

        var videoUri = Uri.parse(stringPath)


        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        player_view.player = player

        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "myapplication"), null)
        // This is the MediaSource representing the media to be played.
        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri)
        // Prepare the player with the source.
        player.prepare(videoSource)
        player.addListener(this)

        player.playWhenReady
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
        Log.i(tag, "onLoadingChanged")
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        Log.i(tag, "onLoadingChanged")
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        Log.i(tag, "onLoadingChanged")
    }
///sdcard/DCIM/Camera/assist-3-0.mp4
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        Log.i(tag, "onPlayerStateChanged")
    }











    override fun onSeekProcessed() {
        Log.i(tag, "onSeekProcessed")
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
        Log.i(tag, "onTracksChanged")
        Log.i(tag, trackGroups?.toString())
    }

    override fun onPositionDiscontinuity(reason: Int) {
        Log.i(tag, "onPositionDiscontinuity")
        Log.i(tag, reason?.toString())
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        Log.i(tag, "onRepeatModeChanged")
        Log.i(tag, repeatMode?.toString())
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        Log.i(tag, "onShuffleModeEnabledChanged")
        Log.i(tag, shuffleModeEnabled?.toString())
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
        Log.i(tag, "onTimelineChanged")
        Log.i(tag, timeline?.toString())
    }

}