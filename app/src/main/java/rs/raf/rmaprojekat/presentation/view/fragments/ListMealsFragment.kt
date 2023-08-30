package rs.raf.rmaprojekat.presentation.view.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.rmaprojekat.R
import rs.raf.rmaprojekat.databinding.ListMealBinding
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.activities.FragmentContainerListener
import rs.raf.rmaprojekat.presentation.view.activities.MainActivity.Companion.AREA_KEY
import rs.raf.rmaprojekat.presentation.view.activities.MainActivity.Companion.CAT_KEY
import rs.raf.rmaprojekat.presentation.view.activities.MainActivity.Companion.INGRD_KEY
import rs.raf.rmaprojekat.presentation.view.recycler.adapter.MealAdapter
import rs.raf.rmaprojekat.presentation.view.recycler.diff.MealDiffCallback
import rs.raf.rmaprojekat.presentation.view.states.MealsState
import rs.raf.rmaprojekat.presentation.viewmodel.MealViewModel
import timber.log.Timber
import java.util.function.Consumer

class ListMealsFragment : Fragment(R.layout.list_meal) {

    private val mainViewModel: MainContract.MealViewModel by sharedViewModel<MealViewModel>()

    private var _binding: ListMealBinding? = null

    private val binding get() = _binding!!

    var category = ""
    var area = ""
    var ingredient = ""

    private lateinit var preferences: SharedPreferences

    private lateinit var adapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ListMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = activity!!.getSharedPreferences(activity!!.packageName, AppCompatActivity.MODE_PRIVATE)
        init()
    }

    private fun init() {
        initializeData()
        initUi()
        initObservers()
    }

    private fun initializeData() {
        category = preferences.getString(CAT_KEY, "").toString()
        area = preferences.getString(AREA_KEY, "").toString()
        ingredient = preferences.getString(INGRD_KEY, "").toString()
    }

    private fun initUi() {
        initRecycler()
        initListeners()
    }

    private fun initRecycler() {
        binding.listRv.layoutManager = LinearLayoutManager(context)
        adapter = MealAdapter(
            MealDiffCallback(),
            Consumer { meal ->
                val listener = activity as? FragmentContainerListener
                listener?.replaceFragment(MealFragment.newInstance(meal.strMeal))
            }
        )
        binding.listRv.adapter = adapter
    }

    private fun initListeners() {
        binding.inputEt.doAfterTextChanged {
            val filter = it.toString()
            mainViewModel.getByName(filter)
        }

    }

    private fun initObservers() {
        mainViewModel.mealsState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderState(it)
        })

        binding.backBtn.setOnClickListener { v ->
            val listener = activity as? FragmentContainerListener
            listener?.backPressed(this)
        }

        mainViewModel.getAll()

        if (category != "") {
            mainViewModel.fetchByCategory(category)
        } else if (area != "") {
            mainViewModel.fetchByArea(area)
        } else if (ingredient != "") {
            mainViewModel.fetchByIngredient(ingredient)
        }
    }

    private fun renderState(state: MealsState) {
        when (state) {
            is MealsState.Success -> {
                showLoadingState(false)
                adapter.submitList(state.meals)
            }
            is MealsState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealsState.DataFetched -> {
                showLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is MealsState.Loading -> {
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