# Course Catalog
WGU Android App project representing a student course catalog system

# Functionality
Students can use this app to track the beginning and end dates for the courses they're taking each term, their course mentor for each class, contact their course mentor through the app via SMS or email, set pop-up reminders for beginning and end dates for courses and tests, and take notes for their classes, terms, or mentors.

# Tech Stack
* Programmed in Kotlin using Android Jetpack suite of tools
* Uses SQLite database to track saved term / course / mentor / test information

# Main Features Demonstrated
1. Multi-Threading
* Improved performance
* Running background tasks such as notifications for future times / dates
2. Room persistence library
* Connecting to local SQLite database
* Managing relationship between database objects
* Converting data when writing to / reading from database
3. RecyclerView
* Renders list of objects with scroll bar
* Improved performance when rendering lists by reusing objects
4. Notifications
* Run on background thread
* User is notified when important date / time is coming up
* Notifications are grouped if there is more than one
5. Pop-up messages
6. ContactsContract
* Used to access user's contact list and send SMS / email messages from app
7. ViewModels
8. Intents
