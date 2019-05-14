package com.paradroid.simpledice.model

class Round(
    var diceList: MutableList<Dice> = mutableListOf(),
    var numberOfFaces: Int = 6,
    var numberOfRolls: Int = 1
) {

    fun createDices() {
        diceList.clear()
        for (i in 1..numberOfRolls) {
            diceList.add(Dice(numberOfFaces))
        }
    }


    fun rollOrReRollDices(): MutableList<Dice> {

        if (diceList.size != numberOfRolls) this.createDices()

        if (diceList.all { dice -> !dice.selected }) {
            diceList.forEach { it.rollDice() }
        } else {
            diceList.forEach { if (it.selected) it.rollDice() }
        }
        return diceList
    }
}
