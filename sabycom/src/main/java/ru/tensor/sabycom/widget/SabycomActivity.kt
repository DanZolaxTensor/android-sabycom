package ru.tensor.sabycom.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.tensor.sabycom.data.UrlUtil

/**
 * @author ma.kolpakov
 */
internal class SabycomActivity : AppCompatActivity() {
    private var dialog: SabycomDialog? = null
    private val viewModel: SabycomActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.state.observe(this) { state ->
            when (state) {
                WebWidgetState.OPENED -> {
                    dialog = supportFragmentManager.findFragmentByTag(SABYCOM_TAG) as SabycomDialog?
                    if (dialog != null) return@observe
                    dialog = SabycomDialog.newInstance(
                        UrlUtil.buildWidgetUrl(
                            userId = viewModel.getUser().id.toString(),
                            apiKey = viewModel.getApiKey()
                        ),
                        viewModel.getUser()
                    ).apply {
                        show(supportFragmentManager, SABYCOM_TAG)
                    }
                }
                WebWidgetState.CLOSED -> {
                    dialog?.dismiss()
                }
                else -> throw IllegalStateException()
            }
        }
    }

    internal companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SabycomActivity::class.java)
        }
    }

}

private const val SABYCOM_TAG = "Sabycom"
