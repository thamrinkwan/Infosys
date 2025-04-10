package com.example.infosys.view.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.infosys.R
import com.example.infosys.model.User

class UserAdapter(val usersList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    fun updateUserList(newUsers: List<User>) {
        usersList.clear()
        usersList.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_user, parent, false)
        return UserViewHolder(
            view
        )
    }

    override fun getItemCount() = usersList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.lblID.text = usersList[position].userID
        holder.lblName.text = usersList[position].userName
        holder.lblLevel.text = usersList[position].userLevel
        if (usersList[position].userActive == 1) {
            holder.imageView.setImageResource(R.drawable.ic_user)
        } else {
            holder.imageView.setImageResource(R.drawable.ic_user_disable)
        }
    }

    class UserViewHolder(var view: View) : RecyclerView.ViewHolder(view){
        val lblID: TextView = itemView.findViewById(R.id.lblID)
        val lblName: TextView = itemView.findViewById(R.id.lblName)
        val lblLevel: TextView = itemView.findViewById(R.id.lblLevel)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}