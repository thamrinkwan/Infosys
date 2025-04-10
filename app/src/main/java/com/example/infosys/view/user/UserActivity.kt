package com.example.infosys.view.user

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.infosys.InfosysApplication.Companion.USER
import com.example.infosys.R
import com.example.infosys.RecyclerViewSwipeDecorator
import com.example.infosys.model.Message
import com.example.infosys.model.User
import com.example.infosys.viewmodel.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class UserActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel
    private val usersListAdapter =
        UserAdapter(arrayListOf())
    private var selectedUser: User? = null
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        coordinatorLayout = findViewById(R.id.coordinatorLayout)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.getUsers()
        observeViewModel()

        val usersList = findViewById<RecyclerView>(R.id.usersList)

        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                //do something
            }
        }

        findViewById<FloatingActionButton>(R.id.btnAdd).setOnClickListener {
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra("Mode", true)
            launcher.launch(intent)
        }

        usersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersListAdapter

            val callback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    try {
                        val position = viewHolder.adapterPosition
                        (usersList!!.adapter as UserAdapter?)!!.notifyItemChanged(position)
                        if (direction == ItemTouchHelper.LEFT) {
                            //remove user
                            selectedUser = usersListAdapter.usersList[position]

                            if (selectedUser!!.userID != USER.userID) {
                                val builder = AlertDialog.Builder(context)
                                builder.setTitle("Remove User")
                                builder.setMessage(
                                    "User: %s\nName: %s\n\nAre you sure?".format(
                                        selectedUser!!.userID,
                                        selectedUser!!.userName
                                    )
                                )

                                builder.setPositiveButton(android.R.string.yes) { _, _ ->
                                    deleteUser()
                                }

                                builder.setNegativeButton(android.R.string.no) { _, _ ->
                                    //do nothing
                                }
                                builder.show()
                            }
                        } else if (direction == ItemTouchHelper.RIGHT) {
                            //edit user
                            selectedUser = usersListAdapter.usersList[position]
                            editUser()
                        }
                    } catch (e: Exception) {
                        //snackbar(e.message)
                    }
                }

                // You must use @RecyclerViewSwipeDecorator inside the onChildDraw method
                override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                    RecyclerViewSwipeDecorator.Builder(
                        context!!,
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(context!!, R.color.colorDelete))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(context!!, R.color.colorEdit))
                        .addSwipeRightActionIcon(R.drawable.ic_edit)
                        .create()
                        .decorate()
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(usersList)
        }
        val refreshUserLayout = findViewById<SwipeRefreshLayout>(R.id.refreshUserLayout)
        refreshUserLayout.setOnRefreshListener {
            viewModel.getUsers()
            refreshUserLayout.isRefreshing = false
        }
    }

    fun editUser() {
        val intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra("Mode", false)
        intent.putExtra("ID", selectedUser!!.userID)
        intent.putExtra("Name", selectedUser!!.userName)
        intent.putExtra("Level", selectedUser!!.userLevel)
        intent.putExtra("Active", selectedUser!!.userActive)
        launcher.launch(intent)
    }

    fun deleteUser() {
        if (selectedUser != null) {
            viewModel.deleteUser(selectedUser!!)
        }
    }

    fun loadingMode(yes: Boolean) {
        if (yes) {
            findViewById<ProgressBar>(R.id.userLoading).visibility = View.VISIBLE
            findViewById<RecyclerView>(R.id.usersList).visibility = View.GONE
        } else {
            findViewById<ProgressBar>(R.id.userLoading).visibility = View.GONE
            findViewById<RecyclerView>(R.id.usersList).visibility = View.VISIBLE
        }
    }

    private fun observeViewModel() {
        viewModel.users.observe(this, Observer { users: List<User> ->
            users.let {
                usersListAdapter.updateUserList(users)
            }
        })
        viewModel.deleteUser.observe(this, Observer {
            it.let {
                if (it.id > 0) {
                    if (it.id == 5) {
                        //re login
                    } else {
                        val msg = if (it.id == 1) it.message else it.message + "\n" + it.extra1
                        Snackbar
                            .make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT)
                            .show()
                        viewModel.getUsers()
                    }
                    viewModel.deleteUser.value = Message(-1, "","","","")
                }
            }
        })


        viewModel.error.observe(this, Observer { error: String ->
            error.let {
                if (it != "") {
                    val coordinatorLayout: CoordinatorLayout = findViewById(R.id.coordinatorLayout)

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