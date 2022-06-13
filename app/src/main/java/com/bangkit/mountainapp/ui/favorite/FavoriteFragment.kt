package com.bangkit.mountainapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.mountainapp.R
import com.bangkit.mountainapp.data.local.FavoriteData
import com.bangkit.mountainapp.databinding.FragmentFavoriteBinding
import com.bangkit.mountainapp.ui.feeds.ListFeedAdapter
import java.util.*


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    private lateinit var rvFavorite: RecyclerView
    private val list = ArrayList<FavoriteData>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[FavoriteViewModel::class.java]

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rvFavorite = binding.rvUserfoll
        rvFavorite.setHasFixedSize(true)

        list.addAll(listFavorite)
        showRecyclerList()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {


    }

    private val listFavorite: ArrayList<FavoriteData>
        get() {
            val dataName = resources.getStringArray(R.array.mountain_favorite_name)
            val dataDescription = resources.getStringArray(R.array.mountain_favorite_location)
            val dataPhoto = resources.obtainTypedArray(R.array.favorite_photo)
            val listFavorite = ArrayList<FavoriteData>()
            for (i in dataName.indices) {
                val favorit = FavoriteData(dataPhoto.getResourceId(i, -1), dataName[i], dataDescription[i] )
                listFavorite.add(favorit)
            }
            return listFavorite
        }

    private fun showRecyclerList() {
        binding.rvUserfoll.layoutManager = LinearLayoutManager(context)
        val listHeroAdapter = ListFavoriteAdapter(list)
        binding.rvUserfoll.adapter = listHeroAdapter
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}