package mydreamappz.dev.android.dashboard.data

import kotlinx.coroutines.flow.Flow
import mydreamappz.dev.android.dashboard.data.model.EntriesApiResponse
import mydreamappz.dev.android.dashboard.data.model.InvoiceApiModel
import mydreamappz.dev.android.dashboard.data.model.JobApiModel
import mydreamappz.dev.android.dashboard.data.remote.ApiDataSource

class DataRepository(
    private val apiDataSource: ApiDataSource
) {

    /**
     * This API returns jobs in realtime using which stats can be computed
     */
    fun observeJobs(): Flow<List<JobApiModel>> {
        return apiDataSource.observeJobs()
    }

    /**
     * This API returns invoices in realtime using which stats can be computed
     */
    fun observeInvoices(): Flow<List<InvoiceApiModel>> {
        return apiDataSource.observeInvoices()
    }

    /**
     * This is actual API returns in realtime data
     */
    suspend fun getApiEntries(): EntriesApiResponse {
        return apiDataSource.getApiEntries()
    }
}
