package com.example.vinyls.view

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.vinyls.R
import com.example.vinyls.databinding.FragmentAlbumCreateBinding
import com.example.vinyls.models.Album
import com.example.vinyls.viewmodels.AlbumCreateViewModel
import org.json.JSONObject

import androidx.lifecycle.*
import androidx.test.core.app.ActivityScenario.launch
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentAlbumCreate.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentAlbumCreate : Fragment() {
    private var _binding: FragmentAlbumCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AlbumCreateViewModel
    private lateinit var album: Album
    private lateinit var strAlbum: String

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumCreateBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var i:Int=0
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = "Crear Album"
        viewModel = ViewModelProvider(this, AlbumCreateViewModel.Factory(activity.application)).get(
            AlbumCreateViewModel::class.java)
        binding.btnCrearAlbumRed.setOnClickListener{
            /** strAlbum="{\n    \"name\": \"El Cocinero Mayor\",\n    \"cover\": \"https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg\",\n    \"releaseDate\": \"1984-08-01T00:00:00-05:00\",\n    \"description\": \"Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983.\",\n    \"genre\": \"Salsa\",\n    \"recordLabel\": \"Elektra\"\n}"  **/
            strAlbum="{\n    \"name\": \""+binding.txtEditNombreAlbum.text.toString()+"\",\n    \"cover\": \""+binding.txtEditCoverAlbum.text.toString()+"\",\n    \"releaseDate\": \""+binding.txtEditFechaAlbum.text.toString()+"\",\n    \"description\": \""+binding.txtEditDescAlbum.text.toString()+"\",\n    \"genre\": \""+binding.txtEditGeneroAlbum.text.toString()+"\",\n    \"recordLabel\": \""+binding.txtEditRecordAlbum.text.toString()+"\"\n}"
            lifecycleScope.launch{
                val idAlbum=async{ viewModel.createAlbumFromNetwork(JSONObject(strAlbum)) }
                i=idAlbum.await()
            }
            /** val action = FragmentAlbumCreateDirections.actionFragmentAlbumCreateToFragmentAlbumDetail(binding.txtEditNombreAlbum.text.toString(), binding.txtEditGeneroAlbum.text.toString(), binding.txtEditCoverAlbum.text.toString(), binding.txtEditFechaAlbum.text.toString(), binding.txtEditDescAlbum.text.toString())
            view?.findNavController()?.navigate(action) **/
        }
        viewModel.album.observe(viewLifecycleOwner, /**binding. = this**/
        Observer<Album> {
            it.apply {
                val action = FragmentAlbumCreateDirections.actionFragmentAlbumCreateToFragmentAlbumDetail(it.name, it.genre, it.cover, it.releaseDate, it.description)
                view?.findNavController()?.navigate(action)
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentAlbumCreate.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentAlbumCreate().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}