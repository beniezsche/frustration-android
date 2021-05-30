package i.am.frustrated.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import i.am.frustrated.R


class SplashActivity : BaseActivity() {

    companion object {
        const val TAG = "firebase_debug"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler(Looper.getMainLooper()).postDelayed({ // This method will be executed once the timer is over
            val i = Intent(this@SplashActivity, OnBoardingActivity::class.java)
            startActivity(i)
            finish()
        }, 1000)

    }

}