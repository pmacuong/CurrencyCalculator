package com.example.currencycalculator

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.TextWatcher
import android.text.Editable
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var fromAmountEditText: EditText
    private lateinit var toAmountEditText: EditText
    private lateinit var fromCurrencySpinner: Spinner
    private lateinit var toCurrencySpinner: Spinner
    private lateinit var resultTextView: TextView

    private val currencies = arrayOf("USD", "EUR", "VND")
    private val conversionRates = mapOf(
        "USD" to mapOf("EUR" to 0.93, "VND" to 25310.00, "USD" to 1.00),
        "EUR" to mapOf("USD" to 1.08, "VND" to 27333.00, "EUR" to 1.00),
        "VND" to mapOf("EUR" to 0.000037, "USD" to 0.000040, "VND" to 1.00)
    )

    private var isConverting = false
    private var isProgrammaticChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fromAmountEditText = findViewById(R.id.from_amount_edit_text)
        toAmountEditText = findViewById(R.id.to_amount_edit_text)
        fromCurrencySpinner = findViewById(R.id.from_currency_spinner)
        toCurrencySpinner = findViewById(R.id.to_currency_spinner)
        resultTextView = findViewById(R.id.result_text_view)

        val fromAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        fromCurrencySpinner.adapter = fromAdapter
        val toAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        toCurrencySpinner.adapter = toAdapter

        fromAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isProgrammaticChange) {
                    if (s.toString()!= fromAmountEditText.tag.toString()) {
                        convertCurrency(fromCurrencySpinner.selectedItem.toString(), toCurrencySpinner.selectedItem.toString(), s.toString())
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        toAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isProgrammaticChange) {
                    if (s.toString()!= toAmountEditText.tag.toString()) {
                        convertCurrency(toCurrencySpinner.selectedItem.toString(), fromCurrencySpinner.selectedItem.toString(), s.toString())
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        fromCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertCurrency(fromCurrencySpinner.selectedItem.toString(), toCurrencySpinner.selectedItem.toString(), fromAmountEditText.text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        toCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertCurrency(toCurrencySpinner.selectedItem.toString(), fromCurrencySpinner.selectedItem.toString(), toAmountEditText.text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun convertCurrency(fromCurrency: String, toCurrency: String, amount: String) {
        if (isConverting) {
            isConverting = false
            return
        }

        if (fromCurrencySpinner!= null && toCurrencySpinner!= null && fromAmountEditText!= null && toAmountEditText!= null) {
            val exchangeRate = conversionRates[fromCurrency]?.get(toCurrency)?: 0.0
            if (amount.isNotEmpty()) {
                val convertedAmount = amount.toDouble() * exchangeRate
                isConverting = true
                if (fromCurrency == fromCurrencySpinner.selectedItem.toString()) {
                    isProgrammaticChange = true
                    fromAmountEditText.tag = amount
                    toAmountEditText.tag = ""
                    toAmountEditText.setText(String.format("%.2f", convertedAmount))
                    isProgrammaticChange = false
                } else {
                    isProgrammaticChange = true
                    toAmountEditText.tag = amount
                    fromAmountEditText.tag = ""
                    fromAmountEditText.setText(String.format("%.2f", convertedAmount))
                    isProgrammaticChange = false
                }
                isConverting = false
            } else {
                toAmountEditText.tag = ""
                fromAmountEditText.tag = ""
                toAmountEditText.setText("")
                fromAmountEditText.setText("")
            }
        } else {
            Log.e("MainActivity", "One or more views are null")
        }
    }
}