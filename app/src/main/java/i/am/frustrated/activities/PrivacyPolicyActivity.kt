package i.am.frustrated.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import i.am.frustrated.R
import kotlinx.android.synthetic.main.activity_guidelines.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.header

class PrivacyPolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        val activityName: TextView = header.findViewById(R.id.tv_activity_name)
        val ivBack: ImageView = header.findViewById(R.id.iv_back)
        activityName.text = "Privacy Policy"
        ivBack.setOnClickListener {
            finish()
        }


        webView_rules.loadUrl("https://github.com/osunatheapp/osuna-docs/blob/main/privacy-policy.md")
    }
}