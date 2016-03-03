package com.paradroid.simpledice.activity

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paradroid.simpledice.R
import com.paradroid.simpledice.adapter.DiceResultAdapter
import com.paradroid.simpledice.model.DiceGame

class MainActivity : AppCompatActivity() {

    private lateinit var seekBarNumberOfFaces: SeekBar
    private lateinit var seekBarNumberOfRoll: SeekBar
    private lateinit var textviewValueFaces: TextView
    private lateinit var textviewValueRolls: TextView
    private lateinit var textviewDiceResult: TextView
    private lateinit var recyclerViewDiceResult: RecyclerView

    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var adapter: DiceResultAdapter


    private var dice = DiceGame()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewDiceResult = findViewById(R.id.recyclerViewListresult)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerViewDiceResult.layoutManager = linearLayoutManager

        textviewValueFaces = findViewById(R.id.textViewValueFaces)
        textviewValueRolls = findViewById(R.id.textViewValueRolls)
        textviewDiceResult = findViewById(R.id.textview_dice_result)


        textviewValueFaces.text = "6"
        textviewValueRolls.text = "1"

        seekBarNumberOfFaces = findViewById(R.id.seekbar_nb_faces)
        seekBarNumberOfFaces.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                dice.numberOfFaces = i
                textviewValueFaces.text = i.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })

        seekBarNumberOfRoll = findViewById(R.id.seekbar_nb_of_rolls)
        seekBarNumberOfRoll.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                dice.numberOfRolls = i
                textviewValueRolls.text = i.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })

        val rollButton: Button = findViewById(R.id.button_roll)
        rollButton.text = "Let's Roll"
        rollButton.setOnClickListener {
            textviewDiceResult.text = ""
            adapter = DiceResultAdapter(dice.rollDice())
            recyclerViewDiceResult.adapter = adapter
        }
    }
}
