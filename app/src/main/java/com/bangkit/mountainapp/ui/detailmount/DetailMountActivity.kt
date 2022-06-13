package com.bangkit.mountainapp.ui.detailmount

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bangkit.mountainapp.R
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.databinding.ActivityDetailMountBinding
import com.bangkit.mountainapp.model.ViewModelFactory
import com.bumptech.glide.Glide

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailMountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMountBinding
    private lateinit var viewModel: DetailMountViewModel
    private lateinit var mountPhoto: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMountBinding.inflate(layoutInflater)
        setupView()
        setContentView(binding.root)

        setupViewModel()
        setupDataDetail()
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DetailMountViewModel::class.java]
    }

    private fun setupDataDetail() {
        val name = intent.getStringExtra(EXTRA_NAME)

        when {
            name.equals("Bromo") -> {
                viewModel.getMount(2)
                mountPhoto = ContextCompat.getDrawable(this, R.drawable.bromo)!!
            }
            name.equals("Ijen") -> {
                viewModel.getMount(3)
                mountPhoto = ContextCompat.getDrawable(this, R.drawable.ijen)!!
            }
            name.equals("Papandayan") -> {
                viewModel.getMount(4)
                mountPhoto = ContextCompat.getDrawable(this, R.drawable.papandayan)!!
            }
            name.equals("Kerinci") -> {
                viewModel.getMount(5)
                mountPhoto = ContextCompat.getDrawable(this, R.drawable.kerinci)!!
            }
            name.equals("Tangkuban Perahu") -> {
                viewModel.getMount(1)
                mountPhoto = ContextCompat.getDrawable(this, R.drawable.tangkuban)!!
            }
        }
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        viewModel.responseMount.observe(this) {
            binding.apply {
                tvItemName.text = getString(R.string.gunung_1s, it.mountainName)
                tvLoc1.text = it.location
                rating.rating = it.stars.toFloat()
                tvElevation.text = it.elevation
                tvHistoryDetail.text = it.history
                Glide.with(this@DetailMountActivity)
                    .load(mountPhoto)
                    .placeholder(circularProgressDrawable)
                    .error(R.mipmap.ic_launcher_round)
                    .into(imgStoryDetail)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
    }
}