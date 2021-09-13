package ru.tenzor.sabycom.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.ViewUtils.doOnApplyWindowInsets
import ru.tenzor.sabycom.R
import ru.tenzor.sabycom.data.UserData
import ru.tenzor.sabycom.databinding.SabycomDialogBinding


/**
 * @author ma.kolpakov
 */
class SabycomDialog : BottomSheetDialogFragment() {
    var onClose: (() -> Unit)? = null
    private lateinit var binding: SabycomDialogBinding
    private lateinit var url: String
    private var preloadedData: String? = null
    private var userData: UserData? = null

    companion object {
        fun newInstance(url: String, preloadedData: String?, userData: UserData?): SabycomDialog {
            return SabycomDialog().apply {
                this.url = url
                this.preloadedData = preloadedData
                this.userData = userData
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
            val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet as View)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return bottomSheetDialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = SabycomDialogBinding.inflate(inflater)
        prepareWebView(binding.webView)

        if (preloadedData.isNullOrEmpty()) {
            binding.webView.loadUrl(url)
        } else {
            binding.webView.loadDataWithBaseURL(url, preloadedData!!, "text/html", Charsets.UTF_8.name(), null)
        }

        return binding.root
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onClose?.invoke()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun prepareWebView(webView: WebView): WebView {
        webView.settings.javaScriptEnabled = true
        return webView
    }

}