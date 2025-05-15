package com.example.spotifyapi.oauth2.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifyapi.app.ui.topartists.TopArtistsActivity
import com.example.spotifyapi.databinding.ActivityLoginBinding
import com.example.spotifyapi.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupButtonListeners()
        handleRedirect(intent)
        observeNavigation()
        observeAuthResult()
    }

    private fun observeAuthResult() {
        loginViewModel.authResult.observe(this) { result ->
            result?.onSuccess { spotifyTokens ->
                Log.d(
                    "LoginActivity",
                    "✅ Token pronto para navegação: ${spotifyTokens.accessToken}"
                )
                loginViewModel.navigate(spotifyTokens.accessToken)
            }?.onFailure {
                Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeNavigation() {
        loginViewModel.navigateToArtists.observe(this) { accessToken ->
            val intent = Intent(this, TopArtistsActivity::class.java).apply {
                putExtra("ACCESS_TOKEN", accessToken)
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("LoginActivity", "onNewIntent chamado com URI: ${intent.data}")
        intent.data?.let { loginViewModel.processRedirect(it) }
    }

    private fun setupButtonListeners() {
        binding.buttonStart.setOnClickListener {
            loginViewModel.checkInternet(this)
        }

        loginViewModel.connectionStatus.observe(this) { isConnected ->
            if (!isConnected) {
                loginViewModel.notifyError("Sem conexão com a internet. Carregando offline.")
            } else {
                startActivity(loginViewModel.getAuthIntent())
            }
        }
    }

    private fun handleRedirect(intent: Intent?) {
        intent?.data?.let { uri ->
            Log.d("LoginActivity", "handleRedirect() chamado com URI: $uri")
            if (uri.toString().startsWith(Constants.REDIRECT_URI)) {
                loginViewModel.handleRedirect(uri, Constants.REDIRECT_URI).observe(this) { result ->
                    result?.onSuccess {
                        val accessToken = it.accessToken ?: ""
                        navigateToTopArtistsActivity(accessToken)
                    }
                }
            }
        } ?: Log.e("LoginActivity", "URI inválida")
    }

    private fun navigateToTopArtistsActivity(accessToken: String) {
        val intent = Intent(this, TopArtistsActivity::class.java).apply {
            putExtra("ACCESS_TOKEN", accessToken)
        }
        startActivity(intent)
        finish()
    }
}