package com.example.infosys.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.infosys.model.DeleteUserService
import com.example.infosys.model.InsertUserService
import com.example.infosys.model.LoginService
import com.example.infosys.model.Message
import com.example.infosys.model.UpdateUserService
import com.example.infosys.model.User
import com.example.infosys.model.UserService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class UserViewModel(application: Application): BaseViewModel(application) {
    private val disposable = CompositeDisposable()
    private val loginService = LoginService()
    private val userService = UserService()
    private val insertUserService = InsertUserService()
    private val updateUserService = UpdateUserService()
    private val deleteUserService = DeleteUserService()

    val login = MutableLiveData<User>()
    val users = MutableLiveData<List<User>>()
    val insertUser = MutableLiveData<Message>()
    val updateUser = MutableLiveData<Message>()
    val deleteUser = MutableLiveData<Message>()


    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun login(u: User) {
        loading.value = true
        disposable.add(
            loginService.login(u.userID, u.userPassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<User>() {
                    override fun onSuccess(u: User) {
                        loading.value = false
                        loginRetrieved(u)
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        error.value = String.format("Error in Login: %s", e.toString())
                    }
                })
        )
    }

    fun getUsers() {
        loading.value = true
        error.value = ""
        disposable.add(
            userService.getUsers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<User>>() {
                    override fun onSuccess(list: List<User>) {
                        usersRetrieved(list)
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        error.value = String.format("Error in Load User: %s", e.toString())
                        loading.value = false
                    }
                })
        )
    }

    fun insertUser(u: User) {
        loading.value = true
        error.value = ""
        disposable.add(
            insertUserService.insertUser(u.userID, u.userName, u.userPassword, u.userLevel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Message>() {
                    override fun onSuccess(list: Message) {
                        insertUserRetrieved(list)
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        error.value = String.format("Error in Insert User: %s", e.toString())
                        loading.value = false
                    }
                })
        )
    }

    fun updateUser(u: User) {
        loading.value = true
        error.value = ""
        disposable.add(
            updateUserService.updateUser(u.userID, u.userName, u.userPassword,
                u.userLevel, u.userActive)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Message>() {
                    override fun onSuccess(list: Message) {
                        updateUserRetrieved(list)
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        error.value = String.format("Error in Update User: %s", e.toString())
                        loading.value = false
                    }
                })
        )
    }

    fun deleteUser(u: User) {
        loading.value = true
        error.value = ""
        disposable.add(
            deleteUserService.deleteUser(u.userID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Message>() {
                    override fun onSuccess(list: Message) {
                        deleteUserRetrieved(list)
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        error.value = String.format("Error in Delete User: %s", e.toString())
                        loading.value = false
                    }
                })
        )
    }

    private fun loginRetrieved(u: User) {
        login.value = u
        loading.value = false
        error.value = ""
    }

    private fun usersRetrieved(list: List<User>) {
        users.value = list
        loading.value = false
        error.value = ""
    }

    private fun insertUserRetrieved(list: Message) {
        insertUser.value = list
        loading.value = false
        error.value = ""
    }

    private fun updateUserRetrieved(list: Message) {
        updateUser.value = list
        loading.value = false
        error.value = ""
    }

    private fun deleteUserRetrieved(list: Message) {
        deleteUser.value = list
        loading.value = false
        error.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}