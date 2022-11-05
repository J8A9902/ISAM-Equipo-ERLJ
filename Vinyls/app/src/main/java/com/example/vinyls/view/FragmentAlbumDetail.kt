package com.example.vinyls.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.vinyls.R
import com.example.vinyls.databinding.FragmentAlbumDetailBinding
import com.example.vinyls.view.adapters.AlbumsAdapter
import kotlinx.android.synthetic.main.fragment_album_detail.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentAlbumDetail : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


}