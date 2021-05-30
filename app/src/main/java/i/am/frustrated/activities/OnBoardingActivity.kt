@file:Suppress("DEPRECATION")

package i.am.frustrated.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import i.am.frustrated.R
import i.am.frustrated.viewmodels.VenterViewModel
import kotlinx.android.synthetic.main.activity_on_boarding.*

@Suppress("DEPRECATION")
class OnBoardingActivity : AppCompatActivity() {

    private lateinit var venterViewModel: VenterViewModel
    private lateinit var sharedPref: SharedPreferences


    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        venterViewModel = ViewModelProviders.of(this).get(VenterViewModel::class.java)

        sharedPref = getSharedPreferences(
            getString(R.string.shared_pref_name),
            Context.MODE_PRIVATE
        )

        //sharedPref.edit().putBoolean(getString(R.string.is_onboading_done),false).commit()

        if(sharedPref.contains(getString(R.string.is_onboading_done))){

            if (sharedPref.getBoolean(getString(R.string.is_onboading_done),false)) {
                goToMainActivity()
            }
            else{
                getCredentials()
            }

        }
        else {
            getCredentials()
        }

//        if(sharedPref.contains(getString(R.string.venter_id)) && sharedPref.contains(getString(R.string.token))){
//            goToMainActivity()
//        }
//        else{
//            getCredentials()
//
//        }

        bt_continue.setOnClickListener {

            sharedPref.edit().putBoolean(getString(R.string.is_onboading_done),true).commit()
            goToMainActivity()
        }
    }

    private fun goToMainActivity(){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @SuppressLint("ApplySharedPref")
    private fun getCredentials() {

        venterViewModel.getVenterCredentials().observe(this, {

            if (it != null) {

                val editor = sharedPref.edit()
                editor.putString(getString(R.string.venter_id), it.venter_id)
                editor.putString(getString(R.string.token), it.token)
                editor.putBoolean(getString(R.string.is_admin),it.is_admin)
                editor.putBoolean(getString(R.string.is_nsfw_allowed),false)
                editor.putInt(getString(R.string.mood_filter), R.integer.mood_filter_default)
                editor.commit()

                progress.visibility = View.GONE
                bt_continue.visibility = View.VISIBLE
            }

        })
    }
}