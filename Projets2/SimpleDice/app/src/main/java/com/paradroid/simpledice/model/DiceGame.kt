package com.paradroid.simpledice.model

data class DiceGame(
    var name: String = "Free play",
    var roundList: MutableList<Round> = mutableListOf()
) {

    fun addRound(): Round {
        val currentRound = Round()
        roundList.add(currentRound)
        return currentRound
    }
}
