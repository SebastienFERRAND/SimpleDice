package com.paradroid.simpledice.adapter

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.paradroid.simpledice.R
import com.paradroid.simpledice.inflate
import com.paradroid.simpledice.model.Dice
import kotlinx.android.synthetic.main.item_dice_result.view.*


class DiceResultAdapter(private val diceResult: List<Dice> = listOf()) :
    RecyclerView.Adapter<DiceResultAdapter.DicesHolder>() {

    val DICES_PER_LINES = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DicesHolder {
        val inflatedView = parent.inflate(R.layout.item_dice_result, false)
        return DicesHolder(inflatedView)
    }

    override fun getItemCount() = diceResult.size.div(DICES_PER_LINES) + 1

    override fun onBindViewHolder(holder: DicesHolder, position: Int) {
        val itemDice = getDiceLinePosition(position)
        holder.bindDices(itemDice, position + 1)
    }

    private fun getDiceLinePosition(position: Int): Array<Dice?> {
        val arrayDices: Array<Dice?> = arrayOfNulls(DICES_PER_LINES)
        var index: Int
        for (i in 0 until DICES_PER_LINES) {
            index = position * DICES_PER_LINES + i
            if (index < diceResult.size) {
                arrayDices[i] = diceResult[index]
            } else {
                break
            }
        }
        return arrayDices
    }

    inner class DicesHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var dices: Array<Dice?> = arrayOfNulls(DICES_PER_LINES)

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {

        }

        fun bindDices(dices: Array<Dice?> = arrayOfNulls(DICES_PER_LINES), position: Int) {
            this.dices = dices
            view.dice_1.setImageResource(dices[0]?.getDiceDrawable() ?: R.drawable.empty_dice)
            view.dice_2.setImageResource(dices[1]?.getDiceDrawable() ?: R.drawable.empty_dice)
            view.dice_3.setImageResource(dices[2]?.getDiceDrawable() ?: R.drawable.empty_dice)

            selectedOrNot(position * 3 - 3, view.dice_1)
            selectedOrNot(position * 3 - 2, view.dice_2)
            selectedOrNot(position * 3 - 1, view.dice_3)

            view.dice_1.setOnClickListener {
                selectDice(position * 3 - 3, view.dice_1)
            }
            view.dice_2.setOnClickListener {
                selectDice(position * 3 - 2, view.dice_2)
            }
            view.dice_3.setOnClickListener {
                selectDice(position * 3 - 1, view.dice_3)
            }
        }

        private fun selectDice(index: Int, imageView: ImageView) {
            if (index < diceResult.size) {
                diceResult[index].selected = !diceResult[index].selected
                selectedOrNot(index, imageView)
            }
        }

        private fun selectedOrNot(index: Int, imageView: ImageView) {
            if (index < diceResult.size) {
                if (diceResult[index].selected) {
                    imageView.setBackgroundColor(Color.LTGRAY)
                } else {
                    imageView.setBackgroundColor(Color.TRANSPARENT)
                }
            }
        }
    }
}
