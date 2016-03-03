package com.paradroid.simpledice.model

import com.paradroid.simpledice.R
import kotlin.random.Random

open class DiceGame(var numberOfFaces: Int = 6, var numberOfRolls: Int = 1) {

    fun rollDice(): MutableList<Int> {
        val listResult = mutableListOf<Int>()
        for (i in 1..numberOfRolls) {
            val random = Random.nextInt(numberOfFaces) + 1
            listResult.add(random)
        }
        return listResult
    }

    fun getDiceDrawableResourceListFromIntList(listResult: MutableList<Int>): MutableList<Int> {
        var listResourcesResult = mutableListOf<Int>()
        for (result in listResult) {
            when (result) {
                1 -> listResourcesResult.add(R.drawable.dice_1)
                2 -> listResourcesResult.add(R.drawable.dice_2)
                3 -> listResourcesResult.add(R.drawable.dice_3)
                4 -> listResourcesResult.add(R.drawable.dice_4)
                5 -> listResourcesResult.add(R.drawable.dice_5)
                6 -> listResourcesResult.add(R.drawable.dice_6)
                else -> R.drawable.dice_1
            }
        }
        return listResourcesResult
    }

    fun getDiceDrawableResourceFromInt(result: Int): Int {
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
