package com.example.infosys.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.infosys.R
import com.example.infosys.InfosysApplication.Companion.USER
import com.example.infosys.model.User
import com.example.infosys.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        USER = User("", "", "","", 1)

        findViewById<Button>(R.id.btnChange).setOnClickListener {
            if (validate()) login()
        }

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        loadingMode(false)
        observeViewModel()
    }

    private fun validate(): Boolean {
        var valid = true
        val txtID = findViewById<EditText>(R.id.txtID)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val id: String = txtID.text.toString().trim()
        val password: String = txtPassword.text.toString().trim()
        if (id.isEmpty()) {
            txtID.error = "enter a ID"
            valid = false
        } else {
            txtID.error = null
        }
        if (password.isEmpty()) {
            txtPassword.error = "password cannot be blank"
            valid = false
        } else {
            txtPassword.error = null
        }
        return valid
    }

    fun login() {
        loadingMode(true)
        val txtID = findViewById<EditText>(R.id.txtID)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val id: String = txtID.text.toString().trim()
        val password: String = txtPassword.text.toString().trim()

        val user = User(id,"", password,"",1)
        viewModel.login(user)
    }

    fun loadingMode(yes: Boolean) {
        if (yes) {
            findViewById<ConstraintLayout>(R.id.layoutLoading).visibility = View.VISIBLE
            findViewById<ScrollView>(R.id.scrollViewLogin).visibility = View.GONE
        } else {
            findViewById<ConstraintLayout>(R.id.layoutLoading).visibility = View.GONE
            findViewById<ScrollView>(R.id.scrollViewLogin).visibility = View.VISIBLE
        }
    }

    private fun observeViewModel() {
        viewModel.login.observe(this, Observer { user: User ->
            user.let {
                loadingMode(false)
                if (user.userID == "") {
                    Snackbar
                        .make(findViewById(R.id.coordinatorLayout), user.userName + "Wrong ID/Password OR Time not allowed", Snackbar.LENGTH_LONG)
                        .setAction("OK") { }
                        .show()
                } else {
                    USER = User(user.userID, user.userName, user.userPassword, user.userLevel, user.userActive)
                    Snackbar
                        .make(findViewById(R.id.coordinatorLayout), user.userName + "ID & Password are valid", Snackbar.LENGTH_LONG)
                        .setAction("OK") { }
                        .show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        })

        viewModel.error.observe(this, Observer { error: String ->
            error.let {
                if (it != "") {
                    Snackbar
                        .make(findViewById(R.id.coordinatorLayout), it, Snackbar.LENGTH_LONG)
                        .show()
                    viewModel.error.value = ""
                }
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            loadingMode(isLoading)
        })
    }
}