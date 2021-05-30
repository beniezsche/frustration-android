package i.am.frustrated.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import i.am.frustrated.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val postFragmentAdapter = PostFragmentAdapter(this)
//        viewPager.adapter = postFragmentAdapter

        val ivSettings = header.findViewById<ImageView>(R.id.iv_settings)


//        TabLayoutMediator(tabLayout_posts, viewPager) { tab, position ->
//
//            if(position == 0){
//                tab.text = getString(R.string.feed_all_posts)
////                tab.icon?.setColorFilter(resources.getColor(R.color.bg_purple),PorterDuff.Mode.SRC_IN)  //setTint(resources.getColor(R.color.bg_purple))
//                //tabLayout_posts.getTabAt(1)?.icon?.set  setTint(resources.getColor(R.color.color_text_white))
//                //tab.setCustomView()
//            }
//            else if(position == 1){
//                tab.text = getString(R.string.feed_my_posts)
////                tab.icon?.setTint(resources.getColor(R.color.bg_purple))
////                tabLayout_posts.getTabAt(0)?.icon?.setTint(resources.getColor(R.color.color_text_white))
//            }
//
//        }.attach()

        fab_add_post.setOnClickListener {

            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
            //finish()
        }

        ivSettings.setOnClickListener {

            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        iv_profile.setOnClickListener {

            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)

        }

//        tabLayout_posts.getTabAt(0)?.setIcon(R.drawable.ic_home)
//        tabLayout_posts.getTabAt(1)?.setIcon(R.drawable.ic_profile)

    }

//    class PostFragmentAdapter(baseActivity: BaseActivity) : FragmentStateAdapter(baseActivity) {
//
//        override fun getItemCount(): Int = 2
//
//        override fun createFragment(position: Int): Fragment {
//
//            if(position == 0){
//                return AllPostsFragment()
//            }
//            else if(position == 1){
//                return MyPostsFragment()
//            }
//
//            return Fragment()
//        }
//    }


}