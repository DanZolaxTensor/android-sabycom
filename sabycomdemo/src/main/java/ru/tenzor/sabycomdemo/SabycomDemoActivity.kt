package ru.tenzor.sabycomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import ru.tenzor.sabycom.Sabycom
import ru.tenzor.sabycom.data.UserData
import ru.tenzor.sabycomdemo.databinding.ActivitySabycomDemoBinding
import java.util.UUID

/**
 * @author ma.kolpakov
 */
class SabycomDemoActivity : AppCompatActivity() {
    private val viewModel: DemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySabycomDemoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE)

        var stringUUID = sharedPreferences.getString(UUID_KEY, null)
        if (stringUUID.isNullOrEmpty()) {
            stringUUID = UUID.randomUUID().toString()
            val editor = sharedPreferences.edit()
            editor.putString(UUID_KEY, stringUUID)
            editor.apply()
        }

        Sabycom.registerUser(UserData(UUID.fromString(stringUUID)))

        binding.showSabycom.setOnClickListener {
            Sabycom.show(this)
        }

        viewModel.messageCounter.observe(this) {
            binding.counter.text = it.toString()
        }

    }

}

private const val SHARED_PREFERENCE_NAME = "SabycomDemo"
private const val UUID_KEY = "uuid"