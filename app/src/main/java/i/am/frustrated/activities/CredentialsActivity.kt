package i.am.frustrated.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import i.am.frustrated.R
import kotlinx.android.synthetic.main.activity_credentials.*
import kotlinx.android.synthetic.main.activity_settings.header

class CredentialsActivity : BaseActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credentials)

        val activityName: TextView = header.findViewById(R.id.tv_activity_name)
        val ivBack: ImageView = header.findViewById(R.id.iv_back)

        val sharedPref = getSharedPreferences(getString(R.string.shared_pref_name),Context.MODE_PRIVATE)

        val venterId = sharedPref.getString(getString(R.string.venter_id),null)

        activityName.text = "Credentials"
        ivBack.setOnClickListener {
            finish()
        }

        tv_user_id.text = venterId


    }
}