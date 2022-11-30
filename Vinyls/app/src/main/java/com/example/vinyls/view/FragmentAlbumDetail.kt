package com.example.vinyls.view

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls.R
import com.example.vinyls.databinding.FragmentAlbumDetailBinding
import com.example.vinyls.models.Collector
import com.example.vinyls.models.Track
import com.example.vinyls.view.adapters.CollectorsAdapter
import com.example.vinyls.view.adapters.TracksAdapter
import com.example.vinyls.viewmodels.CollectorListViewModel
import com.example.vinyls.viewmodels.TrackListViewModel
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@Suppress("DEPRECATION")
class FragmentAlbumDetail : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: TrackListViewModel
    private var viewModelAdapter: TracksAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        val args: FragmentAlbumDetailArgs by navArgs()
        val view = binding.root
        val tvName : TextView = view.findViewById(R.id.tvName)
        val tvGenre : TextView = view.findViewById(R.id.tvGenre)
        val ivCover : ImageView = view.findViewById(R.id.ivCover)
        val tvFecha : TextView = view.findViewById(R.id.tvFecha)
        val tvDescription : TextView = view.findViewById(R.id.tvDescription)
        val ivBotonAgregarTrack : ImageView = view.findViewById(R.id.ivBotonAgregarTrack)

        ivBotonAgregarTrack.setOnClickListener{
            val action = FragmentAlbumDetailDirections.actionFragmentAlbumDetailToFragmentAddTrack(
                tvName.text.toString(),
                args.id
            )
            // Navigate using that action
            view.findNavController().navigate(action)
        }


        tvName.text = args.name
        tvGenre.text = args.genero
        tvFecha.text = args.fecha
        tvDescription.text = args.description
        val imgUrl : String? = args.cover
        Picasso.get().load(imgUrl).into(ivCover);
        viewModelAdapter = TracksAdapter()
        return view


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.rvTracks
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args: FragmentAlbumDetailArgs by navArgs()
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, TrackListViewModel.Factory(activity.application, args.id)).get(TrackListViewModel::class.java)
        viewModel.tracks.observe(viewLifecycleOwner, Observer<List<Track>> {
            it.apply {
                viewModelAdapter!!.tracks = this
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    fun showDialog() {
    }


}