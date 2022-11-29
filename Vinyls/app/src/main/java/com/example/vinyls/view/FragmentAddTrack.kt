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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.vinyls.R
import com.example.vinyls.databinding.FragmentAddTrackBinding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.vinyls.models.Track
import com.example.vinyls.viewmodels.TrackCreateViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentAddTrack.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentAddTrack : Fragment() {
    private var _binding: FragmentAddTrackBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TrackCreateViewModel
    private lateinit var track: Track
    private lateinit var strTrack: String
    private val args: FragmentAddTrackArgs by navArgs()

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTrackBinding.inflate(inflater, container, false)
        val view = binding.root
        val tvAlbumTittle : TextView = view.findViewById(R.id.tvAlbumName)

        tvAlbumTittle.text = args.albumName

        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var i:Int=0
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, TrackCreateViewModel.Factory(activity.application)).get(
            TrackCreateViewModel::class.java)
        binding.btnAddTrack.setOnClickListener{
            strTrack="{\n    \"name\": \""+binding.etName.text.toString()+"\",\n    \"duration\": \""+binding.etDuration.text.toString()+"\"\n}"
            lifecycleScope.launch{
                val idTrack=async{ viewModel.createTrackFromNetwork(JSONObject(strTrack), args.albumId) }
                i=idTrack.await()

            }

        }
        viewModel.track.observe(viewLifecycleOwner, /**binding. = this**/
            Observer<Track> {
                it.apply {
                    val action = FragmentAddTrackDirections.actionFragmentAddTrackToFragmentAlbumList()
                    Toast.makeText(activity, "Canción agregada", Toast.LENGTH_LONG).show()
                    view?.findNavController()?.navigate(action)
                }
            })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }
    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}