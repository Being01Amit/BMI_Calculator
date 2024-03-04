package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // dec variables
    private lateinit var weight_input : EditText
    private lateinit var feet_input : EditText
    private lateinit var inches_input : EditText
    private lateinit var btnCalculate : Button
    private lateinit var bmiResult : TextView
    private lateinit var info : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // link UI Element
        weight_input  = findViewById(R.id.weight_input)
        feet_input = findViewById(R.id.feet_input)
        inches_input = findViewById(R.id.inches_input)
        btnCalculate = findViewById(R.id.btnCalculat)
        info = findViewById(R.id.info)
        bmiResult = findViewById(R.id.bmi_result)

        // perform specific action when cleck

        info.setOnClickListener{
            val intent = Intent(this, Information_page::class.java)
            startActivity(intent)
        }
        btnCalculate.setOnClickListener {
            val weightStr = weight_input.text.toString().trim()
            val feetStr = feet_input.text.toString().trim()
            val inchesStr = inches_input.text.toString().trim()

            // Check for empty fields
            if (isEmpty(weightStr, feetStr, inchesStr)) {
                bmiResult.text = getString(R.string.error_empty_fields)
                return@setOnClickListener
            }

            // Validate values (example: weight > 0, feet and inches positive)
            if (!validateValues(weightStr, feetStr, inchesStr)) {
                bmiResult.text = getString(R.string.invalid_input)
                return@setOnClickListener
            }

            // Convert inputs to doubles (with error handling)
            val weight = try {
                weightStr.toDouble()
            } catch (e: NumberFormatException) {
                bmiResult.text = getString(R.string.invalid_weight)
                return@setOnClickListener
            }
            val feet = feetStr.toDouble()
            val inches = inchesStr.toDouble()

            // Convert height to meters
            val heightInMeters = feetAndInchesToMeters(feet, inches)

            // Calculate BMI
            val bmi = weight / (heightInMeters * heightInMeters)

            // Display the result with formatted string
            bmiResult.text = String.format(
                getString(R.string.bmi_result_format), bmi, getBmiCategory(bmi)
            )
        }
    }
    private fun isEmpty(vararg fields: String): Boolean {
        return fields.any { TextUtils.isEmpty(it) }
    }

    private fun validateValues(weightStr: String, feetStr: String, inchesStr: String): Boolean {
        // Implement validation logic (e.g., weight > 0, feet and inches positive)
        if (weightStr.toDoubleOrNull()!! <= 0 || feetStr.toDoubleOrNull()!! < 0 || inchesStr.toDoubleOrNull()!! < 0) {
            return false
        }
        return true
    }

    private fun feetAndInchesToMeters(feet: Double, inches: Double): Double {
        val totalInches = feet * 12 + inches
        return totalInches * 0.0254
    }
    private fun getBmiCategory(bmi: Double): String {
        return when (bmi) {
            in Double.MIN_VALUE..18.4 -> "Underweight"
            in 18.5..24.9 -> "Normal weight"
            in 25.0..29.9 -> "Overweight"
            else -> "Obese"
        }
    }
}