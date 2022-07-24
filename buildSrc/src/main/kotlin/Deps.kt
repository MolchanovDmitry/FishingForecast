object Deps {

    const val androidx_core_ktx = "androidx.core:core-ktx:1.6.0"
    const val androidx_appcompat = "androidx.appcompat:appcompat:1.3.0"

    // Design
    const val material = "com.google.android.material:material:1.4.0"
    const val androidx_constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.0"

    // for by viewModels and result api
    const val androidx_activity_ktx = "androidx.activity:activity-ktx:1.2.0"
    const val androidx_fragment_ktx = "androidx.fragment:fragment-ktx:1.3.0"

    // kotlinx
    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0"
    const val serialization_json = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2"
    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt"
    const val coroutines_core_serialization = "org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.1"

    // Video player
    const val exo_player = "com.google.android.exoplayer:exoplayer:2.14.1"

    // Http client
    const val okhttp = "com.squareup.okhttp3:okhttp:4.9.0"
    const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:4.9.0"

    // Lifecycle
    const val androidx_lifecycle_runtime_ktx =
        "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0"
    const val androidx_lifecycle_viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0-alpha03"

    // Firebase
    const val firebase_bom = "com.google.firebase:firebase-bom:28.2.1"
    const val firebase_database_ktx = "com.google.firebase:firebase-database-ktx"

    // Leak canary
    const val leak_canary = "com.squareup.leakcanary:leakcanary-android:2.7"

    // map
    const val play_services_maps = "com.google.android.gms:play-services-maps:18.0.2"

    // for custom marker
    const val maps_utils = "com.google.maps.android:android-maps-utils-v3:2.3.0"
    const val maps_ktx = "com.google.maps.android:maps-ktx:3.0.0"

    const val maps_v3 = "com.google.maps.android:maps-v3-ktx:2.2.0"

    /* Compose */
    const val compose_version = "1.1.1"

    const val compose_activity = "androidx.activity:activity-compose:1.3.1"
    const val compose_ui = "androidx.compose.ui:ui:$compose_version"

    // Tooling support (Previews, etc.)
    const val compose_ui_tooling = "androidx.compose.ui:ui-tooling:$compose_version"

    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    const val compose_foundation = "androidx.compose.foundation:foundation:$compose_version"

    // Material Design
    const val compose_material = "androidx.compose.material:material:$compose_version"

    // Material design icons
    const val compose_icons_core = "androidx.compose.material:material-icons-core:$compose_version"
    const val compose_icons_extended =
        "androidx.compose.material:material-icons-extended:$compose_version"

    // Integration with ViewModels
    const val compose_view_model = "androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0"

    const val compose_navigation = "androidx.navigation:navigation-compose:2.4.0-rc01"

    //Koin
    private const val koin_version = "3.1.4"
    const val koin_core = "io.insert-koin:koin-core:$koin_version"
    const val koin_android = "io.insert-koin:koin-android:$koin_version"
    const val koin_compose =  "io.insert-koin:koin-androidx-compose:$koin_version"

    // Graphics
    const val plot = "com.github.madrapps:plot:0.1.1"


    // Unit test
    const val junit = "junit:junit:4.13.2"
    const val mockito_core = "org.mockito:mockito-core:4.1.0"
    const val mockito_inline = "org.mockito:mockito-inline:4.1.0"
    const val robolectric = "org.robolectric:robolectric:4.5.1"
    const val androidx_test_core_ktx = "androidx.test:core-ktx:1.3.0"
    const val mock_web_server = "com.squareup.okhttp3:mockwebserver:4.3.1"
    const val coroutine_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2"
}