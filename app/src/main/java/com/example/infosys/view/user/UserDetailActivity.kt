package com.example.infosys.view.user

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.infosys.R
import com.example.infosys.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputLayout
import com.example.infosys.model.User
import com.google.android.material.snackbar.Snackbar
import com.example.infosys.model.Message

class UserDetailActivity : AppCompatActivity() {
    private val listLevel = arrayOf("admin", "manager", "staff")
    private lateinit var viewModel: UserViewModel
    private var newMode: Boolean = false

    lateinit var spinnerLevel: Spinner
    lateinit var txtID: EditText
    lateinit var txtName: EditText
    lateinit var txtPassword: EditText
    lateinit var switchActive: Switch
    lateinit var layoutID: TextInputLayout
    lateinit var layoutName: TextInputLayout
    lateinit var layoutPassword: TextInputLayout
    lateinit var lblLevel: TextView
    lateinit var loading: ProgressBar
    lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            if (validate()) {
                if (newMode)
                    insertUser()
                else
                    updateUser()
            }
        }

        spinnerLevel = findViewById(R.id.spinnerLevel)
        txtID = findViewById(R.id.txtID)
        txtName = findViewById(R.id.txtName)
        txtPassword = findViewById(R.id.txtPassword)
        switchActive = findViewById(R.id.switchActive)
        layoutID = findViewById(R.id.layoutID)
        layoutName = findViewById(R.id.layoutName)
        layoutPassword = findViewById(R.id.layoutPassword)
        lblLevel = findViewById(R.id.lblLevel)
        loading = findViewById(R.id.loading)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)


        val adapter= ArrayAdapter(this, android.R.layout.simple_list_item_1, listLevel)
        spinnerLevel.adapter=adapter

        newMode = intent.getBooleanExtra("Mode", false)
        val id: String? = intent.getStringExtra("ID")
        val name: String? = intent.getStringExtra("Name")
        val level: String? = intent.getStringExtra("Level")
        val active: Int = intent.getIntExtra("Active", 1)

        if (newMode){
            txtID.inputType = InputType.TYPE_CLASS_TEXT
            txtID.isFocusable = true
            txtID.isClickable = true
        } else {
            txtID.inputType = InputType.TYPE_NULL
            txtID.isFocusable = false
            txtID.isClickable = false

            txtID.setText(id)
            txtName.setText(name)
            spinnerLevel.setSelection((spinnerLevel.adapter as ArrayAdapter<String>).getPosition(level))
            switchActive.isChecked = active == 1
        }

        val vmp = ViewModelProvider(this)
        viewModel = vmp.get(UserViewModel::class.java)

        observeViewModel()

        loadingMode(false)
    }

    private fun validate(): Boolean {
        var valid = true
        val id: String = txtID.text.toString().trim()
        val name: String = txtName.text.toString().trim()
        val password: String = txtPassword.text.toString().trim()

        when {
            id.isEmpty() -> {
                txtID.error = "enter an ID"
                valid = false
            }
            else -> {
                txtID.error = null
            }
        }

        when {
            name.isEmpty() -> {
                txtName.error = "enter a Name"
                valid = false
            }
            else -> {
                txtName.error = null
            }
        }

        if (newMode) {
            when {
                password.isEmpty() -> {
                    txtPassword.error = "password cannot be blank"
                    valid = false
                }
                else -> {
                    txtPassword.error = null
                }
            }
        }
        return valid
    }

    private fun loadingMode(yes: Boolean) {
        if (yes) {
            loading.visibility = View.VISIBLE
            switchActive.visibility = View.INVISIBLE
            layoutID.visibility = View.INVISIBLE
            layoutName.visibility = View.INVISIBLE
            layoutPassword.visibility = View.INVISIBLE
            lblLevel.visibility = View.INVISIBLE
            spinnerLevel.visibility = View.INVISIBLE
        } else {
            loading.visibility = View.GONE
            switchActive.visibility = View.VISIBLE
            layoutID.visibility = View.VISIBLE
            layoutName.visibility = View.VISIBLE
            layoutPassword.visibility = View.VISIBLE
            lblLevel.visibility = View.VISIBLE
            spinnerLevel.visibility = View.VISIBLE
        }
    }

    private fun insertUser() {
        loadingMode(true)
        val level: String = spinnerLevel.selectedItem.toString()
        val active = if (switchActive.isChecked) 1 else 0
        val user = User(txtID.text.toString(), txtName.text.toString(), txtPassword.text.toString(), level, active)
        viewModel.insertUser(user)
    }

    private fun updateUser() {
        loadingMode(true)
        val level: String = spinnerLevel.selectedItem.toString()
        val active = if (switchActive.isChecked) 1 else 0
        val user = User(txtID.text.toString(), txtName.text.toString(), txtPassword.text.toString(), level, active)
        viewModel.updateUser(user)
    }

    private fun observeViewModel() {
        viewModel.insertUser.observe(this, Observer {
            it.let {
                if (it.id > 0) {
                    if (it.id == 5) {
                        //re login
                    } else {
                        val msg = if (it.id == 1) it.message else it.message + "\n" + it.extra1
                        Snackbar
                            .make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK") {

                            }
                            .show()
                    }
                    viewModel.insertUser.value = Message(-1, "","","","")
                }
            }
        })

        viewModel.updateUser.observe(this, Observer {
            it.let {
                if (it.id > 0) {
                    if (it.id == 5) {
                        //re login
                    } else {
                        val msg = if (it.id == 1) it.message else it.message + "\n" + it.extra1
                        Snackbar
                            .make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK") {

                            }
                            .show()
                    }
                    viewModel.updateUser.value = Message(-1, "", "", "", "")
                }
            }
        })

        viewModel.error.observe(this, Observer { error: String ->
            error.let {
                if (it != "") {
                    Snackbar
                        .make(coordinatorLayout, it, Snackbar.LENGTH_LONG)
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