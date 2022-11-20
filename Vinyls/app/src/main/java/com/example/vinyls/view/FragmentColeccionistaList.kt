package com.example.vinyls.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls.R
import com.example.vinyls.databinding.FragmentColeccionistaListBinding
import com.example.vinyls.models.Collector
import com.example.vinyls.view.adapters.CollectorsAdapter
import com.example.vinyls.viewmodels.CollectorListViewModel
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentColeccionistaList.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentColeccionistaList : Fragment() {
    private var _binding: FragmentColeccionistaListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorListViewModel
    private var viewModelAdapter: CollectorsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentColeccionistaListBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = CollectorsAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.collectorRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, CollectorListViewModel.Factory(activity.application)).get(
            CollectorListViewModel::class.java)
        viewModel.collectors.observe(viewLifecycleOwner, Observer<List<Collector>> {
            it.apply {
                viewModelAdapter!!.collector = this.sortedBy { it.name }

                if(this.isEmpty()) showDialog()
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
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("No hay coleccionistas disponibles")
                .setTitle("Error")
            builder.create()
        }

        alertDialog?.show()
    }
}