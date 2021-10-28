package ru.tensor.sabycom.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

/**
 * @author ma.kolpakov
 */
internal class SabycomActivity : AppCompatActivity() {
    private lateinit var dialog: SabycomDialog
    private val viewModel: SabycomActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.state.observe(this) { state ->
            when (state) {
                is Opened -> {
                    if (supportFragmentManager.findFragmentByTag(SABYCOM_DIALOG_FRAGMENT_TAG) != null) return@observe
                    dialog = SabycomDialog.newInstance(state.url, state.userData)
                    dialog.show(supportFragmentManager, SABYCOM_DIALOG_FRAGMENT_TAG)
                }
                is Closed -> {
                    dialog.dismiss()
                }
            }
        }
    }

    internal companion object {
        /**
         * етод предоставляет интетнт для открытия активити с виджетом онлайн консультации
         */
        fun createIntent(context: Context): Intent {
            return Intent(context, SabycomActivity::class.java)
        }

        private const val SABYCOM_DIALOG_FRAGMENT_TAG = "Sabycom"
    }
}
