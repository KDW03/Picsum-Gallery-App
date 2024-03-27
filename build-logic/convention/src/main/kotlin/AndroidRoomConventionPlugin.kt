import com.example.picsum.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Kotlin Symbol Processing (KSP) 플러그인 적용
            pluginManager.apply("com.google.devtools.ksp")

            dependencies {
                add("implementation", libs.findLibrary("room.runtime").get())
                add("implementation", libs.findLibrary("room.ktx").get())
                // Room 엔티티나 DAO 등의 어노테이션을 처리하기 위한 KSP 프로세서 추가
                add("ksp", libs.findLibrary("room.compiler").get())
            }
        }
    }
}