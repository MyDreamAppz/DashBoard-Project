package mydreamappz.dev.android.dashboard

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import mydreamappz.dev.android.dashboard.data.model.JobApiModel
import mydreamappz.dev.android.dashboard.data.model.JobStatus
import mydreamappz.dev.android.dashboard.ui.main.JobStatsCard
import mydreamappz.dev.android.dashboard.ui.main.MainActivity
import mydreamappz.dev.android.dashboard.ui.main.ProfileCard
import mydreamappz.dev.android.dashboard.ui.theme.AppTheme

import org.junit.Rule
import org.junit.Test

class MainActivityUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val mockNavController = TestNavHostController(ApplicationProvider.getApplicationContext())

    @Test
    fun appLaunchesSuccessfully() {
        // Check if the main content is displayed
        composeTestRule.onNodeWithText("DashBoard").assertIsDisplayed()
    }

    @Test
    fun profileCard_CheckTextContent() {
        // Launch the ProfileCard composable
        composeTestRule.activity.setContent {
            AppTheme {
                ProfileCard()
            }
        }

        // Verify the text content in the ProfileCard
        composeTestRule.onNodeWithText("Friday, December 08 2023").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Profile Picture").assertExists()
            .assertIsDisplayed()
    }


    @Test
    fun jobStatsCard_displaysHeader() {
        // Launch the JobStatsCard with mock data
        composeTestRule.activity.setContent {
            AppTheme {
                JobStatsCard(navController = mockNavController, jobs = mockJobs)
            }
        }

        // Verify that the header text is displayed
        composeTestRule.onNodeWithText("Job Stats", ignoreCase = true).assertIsDisplayed()
    }

    @Test
    fun jobStatsCard_displaysTotalJobsAndCompletedJobsCount() {
        // Launch the JobStatsCard with mock data
        composeTestRule.activity.setContent {
            AppTheme {
                JobStatsCard(navController = mockNavController, jobs = mockJobs)
            }
        }
        // Verify that the total jobs count is displayed
        composeTestRule.onNodeWithText("mockJobsCount Jobs").assertIsDisplayed()

        // Verify that the completed jobs count is displayed
        composeTestRule.onNodeWithText("mockCompletedJobsCount of mockJobsCount completed").assertIsDisplayed()
    }

    @Test
    fun jobStatsCard_displaysJobStatusChart() {
        // Launch the JobStatsCard with mock data
        composeTestRule.activity.setContent {
            AppTheme {
                JobStatsCard(navController = mockNavController, jobs = mockJobs)
            }
        }

        // Verify that the job status chart is displayed
        composeTestRule.onNodeWithContentDescription("Job Status Chart").assertIsDisplayed()
    }

    @Test
     fun jobStatsCard_displaysJobStatusCounts() {
        // Launch the JobStatsCard with mock data
        composeTestRule.activity.setContent {
            AppTheme {
                JobStatsCard(navController = mockNavController, jobs = mockJobs)
            }
        }

        // Verify that the counts for each job status are displayed
        composeTestRule.onNodeWithText("Yet to start", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("In-Progress", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancelled", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Completed", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("In-Complete", ignoreCase = true).assertIsDisplayed()
    }

    // Mock Jobs list
    private val mockJobs: List<JobApiModel> = listOf(
        JobApiModel(jobNumber = 1, title = "Job 1", startTime = "" , endTime = "", status = JobStatus.YetToStart),
        JobApiModel(jobNumber = 2, title = "Job 2", startTime = "" , endTime = "", status = JobStatus.InProgress),
    )
}