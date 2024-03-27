import com.android.build.api.dsl.ApplicationExtension
import com.example.picsum.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType



class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Android 애플리케이션 플러그인 적용
            pluginManager.apply("com.android.application")
            // 프로젝트의 Android 확장을 가져와서 Compose 설정 적용
            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}