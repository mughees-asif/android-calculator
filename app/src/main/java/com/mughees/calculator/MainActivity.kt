package com.mughees.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND = "Operand"
private const val STATE_OPERAND_STORED = "Operand_Stored"

class MainActivity : AppCompatActivity() {

    // Variables to hold the operands and type of calculations
    private var operand: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        // TODO: use a loop
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            // fixed: try/catch stops app crashing when . is pressed on its own
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }
        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)

        buttonNeg.setOnClickListener { view ->
            val value = newNumber.text.toString()
            if (value.isEmpty()) {
                newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue += -1
                    newNumber.setText("-")
                } catch (e: NumberFormatException) {
                    // newNumber was "-" or ".", so clear it
                    newNumber.setText("")
                }
            }
        }
    }

    // calculations
    private fun performOperation(value: Double, operation: String) {
        if (operand == null) {
            operand = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }
            when (pendingOperation) {
                "=" -> operand = value
                "/" -> operand = if (value == 0.0) {
                    Double.NaN          // handle attempt to divide by zero
                } else {
                    operand!! / value
                }
                "*" -> operand = operand!! * value
                "-" -> operand = operand!! - value
                "+" -> operand = operand!! + value
            }
        }
        result.setText(operand.toString())
        newNumber.setText("")
    }

    // fixed: saves the state when switching from Portrait to Landscape
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(operand != null) {
            outState.putDouble(STATE_OPERAND, operand!!)
            outState.putBoolean(STATE_OPERAND_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }
    // fixed: restores the state when switching from Portrait to Landscape
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand = if(savedInstanceState.getBoolean(STATE_OPERAND_STORED, false)) {
            savedInstanceState.getDouble(STATE_OPERAND)
        } else {
            null
        }
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        operation.text = pendingOperation
    }
}
