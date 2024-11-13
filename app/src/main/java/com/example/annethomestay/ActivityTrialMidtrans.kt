package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.annethomestay.databinding.ActivityTrialMidtransBinding
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityTrialMidtrans : AppCompatActivity() {

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityTrialMidtransBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrialMidtransBinding.inflate(layoutInflater)
        enableEdgeToEdge() // Pastikan ini sesuai dengan kebutuhan
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        UiKitApi.Builder()
            .withMerchantClientKey("SB-Mid-client-rTMPU8glYQjPkdDQ") // Client key dari Midtrans
            .withContext(this) // Konteks aktivitas
            .withMerchantUrl("http://192.168.100.100:8000/api/payments/callback/") // Ganti dengan URL server untuk menangani callback
            .enableLog(true) // Aktifkan log SDK
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // Tema warna
            .build()

        setLocaleNew("en") // Mengatur bahasa SDK

        // Menyiapkan launcher untuk menerima hasil dari UI pembayaran
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    Toast.makeText(this, "Transaction ID: ${transactionResult?.transactionId}", Toast.LENGTH_LONG).show() // Tampilkan ID transaksi
                }
            }
        }

        binding.buttonPay.setOnClickListener {
            // Panggil fungsi untuk membuat transaksi
            createTransaction()
        }

        getImageFromApi("01JCGN5TSGNYMPAKSFE9WK04YP.jpg")

        // Sisa kode yang ada
        enableEdgeToEdge()
    }


    // Fungsi untuk mengatur bahasa SDK
    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    // Fungsi untuk membuat transaksi
    private fun createTransaction() {
        val transactionRequest = TransactionRequest(
            total_price = 390000,
            first_name = "Royyan",
            last_name = "Mustova",
            email = "rmustova@gmail.com",
            phone = "085156763911",
            payment_type = "credit_card" // Ganti sesuai dengan jenis pembayaran yang dipilih
        )

        ApiClient.apiService.createTransaction(transactionRequest).enqueue(object : Callback<MidtransResponse> {
            override fun onResponse(call: Call<MidtransResponse>, response: Response<MidtransResponse>) {
                if (response.isSuccessful) {
                    val snapToken = response.body()?.token
                    if (snapToken != null) {
                        startPayment(snapToken) // Mulai proses pembayaran
                    } else {
                        Toast.makeText(this@ActivityTrialMidtrans, "Failed to get Snap Token", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@ActivityTrialMidtrans, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<MidtransResponse>, t: Throwable) {
                Toast.makeText(this@ActivityTrialMidtrans, "Failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Fungsi untuk memulai proses pembayaran
    private fun startPayment(snapToken: String) {
        UiKitApi.getDefaultInstance().startPaymentUiFlow(
            this@ActivityTrialMidtrans, // Aktivitas
            launcher, // ActivityResultLauncher
            snapToken // Snap Token dari Midtrans
        )
    }

    // Fungsi untuk memanggil API dan menampilkan gambar
    private fun getImageFromApi(filename: String) {
        // URL untuk gambar berdasarkan API
        val imageUrl = "http://192.168.100.100:8000/api/images/$filename"

        // Menggunakan Glide untuk memuat gambar
        Glide.with(this)
            .load(imageUrl) // URL gambar
            .into(binding.imageView) // Tempatkan gambar di ImageView
    }
}
