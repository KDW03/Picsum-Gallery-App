import com.android.build.api.dsl.LibraryExtension
import com.example.picsum.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

// feature 모듈에 공통 적용
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                // 라이브러리 공통 적용 common 적용
                apply("picsum.android.library")
                // hilt 적용
                apply("picsum.android.hilt")
            }

            // todo 공부
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "com.example.picsum.core.testing.DoTestRunner"
                }
            }

            // todo domain이 있다면
            dependencies {
                add("implementation", project(":core:model"))
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:data"))
                add("implementation", project(":core:common"))

                add("testImplementation", kotlin("test"))
                add("androidTestImplementation", kotlin("test"))

                add("implementation", libs.findLibrary("coil.kt").get())
                add("implementation", libs.findLibrary("coil.kt.compose").get())

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())

                add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
    }
}