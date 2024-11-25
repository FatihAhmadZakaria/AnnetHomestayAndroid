package com.example.annethomestay

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.example.annethomestay.databinding.FragmentTransactionBinding
import com.example.annethomestay.utils.SessionManager
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentTransaction : Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var transaksiList: ArrayList<Riwayat>
    private lateinit var adapter: DataListTransaksiAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        transaksiList = ArrayList()
        adapter = DataListTransaksiAdapter(requireContext(), transaksiList, this)

        val listView: ListView = binding.listTransaksi
        listView.adapter = adapter

        // Memanggil API untuk mendapatkan data transaksi
        getTransaksiData(userId.toString())
        UiKitApi.Builder()
            .withMerchantClientKey("SB-Mid-client-rTMPU8glYQjPkdDQ") // Client key dari Midtrans
            .withContext(requireContext()) // Konteks aktivitas
            .withMerchantUrl("https://45e4-2001-448a-4040-9ce4-3029-1922-5aa6-9303.ngrok-free.app/api/midtrans/callback/") // Ganti dengan URL server untuk menangani callback
            .enableLog(true) // Aktifkan log SDK
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // Tema warna
            .build()

        setLocaleNew("en")

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    Toast.makeText(requireContext(), "Transaction ID: ${transactionResult?.transactionId}", Toast.LENGTH_LONG).show() // Tampilkan ID transaksi
                }
            }
        }

    }

    fun onBayarClicked(snapToken: String) {
        startPayment(snapToken) // Metode untuk memulai pembayaran
    }

    fun onBatalkanClicked(idReservasi: Int) {
        showCancelDialog(idReservasi) // Menampilkan dialog pembatalan
    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    private fun getTransaksiData(userId: String) {
        ApiClient.apiService.getRiwayat(userId.toInt()).enqueue(object :
            Callback<RiwayatResponse> {  // Pastikan RiwayatResponse digunakan di sini
            override fun onResponse(call: Call<RiwayatResponse>, response: Response<RiwayatResponse>) {
                if (response.isSuccessful) {
                    val transaksi = response.body()?.data ?: emptyList()
                    transaksiList.clear()
                    transaksiList.addAll(transaksi)
                    adapter.notifyDataSetChanged()
                } else {
                    // Tangani error jika API tidak berhasil
                }
            }

            override fun onFailure(call: Call<RiwayatResponse>, t: Throwable) {
                // Tangani kegagalan API
            }
        })
    }

    fun showCancelDialog(reservasiId: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_batal, null)
        val etAlasan = dialogView.findViewById<EditText>(R.id.etAlasan)

        // Membuat referensi ke AlertDialog agar bisa ditutup nanti
        val dialog = AlertDialog.Builder(context)
            .setTitle("Batalkan Pesanan")
            .setMessage("Apakah Anda yakin ingin membatalkan pesanan?")
            .setView(dialogView)
            .setPositiveButton("Ya", null) // OnClickListener di-handle manual
            .setNegativeButton("Tidak", null)
            .create()

        dialog.show()

        // Manual OnClickListener untuk tombol "Ya"
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val alasan = etAlasan.text.toString()
            if (alasan.isNotBlank()) {
                // Lakukan pembatalan dan tutup dialog setelah berhasil
                cancelReservation(reservasiId, alasan) {
                    dialog.dismiss() // Tutup dialog
                }
            } else {
                Toast.makeText(context, "Alasan harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun cancelReservation(idReservasi: Int, alasan: String, onSuccess: () -> Unit) {
        val requestBatal = RequestBatal(id_reservasi = idReservasi, alasan_pembatalan = alasan)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiService.pembatalan(requestBatal)
                CoroutineScope(Dispatchers.Main).launch {
                    if (response.isSuccessful) {
                        val data = response.body()
                        val jumlahRefund = data?.jumlah_refund ?: 0 // Default ke 0 jika null
                        Toast.makeText(
                            context,
                            "Pesanan berhasil dibatalkan, refund: Rp $jumlahRefund",
                            Toast.LENGTH_LONG
                        ).show()
                        // Refresh data transaksi
                        getTransaksiData(sessionManager.getUserId().toString())
                        onSuccess() // Panggil callback sukses untuk menutup dialog
                    } else {
                        Toast.makeText(
                            context,
                            "Gagal membatalkan pesanan: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun startPayment(snapToken: String) {
        UiKitApi.getDefaultInstance().startPaymentUiFlow(
            requireActivity(), // Gunakan aktivitas yang terkait dengan fragment
            launcher, // ActivityResultLauncher yang sudah Anda buat
            snapToken // Snap Token dari transaksi
        )
    }

}