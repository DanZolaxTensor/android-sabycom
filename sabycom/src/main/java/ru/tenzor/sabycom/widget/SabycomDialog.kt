package ru.tenzor.sabycom.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.tenzor.sabycom.R
import ru.tenzor.sabycom.data.UserData
import ru.tenzor.sabycom.databinding.SabycomDialogBinding


/**
 * @author ma.kolpakov
 */
internal class SabycomDialog : BottomSheetDialogFragment() {
    var onClose: (() -> Unit)? = null
    private lateinit var binding: SabycomDialogBinding
    private lateinit var url: String
    private lateinit var userData: UserData

    companion object {
        fun newInstance(url: String, userData: UserData): SabycomDialog {
            return SabycomDialog().apply {
                this.url = url
                this.userData = userData
                val arguments = getOrCreateArguments()
                arguments.putString(ARG_URL,url)
                arguments.putParcelable(ARG_USER_DATA,userData)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        url = getOrCreateArguments().getString(ARG_URL)!!
        userData = getOrCreateArguments().getParcelable(ARG_USER_DATA)!!

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

        binding.webView.loadUrl(url, createHeaders(userData))

        return binding.root
    }

    private fun createHeaders(userData: UserData): MutableMap<String, String> {
        val headers = mutableMapOf("id" to userData.id.toString())
        headers.putIfNotNull("name", userData.name)
        headers.putIfNotNull("surname", userData.surname)
        headers.putIfNotNull("email", userData.email)
        headers.putIfNotNull("phone", userData.phone)
        return headers
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
    private fun getOrCreateArguments(): Bundle {
        if (arguments == null) {
            arguments = Bundle()
        }
        return arguments as Bundle
    }
    private fun MutableMap<String, String>.putIfNotNull(key: String, value: String?) {
        if (value != null) this[key] = value
    }
}
private const val ARG_URL = "URL"
private const val ARG_USER_DATA = "USER_DATA"