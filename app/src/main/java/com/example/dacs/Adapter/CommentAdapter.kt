package com.example.dacs.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs.Data.Comment
import com.example.dacs.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

interface Delete {
    fun onDelete(commentId:String)
}

class CommentAdapter(private val comments: List<Comment>, private val currentUserId: String, private val click: Delete) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cmt = comments[position]
        holder.bind(cmt, currentUserId, click,)
    }

    override fun getItemCount(): Int = comments.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val comment: TextView = itemView.findViewById(R.id.comment)
        private val option: TextView = itemView.findViewById(R.id.option)
        @SuppressLint("InflateParams")
        fun bind(cmt: Comment, currentUserId: String, clicks: Delete?) {
            name.text = cmt.nameUser
            comment.text = cmt.comment
            //Check id de tuong tac
            if (cmt.iduser == currentUserId) {
                option.visibility = View.VISIBLE
            }
            else
            {
                option.visibility = View.GONE
            }

            option.setOnClickListener {
                val popupMenu = PopupMenu(this.itemView.context, option)
                popupMenu.menuInflater.inflate(R.menu.menu_option, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.edit -> {
                            val context =itemView.context
                            val dialogView = LayoutInflater.from(context).inflate(R.layout.edit_comment_dialog, null)

                            val editText = dialogView.findViewById<EditText>(R.id.edit_comment_edittext)
                            editText.setText(comment.text)
                            //Hien thi giao dien edit
                            AlertDialog.Builder(context)
                                .setView(dialogView)
                                .setPositiveButton(R.string.update) { _, _ ->
                                    val updatedText = editText.text.toString()
                                    comment.text = updatedText // noi dung cmt hien tai
                                    updateComment(cmt.id.toString(),
                                        cmt.idphim.toString(),
                                        cmt.iduser.toString(), cmt.nameUser.toString(), editText.text.toString())
                                }
                                .setNegativeButton(R.string.cancel, null)
                                .setTitle("Update Comment")
                                .show()
                            true
                        }
                        R.id.delete -> {
                            // Xử lý khi click vào menu Xóa
                            cmt.id?.let { it1 -> clicks?.onDelete(it1) }
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }

        private fun updateComment(id: String, idphim: String, iduser: String, nameUser: String, toString: String) {
            val db = FirebaseDatabase.getInstance().getReference("Comments").child(id)
            val cmt = Comment(id, idphim, iduser, nameUser, toString)
            db.setValue(cmt)

        }
    }
    }
