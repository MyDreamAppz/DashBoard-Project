package mydreamappz.dev.android.dashboard.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mydreamappz.dev.android.dashboard.R
import mydreamappz.dev.android.dashboard.data.DataRepository
import mydreamappz.dev.android.dashboard.data.model.ApiEntry
import mydreamappz.dev.android.dashboard.data.model.EntriesApiResponse
import mydreamappz.dev.android.dashboard.data.model.InvoiceApiModel
import mydreamappz.dev.android.dashboard.data.model.InvoiceStatus
import mydreamappz.dev.android.dashboard.data.model.JobApiModel
import mydreamappz.dev.android.dashboard.data.model.JobStatus
import mydreamappz.dev.android.dashboard.data.remote.ApiClient
import mydreamappz.dev.android.dashboard.data.remote.ApiDataSource
import mydreamappz.dev.android.dashboard.data.remote.SampleData
import mydreamappz.dev.android.dashboard.ui.theme.AppTheme
import mydreamappz.dev.android.dashboard.ui.theme.Blue
import mydreamappz.dev.android.dashboard.ui.theme.Red
import mydreamappz.dev.android.dashboard.ui.theme.Green
import mydreamappz.dev.android.dashboard.ui.theme.PurpleGrey40
import mydreamappz.dev.android.dashboard.ui.theme.Yellow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import mydreamappz.dev.android.dashboard.ui.theme.Purple40
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)

    // Create an instance of ApiDataSource
    private val apiDataSource = ApiDataSource(ApiClient.apiService)

    // Create an instance of DataRepository
    private val dataRepository = DataRepository(apiDataSource)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val navController = rememberNavController()

                // Collect the jobs as a State
                val jobsData by dataRepository.observeJobs().collectAsState(initial = emptyList())
                // Collect the Invoices as a State
                val invoicesData by dataRepository.observeInvoices()
                    .collectAsState(initial = emptyList())

                // Create a mutable state to hold the API entries
                var entriesData by remember { mutableStateOf(EntriesApiResponse(0, emptyList())) }

                // Call the function to fetch API entries when the activity is created
                LaunchedEffect(Unit) {
                    // Inside a LaunchedEffect, you can call suspend functions
                    val apiResponse = dataRepository.getApiEntries()
                    // Update the mutable state with the obtained data
                    entriesData = EntriesApiResponse(apiResponse.count, apiResponse.entries)

                }

                NavHost(navController = navController, startDestination = "DashBoard") {
                    composable("DashBoard") {
                        DashBoardScreen(navController, jobsData, invoicesData)
                    }
                    composable("JobsStats") {
                        JobsStatsExpandScreen(navController, jobsData, entriesData.entries)
                    }

                }
            }
        }
    }
}

//Displays DashBoard Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    navController: NavController, jobs: List<JobApiModel>, invoicesData: List<InvoiceApiModel>
) {

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
    ) {
        TopAppBar(title = { Text(text = "DashBoard") })

        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

        //Greeting Customer view
        ProfileCard()

        Spacer(modifier = Modifier.height(16.dp))

        //Creates Job Stats View
        JobStatsCard(navController, jobs = jobs)

        Spacer(modifier = Modifier.height(16.dp))

        //Creates Invoice Stats view
        InvoiceStatsCard(invoicesData)
    }
}

//Displays Job Stats Detailed Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsStatsExpandScreen(
    navController: NavController, jobs: List<JobApiModel>, entriesApiData: List<ApiEntry>
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(title = { Text(text = "Jobs (${jobs.size})") },

            navigationIcon = {
                IconButton(onClick = { navController.navigate("DashBoard") }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

        //Show Jobs stats chart in Jobs expanded screen
        TotalJobsAndCompletedJobsCount(jobs)

        //Show jobs stats chart in Jobs expanded screen
        JobStatusChart(jobs)

        Spacer(modifier = Modifier.height(16.dp))

        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

        //Creates Job Stats Tab view
        JobDetailsScreen(jobs, entriesApiData)
    }
}

//DashBoard Customer Card view
@Composable
fun ProfileCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(0.5.dp, Color.LightGray, shape = RoundedCornerShape(4.dp)),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onTertiary,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .wrapContentWidth(Alignment.Start)

            ) {
                // Greet Customer
                Text(
                    text = "Hello, ${getCustomerName()}! \uD83D\uDC4B ",
                    style = typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                // Display Current Date
                Text(
                    text = "${getCurrentDate()}",
                    style = typography.bodySmall,
                    fontWeight = FontWeight.Normal
                )
            }

            // Customer Profile Picture
            Image(
                painter = painterResource(id = R.drawable.android_logo_background),
                contentDescription = "Profile Picture",
                alignment = Alignment.CenterEnd,
                modifier = Modifier
                    .size(50.dp)
                    .weight(0.5f)
                    .wrapContentWidth(Alignment.End)
            )
        }
    }
}

