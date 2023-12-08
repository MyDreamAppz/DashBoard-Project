package mydreamappz.dev.android.dashboard.data.remote

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mydreamappz.dev.android.dashboard.data.model.EntriesApiResponse
import mydreamappz.dev.android.dashboard.data.model.InvoiceApiModel
import mydreamappz.dev.android.dashboard.data.model.JobApiModel
import kotlin.time.Duration.Companion.seconds

class ApiDataSource(private val apiService: ApiService) {

    fun observeJobs(): Flow<List<JobApiModel>> {
        return flow {
            while (true) {
                emit(SampleData.generateRandomJobList(20))
                delay(30.seconds.inWholeMilliseconds)
            }
        }
    }

    fun observeInvoices(): Flow<List<InvoiceApiModel>> {
        return flow {
            while (true) {
                emit(SampleData.generateRandomInvoiceList(20))
                delay(30.seconds.inWholeMilliseconds)
            }
        }
    }

    suspend fun getApiEntries(): EntriesApiResponse {
        return apiService.getApiEntries()
    }
}
