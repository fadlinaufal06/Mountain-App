package com.bangkit.mountainapp.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.mountainapp.R
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.databinding.FragmentProfileBinding
import com.bangkit.mountainapp.helper.setProfilePicture
import com.bumptech.glide.Glide

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var viewModel: ProfileViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var preference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clickLogout.setOnClickListener {
            viewModel.logout(UserPreference.getInstance(requireActivity().dataStore))
            Toast.makeText(activity, getString(R.string.logout_successful), Toast.LENGTH_SHORT)
                .show()
        }

        binding.clickYourAccount.setOnClickListener {
            Toast.makeText(activity, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
        }

        binding.clickLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        preference = UserPreference.getInstance(requireContext().dataStore)

        viewModel.getUser(preference).observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                with(binding) {
                    tvItemUsername.text = user.username
                    emailUser.text = user.email
                    val urlRandomAvatar = setProfilePicture(user.username)

                    Glide.with(requireActivity())
                        .load(urlRandomAvatar)
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .error(R.mipmap.ic_launcher_round)
                        .into(imgItemPhoto)
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}