package com.example.vinyls.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import com.example.vinyls.R
import com.example.vinyls.databinding.FragmentAlbumDetailBinding
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentCollectorDetail.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentCollectorDetail : Fragment() {
    private var _binding: FragmentAlbumDetailBinding? = null

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        val args: FragmentCollectorDetailArgs by navArgs()
        val view = inflater.inflate(R.layout.fragment_collector_detail, container, false);
        val tvName : TextView = view.findViewById(R.id.collector_name)
        val tvTelephone : TextView = view.findViewById(R.id.tvTelephone)
        val tvEmail : TextView = view.findViewById(R.id.tvEmail)

        tvName.text = args.name
        tvTelephone.text = args.telephone
        tvEmail.text = args.email


        return view
    }
}