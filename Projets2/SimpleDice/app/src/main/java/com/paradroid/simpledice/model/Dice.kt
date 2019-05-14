package com.paradroid.simpledice.model

import com.paradroid.simpledice.R
import kotlin.random.Random

data class Dice(var facesNb: Int = 6) {
    var result: Int = 0
    var selected: Boolean = false

    fun rollDice() {
        this.result = Random.nextInt(facesNb) + 1
    }


    fun getDiceDrawable(): Int {
        return when (result) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.empty_dice
        }
    }
}

