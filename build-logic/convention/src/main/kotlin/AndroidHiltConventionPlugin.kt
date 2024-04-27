import com.example.picsum.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // / Hilt와 Kotlin Annotation Processor Tool (kapt) 플러그인 적용
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("org.jetbrains.kotlin.kapt")
            }
            dependencies {
                "implementation"(libs.findLibrary("hilt.android").get())
                "kapt"(libs.findLibrary("hilt.compiler").get())
                // 테스트를 위한 Hilt 컴파일러 추가
                "kaptAndroidTest"(libs.findLibrary("hilt.compiler").get())
                "kaptTest"(libs.findLibrary("hilt.compiler").get())
            }

        }
    }
}