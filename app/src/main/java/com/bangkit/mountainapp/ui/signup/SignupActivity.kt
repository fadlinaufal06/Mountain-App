package com.bangkit.mountainapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bangkit.mountainapp.R
import com.bangkit.mountainapp.databinding.ActivitySignupBinding
import com.bangkit.mountainapp.ui.login.LoginActivity


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignupViewModel
    private var isPasswordShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setupView()
        setContentView(binding.root)

        setupViewModel()
        setMyButtonEnable()
        setupAction()
        setupTextChangedListener()
        setupObserve()
        playAnimation()
    }


    private fun setupView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
    }

    private fun setupAction() {
        binding.login.setOnClickListener {
            Intent(this@SignupActivity, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

        binding.tvShowHidePass.setOnClickListener {
            setShowHidePassword()
        }

        binding.btnSignup.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.register(username, email, password)
        }
    }

    private fun setupTextChangedListener() {
        binding.usernameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
                setVisibleShowHidePassword()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun setupObserve() {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.responseRegister.observe(this) {
            if (it.equals("success")) {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.user_created))
                    setMessage(getString(R.string.login_to_continue))
                    setPositiveButton(getString(R.string.continue_btn)) { _, _ ->
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.sorry))
                    setMessage(getString(R.string.unable_to_signup))
                    setPositiveButton(getString(R.string.try_again)) { dialog, _ ->
                        dialog.cancel()
                    }
                    create()
                    show()
                }
            }
        }

        viewModel.onFailure.observe(this) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.sorry))
                setMessage(it)
                setPositiveButton(getString(R.string.try_again)) { dialog, _ ->
                    dialog.cancel()
                }
                create()
                show()
            }
        }
    }

    private fun setShowHidePassword() {
        if (!isPasswordShown) {
            binding.apply {
                passwordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                tvShowHidePass.text = resources.getString(R.string.hide)
                isPasswordShown = true
            }
        } else {
            binding.apply {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                tvShowHidePass.text = resources.getString(R.string.show)
                isPasswordShown = false
            }
        }
    }

    private fun setVisibleShowHidePassword() {
        val resultPass = binding.passwordEditText.text
        if (!(resultPass != null && resultPass.toString().isNotEmpty() && resultPass.length >= 6)) {
            binding.tvShowHidePass.visibility = View.GONE
        } else {
            binding.tvShowHidePass.visibility = View.VISIBLE
        }
    }

    private fun setMyButtonEnable() {
        val resultUsername = binding.usernameEditText.text
        val resultEmail = binding.emailEditText.text
        val resultPass = binding.passwordEditText.text
        binding.btnSignup.isEnabled =
            resultUsername != null && resultUsername.toString().isNotEmpty() &&
                    resultEmail != null && resultEmail.toString()
                .isNotEmpty() && resultEmail.isValidEmail() &&
                    resultPass != null && resultPass.toString()
                .isNotEmpty() && resultPass.length >= 6
        binding.btnSignup.text = resources.getString(R.string.signup)
    }

    private fun CharSequence.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgMount, View.TRANSLATION_Y, -30f, -5f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.bgImgView, View.TRANSLATION_Y, -30f, 0f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val appName = ObjectAnimator.ofFloat(binding.tvAppName, View.ALPHA, 1f).setDuration(500)
        val descAppName =
            ObjectAnimator.ofFloat(binding.tvDescAppName, View.ALPHA, 1f).setDuration(500)
        val tvSignup =
            ObjectAnimator.ofFloat(binding.tvSignup, View.TRANSLATION_X, -130f, 0f).setDuration(500)
        val tvCredential =
            ObjectAnimator.ofFloat(binding.tvFillCredentials, View.TRANSLATION_X, -130f, 0f)
                .setDuration(500)
        val username =
            ObjectAnimator.ofFloat(binding.usernameEditText, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val tvShowHidePass =
            ObjectAnimator.ofFloat(binding.tvShowHidePass, View.ALPHA, 1f).setDuration(500)
        val buttonSignup =
            ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)
        val descAccount =
            ObjectAnimator.ofFloat(binding.descAccount, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.login, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(appName, descAppName, tvSignup, tvCredential)
        }

        AnimatorSet().apply {
            playSequentially(
                together,
                username, email, password, tvShowHidePass,
                buttonSignup,
                descAccount,
                login
            )
            start()
        }
    }
}