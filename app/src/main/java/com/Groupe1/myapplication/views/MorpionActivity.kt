package com.Groupe1.myapplication.views

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.Groupe1.myapplication.R
import com.Groupe1.myapplication.SignInActivity
import com.Groupe1.myapplication.utils.FirebaseUtils.Extensions.toast
import com.Groupe1.myapplication.utils.FirebaseUtils.FirebaseUtils.firebaseAuth
import com.Groupe1.myapplication.utils.FirebaseUtils.FirebaseUtils.firebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MorpionActivity : AppCompatActivity(), View.OnClickListener {

    private var cells = mutableMapOf<Int, String>()
    private var isX = true
    private var winner:String=""
    private var totalCell=9
    private lateinit var txtResult:TextView
    private val x = "X"
    private val o = "O"
    private var btns = arrayOfNulls<Button>(totalCell)
    private val combinations:Array<IntArray> = arrayOf(
        intArrayOf(0,1,2),
        intArrayOf(3,4,5),
        intArrayOf(6,7,8),
        intArrayOf(0,3,6),
        intArrayOf(1,4,7),
        intArrayOf(2,5,8),
        intArrayOf(0,4,8),
        intArrayOf(2,4,6)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtResult=findViewById(R.id.resultat)

        for (i in 1..totalCell) {
            var button=findViewById<Button>(resources.getIdentifier("btn$i","id",packageName))
            button.setOnClickListener(this)
            btns[i-1]=button
        }

        //Déconnexion
        BtnSignOut_Morpion.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            toast("Déconnexion")
            finish()
        }

        //Affichage email
        if (firebaseUser != null) {
            val userEmail: String? = firebaseUser.email

            val textView: TextView = findViewById(R.id.Et_Joueur)
            textView.text = userEmail
        } else {
            toast("Aucun utilisateur n'est connecté")
        }

    }
    private fun btnSetected(button: Button) {
        var index = 0
        when(button.id){
            R.id.btn1->index=0
            R.id.btn2->index=1
            R.id.btn3->index=2
            R.id.btn4->index=3
            R.id.btn5->index=4
            R.id.btn6->index=5
            R.id.btn7->index=6
            R.id.btn8->index=7
            R.id.btn9->index=8
        }
        playGame(index, button)
        checkWinner()
        update()
    }

    private fun checkWinner(){
        if(cells.isNotEmpty()){
            for ( combination in combinations) {
                var(a,b,c)=combination
                if(cells[a]!=null && cells[a]==cells[b] && cells[a]==cells[c]){
                    this.winner=cells[a].toString()
                }
            }
        }
    }

    private fun update() {
        when{
            winner.isNotEmpty()->{
                txtResult.text=resources.getString(R.string.winner, winner)
                txtResult.setTextColor(Color.GREEN)
            }
            cells.size==totalCell->{
                txtResult.text="Egalité"
            }
            else->{
                txtResult.text=resources.getString(R.string.next_player,if(isX)x else o)
            }
        }
    }

    private fun playGame(index:Int,button: Button){
        if (!winner.isNullOrEmpty()){
            Toast.makeText(this,"Jeux Fini", Toast.LENGTH_LONG).show()
            return
        }
        when{
            isX->cells[index]=x
            else->cells[index]=o
        }
        button.text=cells[index]
        button.isEnabled=false
        isX=!isX
    }

    fun resetButton(){
        for (i in 1..totalCell) {
            var button=btns[i-1]
            button?.text=""
            button?.isEnabled=true
        }
    }

    fun newGame(){
        cells= mutableMapOf()
        isX=true
        winner=""
        txtResult.text=resources.getString(R.string.next_player, x)
        txtResult.setTextColor(Color.BLACK)
        resetButton()
    }

    fun reset(view: View){
        newGame()
    }

    override fun onClick(v: View?) {
        btnSetected(v as Button)
    }

}