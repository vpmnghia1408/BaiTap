package com.example.baitap22
import com.example.baitap22.R
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // 🔹 Khai báo các biến toàn cục với lateinit
    private lateinit var edtName: EditText
    private lateinit var edtAge: EditText
    private lateinit var btnCheck: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        edtName = findViewById(R.id.edtName)
        edtAge = findViewById(R.id.edtAge)
        btnCheck = findViewById(R.id.btnCheck)


        btnCheck.setOnClickListener {
            val name = edtName.text.toString().trim()
            val ageText = edtAge.text.toString().trim()

            if (name.isEmpty() || ageText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageText.toIntOrNull()
            if (age == null || age < 0) {
                Toast.makeText(this, "Tuổi không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val result = when {
                age > 65 -> "Người già"
                age >= 6 -> "Người lớn"
                age >= 2 -> "Trẻ em"
                else -> "Em bé"
            }


            Toast.makeText(this, "$name là $result", Toast.LENGTH_LONG).show()
        }
    }
}
