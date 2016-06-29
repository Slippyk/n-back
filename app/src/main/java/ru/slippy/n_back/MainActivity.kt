package ru.slippy.n_back

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import java.util.ArrayList
import java.util.LinkedList
import java.util.Random

class MainActivity : Activity() {

    private var list: LinkedList<Int>? = null
    private var level: Int = 0
    private var count: Int = 0
    private var fields: ArrayList<Int>? = null
    private var handler: Handler? = null
    private var background: Drawable? = null
    private var r: Random? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        r = Random()
        handler = Handler()
        list = LinkedList<Int>()
        fields = ArrayList<Int>()
        fields!!.add(R.id.field1)
        fields!!.add(R.id.field2)
        fields!!.add(R.id.field3)
        fields!!.add(R.id.field4)
        fields!!.add(R.id.field5)
        fields!!.add(R.id.field6)
        fields!!.add(R.id.field7)
        fields!!.add(R.id.field8)
        fields!!.add(R.id.field9)
    }

    fun selectLevel(v: View) {
        level = Integer.parseInt((v as Button).text.toString())

        val message = String.format(resources.getString(R.string.choose_level), level)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        list!!.clear()
        count = 0

        nextElement()
        setContentView(R.layout.game)
        setEnableFields(false)
    }

    fun nextElement() {
        list!!.add(r!!.nextInt(8))
        handler!!.postDelayed({
            showElement(list!!.last)
            if (list!!.size < level) {
                nextElement()
            }
        }, NEXT_DELAY.toLong())
    }

    fun selectField(v: View) {
        setEnableFields(false)
        val expected = list!!.removeFirst()
        val result = fields!!.indexOf(v.id)
        if (expected == result) {
            showCorrect()
        } else {
            showIncorrect()
        }
    }

    fun showElement(i: Int) {
        val view = findViewById(fields!![i])
        background = view.background
        view.setBackgroundColor(resources.getColor(android.R.color.black))
        handler!!.postDelayed({ hideElement(i) }, SHOW_DELAY.toLong())
    }

    fun hideElement(i: Int) {
        findViewById(fields!![i]).background = background
        if (list!!.size == level) {
            setEnableFields(true)
        }
    }

    fun setEnableFields(enabled: Boolean) {
        for (id in fields!!) {
            findViewById(id).isClickable = enabled
        }
    }

    private fun showCorrect() {
        val result = findViewById(R.id.text_result) as TextView
        result.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))
        result.setText(R.string.correct)
        handler!!.postDelayed({ hideCorrect() }, CORRECT_DELAY.toLong())
        count++
        nextElement()
    }

    fun hideCorrect() {
        val result = findViewById(R.id.text_result) as TextView
        result.setBackgroundColor(resources.getColor(android.R.color.white))
        result.text = ""
    }

    private fun showIncorrect() {
        val result = findViewById(R.id.text_result) as TextView
        result.setBackgroundColor(resources.getColor(android.R.color.holo_red_dark))
        result.setText(R.string.incorrect)
        val message = String.format(resources.getString(R.string.final_count), count)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        handler!!.postDelayed({ setContentView(R.layout.activity_main) }, INCORRECT_DELAY.toLong())
    }

    companion object {

        internal val SHOW_DELAY = 700
        internal val NEXT_DELAY = 1000
        internal val CORRECT_DELAY = 500
        internal val INCORRECT_DELAY = 3000
    }
}
