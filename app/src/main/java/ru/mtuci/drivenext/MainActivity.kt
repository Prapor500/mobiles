package ru.mtuci.drivenext

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.mtuci.drivenext.databinding.ActivityMainBinding
import ru.mtuci.drivenext.presentation.MainViewModel
import ru.mtuci.drivenext.presentation.StartupState
import ru.mtuci.drivenext.ui.LoginFragment
import ru.mtuci.drivenext.ui.MainFragment
import ru.mtuci.drivenext.ui.NoConnectionFragment
import ru.mtuci.drivenext.ui.OnboardingFragment
import ru.mtuci.drivenext.ui.SplashFragment

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, bars.top, 0, bars.bottom)
            insets
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state -> render(state) }
                }
            }
        }

        viewModel.start()
    }

    private fun render(state: StartupState) {
        when (state) {
            StartupState.Splash -> showFragment(SplashFragment())
            StartupState.NoConnection -> showFragment(NoConnectionFragment())
            StartupState.Onboarding -> showFragment(OnboardingFragment())
            StartupState.Login -> showFragment(LoginFragment())
            StartupState.Main -> showFragment(MainFragment())
            is StartupState.Error -> showError(state.message)
        }
    }

    private fun showFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.rootContainer, fragment)
        }
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Ошибка")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
