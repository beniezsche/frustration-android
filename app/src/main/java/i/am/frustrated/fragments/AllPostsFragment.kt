@file:Suppress("DEPRECATION")

package i.am.frustrated.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import i.am.frustrated.R
import i.am.frustrated.activities.SplashActivity
import i.am.frustrated.adapters.PostAdapter
import i.am.frustrated.bottomsheet.MoodSelectorBottomSheet
import i.am.frustrated.models.Page
import i.am.frustrated.models.Post
import i.am.frustrated.models.Venter
import i.am.frustrated.viewmodels.PostsViewModel
import i.am.frustrated.viewmodels.VenterViewModel
import kotlinx.android.synthetic.main.activity_on_boarding.*
import kotlinx.android.synthetic.main.fragment_all_posts.*
import kotlin.collections.ArrayList

class AllPostsFragment: Fragment(), MoodSelectorBottomSheet.MoodSelectorBottomSheetListener {

    private lateinit var postAdapter: PostAdapter
    private lateinit var postViewModel: PostsViewModel
    private lateinit var venterViewModel: VenterViewModel
    private var initialPageNo = 1
    private var mood = 0
    private lateinit var venterId: String
    private lateinit var token: String
    private var isNsfwAllowed: Boolean = false
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_all_posts, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postAdapter = PostAdapter()

        postViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        venterViewModel = ViewModelProviders.of(this).get(VenterViewModel::class.java)

        sharedPref = context?.getSharedPreferences(context?.getString(R.string.shared_pref_name), Context.MODE_PRIVATE)!!

        venterId = sharedPref.getString(context?.getString(R.string.venter_id),null)!!
        token = sharedPref.getString(context?.getString(R.string.token),null)!!
        //mood = sharedPref?.getInt(getString(R.string.mood_filter),0)
        isNsfwAllowed = sharedPref.getBoolean(getString(R.string.is_nsfw_allowed),false)

        init()

        if(!sharedPref.getBoolean(getString(R.string.vent_cred_upload), false))
            sendVenterCredentials()

        //sendVenterCredentials()

        swipe_refresh_main_feed.setOnRefreshListener {
            //loadPostList(initialPageNo)
            loadInitialPostList()
        }

        ll_select_mood.setOnClickListener {

            val moodSelectorBottomSheet = MoodSelectorBottomSheet(this)
            moodSelectorBottomSheet.show(parentFragmentManager,moodSelectorBottomSheet.tag)

            //moodSelectorBottomSheet

        }

    }

    @SuppressLint("HardwareIds", "ApplySharedPref")
    private fun sendVenterCredentials() {

        val secureId = Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(SplashActivity.TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val fcmToken = task.result

                val venter = Venter()
                venter.venter_id = venterId
                venter.token = token
                venter.android_id = secureId
                venter.fcm_token = fcmToken

                venterViewModel.uploadVenterCredentials(venter).observe(viewLifecycleOwner, {

                    if (it != null) {
                        val editor = sharedPref.edit()
                        editor.putBoolean(getString(R.string.vent_cred_upload),true)
                        editor.commit()
                    }
                })
            })
    }


    private fun init() {

        val layoutManager = LinearLayoutManager(context)

        rv_all_posts.layoutManager = layoutManager
        rv_all_posts.adapter = postAdapter

        rv_all_posts.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) {

                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()


                    if ( (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        Log.d("...", "Last Item Wow !")
                        loadPostList(++initialPageNo)

                    }

                }

            }
        })

        loadInitialPostList()

    }

    private fun loadInitialPostList(){

        val page = Page()
        page.venter_id = venterId
        page.token = token
        page.nsfw_allowed = isNsfwAllowed
        page.mood = mood
        page.page = 1


        postViewModel.getAllPosts(page).observe(viewLifecycleOwner, {

            if(!it.isNullOrEmpty()){

                rv_all_posts.visibility = View.VISIBLE
                no_posts.visibility = View.GONE
                ll_loading_posts.visibility = View.GONE

                postAdapter.postList = null
                postAdapter.postList = it as ArrayList<Post>
                postAdapter.notifyDataSetChanged()

                initialPageNo = 1

            }

            else {
                ll_loading_posts.visibility = View.GONE
                rv_all_posts.visibility = View.GONE
                no_posts.visibility = View.VISIBLE
            }

            swipe_refresh_main_feed.isRefreshing = false

        })

    }

    private fun loadPostList(pageNo: Int) {

        val page = Page()
        page.venter_id = venterId
        page.token = token
        page.nsfw_allowed = isNsfwAllowed
        page.mood = mood
        page.page = pageNo

        postViewModel.getAllPosts(page).observe(viewLifecycleOwner, {

            if(!it.isNullOrEmpty()){

                postAdapter.addPosts(it as ArrayList<Post>)

            }

//            else {
//
//                //no more posts to show
//
//
//            }

            swipe_refresh_main_feed.isRefreshing = false

        })

    }

    override fun onClick(mood: Int) {

        when(mood) {

            resources.getInteger(R.integer.mood_filter_all) -> {
                Glide.with(this).load(R.drawable.background_tab_layout_selected).into(iv_selected_mood)
            }

            resources.getInteger(R.integer.mood_filter_ennui) -> {
                Glide.with(this).load(R.drawable.mood_ennui).into(iv_selected_mood)
            }

            resources.getInteger(R.integer.mood_filter_sad) -> {
                Glide.with(this).load(R.drawable.mood_sad).into(iv_selected_mood)
            }

            resources.getInteger(R.integer.mood_filter_happy) -> {
                Glide.with(this).load(R.drawable.mood_happy).into(iv_selected_mood)
            }

            resources.getInteger(R.integer.mood_filter_raging) -> {
                Glide.with(this).load(R.drawable.mood_angry).into(iv_selected_mood)
            }


        }

        this.mood = mood
        loadInitialPostList()
    }
}