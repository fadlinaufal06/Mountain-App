package com.bangkit.mountainapp.ui.feeds

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.mountainapp.R
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.databinding.FragmentFeedsBinding
import com.bangkit.mountainapp.helper.setProfilePicture
import com.bangkit.mountainapp.ui.FeedUploadActivity
import com.bumptech.glide.Glide
import com.getbase.floatingactionbutton.AddFloatingActionButton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedsBinding? = null
    private lateinit var viewModel: FeedViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var preference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[FeedViewModel::class.java]


        _binding = FragmentFeedsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val feedUploadButton: AddFloatingActionButton = binding.fabAdd
        feedUploadButton.setOnClickListener {
            val intent = Intent(activity, FeedUploadActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = UserPreference.getInstance(requireContext().dataStore)

        viewModel.getUser(preference).observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                with(binding) {
                    greeting1.text = getString(R.string.greeting, user.username)
                    val urlRandomAvatar = setProfilePicture(user.username)

                    Glide.with(requireActivity())
                        .load(urlRandomAvatar)
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .error(R.mipmap.ic_launcher_round)
                        .into(profilePic)
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}