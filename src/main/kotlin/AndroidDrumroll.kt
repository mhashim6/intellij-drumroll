import com.android.ide.common.blame.Message
import com.android.tools.idea.gradle.project.build.GradleBuildContext
import com.android.tools.idea.project.AndroidProjectBuildNotifications
import com.intellij.openapi.project.Project

/**
 *@author mhashim6 on 30/09/2019
 */
class AndroidDrumroll(private val project: Project) : Drumroll(project) {

    init {
        AndroidProjectBuildNotifications.subscribe(project) { ctx ->
            if (ctx is GradleBuildContext) ctx.buildResult.run {
                notifyState(
                    when {
                        isBuildSuccessful -> BuildState.Success
                        getCompilerMessages(Message.Kind.WARNING).isNotEmpty() -> BuildState.Warning
                        else -> BuildState.Error
                    }
                )
            }
        }
    }

}