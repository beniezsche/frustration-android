package i.am.frustrated.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import i.am.frustrated.R
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : BaseActivity() {
    @SuppressLint("ApplySharedPref", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val activityName: TextView = header.findViewById(R.id.tv_activity_name)
        val ivBack: ImageView = header.findViewById(R.id.iv_back)
        activityName.text = "Settings"
        ivBack.setOnClickListener {
            goToMainActivity()
        }

        val sharedPref = getSharedPreferences(
            getString(R.string.shared_pref_name),
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        val isNsfwAllowed = sharedPref.getBoolean(getString(R.string.is_nsfw_allowed), false)


        cl_item_credentials.setOnClickListener {
            val intent = Intent(this, CredentialsActivity::class.java)
            startActivity(intent)
        }


        switch_nsfw_allowed.isChecked = isNsfwAllowed
        switch_nsfw_allowed.setOnCheckedChangeListener { _, isChecked ->

            editor.putBoolean(getString(R.string.is_nsfw_allowed), isChecked)
            editor.commit()

        }

        cl_item_contact.setOnClickListener {
            val to = "osunatheapp@gmail.com"
            //val subject: String = ""
            val message = "Hey guys, what's up?"


            val email = Intent(Intent.ACTION_SEND)
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
//            email.putExtra(Intent.EXTRA_SUBJECT, subject)
            email.putExtra(Intent.EXTRA_TEXT, message)

            //need this to prompts email client only
            email.type = "message/rfc822"

            startActivity(Intent.createChooser(email, "Choose an Email client :"))
        }

        cl_item_share_app.setOnClickListener {

            /*Create an ACTION_SEND Intent*/
            val intent = Intent(Intent.ACTION_SEND)
            /*This will be the actual content you wish you share.*/
            val shareBody = "https://play.google.com/store/apps/details?id=i.am.frustrated"
            /*The type of the content is text, obviously.*/
            /*The type of the content is text, obviously.*/intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            /*Fire!*/startActivity(Intent.createChooser(intent, "Share using"))
        }


        cl_item_rules.setOnClickListener {
            val intent = Intent(this, GuidelinesActivity::class.java)
            startActivity(intent)
        }

        cl_item_privacy_policy.setOnClickListener {
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        cl_item_info.setOnClickListener {
            val confirmationDialog = AlertDialog.Builder(this)
            confirmationDialog.setMessage("Are you sure you want to delete your account?")

            confirmationDialog.setNegativeButton("No") { dialog, _: Int ->
                dialog.dismiss()
            }

            confirmationDialog.setPositiveButton("Yes") { _, _: Int ->
                editor.clear().commit()

                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
                goToOnboardingActivity()
            }


            confirmationDialog.show()
        }
    }

    override fun onBackPressed() {

        goToMainActivity()
    }
}