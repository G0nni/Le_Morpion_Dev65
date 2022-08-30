package com.Groupe1.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.Groupe1.myapplication.utils.FirebaseUtils.Extensions.toast
import com.Groupe1.myapplication.utils.FirebaseUtils.FirebaseUtils.firebaseAuth
import com.Groupe1.myapplication.utils.FirebaseUtils.FirebaseUtils.firebaseUser
import com.Groupe1.myapplication.views.MorpionActivity
import kotlinx.android.synthetic.main.fragment_inscription.*


class CreateAccountActivity : AppCompatActivity() {

    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var createAccountInputsArray: Array<EditText>

    //Evenements
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_inscription)
        createAccountInputsArray = arrayOf(etSEmailAddress, etSPassword, etSConfPassword)
        btnSSigned.setOnClickListener {
            signIn()
        }

        tvRedirectLogin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    //Vérification que aucun champs n'est vides
    private fun notEmpty(): Boolean = etSEmailAddress.text.toString().trim().isNotEmpty() &&
            etSPassword.text.toString().trim().isNotEmpty() &&
            etSConfPassword.text.toString().trim().isNotEmpty()

    //Vérification que le mot de passe comporte au moins 6 caractères
    private fun PasswordLength(): Boolean {
        var length = false

        if (notEmpty() &&
            etSPassword.text.toString().trim().length >= 6
        ) {
            length = true
        } else {
            toast("Le mot de passe doit contenir 6 caractères minimum!")
        }

        return length
    }

    //Vérification de conformité des mdp
    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() &&
            etSPassword.text.toString().trim() == etSConfPassword.text.toString().trim() &&
            PasswordLength()
        ) {
            identical = true
        } else if (!notEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} est requis"
                }
            }
        } else {
            toast("Les mots de passes ne sont pas les mêmes!")
        }
        return identical
    }

    //Vérification que le champ contient bien un email
    private fun EmailExist(): Boolean {
        var email = false
        if (identicalPassword() &&
            etSEmailAddress.text.toString().trim().contains("@") &&
            etSEmailAddress.text.toString().trim().contains(".")
        ) {
            email = true
        } else {
            toast("Vous n'avez pas saisi d'adresse email!")
        }

        return email
    }

    //Création de compte
    private fun signIn() {
        if (EmailExist()) {
            userEmail = etSEmailAddress.text.toString().trim()
            userPassword = etSPassword.text.toString().trim()

            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        toast("Votre compte a bien été créé!")
                        sendEmailVerification()
                        startActivity(Intent(this, MorpionActivity::class.java))
                        finish()
                    } else {
                        toast("Une erreur d'enregistrement a été rencontré!")
                    }
                }
        }
    }

    //Email de vérification mais flemme de mettre en place un smtp :)
    private fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("Email envoyé à $userEmail")
                }
            }
        }
    }
}