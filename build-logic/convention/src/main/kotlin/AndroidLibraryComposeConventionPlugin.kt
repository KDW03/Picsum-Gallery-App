import com.android.build.api.dsl.LibraryExtension
import com.example.picsum.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Android 라이브러리 플러그인 적용
            pluginManager.apply("com.android.library")

            // LibraryExtension 타입의 확장을 가져와서 compose 설정 적용
            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}
