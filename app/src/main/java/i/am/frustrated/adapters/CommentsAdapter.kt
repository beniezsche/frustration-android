package i.am.frustrated.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import i.am.frustrated.R
import i.am.frustrated.models.Comment
import i.am.frustrated.models.Post
import i.am.frustrated.util.MoodUtil


class CommentsAdapter(var context: Context):  RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    var commentList: ArrayList<Comment>? = null
    var post: Post? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentViewHolder {

        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment,parent,false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

        val comment = commentList?.get(position)

        if (comment != null && post != null) {

            if(comment.venterId == post!!.venter_id)
                holder.tvCommentText.setTextColor(MoodUtil.getPrimaryColorFromMood(context,post!!.mood))
            else
                holder.tvCommentText.setTextColor(Color.WHITE)

            holder.tvCommentText.text = comment.commentText

        }
    }

    override fun getItemCount(): Int {
        return if (commentList != null) {
            commentList!!.size
        } else
            0
    }

    fun setParentPost(post: Post) {
        this.post = post
    }

    class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val tvCommentText: TextView = itemView.findViewById(R.id.tv_comment_text)
    }
}