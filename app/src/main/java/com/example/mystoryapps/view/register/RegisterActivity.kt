package com.example.mystoryapps.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapps.R
import com.example.mystoryapps.data.Result
import com.example.mystoryapps.databinding.ActivityRegistrasiBinding
import com.example.mystoryapps.view.ViewModelFactory
import com.example.mystoryapps.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrasiBinding
    private val registerViewModel: RegisterViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animation()
        checkChangeEditText()

        binding.apply {
            tvLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }

            btnSignUp.setOnClickListener {
                onLoading()
                val name = binding.nameEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                registerViewModel.userRegister(name, email, password).observe(this@RegisterActivity) { result ->
                    when(result){
                        is Result.Loading -> {
                            onLoading()
                        }
                        is Result.Success -> {
                            onSuccess()
                        }
                        is Result.Error -> {
                            onError(resources.getString(R.string.email_unique))
                        }
                    }
                }
            }
        }
    }

    private fun animation(){
        binding.nameEditText.hint = getString(R.string.fullname)

        val email = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(700)
        val name = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(700)
        val password = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(700)
        val signUp = ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 1f).setDuration(500)
        val loginMove = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(email, name, password)
        }

        AnimatorSet().apply {
            playSequentially(together, signUp, loginMove)
            start()
        }
    }

    private fun checkChangeEditText() {
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
        })
        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
        })
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
        })
    }

    private fun setMyButtonEnable() {
        val resultName = binding.nameEditText
        val resultEmail = binding.emailEditText
        val resultPassword = binding.passwordEditText

        val enable = resultEmail.error.isNullOrEmpty() && resultName.error.isNullOrEmpty() && resultPassword.error.isNullOrEmpty()
        binding.btnSignUp.isEnabled = enable
    }

    private fun onLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onError(message: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess(){
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finishAffinity()
        binding.progressBar.visibility = View.GONE
    }
}