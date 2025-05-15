package com.example.spotifyapi.oauth2.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifyapi.app.ui.topartists.TopArtistsActivity
import com.example.spotify.utils.Constants
import com.example.spotifyapi.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        Log.d("LoginActivity", "onCreate chamado. Intent data: ${intent?.data}")

        setupButtonListeners()
        handleRedirect(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("LoginActivity", "onNewIntent chamado com URI: ${intent.data}")
        handleRedirect(intent)
    }

    private fun setupButtonListeners() {
        binding.buttonStart.setOnClickListener {
            if (!loginViewModel.isInternetAvailable(this)) {
                Toast.makeText(
                    this, "Sem conexão com a internet. Carregando offline.", Toast.LENGTH_SHORT
                ).show()
                navigateToTopArtistsActivity(accessToken = "")
            } else {
                val authIntent = loginViewModel.startAuthentication()
                startActivity(authIntent)
            }
        }
    }

    private fun handleRedirect(intent: Intent?) {
        intent?.data?.let { uri ->
            Log.d("LoginActivity", "handleRedirect() chamado com URI: $uri")
            if (uri.toString().startsWith(Constants.REDIRECT_URI)) {
                loginViewModel.handleRedirect(uri, Constants.REDIRECT_URI).observe(this) { result ->
                    result?.onSuccess {
                        val accessToken = it.token?.accessToken ?: ""
                        Log.d("LoginActivity", "✅ Token pronto para navegação: $accessToken")
                        navigateToTopArtistsActivity(accessToken)
                    }?.onFailure {
                        Log.e("LoginActivity", "❌ Erro ao obter token: ${it.message}")
                        Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_SHORT).show()
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