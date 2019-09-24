package com.example.bradmoeller.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_kotlin_test.*

/**
 * https://kotlinexpertise.com/coping-with-kotlins-scope-functions/
 */
class KotlinTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_kotlin_test)

        button.setOnClickListener {
            onClick()
        }
    }

    private fun onClick() {
        letExample(null)
        letExample(5)
        runExample("test string")
    }

    /**
     *  val str: String = "..."
     *  val result = str.xxx {
        print(this) // Receiver
        print(it) // Argument
        42 // Block return value
        }
     *
     * ╔══════════╦═════════════════╦═══════════════╦═══════════════╗
       ║ Function ║ Receiver (this) ║ Argument (it) ║    Result     ║
       ╠══════════╬═════════════════╬═══════════════╬═══════════════╣
       ║ let      ║ this@MyClass    ║ String("...") ║ Int(42)       ║
       ║ run      ║ String("...")   ║ N\A           ║ Int(42)       ║
       ║ run*     ║ this@MyClass    ║ N\A           ║ Int(42)       ║
       ║ with*    ║ String("...")   ║ N\A           ║ Int(42)       ║
       ║ apply    ║ String("...")   ║ N\A           ║ String("...") ║
       ║ also     ║ this@MyClass    ║ String("...") ║ String("...") ║
       ╚══════════╩═════════════════╩═══════════════╩═══════════════╝
     */

    /**
     * "it" is the object let is called on
     */
    private fun letExample(number: Int?) {
        // print the number
        number?.let {
            Toast.makeText(this, "letExample - number: $number", Toast.LENGTH_SHORT).show()
        }

        // print the number + 10
        val add10 = number?.let { it + 10 } ?: 10
        Toast.makeText(this, "letExample - add10: $add10", Toast.LENGTH_SHORT).show()
    }

    /**
     * "this" is the object run is called on
     */
    private fun runExample(string: String?) {
        string?.run {
            Toast.makeText(this@KotlinTestActivity, capitalize(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun alsoExample() {

    }
}