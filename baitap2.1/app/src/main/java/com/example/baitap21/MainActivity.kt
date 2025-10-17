package com.example.baitap21
import com.example.baitap21.R
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var edtNumber: EditText
    private lateinit var btnCreate: Button
    private lateinit var tvError: TextView
    private lateinit var tvInvalid: TextView
    private lateinit var tvValid: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtNumber = findViewById(R.id.edtNumber)
        btnCreate = findViewById(R.id.btnCreate)
        tvError = findViewById(R.id.tvError)
        tvInvalid = findViewById(R.id.tvInvalid)
        tvValid = findViewById(R.id.tvValid)

    btnCreate.setOnClickListener {
        val email = edtNumber.text.toString().trim()

        tvError.visibility = View.GONE
        tvInvalid.visibility = View.GONE
        tvValid.visibility = View.GONE

        if(email.isEmpty()){
            tvError.visibility = View.VISIBLE
        }else if(!email.contains("@")){
            tvInvalid.visibility = View.VISIBLE
        }else{
            tvValid.visibility = View.VISIBLE
        }
    }

    }
}