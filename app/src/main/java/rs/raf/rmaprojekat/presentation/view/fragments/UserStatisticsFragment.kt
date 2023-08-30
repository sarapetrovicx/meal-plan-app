package rs.raf.rmaprojekat.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.rmaprojekat.R
import rs.raf.rmaprojekat.data.models.saved.AreaCount
import rs.raf.rmaprojekat.data.models.saved.CategoryCount
import rs.raf.rmaprojekat.data.models.saved.DisplayableItem
import rs.raf.rmaprojekat.data.models.saved.MealCount
import rs.raf.rmaprojekat.databinding.FragmentUserStatisticsBinding
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.activities.LoginActivity.Companion.USERNAME
import rs.raf.rmaprojekat.presentation.view.recycler.adapter.StatAdapter
import rs.raf.rmaprojekat.presentation.viewmodel.SavedMealViewModel
import timber.log.Timber


class UserStatisticsFragment : Fragment(R.layout.fragment_user_statistics) {

    private val savedMealViewModel: MainContract.SavedMealViewModel by sharedViewModel<SavedMealViewModel>()

    private var _binding: FragmentUserStatisticsBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: StatAdapter<DisplayableItem>

    private var pickedNumber: Int = 0

    private var pickedPos: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initObservers() {
        savedMealViewModel.top.observe(viewLifecycleOwner) { topCategories ->
            if (topCategories != null) {
                adapter.submitList(topCategories)
            }
        }
        savedMealViewModel.topMeals.observe(viewLifecycleOwner) { top ->
            if (top != null) {
                adapter.submitList(top)
            }
        }
        savedMealViewModel.topAreas.observe(viewLifecycleOwner) { top ->
            if (top != null) {
                adapter.submitList(top)
            }
        }
        savedMealViewModel.mealsState.observe(viewLifecycleOwner) {
            Timber.e(it.toString())
        }

    }

    private fun initUi() {
        initSpinner()
        initNumberPicker()
        initUsername()
        initRecyclerView()
    }

    private fun initRecyclerView() {

        val diffCallback = object : DiffUtil.ItemCallback<DisplayableItem>() {
            override fun areItemsTheSame(oldItem: DisplayableItem, newItem: DisplayableItem): Boolean {
                return oldItem.getDisplayText() == newItem.getDisplayText()
            }

            override fun areContentsTheSame(oldItem: DisplayableItem, newItem: DisplayableItem): Boolean {
                return oldItem.getDisplayText() == newItem.getDisplayText()
            }
        }

        adapter = StatAdapter(diffCallback)
        binding.listRv.layoutManager = LinearLayoutManager(requireContext())
        binding.listRv.adapter = adapter
    }


    private fun initUsername(){
        val sharedPreferences = activity!!.getSharedPreferences(activity!!.packageName, AppCompatActivity.MODE_PRIVATE)
        var username = sharedPreferences.getString(USERNAME, "")?.uppercase()
        binding.username.text = "User: $username"
        binding.top.text = "Top"
    }


    private fun initSpinner(){
        val items = listOf("Categories", "Meals", "Areas")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                pickedPos = position
                loadSelectedFragment(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected
            }
        }
    }

    private fun initNumberPicker() {
        binding.numberPicker.minValue = 0
        binding.numberPicker.maxValue = 10
        binding.numberPicker.value = 0

        binding.numberPicker.setOnValueChangedListener { _, _, newVal ->
            pickedNumber = newVal
            loadSelectedFragment(pickedPos)
        }
    }


    private fun loadSelectedFragment(selectedPosition: Int) {
        when (selectedPosition) {
            0 -> savedMealViewModel.getTopCategories(pickedNumber)
            1 -> savedMealViewModel.getTopMeals(pickedNumber)
            2 -> savedMealViewModel.getTopAreas(pickedNumber)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
