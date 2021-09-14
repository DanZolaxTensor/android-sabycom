package ru.tenzor.sabycomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import ru.tenzor.sabycom.Sabycom
import ru.tenzor.sabycom.data.UserData
import ru.tenzor.sabycomdemo.databinding.ActivitySabycomDemoBinding
import java.util.UUID

class SabycomDemoActivity : AppCompatActivity() {
   private val viewModel: DemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySabycomDemoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        Sabycom.registerUser(UserData(UUID.randomUUID()))

        Sabycom.preLoad()

        binding.showSabycom.setOnClickListener {
            Sabycom.show(this)
        }

        viewModel.messageCounter.observe(this){
            binding.counter.text = it.toString()
        }

    }

}