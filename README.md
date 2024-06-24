<h1 align="center">Gallery-Compose</h1>

<p align="center">  
🗡️ Gallery-Compose is a gallery app built using MediaStore APIs, demonstrating modern Android development with Jetpack Compose, Hilt, Coroutines, and Flow based on MVVM architecture.
</p>

## Technology Stack

* **Kotlin:** The preferred language for Android development, offering conciseness, safety, and interoperability with Java.
* **Jetpack Compose:** A modern toolkit for building native UIs in a declarative manner.
* **Jetpack Libraries:** A suite of libraries that provide essential building blocks for Android apps, including:
    * **ViewModel:** Manages UI-related data in a lifecycle-conscious way.
    * **Room:** Provides an abstraction layer over SQLite for persistent data storage.
    * **Navigation:** Simplifies in-app navigation.
    * **Hilt:** A dependency injection library for Android.

## Packaging Structure

* **common:** Contains constant, enum, etc. files used throughout the project.
* **data:** It contains the repository implementation and all other files that contain the business logic to fetch the Image/Video from the device.
* **domain:** This package contains model classes that are used at the UI layer and repository interfaces.
* **ui:** All UI-related code is in this package. it contains activity, composables, viewmodels etc.
* **di:** This package contains hilt modules.
* **util:** This package contains utility functions.
* **core:** This package contains an abstract implementation of the pagination class that is being used in MediaViewModel. 

