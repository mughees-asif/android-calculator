package com.mughees.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND = "Operand"
private const val STATE_OPERAND_STORED = "Operand_Stored"

class MainActivity : AppCompatActivity() {
    private lateinit var result: EditText
    private lateinit var newNumber: EditText
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    // Variables to hold the operands and type of calculations
    private var operand: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)

        // Data input buttons
        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val buttonDot: Button = findViewById(R.id.buttonDot)

        // Operation buttons
        val buttonEquals: Button = findViewById<Button>(R.id.buttonEquals)
        val buttonMultiply: Button = findViewById<Button>(R.id.buttonMultiply)
        val buttonDivide: Button = findViewById<Button>(R.id.buttonDivide)
        val buttonPlus: Button = findViewById<Button>(R.id.buttonPlus)
        val buttonMinus: Button = findViewById<Button>(R.id.buttonMinus)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        // *improvement* use a loop
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
            displayOperation.text = pendingOperation
        }
        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
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
        displayOperation.text = pendingOperation
    }
}
