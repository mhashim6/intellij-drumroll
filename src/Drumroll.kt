import com.intellij.openapi.compiler.CompilationStatusListener
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompilerTopics
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project

/**
 *@author mhashim6 on 20/03/19
 */
class Drumroll(private val project: Project) : ProjectComponent {
    private val messages = project.messageBus.connect()

    override fun projectOpened() {
        project.messageBus.connect().subscribe(CompilerTopics.COMPILATION_STATUS, object : CompilationStatusListener {
            override fun compilationFinished(aborted: Boolean, errors: Int, warnings: Int, compileContext: CompileContext) {
                when {
                    errors > 0 -> play("failure.mp3")
                    warnings > 0 -> play("metal_gear.mp3")
                }
            }
        })
    }

    override fun projectClosed() {
        stop()
        messages.disconnect()
    }
}

