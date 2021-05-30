package i.am.frustrated.adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import i.am.frustrated.R
import i.am.frustrated.activities.BaseActivity
import i.am.frustrated.activities.PostDetailActivity
import i.am.frustrated.models.Like
import i.am.frustrated.models.Post
import i.am.frustrated.models.ReportPost
import i.am.frustrated.util.MoodUtil
import i.am.frustrated.util.TimeUtil
import i.am.frustrated.viewmodels.PostsViewModel

class PostAdapter: RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
//    private val VIEW_TYPE_LOADING = 1


    var postList: ArrayList<Post> ? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var context: Context
    private lateinit var postViewModel: PostsViewModel
    private var venterId: String? = null
    private var token: String? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        if(viewType == VIEW_TYPE_ITEM) {
            return PostViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_post,
                    parent,
                    false
                )
            )
        }
        else{
            return PostViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_post,
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val post = postList?.get(position)

        if(post != null) {

            val primaryColor = MoodUtil.getPrimaryColorFromMood(context, post.mood)
            val secondaryColor = MoodUtil.getSecondaryColorFromMood(context, post.mood)

            holder.tvUserId.setTextColor(primaryColor)
            holder.tvTime.setTextColor(secondaryColor)
            holder.tvPostText.setTextColor(primaryColor)
            holder.tvLikes.setTextColor(secondaryColor)
            holder.ivMore.setColorFilter(secondaryColor)
            holder.tvComments.setTextColor(secondaryColor)
            holder.ivComments.setColorFilter(secondaryColor)

            holder.tvPostText.text = post.post_text

            val readMore = "...Read More" //context.getString(R.string.read_more)

            holder.tvPostText.post {

                if(holder.tvPostText.lineCount > 3){
                    val end = holder.tvPostText.layout.getLineEnd(2) //  getLineVisibleEnd(2)
                    val spannable = SpannableStringBuilder(
                        post.post_text?.substring(
                            0,
                            end - readMore.length
                        ) + readMore
                    )
                    spannable.setSpan(
                        ForegroundColorSpan(Color.WHITE),
                        end - readMore.length, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    holder.tvPostText.text = spannable
                }

            }

            holder.tvTime.text = String.format(
                context.getString(R.string.time_ago_posted), TimeUtil.timestampToHours(
                    post.created_at.toString()
                )
            )

            if (venterId != null && token != null) {

                if (venterId.equals(post.venter_id)){

                    //holder.ivMore.visibility = View.VISIBLE
                    //holder.tvUserId.visibility = View.VISIBLE
                    holder.ivMore.setOnClickListener {
                        //setDialogOptions(true, post)
                        buildPostOptionsDialog(true, holder, post, position)
                    }

                }
                else {
                    //holder.ivMore.visibility = View.INVISIBLE
                    //holder.tvUserId.visibility = View.INVISIBLE
                    holder.ivMore.setOnClickListener {
                        //setDialogOptions(false, post)
                        buildPostOptionsDialog(false, holder, post, position)
                    }
                }
            }

            if(post.is_by_admin!!) {
                holder.tvUserId.visibility = View.VISIBLE
                holder.tvUserId.text = context.getText(R.string.app_name)
            }
            else{
                holder.tvUserId.visibility = View.INVISIBLE
                holder.tvUserId.text = ""
            }

            holder.tvLikes.text = post.likes?.size!!.toString()

            val likeEmoji = MoodUtil.getLikeEmojiDrawableForMood(context, post.mood)!!
            val unlikeEmoji = MoodUtil.getUnlikeEmojiDrawableForMood(context, post.mood)!!


            if (checkLike(post.likes!!, venterId!!)) {                                        // 1.any{ Like -> Like.venter_id == venter_id }  2. post.likes!!.contains(venter_id)
                //Glide.with(context).load(MoodUtil.getLikeEmojiDrawableForMood(context,post.mood)).into(holder.ivLike)
                Glide.with(context).load(likeEmoji).into(holder.ivLike)

                holder.ivLike.setOnClickListener {
                    likePost(false, holder, post, position, likeEmoji, unlikeEmoji)
                }

            }
            else{
                //Glide.with(context).load(MoodUtil.getUnlikeEmojiDrawableForMood(context,post.mood)).into(holder.ivLike)
                Glide.with(context).load(unlikeEmoji).into(holder.ivLike)

                holder.ivLike.setOnClickListener {
                    likePost(true, holder, post, position, likeEmoji, unlikeEmoji)
                }
            }

            holder.tvComments.text = post.comment_count.toString()

            holder.itemView.setOnClickListener {
                val intent = Intent(context, PostDetailActivity::class.java)
                intent.putExtra("postId", post.post_id)
//                intent.putExtra("post_id", post.post_id)
//                intent.putExtra("venter_id", post.venter_id)
//                intent.putExtra("post_text", post.post_text)
                context.startActivity(intent)
            }
        }
    }

    private fun likePost(
        canLike: Boolean,
        holder: PostViewHolder,
        postLike: Post,
        position: Int,
        likeEmoji: Drawable,
        unlikeEmoji: Drawable
    ){

        val post = Post()
        post.post_id = postLike.post_id
        post.venter_id = venterId
        post.token = token

        if (canLike) {

            //Glide.with(context).load(MoodUtil.getLikeEmojiDrawableForMood(context,post.mood)).into(holder.ivLike)
            Glide.with(context).load(likeEmoji).into(holder.ivLike)
            holder.tvLikes.text = (holder.tvLikes.text.toString().toInt() + 1).toString()

            postViewModel.likePost(post).observe(context as BaseActivity, {
                if (it != null) {
                    //Toast.makeText(context,"Post deleted",Toast.LENGTH_SHORT).show()
//                            Glide.with(context).load(R.drawable.angry).into(holder.ivLike)
//                            holder.tvLikes.text = (holder.tvLikes.text.toString().toInt() + 1).toString()

                    postList?.set(position, it)
                    notifyItemChanged(position)

                    setEmojiForDislike(holder, postLike, position, likeEmoji, unlikeEmoji)

                } else {
                    Toast.makeText(
                        context,
                        "There was a problem liking the post please try again",
                        Toast.LENGTH_SHORT
                    ).show()

                    //Glide.with(context).load(MoodUtil.getUnlikeEmojiDrawableForMood(context,post.mood)).into(holder.ivLike)
                    Glide.with(context).load(unlikeEmoji).into(holder.ivLike)
                    holder.tvLikes.text = (holder.tvLikes.text.toString().toInt() - 1).toString()

                    setEmojiForLike(holder, postLike, position, likeEmoji, unlikeEmoji)

                    //holder.ivLike.isClickable = true
                }
            })

        }

        else {

            //Glide.with(context).load(MoodUtil.getUnlikeEmojiDrawableForMood(context,post.mood)).into(holder.ivLike)
            Glide.with(context).load(unlikeEmoji).into(holder.ivLike)
            holder.tvLikes.text = (holder.tvLikes.text.toString().toInt() - 1).toString()

            postViewModel.deleteLike(post).observe(context as BaseActivity, {
                if (it != null) {
                    //Toast.makeText(context,"Post deleted",Toast.LENGTH_SHORT).show()
//                            Glide.with(context).load(R.drawable.angry).into(holder.ivLike)
//                            holder.tvLikes.text = (holder.tvLikes.text.toString().toInt() + 1).toString()

                    postList?.set(position, it)
                    notifyItemChanged(position)

                    setEmojiForLike(holder, postLike, position, likeEmoji, unlikeEmoji)

                } else {
                    Toast.makeText(
                        context,
                        "There was a problem unliking the post please try again",
                        Toast.LENGTH_SHORT
                    ).show()

                    //Glide.with(context).load(MoodUtil.getLikeEmojiDrawableForMood(context,post.mood)).into(holder.ivLike)
                    Glide.with(context).load(likeEmoji).into(holder.ivLike)
                    holder.tvLikes.text = (holder.tvLikes.text.toString().toInt() + 1).toString()

                    setEmojiForDislike(holder, postLike, position, likeEmoji, unlikeEmoji)

                    //holder.ivLike.isClickable = true
                }
            })


        }

    }

    private fun setEmojiForLike(
        holder: PostViewHolder,
        post: Post,
        position: Int,
        likeEmoji: Drawable,
        unlikeEmoji: Drawable
    ) {

        holder.ivLike.setOnClickListener {
            likePost(true, holder, post, position, likeEmoji, unlikeEmoji)
        }

    }

    private fun setEmojiForDislike(
        holder: PostViewHolder,
        post: Post,
        position: Int,
        likeEmoji: Drawable,
        unlikeEmoji: Drawable
    ) {

        holder.ivLike.setOnClickListener {
            likePost(false, holder, post, position, likeEmoji, unlikeEmoji)
        }

    }

    private fun blockPost(holder: PostViewHolder, post: Post, position: Int) {

        val blockPost = Post()
        blockPost.post_id = post.post_id
        blockPost.venter_id = venterId
        blockPost.token = token

        postViewModel.blockPost(blockPost).observe(context as BaseActivity, {

            if (it != null) {

                postList?.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeRemoved(position, postList!!.size)
                Toast.makeText(context, "Post removed from your feed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "There was a problem removing this post from the feed. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun deletePost(holder: PostViewHolder, post: Post, position: Int) {

        val postDelete = Post()
        postDelete.post_id = post.post_id
        postDelete.venter_id = venterId
        postDelete.token = token

        postViewModel.deletePost(postDelete).observe(context as BaseActivity, {

            if (it != null) {

                postList?.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeRemoved(position, postList!!.size)
                Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "There was a problem deleting this post. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun reportPost(holder: PostViewHolder, post: Post, position: Int, reason: String) {

        val reportPost = ReportPost()
        reportPost.post_id = post.post_id
        reportPost.reason = reason
        reportPost.venter_id = venterId
        reportPost.token = token

        postViewModel.reportPost(reportPost).observe(context as BaseActivity, {

            if (it != null) {

                postList?.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeRemoved(position, postList!!.size)
                Toast.makeText(context, "Post reported!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "There was a problem reporting this post. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun allowComments(
        allowComments: Boolean,
        holder: PostViewHolder,
        post: Post,
        position: Int
    ) {

        val toggleCommentsPost = Post()
        toggleCommentsPost.venter_id = venterId
        toggleCommentsPost.post_id = post.post_id
        toggleCommentsPost.token = token
        toggleCommentsPost.is_comments_enabled = allowComments

        postViewModel.toggleComments(toggleCommentsPost).observe(context as BaseActivity, Observer {

            if (it != null) {

                postList?.set(position, it)
                notifyItemChanged(position)
            } else {
                Toast.makeText(
                    context,
                    "There was a problem with this action. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

    }

    private fun buildPostOptionsDialog(
        isMine: Boolean,
        holder: PostViewHolder,
        post: Post,
        position: Int
    ) {

        val postOptionsDialog = AlertDialog.Builder(context)
        //builderSingle.setTitle("Select One Name:-")
        postOptionsDialog

        val arrayAdapter = ArrayAdapter<String>(
            context,
            android.R.layout.select_dialog_item
        )

        if (isMine) {
            if (post.is_comments_enabled!!)
                arrayAdapter.add(context.getString(R.string.post_option_disable_comments))
            else
                arrayAdapter.add(context.getString(R.string.post_option_enable_comments))

            arrayAdapter.add(context.getString(R.string.post_option_delete))

        }
        else {
            arrayAdapter.add(context.getString(R.string.post_option_report))

            arrayAdapter.add(context.getString(R.string.post_option_block))
        }


        postOptionsDialog.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss()
        }

        postOptionsDialog.setAdapter(arrayAdapter) { _, which ->

            when(arrayAdapter.getItem(which)) {

                context.getString(R.string.post_option_block) -> {
                    areYouSureDialog(R.string.post_option_block, holder, post, position)
                }

                context.getString(R.string.post_option_report) -> {
                    areYouSureDialog(R.string.post_option_report, holder, post, position)
                }

                context.getString(R.string.post_option_delete) -> {
                    areYouSureDialog(R.string.post_option_delete, holder, post, position)
                }

                context.getString(R.string.post_option_enable_comments) -> {
                    areYouSureDialog(R.string.post_option_enable_comments, holder, post, position)
                }

                context.getString(R.string.post_option_disable_comments) -> {
                    areYouSureDialog(R.string.post_option_disable_comments, holder, post, position)
                }

            }
        }

        postOptionsDialog.show()
    }

    private fun buildReportOptionsDialog(holder: PostViewHolder, post: Post, position: Int) {

        val postOptionsDialog = AlertDialog.Builder(context)
        //builderSingle.setTitle("Select One Name:-")
        postOptionsDialog.setTitle("Why are you reporting this?")

        val arrayAdapter = ArrayAdapter<String>(
            context,
            android.R.layout.select_dialog_item
        )

        val optionOne = context.getString(R.string.post_report_option_one)
        val optionTwo = context.getString(R.string.post_report_option_two)
        val optionThree = context.getString(R.string.post_report_option_three)
        val optionFour = context.getString(R.string.post_report_option_four)

        arrayAdapter.add(optionOne)
        arrayAdapter.add(optionTwo)
        arrayAdapter.add(optionThree)
        arrayAdapter.add(optionFour)

        postOptionsDialog.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss()
        }

        postOptionsDialog.setAdapter(arrayAdapter) { _, which ->

            when(arrayAdapter.getItem(which)) {

                optionOne -> {
                    reportPost(holder, post, position, optionOne)
                }

                optionTwo -> {
                    reportPost(holder, post, position, optionTwo)
                }

                optionThree -> {
                    reportPost(holder, post, position, optionThree)
                }

                optionFour -> {
                    reportPost(holder, post, position, optionFour)
                }

            }
        }

        postOptionsDialog.show()
    }

    private fun areYouSureDialog(action: Int, holder: PostViewHolder, post: Post, position: Int) {

        val confirmationDialog = AlertDialog.Builder(context)

        confirmationDialog.setNegativeButton("No") { dialog, i: Int ->
            dialog.dismiss()
        }

        when(action) {

            R.string.post_option_delete -> {

                confirmationDialog.setMessage(
                    String.format(
                        context.getString(R.string.post_action_confirmation),
                        "delete"
                    )
                )
                confirmationDialog.setPositiveButton("Yes") { dialog, which ->
                    deletePost(holder, post, position)
                }

            }

            R.string.post_option_block -> {

                confirmationDialog.setMessage(
                    String.format(
                        context.getString(R.string.post_action_confirmation),
                        "block"
                    )
                )
                confirmationDialog.setPositiveButton("Yes") { dialog, which ->
                    blockPost(holder, post, position)
                }

            }

            R.string.post_option_report -> {

                confirmationDialog.setMessage(
                    String.format(
                        context.getString(R.string.post_action_confirmation),
                        "report"
                    )
                )
                confirmationDialog.setPositiveButton("Yes") { dialog, which ->
                    buildReportOptionsDialog(holder, post, position)
                    //reportPost(holder, post, position)
                }

            }

            R.string.post_option_enable_comments -> {

                confirmationDialog.setMessage(
                    String.format(
                        context.getString(R.string.post_action_toggle_comment_confirmation),
                        "enable comments"
                    )
                )
                confirmationDialog.setPositiveButton("Yes") { dialog, which ->
                    allowComments(true, holder, post, position)
                }

            }

            R.string.post_option_disable_comments -> {

                confirmationDialog.setMessage(
                    String.format(
                        context.getString(R.string.post_action_toggle_comment_confirmation),
                        "disable comments"
                    )
                )

                confirmationDialog.setPositiveButton("Yes") { dialog, which ->
                    allowComments(false, holder, post, position)
                }
            }
        }

        confirmationDialog.show()
    }

    private fun checkLike(list: ArrayList<Like>, venter_id: String): Boolean {

        for (l in list) {
            if (l.venter_id.equals(venter_id)){
                return true
            }
        }

        return false
    }

    fun addPosts(list: ArrayList<Post>) {

        postList?.addAll(list)
        notifyItemRangeInserted(postList!!.size, list.size)
        //notifyItemRangeInserted(pos, list.size)

    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return if(postList == null){
            0
        } else{
            postList?.size!!
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        context = recyclerView.context
        sharedPreferences =  context.getSharedPreferences(
            context.getString(R.string.shared_pref_name),
            Context.MODE_PRIVATE
        )

        venterId = sharedPreferences.getString(context.getString(R.string.venter_id), null)
        token = sharedPreferences.getString(context.getString(R.string.token), null)

        postViewModel = ViewModelProviders.of(context as BaseActivity).get(PostsViewModel::class.java)
    }

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        //val cvContainerPost: CardView = itemView.findViewById(R.id.cv_container_post)
        //val clContainerPost: ConstraintLayout = itemView.findViewById(R.id.cl_container_post)
        val tvPostText: TextView = itemView.findViewById(R.id.tv_post_text)
        val tvLikes: TextView = itemView.findViewById(R.id.tv_likes)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val tvComments: TextView = itemView.findViewById(R.id.tv_comments)
        val ivComments: ImageView = itemView.findViewById(R.id.iv_comment)
        //val tvReadMore: TextView = itemView.findViewById(R.id.tv_read_more)
        val ivLike: ImageView = itemView.findViewById(R.id.iv_like)
        val tvUserId: TextView = itemView.findViewById(R.id.tv_user_id)
        val ivMore:ImageView = itemView.findViewById(R.id.iv_more)
    }

    class LoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}