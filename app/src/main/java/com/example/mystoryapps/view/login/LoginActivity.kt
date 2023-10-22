package com.example.mystoryapps.view.login

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
import com.example.mystoryapps.databinding.ActivityLoginBinding
import com.example.mystoryapps.view.ViewModelFactory
import com.example.mystoryapps.view.list_story.ListStoryActivity
import com.example.mystoryapps.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val loginViewModel: LoginViewModel by viewModels { factory }

        animation()
        checkChangeEditText()

        binding.apply {
            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finishAffinity()
            }

            btnLogin.setOnClickListener {
                onLoading()
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                loginViewModel.userLogin(email, password).observe(this@LoginActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            onLoading()
                        }

                        is Result.Success -> {
                            loginViewModel.saveTokenUser(result.data.loginResult.token)
                            onSuccess()
                        }

                        is Result.Error -> {
                            onError(resources.getString(R.string.check_email_password_wrong))
                        }
                    }
                }
            }
        }
    }

    private fun animation() {
        val email = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(700)
        val password =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(700)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val registerMove =
            ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(email, password)
        }

        AnimatorSet().apply {
            playSequentially(together, login, registerMove)
            start()
        }
    }

    private fun checkChangeEditText() {
        binding.apply {
            emailEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setMyButtonEnable()
                }

                override fun afterTextChanged(s: Editable) {}
            })

            passwordEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setMyButtonEnable()
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }
    }

    private fun setMyButtonEnable() {
        val resultEmail = binding.emailEditText
        val resultPassword = binding.passwordEditText

        val enable = resultEmail.error.isNullOrEmpty() && resultPassword.error.isNullOrEmpty()
        binding.btnLogin.isEnabled = enable
    }

    private fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onError(message: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess() {
        startActivity(Intent(this@LoginActivity, ListStoryActivity::class.java))
        finishAffinity()
        binding.progressBar.visibility = View.GONE
    }
}