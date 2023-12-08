# DashBoardApp

Name of the project:  DashBoardApp
Description : This app shows progress of different jobs and invoices done by customer with boilerplate data
and also I have included extra Api response in In-progress tab to meet Virtusa test requirement.
Programmer: Sandhya

Steps:
1. Requirement gathering
2. Design
3. Coding
4. Testing

**1.	Requirement gathering**
I have made the requirement into an epic and derived some smaller tasks/stories for the same
Epic:
•	Create a DashBoardApp where the app has 2 screens, the dashboard and the jobs screen
•	Need to implement with boilerplate data and real API response.
Stories/Tasks:
1.	Dashboard:
	  Profile Card:
	  	• A simple profile card with a greeting, today's date and a profile picture.
	  * Job stats card:
	  	• Consider "Jobs" as small tasks to be done.
	  	• A job can be in any one of the following statuses : "Yet to Start", "In
		Progress", "Canceled", "Completed", "Incomplete"
	  	• Job stats chart shows a visual representation of the stats
      * Invoice stats card:
		• Invoice is a document which has the cost of the parts and services for a Job
		• An invoice is meant to be paid by the customers once a Job is completed
		• Invoice can be in any of the following statuses : "Draft", "Pending", "Paid", "Bad Debt"
        * Chart :
        •  Dashboard charts are not interactive, but a simple view
        • The chart values are sorted by descending order of the total.
		• Chart built without any libraries
2. Jobs screen:
   • Upon clicking Job stats card in dashboard, the jobs page is shown
   •  Jobs page has the chart at the top, below which jobs are shown in tabs
   by job status
3. Using boilerplate data to display Customer profile, Jobs and Invoice data
4. Upon clicking In-progress added real API services to show actual response in screen.(used this screen as a sample screen)

**2. Design**
	Using designs from the UX screens provided by the interviewer
I have divided the application into 2 screens in Single Activity as follows: Dashboard and Jobs Stats expanded screen
Customer can see Jobs stats and Invoice stats Chart and status details in DashBoard screen, On clicking Jobs stats card, app navigates to
Jobs stats details screen where customer can see a detail list of job number, job title, start and end date. Upon clicking In-progress
user can see real time API ("https://api.publicapis.org/entries") response data
Note: Colors, padding, spaces,font style, font size may differ as I don't have exact details.

**3. Coding**
•	I have used Kotlin, Jetpack Compose programming language for implementing the DashBoardApp (version1)
•	Implemented 2 screens in Single Activity
•	Sample Profile picture image is used
•	I have not used any third party libraries for design purpose

**4. Testing**
	The following unit test cases are validated
1.	Verification of the Dashboard screen
2.	Verification of Jobs Stats and Invoice Stats Chart
3.  Verification of Jobs Stats detail view functionality
4.  Verification of In-progress tab real Api response details

