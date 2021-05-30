@file:Suppress("DEPRECATION")

package i.am.frustrated.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.warkiz.tickseekbar.OnSeekChangeListener
import com.warkiz.tickseekbar.SeekParams
import com.warkiz.tickseekbar.TickSeekBar

import i.am.frustrated.R
import i.am.frustrated.models.Post
import i.am.frustrated.util.TimeUtil
import i.am.frustrated.viewmodels.PostsViewModel
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_create_post.ll_mood_ennui
import kotlinx.android.synthetic.main.activity_create_post.ll_mood_happy
import kotlinx.android.synthetic.main.activity_create_post.ll_mood_sad
import kotlinx.android.synthetic.main.header_write.*


class CreatePostActivity : BaseActivity() {

    private lateinit var postsViewModel: PostsViewModel
    private var millis = TimeUtil.hoursToMillis(24)
    private var mood = 1

    private val nsfwWordsList = arrayOf("asshole","shit","piss","sex","pussy","fuck","dick","cock","slut","skank","whore","handjob","hand job","blow job","blowjob","boobs","masturbate","jerk off","cumming", "randi", "bhosdi","chut", "gaand")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_post)

        val activityName: TextView = header.findViewById(R.id.tv_activity_name)
        val ivBack: ImageView = header.findViewById(R.id.iv_back)
        val btPost: TextView = header.findViewById(R.id.bt_post)
        activityName.text = "Write"
        ivBack.setOnClickListener {
            finish()
        }

        postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)

        val sharedPref = getSharedPreferences(
            getString(R.string.shared_pref_name),
            Context.MODE_PRIVATE
        )
        val venterId = sharedPref.getString(getString(R.string.venter_id), null)
        val token = sharedPref.getString(getString(R.string.token), null)

        setInitialMood()

        bubbleSeekBar.onSeekChangeListener = object: OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {

                if(seekParams!!.fromUser){
                    Log.d("Progress",seekParams.progress.toString())
                    millis = TimeUtil.hoursToMillis(seekParams.progress)

                }

            }

            override fun onStartTrackingTouch(seekBar: TickSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: TickSeekBar?) {

            }

        }

        et_write_post.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (!p0.isNullOrBlank()) {

                    val text = p0.toString()
                    val count = text.length

                    word_count.text = "($count/1000)"

//                    //count words
//                    val text = p0.toString().trim()
//                    val count = text.split("\\s+".toRegex()).size
//                    word_count.text = "($count/1000)"
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        btPost.setOnClickListener {

            if(!et_write_post.text.isNullOrEmpty()){

                btPost.isClickable = false
                showPostProgress(true)

                val post = Post()
                post.post_text= et_write_post.text.trim().toString()
                post.time_limit = millis.toString()
                post.venter_id = venterId
                post.token = token
                post.mood = mood //resources.getInteger(R.integer.mood_filter_happy)
                post.is_comments_enabled = switch_allow_comments.isChecked
                post.is_nsfw = isTextNsfw(et_write_post.text.toString()) //switch_is_post_nsfw.isChecked

                postsViewModel.postPost(post).observe(this, {
                    //Toast.makeText(this,it.link_url,Toast.LENGTH_SHORT).show()

                    if (it != null) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this,"There was a problem in posting this, please try again",Toast.LENGTH_SHORT).show()
                        showPostProgress(false)
                        btPost.isClickable = true
                    }

                })
            }
        }




        setMoodListeners()

    }

    private fun setMoodListeners() {


        ll_mood_ennui.setOnClickListener {
            iv_mood_ennui.background = ContextCompat.getDrawable(this,R.drawable.border_selected_mood)
            tv_mood_ennui.setTextColor(resources.getColor(R.color.bg_purple))

            iv_mood_angry.background = null
            iv_mood_happy.background = null
            iv_mood_sad.background = null

            tv_mood_angry.setTextColor(resources.getColor(R.color.mood_raging_primary_color))
            tv_mood_happy.setTextColor(resources.getColor(R.color.mood_happy_primary_color))
            tv_mood_sad.setTextColor(resources.getColor(R.color.mood_sad_primary_color))
//
            mood = resources.getInteger(R.integer.mood_filter_ennui)
        }

        ll_mood_sad.setOnClickListener {
            iv_mood_sad.background = ContextCompat.getDrawable(this,R.drawable.border_selected_mood)
            tv_mood_sad.setTextColor(resources.getColor(R.color.bg_purple))

            iv_mood_angry.background = null
            iv_mood_happy.background = null
            iv_mood_ennui.background = null

            tv_mood_angry.setTextColor(resources.getColor(R.color.mood_raging_primary_color))
            tv_mood_happy.setTextColor(resources.getColor(R.color.mood_happy_primary_color))
            tv_mood_ennui.setTextColor(resources.getColor(R.color.mood_ennui_primary_color))

            mood = resources.getInteger(R.integer.mood_filter_sad)
        }

        ll_mood_happy.setOnClickListener {
            iv_mood_happy.background = ContextCompat.getDrawable(this,R.drawable.border_selected_mood)
            tv_mood_happy.setTextColor(resources.getColor(R.color.bg_purple))

            iv_mood_angry.background = null
            iv_mood_sad.background = null
            iv_mood_ennui.background = null

            tv_mood_angry.setTextColor(resources.getColor(R.color.mood_raging_primary_color))
            tv_mood_sad.setTextColor(resources.getColor(R.color.mood_sad_primary_color))
            tv_mood_ennui.setTextColor(resources.getColor(R.color.mood_ennui_primary_color))

            mood = resources.getInteger(R.integer.mood_filter_happy)
        }

        ll_mood_angry.setOnClickListener {
            iv_mood_angry.background = ContextCompat.getDrawable(this,R.drawable.border_selected_mood)
            tv_mood_angry.setTextColor(resources.getColor(R.color.bg_purple))

            iv_mood_ennui.background = null
            iv_mood_happy.background = null
            iv_mood_sad.background = null

            tv_mood_happy.setTextColor(resources.getColor(R.color.mood_happy_primary_color))
            tv_mood_sad.setTextColor(resources.getColor(R.color.mood_sad_primary_color))
            tv_mood_ennui.setTextColor(resources.getColor(R.color.mood_ennui_primary_color))

            mood = resources.getInteger(R.integer.mood_filter_raging)
        }
    }

    private fun setInitialMood() {

        iv_mood_ennui.background = ContextCompat.getDrawable(this,R.drawable.border_selected_mood)
        tv_mood_ennui.setTextColor(resources.getColor(R.color.bg_purple))

        iv_mood_angry.background = null
        iv_mood_happy.background = null
        iv_mood_sad.background = null

        tv_mood_angry.setTextColor(resources.getColor(R.color.mood_raging_primary_color))
        tv_mood_happy.setTextColor(resources.getColor(R.color.mood_happy_primary_color))
        tv_mood_sad.setTextColor(resources.getColor(R.color.mood_sad_primary_color))
//
        mood = resources.getInteger(R.integer.mood_filter_ennui)
        
    }


    private fun showPostProgress(shouldShow: Boolean){

        if(shouldShow) {
            bt_post.visibility = View.GONE
            bt_post_loading.visibility = View.VISIBLE
        }
        else {
            bt_post_loading.visibility = View.GONE
            bt_post.visibility = View.VISIBLE
        }
    }

    private fun isTextNsfw(text: String) : Boolean {

        for (word in nsfwWordsList) {
            if (text.contains(word,ignoreCase = true))
                return true
        }

        return false
    }

}