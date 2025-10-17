package com.example.baitap2
import android.graphics.Color
import com.example.baitap2.R
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private  var edtNumber: EditText? = null
    private  var btnCreate: Button? = null
    private  var tvError: TextView? = null
    private  var containerButtons: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtNumber = findViewById(R.id.edtNumber)
        btnCreate = findViewById(R.id.btnCreate)
        tvError = findViewById(R.id.tvError)
        containerButtons = findViewById(R.id.containerButtons)

        btnCreate?.setOnClickListener {
            val input = edtNumber?.text.toString().trim()
            containerButtons?.removeAllViews()
            tvError?.visibility = TextView.GONE

            try {
                val n = input.toInt()
                for (i in 1..n) {
                    val btn = Button(this)
                    btn.text = i.toString()
                    btn.setBackgroundColor(Color.RED)

                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 20, 0, 0)
                    btn.layoutParams = params

                    containerButtons?.addView(btn)
                }
            } catch (e: NumberFormatException) {
                tvError?.visibility = TextView.VISIBLE
            }
        }
    }
}