//DashBoard Job Stats Card view
@Composable
fun JobStatsCard(navController: NavController, jobs: List<JobApiModel>) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(0.5.dp, Color.LightGray, shape = RoundedCornerShape(4.dp))
            .clickable { navController.navigate("JobsStats") },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onTertiary,
        ),
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Job Stats",
                style = typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )

            //Create Chart Headers that shows total jobs and completed jobs count
            TotalJobsAndCompletedJobsCount(jobs)

            //Customised Job Stats Chart
            JobStatusChart(jobs)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ColoredBulletText(
                    text = "Yet to start (${getCountOfStatus(jobs, JobStatus.YetToStart)})",
                    bulletColor = Purple40,
                )
                Spacer(modifier = Modifier.width(16.dp))

                ColoredBulletText(
                    text = "In-Progress (${getCountOfStatus(jobs, JobStatus.InProgress)})",
                    bulletColor = Blue,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ColoredBulletText(
                    text = "Cancelled (${getCountOfStatus(jobs, JobStatus.Canceled)})",
                    bulletColor = Yellow,
                )

                Spacer(modifier = Modifier.width(16.dp))

                ColoredBulletText(
                    text = "Completed (${getCountOfStatus(jobs, JobStatus.Completed)})",
                    bulletColor = Green,
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                ColoredBulletText(
                    text = "In-Complete (${getCountOfStatus(jobs, JobStatus.Incomplete)})",
                    bulletColor = Red,
                )
            }
        }
    }
}

//DashBoard Invoice Stats Card View
@Composable
fun InvoiceStatsCard(invoicesData: List<InvoiceApiModel>) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(0.5.dp, Color.LightGray, shape = RoundedCornerShape(4.dp)),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onTertiary,
        ),
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Invoice Stats",
                style = typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            ) {
                Text(
                    text = "Total value ($${getTotalInvoiceValue(invoicesData)})",
                    style = typography.bodySmall,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.Start)
                )

                Text(
                    text = "$${getTotalCollectedAmount(invoicesData)} collected",
                    style = typography.bodySmall,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.End)
                )

            }
            //Invoice Stats Customised Chart
            InvoiceStatsChart(invoicesData)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ColoredBulletText(
                    text = "Draft ($${getTotalAmountForDraft(invoicesData, InvoiceStatus.Draft)})",
                    bulletColor = Yellow,
                )

                Spacer(modifier = Modifier.width(16.dp))

                ColoredBulletText(
                    text = "Pending ($${
                        getTotalAmountForPending(
                            invoicesData, InvoiceStatus.Pending
                        )
                    })",
                    bulletColor = Blue,
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ColoredBulletText(
                    text = "Paid ($${getTotalAmountForPaid(invoicesData, InvoiceStatus.Paid)})",
                    bulletColor = Green,
                )

                Spacer(modifier = Modifier.width(16.dp))

                ColoredBulletText(
                    text = "Bad Debt ($${
                        getTotalAmountForBadDebt(
                            invoicesData, InvoiceStatus.BadDebt
                        )
                    })",
                    bulletColor = Red,
                )

            }

        }
    }
}

