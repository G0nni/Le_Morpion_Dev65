package com.Groupe1.myapplication.utils

import android.app.Activity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUtils {
    private val mAuth: FirebaseAuth? = null

    object FirebaseUtils {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    }

    object Extensions {
        fun Activity.toast(msg: String){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}