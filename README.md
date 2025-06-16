# NewsApp-MVVM-Architecture


![Mvvm Architecure](https://github.com/user-attachments/assets/f9d3604a-8c23-40b6-9394-4926c08347ca)

---
### App Screens from Google Pixel 9 Pro

<p float="left">
  <img src="https://github.com/user-attachments/assets/aff1b471-84ae-433d-87b0-c52c07e0753a" width="300" />
  <img src="https://github.com/user-attachments/assets/321d44b0-d4d7-41b2-bfa5-b0fa628cafa7" width="300" /> 
  <img src="https://github.com/user-attachments/assets/4d462fdb-8515-4eb7-9c31-e626e7fa0648" width="300" /> 
  <img src="https://github.com/user-attachments/assets/bfaad642-07cc-441b-8ad2-bdfada432582" width="300" /> 
  <img src="https://github.com/user-attachments/assets/d1779a03-4cf4-4ad8-b209-70b0dd42f10b" width="300" /> 
</p>

---
## App Architecture: Key Components and Data Flow

Description below outlines the key components and data flow within this application's architecture, emphasizing a clear separation of concerns for improved maintainability and testability.

---

### Key Components and Their Roles

1.  **UI Layer (View)**:
    * **Compose Screens** (e.g., `SearchActivity`)
    * **Role**: Responsible for displaying data to the user and capturing all user interactions.

2.  **ViewModel Layer**:
    * **XXXViewModel** (e.e.g `SearchViewModel`)
    * **Role**: Manages UI-related data and state, processes user actions, and communicates with the **Repository Layer**.

3.  **Repository Layer**:
    * **SearchRepository**
    * **Role**: Serves as the single source of truth for data. It decides when and how to  fetch data from the network.

4.  **Network Layer**:
    * **NetworkService**
    * **Role**: Handles all API calls to the external news service.

5.  **External API**:
    * The actual news service API that provides the data consumed by the application.

---

### Data Flow

Understanding the data flow illustrates how different components interact to deliver information to the user:

1.  **User Interaction**: The user interacts with the UI (e.g., enters a search query).
2.  **UI Notification**: The UI notifies the **ViewModel** of the user's action.
3.  **ViewModel Request**: The **ViewModel** requests data from the **Repository**.
4.  **Repository Fetch**: The **Repository** fetches data from the **NetworkService**.
5.  **NetworkService API Call**: The **NetworkService** makes an API call to the **External API** (the news service).
6.  **Data Flow Back**: Data flows back up the chain, potentially undergoing transformations at each layer to fit the needs of the higher layers.
7.  **ViewModel Update**: The **ViewModel** updates its state with the received data.
8.  **UI Update**: The UI observes the **ViewModel's** state changes and updates accordingly to display the new information to the user.

---

### Additional Components

While not explicitly shown in a simple diagram, the following components are crucial for the application's functionality:

* **Dependency Injection (Hilt)**: Utilized for providing dependencies throughout the application, simplifying object creation and management.
* **Kotlin Coroutines and Flow**: Employed for asynchronous programming and reactive streams, enabling efficient handling of long-running operations and data changes.

This architectural approach promotes a clear **separation of concerns**, enhancing the **testability** and **maintainability** of this application. Each layer has a specific responsibility, making it easier to modify or replace components without affecting the entire system.

---

### The Complete Project Folder Structure

You can connect with me on:

- [LinkedIn](https://www.linkedin.com/in/gauravbaja)
- [GitHub](https://github.com/gbajaj)
---
```
.
├── main
│   ├── AndroidManifest.xml
│   ├── java
│   │   └── com
│   │       └── gauravbajaj
│   │           └── newsapp
│   │               ├── MainActivity.kt
│   │               ├── NewsApplication.kt
│   │               ├── data
│   │               │   ├── api
│   │               │   │   └── NetworkService.kt
│   │               │   ├── model
│   │               │   │   ├── Article.kt
│   │               │   │   ├── Country.kt
│   │               │   │   ├── Language.kt
│   │               │   │   ├── Source.kt
│   │               │   │   ├── SourcesResponse.kt
│   │               │   │   └── TopHeadlinesResponse.kt
│   │               │   └── repository
│   │               │       ├── NewsRepository.kt
│   │               │       ├── NewsSourcesRepository.kt
│   │               │       ├── SearchRepository.kt
│   │               │       └── TopHeadLinesRepository.kt
│   │               ├── di
│   │               │   ├── module
│   │               │   │   └── ApplicationModule.kt
│   │               │   └── qualifiers.kt
│   │               ├── ui
│   │               │   ├── base
│   │               │   │   ├── UiSearchState.kt
│   │               │   │   ├── UiState.kt
│   │               │   │   └── ViewModelProviderFactory.kt
│   │               │   ├── country_sources
│   │               │   │   ├── CountrySourcesActivity.kt
│   │               │   │   └── CountrySourcesViewModel.kt
│   │               │   ├── languages
│   │               │   │   ├── LanguagesActivity.kt
│   │               │   │   ├── LanguagesScreen.kt
│   │               │   │   └── LanguagesViewModel.kt
│   │               │   ├── news_sources
│   │               │   │   ├── NewsSourcesActivity.kt
│   │               │   │   ├── NewsSourcesScreen.kt
│   │               │   │   └── NewsSourcesViewModel.kt
│   │               │   ├── newslist
│   │               │   │   ├── NewsListActivity.kt
│   │               │   │   └── NewsListViewModel.kt
│   │               │   ├── search
│   │               │   │   ├── SearchActivity.kt
│   │               │   │   └── SearchViewModel.kt
│   │               │   ├── theme
│   │               │   │   ├── Color.kt
│   │               │   │   ├── Theme.kt
│   │               │   │   └── Type.kt
│   │               │   └── topheadlines
│   │               │       ├── TopHeadlineActivity.kt
│   │               │       ├── TopHeadlineViewModel.kt
│   │               │       └── TopHeadlinesScreen.kt
│   │               └── utils
│   │                   ├── AppConstant.kt
│   │                   ├── CustomTabsHelper.kt
│   │                   └── DateUtils.kt
│   ├── new.xml
│   └── res
│       ├── drawable
│       │   ├── ic_arrow_back.xml
│       │   ├── ic_launcher_background.xml
│       │   ├── ic_launcher_foreground.xml
│       │   └── ic_placeholder.xml
│       ├── font
│       │   ├── poppins_bold.ttf
│       │   ├── poppins_regular.ttf
│       │   └── poppins_semibold.ttf
│       ├── mipmap-anydpi-v26
│       │   ├── ic_launcher.xml
│       │   └── ic_launcher_round.xml
│       ├── mipmap-hdpi
│       │   ├── ic_launcher.webp
│       │   └── ic_launcher_round.webp
│       ├── mipmap-mdpi
│       │   ├── ic_launcher.webp
│       │   └── ic_launcher_round.webp
│       ├── mipmap-xhdpi
│       │   ├── ic_launcher.webp
│       │   └── ic_launcher_round.webp
│       ├── mipmap-xxhdpi
│       │   ├── ic_launcher.webp
│       │   └── ic_launcher_round.webp
│       ├── mipmap-xxxhdpi
│       │   ├── ic_launcher.webp
│       │   └── ic_launcher_round.webp
│       ├── values
│       │   ├── colors.xml
│       │   ├── strings.xml
│       │   └── themes.xml
│       ├── values-night
│       │   └── themes.xml
│       └── xml
│           ├── backup_rules.xml
│           ├── data_extraction_rules.xml
│           └── network_security_config.xml
└── test
    └── java
        └── com
            └── gauravbajaj
                └── newsapp
                    ├── ExampleUnitTest.kt
                    ├── MainCoroutineRule.kt
                    ├── data
                    │   ├── api
                    │   │   └── FakeNetworkService.kt
                    │   └── repository
                    │       ├── NewsRepositoryTest.kt
                    │       ├── NewsSourcesRepositoryTest.kt
                    │       └── TopHeadlineRepositoryTest.kt
                    ├── di
                    └── ui
                        ├── test
                        └── topheadlines
                            └── TopHeadlineViewModelTest.kt

```
---

### TODO

- Add more test cases especially Android instrumentation tests.
- Add Room Database to suppor offline first app.
- Work on the feedback received.
---

### Did this project help you? Great...! Please show your love ❤️ by putting a ⭐ on this project ✌️

---
### License

```
   Copyright (C) 2025 Gaurav Bajaj

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
