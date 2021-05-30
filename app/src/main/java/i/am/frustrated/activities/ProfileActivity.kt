package i.am.frustrated.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import i.am.frustrated.R
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val activityName: TextView = header_profile.findViewById(R.id.tv_activity_name)
        val ivBack: ImageView = header_profile.findViewById(R.id.iv_back)
        val btPost: TextView = header_profile.findViewById(R.id.bt_post)

        btPost.visibility = View.GONE
        activityName.text = "Profile"
        ivBack.setOnClickListener {
            finish()
        }
    }
}