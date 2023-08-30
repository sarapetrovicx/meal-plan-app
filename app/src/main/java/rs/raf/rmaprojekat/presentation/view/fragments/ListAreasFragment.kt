package rs.raf.rmaprojekat.presentation.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.rmaprojekat.R
import rs.raf.rmaprojekat.databinding.ListAreaBinding
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.activities.MainActivity
import rs.raf.rmaprojekat.presentation.view.activities.MealsActivity
import rs.raf.rmaprojekat.presentation.view.recycler.adapter.AreaAdapter
import rs.raf.rmaprojekat.presentation.view.recycler.diff.AreaDiffCallback
import rs.raf.rmaprojekat.presentation.view.states.AreasState
import rs.raf.rmaprojekat.presentation.viewmodel.AreaViewModel
import timber.log.Timber
import java.util.function.Consumer

class ListAreasFragment : Fragment(R.layout.list_area) {

    private val areaViewModel: MainContract.AreaViewModel by sharedViewModel<AreaViewModel>()

    private var _binding: ListAreaBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: AreaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ListAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initUi()
        initObservers()
    }

    private fun initUi() {
        initRecycler()
        initListeners()
    }

    private fun initRecycler() {
        binding.listRv.layoutManager = LinearLayoutManager(context)
        adapter = AreaAdapter(
            AreaDiffCallback(),
            Consumer { area ->
                val sharedPreferences =
                    activity!!.getSharedPreferences(activity!!.packageName, Context.MODE_PRIVATE)
                sharedPreferences
                    .edit()
                    .putString(MainActivity.INGRD_KEY, "")
                    .putString(MainActivity.CAT_KEY, "")
                    .putString(MainActivity.AREA_KEY, area.strArea)
                    .apply()
                val intent = Intent(activity, MealsActivity::class.java)
                activity!!.startActivity(intent)
            }
        )
        binding.listRv.adapter = adapter
    }

    private fun initListeners() {
        binding.inputEt.doAfterTextChanged {
            val filter = it.toString()
            areaViewModel.getByName(filter)
        }

        binding.sortButton.setOnClickListener {
            sortAlphabetically()
        }
    }

    private fun initObservers() {
        areaViewModel.areasState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderState(it)
        })
        areaViewModel.getAll()
        areaViewModel.fetchAll()
    }

    private fun renderState(state: AreasState) {
        when (state) {
            is AreasState.Success -> {
                showLoadingState(false)
                adapter.submitList(state.categories)
            }
            is AreasState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is AreasState.DataFetched -> {
                showLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is AreasState.Loading -> {
                showLoadingState(true)
            }
        }
    }

    private fun sortAlphabetically() {
        adapter.currentList.sortedBy { it.strArea }
            .let { sortedList ->
                adapter.submitList(sortedList)
            }
    }

    private fun showLoadingState(loading: Boolean) {
        binding.inputEt.isVisible = !loading
        binding.listRv.isVisible = !loading
        binding.loadingPb.isVisible = loading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}