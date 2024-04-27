import com.android.build.api.dsl.ApplicationExtension
import com.example.picsum.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                // Android 애플리케이션과 Kotlin Android 플러그인 적용
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }


            extensions.configure<ApplicationExtension> {
                // Kotlin 설정 적용
                configureKotlinAndroid(this)
                // targetSdk 버전을 34로 설정
                defaultConfig.targetSdk = 34
            }
        }
    }
}