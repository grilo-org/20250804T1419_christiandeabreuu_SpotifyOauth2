package com.example.spotifyapi.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

object Constants {
    const val REDIRECT_URI = "meuapp://callback"
    const val CODE = "code"
}

fun Fragment.toast(msg: String?) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
