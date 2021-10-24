package com.wasilyk.app.jpegtopngconverter.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import com.wasilyk.app.jpegtopngconverter.R
import com.wasilyk.app.jpegtopngconverter.databinding.ActivityMainBinding
import com.wasilyk.app.jpegtopngconverter.presenter.MainPresenter
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding
    private val activityLauncher = registerForActivityResult(GalleryContract()) {
        presenter.convert(it.toString())
    }

    private val presenter by moxyPresenter { MainPresenter(IConverterImpl(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setConvertBtnClickListener()
        setCancelBtnClickListener()
    }

    private fun setConvertBtnClickListener() {
        binding.convertBtn.setOnClickListener {
            activityLauncher.launch(null)
        }
    }

    private fun setCancelBtnClickListener() {
        binding.cancelBtn.setOnClickListener {
            presenter.cancel()
        }
    }

    override fun complete() = stopLoading(R.string.complete)
    override fun error() = stopLoading(R.string.error)
    override fun cancel() = stopLoading(R.string.cancel)
    override fun loading() {
        binding.apply {
            progressCircular.visibility = View.VISIBLE
            convertBtn.visibility = View.INVISIBLE
            cancelBtn.visibility = View.VISIBLE
            tvStatus.text = resources.getString(R.string.loading)
            tvStatus.visibility = View.VISIBLE
        }
    }

    private fun stopLoading(stringRes: Int) {
        binding.apply {
            progressCircular.visibility = View.GONE
            convertBtn.visibility = View.VISIBLE
            cancelBtn.visibility = View.GONE
            tvStatus.text = resources.getString(stringRes)
            tvStatus.visibility = View.VISIBLE
        }
    }

    class GalleryContract : ActivityResultContract<String, Uri?>() {
        override fun createIntent(context: Context, input: String?): Intent {
            return Intent(Intent.ACTION_PICK).also {
                it.type = "image/jpg"
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            if(resultCode == RESULT_OK) {
                return intent?.data
            }
            return null
        }
    }
}