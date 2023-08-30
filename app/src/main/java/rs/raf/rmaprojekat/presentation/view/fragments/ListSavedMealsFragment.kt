package rs.raf.rmaprojekat.presentation.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.rmaprojekat.R
import rs.raf.rmaprojekat.data.models.meal.Meal
import rs.raf.rmaprojekat.data.models.saved.SavedMeal
import rs.raf.rmaprojekat.databinding.ListMealBinding
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.activities.FragmentContainerListener
import rs.raf.rmaprojekat.presentation.view.activities.MealsActivity
import rs.raf.rmaprojekat.presentation.view.recycler.adapter.SavedMealAdapter
import rs.raf.rmaprojekat.presentation.view.recycler.diff.SavedMealDiffCallback
import rs.raf.rmaprojekat.presentation.view.states.SavedMealsState
import rs.raf.rmaprojekat.presentation.viewmodel.MealViewModel
import rs.raf.rmaprojekat.presentation.viewmodel.SavedMealViewModel
import timber.log.Timber
import java.util.*
import java.util.function.Consumer

class ListSavedMealsFragment : Fragment(R.layout.list_saved_meals) {

    private val mainViewModel: MainContract.MealViewModel by sharedViewModel<MealViewModel>()

    private val savedMealViewModel: MainContract.SavedMealViewModel by sharedViewModel<SavedMealViewModel>()

    private var _binding: ListMealBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: SavedMealAdapter

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
        init()
    }

    private fun init() {
        initUi()
        initListeners()
        initObservers()
    }


    private fun initUi() {
        initRecycler()
    }

    private fun initRecycler() {
        binding.listRv.layoutManager = LinearLayoutManager(context)
        adapter = SavedMealAdapter(
            SavedMealDiffCallback(),
            { meal ->
                val listener = activity as? FragmentContainerListener
                listener?.replaceFragment(MealFragment.newInstance(meal.strMeal))
            },
            { meal ->
               showDialog(meal)
            },
            { meal ->
                savedMealViewModel.delete(meal)
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

    private fun showDialog(meal: SavedMeal){
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog, null)

        val datePicker = dialogView.findViewById<DatePicker>(R.id.date_picker)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinner)

        val items = listOf("Breakfast", "Lunch", "Dinner", "Snack")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter



        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Change Date and Meal")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->

                val selectedDate = getDate(datePicker)
                val selectedMeal = spinner.selectedItem.toString()

                savedMealViewModel.update(
                    SavedMeal(meal.idMeal, meal.strMeal, meal.strArea, meal.strCategory,
                    meal.strInstructions, meal.strYoutube, meal.ingredients, meal.measures,
                    meal.strMealThumb, selectedDate, selectedMeal, meal.userId))
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()

    }

    private fun getDate(datePicker: DatePicker): Long {
        val cal = Calendar.getInstance()
        val year = datePicker.year
        var month = datePicker.month
        month += 1
        val day = datePicker.dayOfMonth

        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1)
        cal.set(Calendar.DAY_OF_MONTH, day)
        return cal.timeInMillis
    }

    private fun initObservers() {
        savedMealViewModel.mealsState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderState(it)
        })

        binding.backBtn.setOnClickListener { v ->
            val listener = activity as? FragmentContainerListener
            listener?.backPressed(this)
        }

        savedMealViewModel.getAll()
    }

    private fun renderState(state: SavedMealsState) {
        when (state) {
            is SavedMealsState.Success -> {
                showLoadingState(false)
                adapter.submitList(state.saved)
            }
            is SavedMealsState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is SavedMealsState.DataFetched -> {
                showLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is SavedMealsState.Loading -> {
                showLoadingState(true)
            }
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