import com.intellij.openapi.compiler.CompilationStatusListener
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompilerTopics
import com.intellij.openapi.project.Project
import kotlin.random.Random.Default.nextBoolean

/**
 *@author mhashim6 on 20/03/19
 */
open class Drumroll(private val project: Project) {

    enum class BuildState { Success, Error, Warning }

    private val messages by lazy { project.messageBus.connect() }
    private val success = arrayOf("Ba-Dum-Tss!", "wow", "boring")
    private val error = arrayOf("failure", "doh", "oh_geez", "better_call_saul")
    private val warnings = arrayOf("metal_gear")
    private val timeCards = arrayOf("eternity_later", "moments_later", "pair_of_pants_later", "inches_later")

    init {
        messages.subscribe(CompilerTopics.COMPILATION_STATUS, object : CompilationStatusListener {
            override fun compilationFinished(aborted: Boolean, errors: Int, warnings: Int, compileContext: CompileContext) {
                notifyState(
                        when {
                            errors > 0 -> BuildState.Error
                            warnings > 0 -> BuildState.Warning
                            else -> BuildState.Success
                        }
                )
            }
        })
    }

    fun notifyState(state: BuildState) {
        play(
                if (nextBoolean() && nextBoolean())
                    timeCards.random()
                else
                    when (state) {
                        BuildState.Success -> success.random()
                        BuildState.Error -> error.random()
                        BuildState.Warning -> warnings.random()
                    }
        )
    }
}

