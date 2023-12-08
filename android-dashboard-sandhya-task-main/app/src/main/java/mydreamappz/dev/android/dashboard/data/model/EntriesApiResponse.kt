package mydreamappz.dev.android.dashboard.data.model

data class EntriesApiResponse(
    val count: Int,
    val entries: List<ApiEntry>
)

data class ApiEntry(
    val API: String,
    val Description: String,
    val Auth: String,
    val HTTPS: Boolean,
    val Cors: String,
    val Link: String,
    val Category: String
)