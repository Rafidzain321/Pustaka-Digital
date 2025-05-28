package com.example.pustaka.basic_api.ui.view

import android.R
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pustaka.basic_api.data.model.Book
import com.example.pustaka.basic_api.data.network.RetrofitInstance
import com.example.pustaka.basic_api.data.repository.BookRepository
import com.example.pustaka.basic_api.ui.adapter.BookAdapter
import com.example.pustaka.basic_api.ui.viewModel.BookViewModel2
import com.example.pustaka.basic_api.utils.Resource
import com.example.pustaka.basic_api.utils.ViewModelFactory
import com.example.pustaka.databinding.BottomSheetLayoutBinding
import com.example.pustaka.databinding.FragmentManageBookBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ManageBookFragment : Fragment() {
    private var _binding: FragmentManageBookBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BookAdapter

    private var selectedImageUri: Uri? = null
    private var selectedPdfUri: Uri? = null

    private lateinit var coverPreview : ImageView
    private lateinit var pdfPreview : TextView


    private lateinit var kategoriSpinner: Spinner
    private lateinit var kategoriList: List<String>

    private val bookViewModel: BookViewModel2 by activityViewModels {
        ViewModelFactory(BookViewModel2::class.java) {
            val repository = BookRepository(RetrofitInstance.getPerpusApi())
            BookViewModel2(repository)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManageBookBinding.inflate(inflater, container, false)

        adapter = BookAdapter(
            mutableListOf(),
            onDeleteClickListener = { book ->  deleteBook(book)}
        )

        binding.adminRecyclerView.adapter = adapter
        binding.adminRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        setupFAB()
        getAllBooks()
        return binding.root
    }

    private fun getAllBooks() {
        bookViewModel.getBooks(requireContext())
        bookViewModel.data.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Empty -> {}
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val books = resource.data?.map { book ->
                        Book(
                            book.id,
                            book.judul,
                            book.penulis,
                            book.kategori,
                            book.tanggal_terbit,
                            book.synopsis,
                            book.file_pdf,
                            book.cover,
                            book.createdAt,
                            book.updatedAt
                        )
                    }
                    if (books != null) {
                        adapter.updateData(books.toMutableList())
                    }
                }
            }
        }
    }

    private fun setupFAB() {
        binding.fabAddBook.setOnClickListener {
            val bottomSheetBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
            showBottomSheetDialog(bottomSheetBinding)
        }

        binding.adminRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                when {
                    dy > 0 && binding.fabAddBook.isShown -> binding.fabAddBook.hide()
                    dy < 0 && !binding.fabAddBook.isShown -> binding.fabAddBook.show()
                }
            }
        })
    }

    private fun showBottomSheetDialog(binding: BottomSheetLayoutBinding) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(binding.root)

        kategoriSpinner = binding.inputKategori
        kategoriList = listOf("Comics", "Education", "Novel")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, kategoriList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kategoriSpinner.adapter = adapter

        coverPreview = binding.previewCover

        binding.inputDate.setOnClickListener {
            showDatePicker(binding)
        }

        // Handle other interactions
        binding.buttonUploadCover.setOnClickListener { openImageChooser() }
        binding.buttonUploadPdf.setOnClickListener { openPdfChooser() }

        binding.saveButton.setOnClickListener {
            val title = binding.inputTitle.text.toString()
            val author = binding.inputAuthor.text.toString()
            val synopsis = binding.inputSynopsis.text.toString()
            val date = binding.inputDate.text.toString()  // Get the selected date
            val kategori = kategoriSpinner.selectedItem.toString()

            if (title.isEmpty() || author.isEmpty() || kategori.isEmpty() || synopsis.isEmpty() || selectedImageUri == null || selectedPdfUri == null) {
                Toast.makeText(requireContext(), "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                val imagePart = prepareImagePart(selectedImageUri!!)
                val pdfPart = preparePdfPart(selectedPdfUri!!)
                createBook(title, author, kategori, date, synopsis, imagePart, pdfPart)
                bottomSheetDialog.dismiss()
            }
        }

        bottomSheetDialog.show()
    }

    private fun showDatePicker(binding: BottomSheetLayoutBinding) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.show(parentFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID")).format(Date(selection))
            val formattedDate = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(Date(selection))
            binding.inputDate.setText(formattedDate)  // Set the selected date in the input field
        }
    }

    private fun openImageChooser() {
        imagePickerLauncher.launch("image/*")
    }

    private fun openPdfChooser() {
        pdfPickerLauncher.launch("application/pdf")
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Picasso.get().load(selectedImageUri).into(coverPreview)
        }
    }

    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedPdfUri = uri
    }

    private fun getFileNameFromUri(uri: Uri?): String {
        var fileName = ""
        val cursor = uri?.let { activity?.contentResolver?.query(it, null, null, null, null) }
        cursor?.use {
            val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            if (it.moveToFirst() && nameIndex != -1) {
                fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }

    private fun prepareImagePart(uri: Uri): MultipartBody.Part {
        val fileName = getFileNameFromUri(uri)
        val file = File(requireContext().cacheDir, fileName)
        activity?.contentResolver?.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("cover", file.name, requestBody)
    }

    private fun preparePdfPart(uri: Uri): MultipartBody.Part {
        val fileName = getFileNameFromUri(uri)
        val file = File(requireContext().cacheDir, fileName)
        activity?.contentResolver?.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file_pdf", file.name, requestBody)
    }

    private fun createBook(title: String, author: String, kategori: String, date: String, synopsis: String, cover: MultipartBody.Part, filePdf: MultipartBody.Part) {
        val titleRequestBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val authorRequestBody = author.toRequestBody("text/plain".toMediaTypeOrNull())
        val kategoriRequestBody = kategori.toRequestBody("text/plain".toMediaTypeOrNull())
        val dateRequestBody = date.toRequestBody("text/plain".toMediaTypeOrNull())
        val synopsisRequestBody = synopsis.toRequestBody("text/plain".toMediaTypeOrNull())

        bookViewModel.createBook(
            titleRequestBody,
            authorRequestBody,
            kategoriRequestBody,
            dateRequestBody,
            synopsisRequestBody,
            cover,
            filePdf,
            requireActivity()
        )

        bookViewModel.createStatus.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Empty -> {}
                is Resource.Loading -> Toast.makeText(requireContext(), "Sedang menambahkan buku...", Toast.LENGTH_SHORT).show()
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Buku berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    getAllBooks()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message ?: "Gagal menambahkan buku.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun deleteBook(book : Book){
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi")
            .setMessage("Hapus ${book.judul} dari menu?")
            .setPositiveButton("Ya, Hapus") { dialog, _ ->
                bookViewModel.deleteBook(requireContext(), book.id)
                bookViewModel.deleteStatus.observe(viewLifecycleOwner) { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                        }

                        is Resource.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "Buku berhasil dihapus!",
                                Toast.LENGTH_SHORT
                            ).show()
                            adapter.removeItem(book)
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Gagal menghapus buku.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is Resource.Empty -> {
                            Log.d("Data buku", "Data Kosong. (${resource.message})")
                        }
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
