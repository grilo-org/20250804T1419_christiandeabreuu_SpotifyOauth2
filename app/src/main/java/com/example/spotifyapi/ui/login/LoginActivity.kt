package com.example.spotifyapi.ui.login

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.spotify.utils.Constants
import com.example.spotifyapi.MainActivity
import com.example.spotifyapi.R
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
            if (!isInternetAvailable()) {
                Toast.makeText(this, "Sem conexão com a internet. Carregando offline.", Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            } else {
                startAuthentication()
            }
        }
    }

    private fun startAuthentication() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AUTH_URL))
        startActivity(intent)
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun handleRedirect(intent: Intent?) {
        intent?.data?.let { uri ->
            Log.d("LoginActivity", "handleRedirect() chamado com URI: $uri")
            if (uri.toString().startsWith(Constants.REDIRECT_URI)) {
                loginViewModel.handleRedirect(uri, Constants.REDIRECT_URI).observe(this) { result ->
                    result?.onSuccess { tokenState ->
                        tokenState.token?.let { tokens ->
                            Log.d("LoginActivity", "Tokens recebidos: accessToken=${tokens.accessToken}")

                            if (loginViewModel.saveTokens(tokens.accessToken, tokens.refreshToken)) {
                                navigateToMainActivity()
                            } else {
                                Log.e("LoginActivity", "Falha ao salvar tokens!")
                            }
                        }
                    }?.onFailure { e ->
                        Log.e("LoginActivity", "Erro ao obter token: ${e.message}")
                    }
                }
            }
        } ?: Log.e("LoginActivity", "URI inválida")
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}