package com.example.spotifyapi.auth.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifyapi.R
import com.example.spotifyapi.app.ui.app.AppActivity
//import com.example.spotifyapi.app.ui.topartists.TopArtistsActivity
import com.example.spotifyapi.databinding.ActivityLoginBinding
import com.example.spotifyapi.utils.Constants
import com.example.spotifyapi.utils.NetworkUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListeners()
        handleRedirect(intent)
        observeNavigation()
        observeAuthResult()
    }

    private fun observeAuthResult() {
        loginViewModel.authResult.observe(this) { result ->
            result?.onSuccess { spotifyTokens ->
                loginViewModel.updateAccessToken(spotifyTokens.accessToken)
            }?.onFailure {
                Toast.makeText(this, getString(R.string.auth_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeNavigation() {
        loginViewModel.navigateToArtists.observe(this) {
            val intent = Intent(this, AppActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { loginViewModel.processRedirect(it) }
    }


    private fun setupButtonListeners() {
        binding.buttonStart.setOnClickListener {
            if (NetworkUtils.isInternetAvailable(this)) {
                startActivity(loginViewModel.getAuthIntent())
            } else {
                navigateToOfflineMode()
            }
        }
    }

    private fun navigateToOfflineMode() {
        val intent = Intent(this, AppActivity::class.java)
        Toast.makeText(this, "Modo offline", Toast.LENGTH_SHORT).show()
        startActivity(intent)

    }

    private fun handleRedirect(intent: Intent?) {
        intent?.data?.let { uri ->
            if (uri.toString().startsWith(Constants.REDIRECT_URI)) {
                loginViewModel.handleRedirect(uri, Constants.REDIRECT_URI).observe(this) { result ->
                    result?.onSuccess {

                        navigateToTopAppActivity()
                    }
                }
            }
        }
    }

    private fun navigateToTopAppActivity() {
        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
        finish()
    }
}