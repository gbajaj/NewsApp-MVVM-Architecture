# NewsApp-MVVM-Architecture

## The Complete Project Folder Structure
.
├── README.md
├── app
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src
│       ├── androidTest
│       │   └── java
│       │       └── com
│       │           └── gauravbajaj
│       │               └── newsapp
│       │                   ├── ExampleInstrumentedTest.kt
│       │                   └── data
│       │                       └── api
│       ├── main
│       │   ├── AndroidManifest.xml
│       │   ├── java
│       │   │   └── com
│       │   │       └── gauravbajaj
│       │   │           └── newsapp
│       │   │               ├── MainActivity.kt
│       │   │               ├── NewsApplication.kt
│       │   │               ├── data
│       │   │               │   ├── api
│       │   │               │   │   └── NetworkService.kt
│       │   │               │   ├── model
│       │   │               │   │   ├── Article.kt
│       │   │               │   │   ├── Country.kt
│       │   │               │   │   ├── Language.kt
│       │   │               │   │   ├── Source.kt
│       │   │               │   │   ├── SourcesResponse.kt
│       │   │               │   │   └── TopHeadlinesResponse.kt
│       │   │               │   └── repository
│       │   │               │       ├── NewsRepository.kt
│       │   │               │       ├── NewsSourcesRepository.kt
│       │   │               │       ├── SearchRepository.kt
│       │   │               │       └── TopHeadLinesRepository.kt
│       │   │               ├── di
│       │   │               │   ├── module
│       │   │               │   │   └── ApplicationModule.kt
│       │   │               │   └── qualifiers.kt
│       │   │               ├── ui
│       │   │               │   ├── base
│       │   │               │   │   ├── UiSearchState.kt
│       │   │               │   │   ├── UiState.kt
│       │   │               │   │   └── ViewModelProviderFactory.kt
│       │   │               │   ├── country_sources
│       │   │               │   │   ├── CountrySourcesActivity.kt
│       │   │               │   │   └── CountrySourcesViewModel.kt
│       │   │               │   ├── languages
│       │   │               │   │   ├── LanguagesActivity.kt
│       │   │               │   │   ├── LanguagesScreen.kt
│       │   │               │   │   └── LanguagesViewModel.kt
│       │   │               │   ├── news_sources
│       │   │               │   │   ├── NewsSourcesActivity.kt
│       │   │               │   │   ├── NewsSourcesScreen.kt
│       │   │               │   │   └── NewsSourcesViewModel.kt
│       │   │               │   ├── newslist
│       │   │               │   │   ├── NewsListActivity.kt
│       │   │               │   │   └── NewsListViewModel.kt
│       │   │               │   ├── search
│       │   │               │   │   ├── SearchActivity.kt
│       │   │               │   │   └── SearchViewModel.kt
│       │   │               │   ├── theme
│       │   │               │   │   ├── Color.kt
│       │   │               │   │   ├── Theme.kt
│       │   │               │   │   └── Type.kt
│       │   │               │   └── topheadlines
│       │   │               │       ├── TopHeadlineActivity.kt
│       │   │               │       ├── TopHeadlineViewModel.kt
│       │   │               │       └── TopHeadlinesScreen.kt
│       │   │               └── utils
│       │   │                   ├── AppConstant.kt
│       │   │                   ├── CustomTabsHelper.kt
│       │   │                   └── DateUtils.kt
│       │   ├── new.xml
│       │   └── res
│       │       ├── drawable
│       │       │   ├── bg_category.xml
│       │       │   ├── ic_arrow_back.xml
│       │       │   ├── ic_arrow_forward.xml
│       │       │   ├── ic_article_placeholder.xml
│       │       │   ├── ic_check.xml
│       │       │   ├── ic_close.xml
│       │       │   ├── ic_error.xml
│       │       │   ├── ic_filter_list.xml
│       │       │   ├── ic_launcher_background.xml
│       │       │   ├── ic_launcher_foreground.xml
│       │       │   ├── ic_placeholder.xml
│       │       │   ├── ic_refresh.xml
│       │       │   └── ic_search.xml
│       │       ├── font
│       │       │   ├── poppins_bold.ttf
│       │       │   ├── poppins_regular.ttf
│       │       │   └── poppins_semibold.ttf
│       │       ├── menu
│       │       │   └── menu_languages.xml
│       │       ├── mipmap-anydpi-v26
│       │       │   ├── ic_launcher.xml
│       │       │   └── ic_launcher_round.xml
│       │       ├── mipmap-hdpi
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-mdpi
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xhdpi
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xxhdpi
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xxxhdpi
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── values
│       │       │   ├── colors.xml
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       ├── values-night
│       │       │   └── themes.xml
│       │       └── xml
│       │           ├── backup_rules.xml
│       │           ├── data_extraction_rules.xml
│       │           └── network_security_config.xml
│       └── test
│           └── java
│               └── com
│                   └── gauravbajaj
│                       └── newsapp
│                           ├── ExampleUnitTest.kt
│                           ├── MainCoroutineRule.kt
│                           ├── data
│                           │   ├── api
│                           │   │   └── FakeNetworkService.kt
│                           │   └── repository
│                           │       ├── NewsRepositoryTest.kt
│                           │       ├── NewsSourcesRepositoryTest.kt
│                           │       └── TopHeadlineRepositoryTest.kt
│                           ├── di
│                           └── ui
│                               ├── test
│                               └── topheadlines
│                                   └── TopHeadlineViewModelTest.kt
├── build.gradle.kts
├── gradle
│   ├── libs.versions.toml
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
├── print.txt
├── settings.gradle.kts
└── tree

58 directories, 99 files
