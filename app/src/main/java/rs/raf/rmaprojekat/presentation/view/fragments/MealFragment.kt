package rs.raf.rmaprojekat.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import rs.raf.rmaprojekat.R
import rs.raf.rmaprojekat.databinding.FragmentMealBinding

import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.rmaprojekat.data.models.meal.Meal
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.activities.FragmentContainerListener
import rs.raf.rmaprojekat.presentation.view.states.MealsState
import rs.raf.rmaprojekat.presentation.viewmodel.MealViewModel
import timber.log.Timber

class MealFragment : Fragment(R.layout.fragment_meal) {

    private val mealViewModel: MainContract.MealViewModel by sharedViewModel<MealViewModel>()

    private var _binding: FragmentMealBinding? = null

    private val binding get() = _binding!!

    private lateinit var meal: Meal

    private lateinit var mealName: String

    companion object {
        private const val ARG_MEAL= "meal"

        fun newInstance(meal: String): MealFragment {
            val args = Bundle()
            args.putString(ARG_MEAL, meal)
            val fragment = MealFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealName = it.getString(ARG_MEAL, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initObservers()
    }

    private fun initUI() {
        Glide.with(this)
            .load(meal.strMealThumb)
            .into(binding.mealImageView)

        binding.mealNameTextView.text = meal.strMeal
        binding.categoryTextView.text = "Category: ${meal.strCategory}"
        binding.areaTextView.text = "Area: ${meal.strArea}"
        binding.tagsTextView.text = "Tags: ${meal.tags}"
        binding.youtubeLinkTextView.text = meal.strYoutube
        binding.instructionsTextView.text = meal.strInstructions

        val ingredients = meal.ingredients
        val measures = meal.measures

        val combinedIngredientsAndMeasures = mutableListOf<String>()

        for (i in 0 until minOf(ingredients.size, measures.size)) {
            val ingredient = ingredients[i]
            val measure = measures[i]
            val combined = "$ingredient - $measure"
            combinedIngredientsAndMeasures.add(combined)
        }

        val combinedText = combinedIngredientsAndMeasures.joinToString("\n")
        binding.ingredientsAndMeasuresTextView.text = combinedText


    }

    private fun initObservers() {
        mealViewModel.mealsState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderState(it)
        })

        mealViewModel.fetchByName(mealName)

        binding.backBtn.setOnClickListener { v ->
            val listener = activity as? FragmentContainerListener
            listener?.backPressed(this)
        }

        binding.saveBtn.setOnClickListener { v ->
            val listener = activity as? FragmentContainerListener
            listener?.replaceFragment(SaveMealFragment.newInstance(mealName))
        }
    }



    private fun renderState(state: MealsState) {
        when (state) {
            is MealsState.Success -> {
                meal = state.meals.firstOrNull() ?: Meal("","","",
                    "","","", "", emptyList(), emptyList(),"")
                initUI()
            }
            is MealsState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealsState.DataFetched -> {
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}