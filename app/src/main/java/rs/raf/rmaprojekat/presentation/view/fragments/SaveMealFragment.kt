package rs.raf.rmaprojekat.presentation.view.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.rmaprojekat.R
import rs.raf.rmaprojekat.data.models.meal.Meal
import rs.raf.rmaprojekat.data.models.saved.SavedMeal
import rs.raf.rmaprojekat.databinding.FragmentSaveMealBinding
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.activities.FragmentContainerListener
import rs.raf.rmaprojekat.presentation.view.activities.LoginActivity.Companion.USER_ID
import rs.raf.rmaprojekat.presentation.view.states.MealsState
import rs.raf.rmaprojekat.presentation.viewmodel.MealViewModel
import rs.raf.rmaprojekat.presentation.viewmodel.SavedMealViewModel
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class SaveMealFragment : Fragment(R.layout.fragment_save_meal) {

    private val mealViewModel: MainContract.MealViewModel by sharedViewModel<MealViewModel>()

    private val savedMealViewModel: MainContract.SavedMealViewModel by sharedViewModel<SavedMealViewModel>()


    private var _binding: FragmentSaveMealBinding? = null

    private val binding get() = _binding!!

    private var datePickerDialog: DatePickerDialog? = null

    private lateinit var meal: Meal

    private lateinit var mealName: String

    private  var selectedDate: Long = 0

    private var selected: String = "Breakfast"

    private val CAMERA_REQUEST_CODE = 111

    private lateinit var preferences: SharedPreferences


    companion object {
        private const val ARG_MEAL= "meal"

        fun newInstance(meal: String): SaveMealFragment {
            val args = Bundle()
            args.putString(ARG_MEAL, meal)
            val fragment = SaveMealFragment()
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
        _binding = FragmentSaveMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = activity!!.getSharedPreferences(activity!!.packageName, AppCompatActivity.MODE_PRIVATE)
        init()
    }

    private fun init() {
        initDatePicker()
        initSpinner()
        initObservers()
    }

    private fun initUI() {
        Glide.with(this)
            .load(meal.strMealThumb)
            .into(binding.mealImageView)
            binding.mealNameTextView.text = meal.strMeal
    }

    private fun initSpinner(){
        val items = listOf("Breakfast", "Lunch", "Dinner", "Snack")
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
                loadSelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected
            }
        }
    }

    private fun initObservers() {
        mealViewModel.mealsState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderState(it)
        })
        mealViewModel.fetchByName(mealName);

        binding.backBtn.setOnClickListener {
            val listener = activity as? FragmentContainerListener
            listener?.backPressed(this)
        }

        var userId = preferences.getLong(USER_ID, 1)

        binding.saveBtn.setOnClickListener {
            savedMealViewModel.add(
                SavedMeal(0, meal.strMeal, meal.strArea, meal.strCategory,
                    meal.strInstructions, meal.strYoutube, meal.ingredients,
                    meal.measures, meal.strMealThumb,
                    selectedDate, selected, userId
                ))

            val alertDialog = AlertDialog.Builder(requireContext())
                .setTitle("Notification")
                .setMessage("Meal saved successfully.")
                .setPositiveButton("Ok") { _, _ ->
                    val listener = activity as? FragmentContainerListener
                    listener?.backPressed(this)
                }
                .create()
            alertDialog.show()
        }

        binding.datePickerButton.setOnClickListener {
            openDatePicker(it)
        }

        binding.mealImageView.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE)
            } else {
                openCamera()
            }
        }
    }

    private fun loadSelected(selectedPosition: Int) {
        val sel = when (selectedPosition) {
            0 -> "Breakfast"
            1 -> "Lunch"
            2 -> "Dinner"
            3 -> "Snack"
            else -> throw IllegalArgumentException("Invalid position")
        }
        selected = sel
    }

    private fun getTodaysDate(): String? {
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        var month = cal[Calendar.MONTH]
        month += 1
        val day = cal[Calendar.DAY_OF_MONTH]

        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1) // Calendar.MONTH is 0-based
        cal.set(Calendar.DAY_OF_MONTH, day)

        selectedDate = cal.timeInMillis

        return makeDateString(day, month, year)
    }

    private fun initDatePicker() {
        val dateSetListener =
            OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month += 1

                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month - 1) // Calendar.MONTH is 0-based
                cal.set(Calendar.DAY_OF_MONTH, day)

                selectedDate = cal.timeInMillis
                val date = makeDateString(day, month, year)
                binding.datePickerButton.text = date
            }
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        val style = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(context!!, style, dateSetListener, year, month, day)
        binding.datePickerButton.text = getTodaysDate()
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String? {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "JAN"
        if (month == 2) return "FEB"
        if (month == 3) return "MAR"
        if (month == 4) return "APR"
        if (month == 5) return "MAY"
        if (month == 6) return "JUN"
        if (month == 7) return "JUL"
        if (month == 8) return "AUG"
        if (month == 9) return "SEP"
        if (month == 10) return "OCT"
        if (month == 11) return "NOV"
        return if (month == 12) "DEC" else "JAN"
    }

    fun openDatePicker(view: View?) {
        datePickerDialog?.show()
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
            is MealsState.Loading -> {
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity?.startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?

            if (imageBitmap != null) {
//                saveImageToExternalStorage(imageBitmap)
                binding.mealImageView.setImageBitmap(imageBitmap)
            }
        }
    }


    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "rs.raf.rmaprojekat.data.models.MyFileProvider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                }
            }
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
//            sendBroadcast(mediaScanIntent)
        }
    }



    private fun saveImageToExternalStorage(bitmap: Bitmap) {
        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val directory = File(root, "MyCameraApp")
        directory.mkdirs()

        val file = File(directory, "image.png")

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            // Image is saved to external storage at this point
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    val MEDIA_TYPE_IMAGE = 1
    val MEDIA_TYPE_VIDEO = 2

    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    private fun getOutputMediaFile(type: Int): File? {
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "MyCameraApp"
        )

        mediaStorageDir.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory")
                    return null
                }
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return when (type) {
            MEDIA_TYPE_IMAGE -> {
                File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
            }
            MEDIA_TYPE_VIDEO -> {
                File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
            }
            else -> null
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}