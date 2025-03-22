📌 Setup & Run Instructions
🔧 Prerequisites
Android Studio (Latest version)
Java 8 or higher
Kotlin
Firebase account

🚀 Steps to Run the Project
Clone the repository:
git clone <paste url here>
cd yourProject
Open Android Studio and select Open an Existing Project.
Connect your project to Firebase:
Go to Tools → Firebase in Android Studio.
Add dependencies
Add Google Services JSON file (google-services.json) inside the app/ directory.
Run the project on an Emulator or Physical Device.

📚 Third-Party Libraries
This project uses the following libraries:
Library	
Room: Database	Local storage for tasks
LiveData & ViewModel:	Manages UI-related data
Retrofit	:API calls
Firebase Analytics	:User event tracking
Firebase Crashlytics:	Error reporting
Coroutines:	Background threading
check gradle file for dependenciew and plugins

🛠 Design Decisions
MVVM Architecture:
Separation of concerns using ViewModel, LiveData, and Repository pattern.
Offline Support:
Tasks from API are fetched once and stored in Room DB.
Local changes persist without being overridden by API updates.
Error Handling:
Crash scenarios are logged in Firebase Crashlytics.
Network failures are handled using try-catch.
Analytics Integration:
Firebase logs user interactions to analyze app usage.

📢 Future Improvements
Implement WorkManager to schedule background data sync.
Add Unit Tests for ViewModel and Repository.
Improve UI/UX with animations.

🔥 Crash Reporting
🖼️ Screenshots of Firebase Crashlytics Console
![Screenshot 2025-03-22 080010](https://github.com/user-attachments/assets/68824003-d8fc-4fe2-ab93-1b322b556251)

📊 Firebase Analytics Events
![Screenshot 2025-03-22 081028](https://github.com/user-attachments/assets/5cc2e02f-9143-45a1-94b0-2bfd37c8a7e2)


📱 Mobile Screen Recording of Crash


https://github.com/user-attachments/assets/2ad1dc97-8e43-4585-9223-1df94e519490

Chech crash :After click of warning icon 




