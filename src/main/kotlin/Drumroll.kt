import com.intellij.execution.ExecutionListener
import com.intellij.execution.ExecutionManager
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.diagnostic.Logger

import com.intellij.openapi.project.Project
import com.intellij.task.ProjectTaskContext
import com.intellij.task.ProjectTaskListener
import com.intellij.task.ProjectTaskManager


/**
 *@author mhashim6 on 20/03/19
 */
open class Drumroll(private val project: Project) {
    private val messages by lazy { project.messageBus.connect() }

    init {
        messages.subscribe(ExecutionManager.EXECUTION_TOPIC, eventConsumer)
        messages.subscribe(ProjectTaskListener.TOPIC, eventConsumer)
    }

    companion object {
        private val eventConsumer = Listener()
        private val logger by lazy { Logger.getInstance("Drumroll") }

        private var playContext = Context.None

        private fun taskTypeOf(env: ExecutionEnvironment) = env.run {
            runnerAndConfigurationSettings?.configuration?.beforeRunTasks?.let {
                logger.warn("before tasks: $it")
            }
            runnerAndConfigurationSettings?.name ?: runProfile.name
        }.lowercase()

        private fun ExecutionEnvironment.isLengthyTask() =
            taskTypeOf(this).also {
                logger.warn("task type: ${it}\n")
            }.containsAny("build", "assemble", "make", "beforeruntask")

        class Listener : ExecutionListener, ProjectTaskListener {

            override fun started(context: ProjectTaskContext) {
                super.started(context)
                logger.warn("context: ${context.runConfiguration?.name}\n")

                notifyState(BuildState.Starting, Context.ProjectTask)
            }

            override fun finished(result: ProjectTaskManager.Result) {
                super.finished(result)
                logger.warn("result: ${result.context.runConfiguration?.name}\n")

                notifyState(
                    when {
                        result.hasErrors() -> BuildState.Error
                        else -> BuildState.Success
                    },
                    Context.ProjectTask
                )
            }

            override fun processStarted(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
                super.processStarted(executorId, env, handler)
                if (!env.isLengthyTask()) return
                logger.warn("executor: $executorId :: ${env.executionTarget.displayName} :: ${taskTypeOf(env)}\n")
                notifyState(BuildState.Starting, Context.Execution)
            }

            override fun processTerminated(
                executorId: String, env: ExecutionEnvironment, handler: ProcessHandler, exitCode: Int
            ) {
                super.processTerminated(executorId, env, handler, exitCode)
                if (!env.isLengthyTask()) return
                logger.warn("executor: $executorId :: ${env.executionTarget.displayName} :: ${taskTypeOf(env)}\n")

                notifyState(
                    when (exitCode) {
                        0 -> BuildState.Success
                        else -> BuildState.Error
                    },
                    Context.Execution
                )
            }
        }

        private val timeCards = arrayOf("eternity_later", "moments_later", "pair_of_pants_later", "inches_later")
        private val success = arrayOf("Ba-Dum-Tss!", "wow", "boring") + timeCards
        private val error = arrayOf("failure", "doh", "oh_geez", "better_call_saul", "metal_gear")

        fun notifyState(state: BuildState, context: Context) {
            if (isPlaying() && context != playContext) {
                playContext = context
                return
            }
            play(
                when (state) {
                    BuildState.Starting -> "drumroll_faded"
                    BuildState.Success -> success.random()
                    else -> error.random()
                }
            )
            playContext = context
        }
    }
}