//Job Stats expanded Screen
@Composable
fun JobDetailsScreen(jobs: List<JobApiModel>, entriesApiData: List<ApiEntry>) {

    var selectedTabIndex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            }) {
            Tab(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 }) {
                Text(
                    text = "Yet to start (${getCountOfStatus(jobs, JobStatus.YetToStart)})",
                    style = typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Tab(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 }) {
                Text(
                    text = "In-Progress (${getCountOfStatus(jobs, JobStatus.InProgress)})",
                    style = typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Tab(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 }) {
                Text(
                    text = "Cancelled (${getCountOfStatus(jobs, JobStatus.Canceled)})",
                    style = typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        // Tab Content
        when (selectedTabIndex) {
            //Yet to start
            0 -> {
                JobYetToStartList(jobs)
            }
            //In-progress
            // Show real API data in this tab
            1 -> {
                EntriesList(entriesApiData)
            }
            //Cancelled
            2 -> {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally), text = "Loading..."
                )
            }
        }
    }
}

//Jobs Stats expanded Tab(YetToStartList)Screen
@Composable
fun JobYetToStartList(jobs: List<JobApiModel>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .verticalScroll(scrollState)
    ) {
        for (job in jobs) {
            JobYetToStartItem(job = job)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun JobYetToStartItem(job: JobApiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .border(0.5.dp, Color.LightGray, shape = RoundedCornerShape(4.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onTertiary,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "#${job.jobNumber}", style = typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${job.title}", style = typography.bodyMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatDateTimeRange(job.startTime, job.endTime),
                style = typography.bodyMedium
            )

        }
    }
}

@Composable
fun EntriesList(entries: List<ApiEntry>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .verticalScroll(scrollState)
    ) {
        for (entry in entries) {
            ShowEntriesList(entry = entry)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// In-progress tab show real api response a list with 3 values
@Composable
private fun ShowEntriesList(entry: ApiEntry) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .border(0.5.dp, Color.LightGray, shape = RoundedCornerShape(4.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onTertiary,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = entry.Category,
                style = typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = entry.Description,
                style = typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = entry.Link,
                style = typography.bodyMedium,
            )

        }
    }
}


//Create Chart Headers that shows total jobs and completed jobs count
@Composable
fun TotalJobsAndCompletedJobsCount(jobs: List<JobApiModel>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        Text(
            text = "${jobs.size} Jobs",
            style = typography.bodySmall,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start)
        )

        Text(
            text = "${getCompletedJobsCount(jobs)} of ${jobs.size} completed",
            style = typography.bodySmall,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.End)
        )

    }
}

//Customised Jobs Stats Chart
@Composable
fun JobStatusChart(jobs: List<JobApiModel>) {
    val jobStatusCounts = getJobStatusCounts(jobs)

    val totalJobs = jobs.size

    // Sort the job status counts in descending order
    val sortedJobStatusCounts = jobStatusCounts.entries.sortedByDescending { it.value }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        var currentX = 0f

        sortedJobStatusCounts.forEach { (status, count) ->
            val percentage = count.toFloat() / totalJobs.toFloat()
            val barWidth = size.width * percentage

            drawRect(
                color = getJobStatusColor(status),
                size = Size(barWidth, size.height),
                topLeft = Offset(currentX, 0f)
            )

            currentX += barWidth
        }
    }
}

//Customised Invoice Stats Chart
@Composable
fun InvoiceStatsChart(invoices: List<InvoiceApiModel>) {
    val invoiceStatusCounts = getInvoiceStatusCounts(invoices)
    val totalInvoices = invoices.size

    // Sort the invoice status counts in descending order
    val sortedInvoiceStatusCounts = invoiceStatusCounts.entries.sortedByDescending { it.value }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        var currentX = 0f

        sortedInvoiceStatusCounts.forEach { (status, count) ->
            val percentage = count.toFloat() / totalInvoices.toFloat()
            val barWidth = size.width * percentage

            drawRect(
                color = getInvoiceStatusColor(status),
                size = Size(barWidth, size.height),
                topLeft = Offset(currentX, 0f)
            )

            currentX += barWidth
        }
    }
}

//Fetch Random Customer name
fun getCustomerName(): String? {

    val customerList = SampleData.generateRandomInvoiceList(1)

    return customerList.getOrNull(0)?.customerName
}

// Simple Date format
fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("EEEE, MMMM dd yyyy", Locale.getDefault())
    return dateFormat.format(Date())
}

