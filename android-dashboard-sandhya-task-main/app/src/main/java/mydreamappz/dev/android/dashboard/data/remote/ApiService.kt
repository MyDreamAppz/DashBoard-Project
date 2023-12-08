package mydreamappz.dev.android.dashboard.data.remote

import mydreamappz.dev.android.dashboard.data.model.EntriesApiResponse
import retrofit2.http.GET

interface ApiService {
    @GET("entries")
    suspend fun getApiEntries(): EntriesApiResponse
}