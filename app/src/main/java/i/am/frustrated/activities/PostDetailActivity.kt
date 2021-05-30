@file:Suppress("DEPRECATION")

package i.am.frustrated.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import i.am.frustrated.R
import i.am.frustrated.adapters.CommentsAdapter
import i.am.frustrated.models.Like
import i.am.frustrated.models.Post
import i.am.frustrated.models.PostComment
import i.am.frustrated.models.ReportPost
import i.am.frustrated.util.MoodUtil
import i.am.frustrated.util.TimeUtil
import i.am.frustrated.viewmodels.PostsViewModel
import kotlinx.android.synthetic.main.activity_post_detail.*


class PostDetailActivity : AppCompatActivity() {

    private lateinit var postViewModel: PostsViewModel
    private lateinit var venterId: String
    private lateinit var token: String
    //private var post: Post? = null
    private lateinit var postId: String

    private var commentsAdapter = CommentsAdapter(this)

    private lateinit var cvContainerPost: CardView
    private lateinit var tvPostText: TextView
    private lateinit var tvLikes: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvComments: TextView
    private lateinit var ivLike: ImageView
    private lateinit var ivComments: ImageView
    private lateinit var tvUserId: TextView
    private lateinit var ivMore:ImageView

    private lateinit var likeEmoji: Drawable
    private lateinit var unlikeEmoji: Drawable


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val sharedPref = getSharedPreferences(
            getString(R.string.shared_pref_name),
            Context.MODE_PRIVATE
        )

        venterId = sharedPref.getString(getString(R.string.venter_id), null)!!
        token = sharedPref.getString(getString(R.string.token), null)!!

        postViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)

        //post = intent.getParcelableExtra("post")

        postId = intent.getStringExtra("postId")!!


        //val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        val linearLayoutManager = LinearLayoutManager(this)
        rv_comments.layoutManager = linearLayoutManager //staggeredGridLayoutManager

        rv_comments.adapter = commentsAdapter

        val activityName: TextView = header.findViewById(R.id.tv_activity_name)
        val ivBack: ImageView = header.findViewById(R.id.iv_back)

        activityName.text = "Read Post" //"Post by $venter_id"
        ivBack.setOnClickListener {
            finish()
        }


        cvContainerPost = post_card as CardView
        tvPostText = post_card.findViewById(R.id.tv_post_text)
        tvLikes = post_card.findViewById(R.id.tv_likes)
        tvTime = post_card.findViewById(R.id.tv_time)
        tvComments = post_card.findViewById(R.id.tv_comments)
        ivLike= post_card.findViewById(R.id.iv_like)
        ivComments = post_card.findViewById(R.id.iv_comment)
        tvUserId = post_card.findViewById(R.id.tv_user_id)
        ivMore = post_card.findViewById(R.id.iv_more)

        val post = Post()

        post.post_id = postId
        post.venter_id = venterId
        post.token = token
        


        postViewModel.getSinglePost(post).observe(this, { rPost ->

            if (rPost != null) {

                showPostAndCommentUI()

                setData(rPost)

                bt_post_comment.setOnClickListener {

                    val commentText = et_write_comment.text.trim().toString()

                    if(commentText.isNotBlank()){
                        it.isClickable = false
                        postComment(post.post_id!!, commentText)
                    }
                    else{
                        et_write_comment.setText("")
                    }
                }
            }

        })

        tvPostText.maxLines = Int.MAX_VALUE
        cvContainerPost.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

    }

    private fun setData(post: Post) {

        //             tvUserId.setTextColor(Color.parseColor(post.color))
//             tvPostText.setTextColor(Color.parseColor("#ff5252"))
//             tvLikes.setTextColor(Color.parseColor(post.color))
//             ivMore.setColorFilter(Color.parseColor(post.color))

        val primaryColor = MoodUtil.getPrimaryColorFromMood(this, post.mood)
        val secondaryColor = MoodUtil.getSecondaryColorFromMood(this, post.mood)

        tvUserId.setTextColor(primaryColor)
        tvTime.setTextColor(secondaryColor)
        tvPostText.setTextColor(primaryColor)
        tvLikes.setTextColor(secondaryColor)
        ivMore.setColorFilter(secondaryColor)
        tvComments.setTextColor(secondaryColor)
        ivComments.setColorFilter(secondaryColor)

        tvPostText.text = post.post_text

        tvTime.text = String.format(
            getString(R.string.time_ago_posted), TimeUtil.timestampToHours(
                post.created_at.toString()
            )
        )

        if (venterId != null && token != null) {

            if (venterId == post.venter_id){

                // ivMore.visibility = View.VISIBLE
                // tvUserId.visibility = View.VISIBLE
                 tvUserId.text = """"""
                 ivMore.setOnClickListener {
                    //setDialogOptions(true, post)
                    buildPostOptionsDialog(true, post)
                }

            }
            else {
                // ivMore.visibility = View.INVISIBLE
                // tvUserId.visibility = View.INVISIBLE
                 tvUserId.text = """"""
                 ivMore.setOnClickListener {
                    //setDialogOptions(false, post)
                    buildPostOptionsDialog(false, post)
                }
            }
        }

        tvLikes.text = post.likes?.size!!.toString()

        likeEmoji = MoodUtil.getLikeEmojiDrawableForMood(this, post.mood)!!
        unlikeEmoji = MoodUtil.getUnlikeEmojiDrawableForMood(this, post.mood)!!


        if (checkLike(post.likes!!, venterId)) {                                        // 1.any{ Like -> Like.venter_id == venter_id }  2. post.likes!!.contains(venter_id)
            Glide.with(this).load(likeEmoji).into(ivLike)

             ivLike.setOnClickListener {
                likePost(false, post)
            }

        }
        else{
            Glide.with(this).load(unlikeEmoji).into(ivLike)

             ivLike.setOnClickListener {
                likePost(true, post)
            }
        }

        tvComments.text = post.comments?.size!!.toString()

        setComments(post)

    }

    private fun likePost(canLike: Boolean, postLike: Post){

        val post = Post()
        post.post_id = postLike.post_id
        post.venter_id = venterId
        post.token = token

        if (canLike) {

            Glide.with(this).load(likeEmoji).into(ivLike)
            tvLikes.text = ( tvLikes.text.toString().toInt() + 1).toString()

            postViewModel.likePost(post).observe(this, {
                if (it != null) {
                    //Toast.makeText(context,"Post deleted",Toast.LENGTH_SHORT).show()
//                            Glide.with(context).load(R.drawable.angry).into( ivLike)
//                             tvLikes.text = ( tvLikes.text.toString().toInt() + 1).toString()

//                    postList?.set(position, it)
//                    notifyItemChanged(position)

                    setData(it)

                    setEmojiForDislike(postLike)

                } else {
                    Toast.makeText(
                        this,
                        "There was a problem liking the post please try again",
                        Toast.LENGTH_SHORT
                    ).show()

                    Glide.with(this).load(unlikeEmoji).into(ivLike)
                    tvLikes.text = (tvLikes.text.toString().toInt() - 1).toString()

                    setEmojiForLike(postLike)

                    // ivLike.isClickable = true
                }
            })

        }

        else {

            Glide.with(this).load(unlikeEmoji).into(ivLike)
             tvLikes.text = ( tvLikes.text.toString().toInt() - 1).toString()

            postViewModel.deleteLike(post).observe(this, {
                if (it != null) {
                    //Toast.makeText(context,"Post deleted",Toast.LENGTH_SHORT).show()
//                            Glide.with(context).load(R.drawable.angry).into( ivLike)
//                             tvLikes.text = ( tvLikes.text.toString().toInt() + 1).toString()

//                    postList?.set(position, it)
//                    notifyItemChanged(position)

                    setData(it)

                    setEmojiForLike(postLike)

                } else {
                    Toast.makeText(
                        this,
                        "There was a problem unliking the post please try again",
                        Toast.LENGTH_SHORT
                    ).show()

                    Glide.with(this).load(likeEmoji).into(ivLike)
                    tvLikes.text = (tvLikes.text.toString().toInt() + 1).toString()

                    setEmojiForDislike(postLike)

                    // ivLike.isClickable = true
                }
            })

        }

    }

    private fun setEmojiForLike(post: Post) {

        ivLike.setOnClickListener {
            likePost(true, post)
        }

    }

    private fun setEmojiForDislike(post: Post) {

        ivLike.setOnClickListener {
            likePost(false, post)
        }

    }

    private fun blockPost(post: Post) {

        val blockPost = Post()
        blockPost.post_id = post.post_id
        blockPost.venter_id = venterId
        blockPost.token = token

        postViewModel.blockPost(blockPost).observe(this, {

            if (it != null) {

                finish()
                Toast.makeText(this, "Post removed from your feed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "There was a problem removing this post from the feed. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun deletePost(post: Post) {

        val postDelete = Post()
        postDelete.post_id = post.post_id
        postDelete.venter_id = venterId
        postDelete.token = token

        postViewModel.deletePost(postDelete).observe(this, {

            if (it != null) {

                finish()
                Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "There was a problem deleting this post. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun reportPost(post: Post, reason: String) {

        val reportPost = ReportPost()
        reportPost.post_id = post.post_id
        reportPost.venter_id = venterId
        reportPost.token = token
        reportPost.reason = reason

        postViewModel.reportPost(reportPost).observe(this, {

            if (it != null) {

                finish()
                Toast.makeText(this, "Post reported!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "There was a problem reporting this post. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun postComment(postId: String, commentText: String) {

        val postComment = PostComment()
        postComment.post_id = postId
        postComment.comment_text = commentText
        postComment.venter_id = venterId
        postComment.token = token

        postViewModel.postComment(postComment).observe(this, {

            if (it != null) {
                setData(it)
                et_write_comment.text.clear()

                val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    this.currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }

            bt_post_comment.isClickable = true

        })

    }

    private fun allowComments(allowComments: Boolean, post: Post) {

        val toggleCommentsPost = Post()
        toggleCommentsPost.venter_id = venterId
        toggleCommentsPost.post_id = post.post_id
        toggleCommentsPost.token = token
        toggleCommentsPost.is_comments_enabled = allowComments

        postViewModel.toggleComments(toggleCommentsPost).observe(this, {

            if (it != null) {

//                if (it.is_comments_enabled!!){
//
//                }
//                else {
//
//                }

                setData(post)

            } else {
                Toast.makeText(
                    this,
                    "There was a problem with this action. Please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

    }

    private fun setComments(post: Post) {

        if (post.comments != null) {

            commentsAdapter.setParentPost(post)
            commentsAdapter.commentList = post.comments!!
            commentsAdapter.notifyDataSetChanged()

        }

        if (post.is_comments_enabled!!){
            cl_comment_layout.visibility = View.VISIBLE
        }
        else {
            cl_comment_layout.visibility = View.GONE
        }


    }

    private fun showPostAndCommentUI() {

        postProgressBar.visibility = View.GONE
        scrollView.visibility = View.VISIBLE
        cl_comment_layout.visibility = View.VISIBLE

    }
    private fun buildPostOptionsDialog(isMine: Boolean, post: Post) {

        val postOptionsDialog = AlertDialog.Builder(this)
        //builderSingle.setTitle("Select One Name:-")

        val arrayAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.select_dialog_item
        )

        if (isMine) {
            if (post.is_comments_enabled!!)
                arrayAdapter.add(getString(R.string.post_option_disable_comments))
            else
                arrayAdapter.add(getString(R.string.post_option_enable_comments))

            arrayAdapter.add(getString(R.string.post_option_delete))

        }
        else {
            arrayAdapter.add(getString(R.string.post_option_report))

            arrayAdapter.add(getString(R.string.post_option_block))
        }


        postOptionsDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss()
        }

        postOptionsDialog.setAdapter(arrayAdapter) { _, which ->

            when(arrayAdapter.getItem(which)) {

                getString(R.string.post_option_block) -> {
                    areYouSureDialog(R.string.post_option_block, post)
                }

                getString(R.string.post_option_report) -> {
                    areYouSureDialog(R.string.post_option_report, post)
                }

                getString(R.string.post_option_delete) -> {
                    areYouSureDialog(R.string.post_option_delete, post)
                }

                getString(R.string.post_option_enable_comments) -> {
                    areYouSureDialog(R.string.post_option_enable_comments, post)
                }

                getString(R.string.post_option_disable_comments) -> {
                    areYouSureDialog(R.string.post_option_disable_comments, post)
                }

            }
        }

        postOptionsDialog.show()
    }

    private fun buildReportOptionsDialog(post: Post) {

        val postOptionsDialog = AlertDialog.Builder(this)
        //builderSingle.setTitle("Select One Name:-")
        postOptionsDialog.setTitle("Why are you reporting this?")

        val arrayAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.select_dialog_item
        )

        val optionOne = getString(R.string.post_report_option_one)
        val optionTwo = getString(R.string.post_report_option_two)
        val optionThree = getString(R.string.post_report_option_three)
        val optionFour = getString(R.string.post_report_option_four)

        arrayAdapter.add(optionOne)
        arrayAdapter.add(optionTwo)
        arrayAdapter.add(optionThree)
        arrayAdapter.add(optionFour)

        postOptionsDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss()
        }

        postOptionsDialog.setAdapter(arrayAdapter) { _, which ->

            when(arrayAdapter.getItem(which)) {

                optionOne -> {
                    reportPost(post, optionOne)
                }

                optionTwo -> {
                    reportPost(post, optionTwo)
                }

                optionThree -> {
                    reportPost(post, optionThree)
                }

                optionFour -> {
                    reportPost(post, optionFour)
                }

            }
        }

        postOptionsDialog.show()
    }

    private fun areYouSureDialog(action: Int, post: Post) {

        val confirmationDialog = AlertDialog.Builder(this)

        confirmationDialog.setNegativeButton("No") { dialog, _: Int ->
            dialog.dismiss()
        }

        when(action) {

            R.string.post_option_delete -> {

                confirmationDialog.setMessage(
                    String.format(
                        getString(R.string.post_action_confirmation),
                        "delete"
                    )
                )
                confirmationDialog.setPositiveButton("Yes") { _, _ ->
                    deletePost(post)
                }

            }

            R.string.post_option_block -> {

                confirmationDialog.setMessage(
                    String.format(
                        getString(R.string.post_action_confirmation),
                        "block"
                    )
                )
                confirmationDialog.setPositiveButton("Yes") { _, _ ->
                    blockPost(post)
                }

            }

            R.string.post_option_report -> {

                confirmationDialog.setMessage(
                    String.format(
                        getString(R.string.post_action_confirmation),
                        "report"
                    )
                )
                confirmationDialog.setPositiveButton("Yes") { _, _ ->
                    buildReportOptionsDialog(post)
                    //reportPost(post)
                }

            }

            R.string.post_option_enable_comments -> {

                confirmationDialog.setMessage(
                    String.format(
                        getString(R.string.post_action_toggle_comment_confirmation),
                        "enable comments"
                    )
                )
                confirmationDialog.setPositiveButton("Yes") { _, _ ->
                    allowComments(true, post)
                }

            }

            R.string.post_option_disable_comments -> {

                confirmationDialog.setMessage(
                    String.format(
                        getString(R.string.post_action_toggle_comment_confirmation),
                        "disable comments"
                    )
                )

                confirmationDialog.setPositiveButton("Yes") { _, _ ->
                    allowComments(false, post)
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
}