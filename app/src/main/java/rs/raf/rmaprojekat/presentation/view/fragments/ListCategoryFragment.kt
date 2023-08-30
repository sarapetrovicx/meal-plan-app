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
import rs.raf.rmaprojekat.databinding.ListCategoriesBinding
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.activities.MainActivity.Companion.AREA_KEY
import rs.raf.rmaprojekat.presentation.view.activities.MainActivity.Companion.CAT_KEY
import rs.raf.rmaprojekat.presentation.view.activities.MainActivity.Companion.INGRD_KEY
import rs.raf.rmaprojekat.presentation.view.activities.MealsActivity
import rs.raf.rmaprojekat.presentation.view.recycler.adapter.CategoryAdapter
import rs.raf.rmaprojekat.presentation.view.recycler.diff.CategoryDiffCallback
import rs.raf.rmaprojekat.presentation.view.states.CategoriesState
import rs.raf.rmaprojekat.presentation.viewmodel.CategoryViewModel
import timber.log.Timber
import java.util.function.Consumer

class ListCategoryFragment : Fragment(R.layout.list_categories) {

    private val categoryViewModel: MainContract.CategoryViewModel by sharedViewModel<CategoryViewModel>()

    private var _binding: ListCategoriesBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: CategoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ListCategoriesBinding.inflate(inflater, container, false)
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
        adapter = CategoryAdapter(
            CategoryDiffCallback(),
            Consumer { category ->
                val sharedPreferences =
                    activity!!.getSharedPreferences(activity!!.packageName, Context.MODE_PRIVATE)
                sharedPreferences
                    .edit()
                    .putString(CAT_KEY, category.strCategory)
                    .putString(AREA_KEY, "")
                    .putString(INGRD_KEY, "")
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
            categoryViewModel.getByName(filter)
        }
        binding.sortButton.setOnClickListener {
            sortAlphabetically()
        }
    }

    private fun initObservers() {
        categoryViewModel.categoriesState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderState(it)
        })
        categoryViewModel.getAll()
        categoryViewModel.fetchAll()
    }

    private fun renderState(state: CategoriesState) {
        when (state) {
            is CategoriesState.Success -> {
                showLoadingState(false)
                adapter.submitList(state.categories)
            }
            is CategoriesState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is CategoriesState.DataFetched -> {
                showLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is CategoriesState.Loading -> {
                showLoadingState(true)
            }
        }
    }

    private fun sortAlphabetically() {
        adapter.currentList.sortedBy { it.strCategory }
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