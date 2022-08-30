package com.Groupe1.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.Groupe1.myapplication.utils.FirebaseUtils.Extensions.toast
import com.Groupe1.myapplication.utils.FirebaseUtils.FirebaseUtils.firebaseAuth
import com.Groupe1.myapplication.views.MorpionActivity
import kotlinx.android.synthetic.main.fragment_connexion.*


class SignInActivity  : AppCompatActivity() {

    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText>

    //Evenements
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_connexion)

        signInInputsArray = arrayOf(Email_Connexion, Password_Connexion)
        Page_Inscription.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
            finish()
        }

        Button_Connexion.setOnClickListener {
            signInUser()
        }
    }

    //Vérification que les champs ne sont pas vides
    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    //Connexion
    private fun signInUser() {
        signInEmail = Email_Connexion.text.toString().trim()
        signInPassword = Password_Connexion.text.toString().trim()

        if (notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        toast("Connexion réussie!")
                        startActivity(Intent(this, MorpionActivity::class.java))
                        finish()
                    } else {
                        toast("Une erreur de connexion a été rencontré!")
                    }
                }
        } else {
            signInInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} est requis"
                }
            }
        }
    }
}