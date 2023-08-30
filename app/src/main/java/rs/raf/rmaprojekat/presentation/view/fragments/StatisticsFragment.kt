package rs.raf.rmaprojekat.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.rmaprojekat.R
import rs.raf.rmaprojekat.data.models.saved.SavedMeal
import rs.raf.rmaprojekat.databinding.FragmentStatisticsBinding
import rs.raf.rmaprojekat.presentation.view.activities.FragmentContainerListener
import rs.raf.rmaprojekat.presentation.view.recycler.adapter.SavedMealAdapter
import rs.raf.rmaprojekat.presentation.view.recycler.diff.SavedMealDiffCallback
import rs.raf.rmaprojekat.presentation.view.states.SavedMealsState
import rs.raf.rmaprojekat.presentation.viewmodel.SavedMealViewModel
import timber.log.Timber
import java.util.*
import java.util.function.Consumer
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import rs.raf.rmaprojekat.presentation.contract.MainContract


class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val savedMealViewModel: MainContract.SavedMealViewModel by sharedViewModel<SavedMealViewModel>()

    private var _binding: FragmentStatisticsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        savedMealViewModel.mealsState.observe(viewLifecycleOwner) {
            Timber.e(it.toString())
            renderState(it)
        }
    }

    private fun initUi(data: List<SavedMeal>) {
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val sevenDaysAgo = calendar.timeInMillis

        val mealCountByDay = data
            .filter { it.date >= sevenDaysAgo && it.date <= today }
            .groupBy { getDayFromDate(it.date) }
            .mapValues { (_, meals) -> meals.size }

        val daysLabels = (1..7).map { index ->
            val date = calendar.apply { timeInMillis = sevenDaysAgo + index * 24 * 60 * 60 * 1000 }
            getDayFromDate(date.timeInMillis)
        }

        val entries = daysLabels.mapIndexed { index, day ->
            val count = mealCountByDay[day] ?: 0
            Entry(index.toFloat(), count.toFloat())
        }

        val dataSet = LineDataSet(entries, "Meals Saved")
        val data = LineData(dataSet)

        val chart = binding.lineChart
        chart.data = data

        val xAxis = chart.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index in daysLabels.indices) daysLabels[index].toString() else ""
            }
        }
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.labelCount = 7

        chart.invalidate()
    }


    fun getDayFromDate(timestamp: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun renderState(state: SavedMealsState) {
        when (state) {
            is SavedMealsState.Success -> {
                initUi(state.saved)
            }
            is SavedMealsState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is SavedMealsState.DataFetched -> {
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is SavedMealsState.Loading -> {
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
