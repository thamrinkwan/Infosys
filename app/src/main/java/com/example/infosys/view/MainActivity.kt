package com.example.infosys.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.infosys.R
import com.example.infosys.view.user.UserActivity
import com.example.infosys.view.user.UserDetailActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                //do something
            }
        }
        val intent = Intent(this, LoginActivity::class.java)
        launcher.launch(intent)

        findViewById<Button>(R.id.btnUserList).setOnClickListener {
            val intentUser = Intent(this, UserActivity::class.java)
            launcher.launch(intentUser)
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val intentUser = Intent(this, UserDetailActivity::class.java)
            intentUser.putExtra("Mode", true)
            launcher.launch(intentUser)
        }
    }
}