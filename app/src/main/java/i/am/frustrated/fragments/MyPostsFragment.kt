package i.am.frustrated.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import i.am.frustrated.R
import i.am.frustrated.adapters.PostAdapter
import i.am.frustrated.models.Post
import i.am.frustrated.models.Venter
import i.am.frustrated.viewmodels.PostsViewModel
import kotlinx.android.synthetic.main.fragment_my_posts.*
import kotlinx.android.synthetic.main.fragment_my_posts.no_posts


class MyPostsFragment : Fragment() {

    private lateinit var postAdapter: PostAdapter
    private lateinit var postViewModel: PostsViewModel
    private lateinit var venter_id: String
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postAdapter = PostAdapter()
        @Suppress("DEPRECATION")
        postViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)

        val sharedPref = context!!.getSharedPreferences(getString(R.string.shared_pref_name), Context.MODE_PRIVATE)
        venter_id = sharedPref.getString(getString(R.string.venter_id), null)!!
        token = sharedPref.getString(getString(R.string.token), null)!!


        init()

        swipe_refresh_my_feed.setOnRefreshListener {
            loadPostList()
        }
    }

    private fun init() {

        rv_my_posts.layoutManager = LinearLayoutManager(context)
        rv_my_posts.adapter = postAdapter

        rv_my_posts.visibility = View.GONE
        no_posts.visibility = View.VISIBLE

        loadPostList()

    }

    private fun loadPostList() {

        val venter = Venter()
        venter.venter_id = venter_id
        venter.token = token

        postViewModel.getMyPosts(venter).observe(viewLifecycleOwner, {

            if(!it.isNullOrEmpty()){

                rv_my_posts.visibility = View.VISIBLE
                no_posts.visibility = View.GONE

                postAdapter.postList = it as ArrayList<Post>
                postAdapter.notifyDataSetChanged()

                setPostSize(it.size)
            }
            else {

                rv_my_posts.visibility = View.GONE
                no_posts.visibility = View.VISIBLE

                setPostSize(0)
            }

            swipe_refresh_my_feed.isRefreshing = false

        })

    }

    private fun setPostSize(size: Int) {

        tv_post_no.text = String.format(getString(R.string.no_of_posts),size)
    }


}