//Format dates to show different start and end dates
fun formatDateTimeRange(startDateTime: String, endDateTime: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, hh:mm a", Locale.ENGLISH)
    val currentDate = LocalDateTime.now()

    try {
        val startZonedDateTime = ZonedDateTime.parse(startDateTime)
        val endZonedDateTime = ZonedDateTime.parse(endDateTime)

        if (startZonedDateTime.toLocalDate() == currentDate.toLocalDate()) {
            // Condition 1: If date is the current date
            return "Today, ${startZonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${
                endZonedDateTime.format(
                    DateTimeFormatter.ofPattern("HH:mm")
                )
            }"
        } else if (startZonedDateTime.toLocalDate() == endZonedDateTime.toLocalDate()) {
            // Condition 2: If start and end date are the same
            return "${startZonedDateTime.format(formatter)} - ${
                endZonedDateTime.format(
                    DateTimeFormatter.ofPattern("HH:mm")
                )
            }"
        } else {
            // Condition 3: If start and end date are not the same
            return "${startZonedDateTime.format(formatter)} -> ${endZonedDateTime.format(formatter)}"
        }
    } catch (e: DateTimeParseException) {
        // Handle parsing exception
        return "Invalid date format"
    }
}

// To check number of Jobs done from total jobs
fun getCompletedJobsCount(jobs: List<JobApiModel>): Int {
    return jobs.count { it.status == JobStatus.Completed }
}

// Count of jobs with a specific status
fun getCountOfStatus(jobs: List<JobApiModel>, status: JobStatus): Int {
    return jobs.count { it.status == status }
}

// calculate the total invoice value
fun getTotalInvoiceValue(invoices: List<InvoiceApiModel>): Int {
    return invoices.sumOf { it.total }
}

// calculate the total collected amount in Invoice Card
fun getTotalCollectedAmount(invoices: List<InvoiceApiModel>): Int {
    return invoices.sumOf { it.total }
}

// calculate the total amount for a Draft in Invoice Card
fun getTotalAmountForDraft(invoices: List<InvoiceApiModel>, status: InvoiceStatus): Int {
    return invoices.filter { it.status == status }.sumOf { it.total }
}

// calculate the total amount for a Pending in Invoice Card
fun getTotalAmountForPending(invoices: List<InvoiceApiModel>, status: InvoiceStatus): Int {
    return invoices.filter { it.status == status }.sumOf { it.total }
}

// calculate the total amount for a Pending in Invoice Card
fun getTotalAmountForPaid(invoices: List<InvoiceApiModel>, status: InvoiceStatus): Int {
    return invoices.filter { it.status == status }.sumOf { it.total }
}

// calculate the total amount for a Pending in Invoice Card
fun getTotalAmountForBadDebt(invoices: List<InvoiceApiModel>, status: InvoiceStatus): Int {
    return invoices.filter { it.status == status }.sumOf { it.total }
}

// To get counts for each job status
private fun getJobStatusCounts(jobs: List<JobApiModel>): Map<JobStatus, Int> {
    return jobs.groupingBy { it.status }.eachCount()
}

// To get color based on job status
private fun getJobStatusColor(status: JobStatus): Color {
    return when (status) {
        JobStatus.YetToStart -> PurpleGrey40
        JobStatus.InProgress -> Blue
        JobStatus.Canceled -> Yellow
        JobStatus.Completed -> Green
        JobStatus.Incomplete -> Red
    }
}

// To get counts for each invoice status
private fun getInvoiceStatusCounts(invoices: List<InvoiceApiModel>): Map<InvoiceStatus, Int> {
    return invoices.groupingBy { it.status }.eachCount()
}

// To get color based on invoice status
private fun getInvoiceStatusColor(status: InvoiceStatus): Color {
    return when (status) {
        InvoiceStatus.Draft -> Yellow
        InvoiceStatus.Pending -> Blue
        InvoiceStatus.Paid -> Green
        InvoiceStatus.BadDebt -> Red
    }
}

// To display hint text for Charts colors
@Composable
fun ColoredBulletText(text: String, bulletColor: Color) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Colored square shape bullet
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(bulletColor)
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = text,
            style = typography.bodySmall,
        )
    }
}
