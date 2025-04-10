package com.example.infosys.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.example.infosys.InfosysApplication.Companion.URL
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

@Parcelize
data class User(@ColumnInfo(name = "UserID")
                @SerializedName("UserID")
                val userID: String,

                @ColumnInfo(name = "UserName")
                @SerializedName("UserName")
                val userName: String,

                @ColumnInfo(name = "UserPassword")
                @SerializedName("UserPassword")
                val userPassword: String,

                @ColumnInfo(name = "UserLevel")
                @SerializedName("UserLevel")
                val userLevel: String,

                @ColumnInfo(name = "UserActive")
                @SerializedName("UserActive")
                val userActive: Int
): Parcelable

interface LoginApi {
    @GET("login.php")
    fun login(@Query("UserID") userID: String,
              @Query("UserPassword") userPassword: String): Single<User>
}

interface UserApi {
    @GET("getuser.php")
    fun getUsers(): Single<List<User>>
}

interface InsertUserApi {
    @GET("insertuser.php")
    fun insertUser(@Query("UserID") userID: String,
                   @Query("UserName") userName: String,
                   @Query("UserPassword") userPassword: String,
                   @Query("UserLevel") userLevel: String): Single<Message>
}

interface UpdateUserApi {
    @GET("updateuser.php")
    fun updateUser(@Query("UserID") userID: String,
                   @Query("UserName") userName: String,
                   @Query("UserPassword") userPassword: String,
                   @Query("UserLevel") userLevel: String,
                   @Query("UserActive") userActive: Int): Single<Message>
}

interface DeleteUserApi {
    @GET("deleteuser.php")
    fun deleteUser(@Query("UserID") userID: String) : Single<Message>
}

class LoginService {
    private val api = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(LoginApi::class.java)

    fun login(@Query("UserID") userID: String, @Query("UserPassword") userPassword: String): Single<User> {
        return api.login(userID, userPassword)
    }
}

class UserService {
    private val api = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(UserApi::class.java)

    fun getUsers(): Single<List<User>> {
        return api.getUsers()
    }
}

class InsertUserService {
    private val api = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(InsertUserApi::class.java)

    fun insertUser(@Query("UserID") userID: String,
                   @Query("UserName") userName: String,
                   @Query("UserPassword") userPassword: String,
                   @Query("UserLevel") userLevel: String): Single<Message> {
        return api.insertUser(userID, userName, userPassword, userLevel)
    }
}

class UpdateUserService {
    private val api = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(UpdateUserApi::class.java)

    fun updateUser(@Query("UserID") userID: String,
                   @Query("UserName") userName: String,
                   @Query("UserPassword") userPassword: String,
                   @Query("UserLevel") userLevel: String,
                   @Query("UserActive") userActive: Int): Single<Message> {
        return api.updateUser(userID, userName, userPassword, userLevel, userActive)
    }
}

class DeleteUserService {
    private val api = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(DeleteUserApi::class.java)

    fun deleteUser(@Query("UserID") userID: String): Single<Message> {
        return api.deleteUser(userID)
    }
}
