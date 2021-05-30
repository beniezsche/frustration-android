package i.am.frustrated.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import i.am.frustrated.R
import kotlinx.android.synthetic.main.activity_guidelines.*
import kotlinx.android.synthetic.main.activity_settings.header

class GuidelinesActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guidelines)

        val activityName: TextView = header.findViewById(R.id.tv_activity_name)
        val ivBack: ImageView = header.findViewById(R.id.iv_back)
        activityName.text = "Rules"
        ivBack.setOnClickListener {
            finish()
        }


        webView_rules.loadUrl("https://github.com/osunatheapp/osuna-docs/blob/main/rules.md")
    }
}