package com.example.spotifyapi.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spotifyapi.utils.Constants.BEARER

fun Fragment.toast(msg: String?) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
fun Activity.toast(msg: String?) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun String?.addBearer() = "$BEARER $this"