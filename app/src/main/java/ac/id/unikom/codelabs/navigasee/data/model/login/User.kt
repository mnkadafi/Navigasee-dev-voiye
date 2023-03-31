package ac.id.unikom.codelabs.navigasee.data.model.login

data class User(
        val email: String,
        val nama: String,
        val tipe: String,
        val foto: String,
        val fcm_token: String
)
