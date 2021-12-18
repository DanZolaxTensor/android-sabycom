package ru.tensor.sabycom.widget

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import im.delight.android.webview.AdvancedWebView
import ru.tensor.sabycom.R
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.databinding.SabycomDialogBinding
import ru.tensor.sabycom.push.util.attachNotificationLocker
import ru.tensor.sabycom.widget.js.JSInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.webkit.CookieManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat


/**
 * @author ma.kolpakov
 */
internal class SabycomDialog : BottomSheetDialogFragment(), AdvancedWebView.Listener {
    private lateinit var binding: SabycomDialogBinding
    private lateinit var url: String
    private lateinit var userData: UserData
    private val viewModel: SabycomActivityViewModel by activityViewModels()
    private var isContentScrolling = true
    private var fileUrl = ""
    private var fileName = ""
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    companion object {
        fun newInstance(url: String, userData: UserData): SabycomDialog {
            return SabycomDialog().apply {
                arguments = Bundle()
                this.url = url
                this.userData = userData
                requireArguments().putString(ARG_URL, url)
                requireArguments().putParcelable(ARG_USER_DATA, userData)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SabycomFullScreenDialog)
        attachNotificationLocker(Sabycom.notificationLocker)
        with(requireArguments()) {
            url = getString(ARG_URL)!!
            userData = getParcelable(ARG_USER_DATA)!!
        }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted || checkPermission()) {
                    downloadFile(fileUrl, fileUrl)
                } else {
                    Toast.makeText(requireContext(), "No permission to save file", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                it.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                    height = CoordinatorLayout.LayoutParams.MATCH_PARENT
                }

                behaviour.addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING && isContentScrolling) {
                            behaviour.setState(BottomSheetBehavior.STATE_EXPANDED)
                        } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            dismiss()
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
                })

                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }

        }
        return bottomSheetDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = SabycomDialogBinding.inflate(inflater)

        prepareWebView(binding.webView)

        binding.webView.loadUrl(url)

        return binding.root
    }


    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        binding.webView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.webView.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        binding.webView.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        requireActivity().onBackPressed()
    }

    // Можно использовать JavaScript так как мы загружаем только наш веб-виджет
    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun prepareWebView(webView: AdvancedWebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true

        webView.setListener(requireActivity(), this)

        webView.addJavascriptInterface(
            JSInterface(Sabycom.countController, {
                requireActivity().runOnUiThread {
                    viewModel.hide()
                }
            }, {
                isContentScrolling = it
            }),
            "mobileParent"
        )
    }

    override fun onDownloadRequested(
        url: String,
        suggestedFilename: String,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
        fileName = suggestedFilename
        fileUrl = url
        if (!checkPermission()) {
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            downloadFile(url, suggestedFilename)
        }
    }

    private fun downloadFile(url: String?, suggestedFilename: String?) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, suggestedFilename)

        request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(binding.webView.url))

        val dm = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
    }

    private fun checkPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    override fun onPageStarted(url: String?, favicon: Bitmap?) = Unit

    override fun onPageFinished(url: String?) = Unit

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) = Unit

    override fun onExternalPageRequest(url: String?) = Unit
}

private const val ARG_URL = "URL"
private const val ARG_USER_DATA = "USER_DATA"