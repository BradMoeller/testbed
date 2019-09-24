package com.example.bradmoeller.myapplication.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.bradmoeller.myapplication.R

class LifecycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_list)
        lifecycle.addObserver(LifecycleLogger())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun listeningToCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun listeningToDestroy() {

    }
}