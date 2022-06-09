package com.bangkit.mountainapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.mountainapp.ui.mainactivity.MainActivity
import com.bangkit.mountainapp.R
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.databinding.ActivityLoginBinding
import com.bangkit.mountainapp.model.ViewModelFactory
import com.bangkit.mountainapp.ui.signup.SignupActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var isPasswordShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun setupAction() {
        binding.signup.setOnClickListener {
            Intent(this@LoginActivity, SignupActivity::class.java).also {
                it.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

        binding.tvShowHidePass.setOnClickListener {
            setShowHidePassword()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.login(email, password)
        }
    }

    private fun setupTextChangedListener() {
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
        viewModel.responseLogin.observe(this) {
            if (it) {
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                Intent(this, MainActivity::class.java).also { intent ->
                    intent.flags = FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.sorry))
                    setMessage(getString(R.string.unable_to_login))
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
        val resultEmail = binding.emailEditText.text
        val resultPass = binding.passwordEditText.text
        binding.btnLogin.isEnabled =
            resultEmail != null && resultEmail.toString()
                .isNotEmpty() && resultEmail.isValidEmail() && resultPass != null && resultPass.toString()
                .isNotEmpty() && resultPass.length >= 6
        binding.btnLogin.text = resources.getString(R.string.login)
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
        val tvLogin =
            ObjectAnimator.ofFloat(binding.tvLogin, View.TRANSLATION_X, -130f, 0f).setDuration(500)
        val tvCredential =
            ObjectAnimator.ofFloat(binding.tvFillCredentials, View.TRANSLATION_X, -130f, 0f)
                .setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val tvShowHidePass =
            ObjectAnimator.ofFloat(binding.tvShowHidePass, View.ALPHA, 1f).setDuration(500)
        val buttonLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val descAccount =
            ObjectAnimator.ofFloat(binding.descAccount, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signup, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(appName, descAppName, tvLogin, tvCredential)
        }

        AnimatorSet().apply {
            playSequentially(
                together,
                email, password, tvShowHidePass,
                buttonLogin,
                descAccount,
                signup
            )
            start()
        }
    }
}