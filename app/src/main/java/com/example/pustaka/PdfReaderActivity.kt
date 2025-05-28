package com.example.pustaka

import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.pustaka.databinding.ActivityPdfReaderBinding
import android.graphics.pdf.PdfRenderer
import android.graphics.Bitmap
import java.io.File

class PdfReaderActivity : ComponentActivity() {

    private lateinit var binding: ActivityPdfReaderBinding
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var parcelFileDescriptor: ParcelFileDescriptor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPdfReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil path file PDF dari Intent
        val pdfPath = intent.getStringExtra("PDF_PATH") ?: ""

        if (pdfPath.isEmpty()) {
            Toast.makeText(this, "PDF file not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val pdfFile = File(pdfPath)
        if (!pdfFile.exists()) {
            Toast.makeText(this, "PDF file not found in path: $pdfPath", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        openRenderer(pdfFile)

        // Tampilkan halaman pertama PDF
        if (pdfRenderer?.pageCount ?: 0 > 0) {
            showPage(0)
        }

        // Event listener untuk navigasi halaman
        binding.btnNext.setOnClickListener {
            val nextPageIndex = (currentPage?.index ?: 0) + 1
            if (nextPageIndex < (pdfRenderer?.pageCount ?: 0)) {
                showPage(nextPageIndex)
            } else {
                Toast.makeText(this, "This is the last page", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPrevious.setOnClickListener {
            val prevPageIndex = (currentPage?.index ?: 0) - 1
            if (prevPageIndex >= 0) {
                showPage(prevPageIndex)
            } else {
                Toast.makeText(this, "This is the first page", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openRenderer(file: File) {
        try {
            parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error opening PDF file", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showPage(index: Int) {
        currentPage?.close() // Tutup halaman sebelumnya
        currentPage = pdfRenderer?.openPage(index)

        currentPage?.let { page ->
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            binding.pdfImageView.setImageBitmap(bitmap)

            binding.pageNumberTextView.text = "Page ${index + 1} of ${pdfRenderer?.pageCount}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentPage?.close()
        pdfRenderer?.close()
        parcelFileDescriptor?.close()
    }
}
