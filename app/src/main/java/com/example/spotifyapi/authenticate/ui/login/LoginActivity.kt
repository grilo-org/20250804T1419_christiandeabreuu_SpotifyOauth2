package com.example.spotifyapi.authenticate.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifyapi.app.ui.AppActivity
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
                Log.d(
                    "LoginActivity",
                    "✅ Token pronto para navegação: ${spotifyTokens.accessToken}"
                )
                loginViewModel.updateAcessToken(spotifyTokens.accessToken)
            }?.onFailure {
                Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeNavigation() {
        loginViewModel.navigateToArtists.observe(this) { accessToken ->
            val intent = Intent(this, AppActivity::class.java).apply {
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
            if (NetworkUtils.isInternetAvailable(this)) {
                startActivity(loginViewModel.getAuthIntent())
            } else {
                Log.d("LoginActivity", "Sem internet! Indo")
                navigateToOfflineMode()
            }
        }
    }
//    private fun setupButtonListeners() {
//        binding.buttonStart.setOnClickListener {
//            loginViewModel.checkInternet(this)
//        }
//
//        loginViewModel.connectionStatus.observe(this) { isConnected ->
//            if (!isConnected) {
//                Log.d("LoginActivity", "Sem internet! Indo para tela offline.")
//                navigateToOfflineMode() // Agora realmente navega para a próxima tela offline
//            } else {
//                startActivity(loginViewModel.getAuthIntent()) // Continua autenticando
//            }
//        }
//    }

    private fun navigateToOfflineMode() {
        val intent = Intent(this, AppActivity::class.java)
        Toast.makeText(this, "Modo offline", Toast.LENGTH_SHORT).show()
        startActivity(intent)

    }

    fun goToOfflineScreen() {
        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
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
        val intent = Intent(this, AppActivity::class.java).apply {
            putExtra("ACCESS_TOKEN", accessToken)
        }
        startActivity(intent)
        finish()
    }
}