package com.bangkit.mountainapp.ui.feeds

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.mountainapp.databinding.FragmentFeedsBinding
import com.bangkit.mountainapp.ui.FeedUploadActivity
import com.getbase.floatingactionbutton.AddFloatingActionButton

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(FeedViewModel::class.java)

        _binding = FragmentFeedsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val feedUploadButton: AddFloatingActionButton = binding.fabAdd
        feedUploadButton.setOnClickListener{
            val intent = Intent(activity, FeedUploadActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}