data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String,
)


data class LogoutResponse(
    val message: String
)

data class AuthResponse(
    val id: Int,
    val email: String,
    val telepon: String,
    val username: String,
    val message: String
)


data class TransaksiResponse(
    val success: Boolean,
    val transactionId: Int?,
    val message: String?
)

data class TransaksiPenginapan(
    val id_p: Int,
    val id_mtd: Long,
    val user_id: Int,
    val tgl_in: String,
    val tgl_out: String,
    val durasi_trans_p: Int,
    val stat_trans_p: String,
    val total_bayar: Int,
)

data class TransaksiResponsePenginapan(
    val success: Boolean,
    val message: String,
    val data: TransaksiPenginapan
)
