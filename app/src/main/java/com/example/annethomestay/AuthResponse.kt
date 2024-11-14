import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("nama_depan")
    val nama_depan: String,

    @SerializedName("nama_belakang")
    val nama_belakang: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("phone")
    val phone: String
)


data class LoginRequest(
    val email: String,
    val password: String,
)


data class LogoutResponse(
    val message: String
)

data class AuthResponse(
    val access_token: String,
    val token_type: String,
    val id_user: Int
)

data class UserDetailsResponse(
    val id_user: Int,
    val nama_depan: String,
    val email: String,
    val phone: String
)

