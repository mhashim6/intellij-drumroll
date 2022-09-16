import com.intellij.execution.ExecutionListener
import com.intellij.execution.ExecutionManager
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.diagnostic.Logger

import com.intellij.openapi.project.Project


/**
 *@author mhashim6 on 20/03/19
 */
open class Drumroll(private val project: Project) {
    private val messages by lazy { project.messageBus.connect() }

    init {
        messages.subscribe(ExecutionManager.EXECUTION_TOPIC, Listener())
    }

    companion object {

        private val logger by lazy { Logger.getInstance("Drumroll") }
        private val now: Long
            get() = System.currentTimeMillis()

        private var currentState = BuildState.None
        private val stateUpdateStamp = mutableMapOf(
            BuildState.Success to now,
            BuildState.Error to now,
            BuildState.Warning to now,
            BuildState.None to now,
        )

        private inline fun debounce(state: BuildState, threshold: Int = 5000, block: () -> Unit) {
            if (currentState == state && now - stateUpdateStamp[state]!! < threshold) return
            stateUpdateStamp[state] = now
            block()
        }

        private fun taskTypeOf(env: ExecutionEnvironment) = env.run {
            runnerAndConfigurationSettings?.configuration?.beforeRunTasks?.let {
                logger.warn("before tasks: $it")
                it.forEach { task ->
                    logger.warn("task: ${task.providerId}\n")

                }
            }
            runnerAndConfigurationSettings?.name ?: runProfile.name
        }.lowercase()

        private fun ExecutionEnvironment.isLengthyTask() =
            taskTypeOf(this).contains("build") || taskTypeOf(this).contains("assemble")

        class Listener : ExecutionListener {

            override fun processStarted(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
                super.processStarted(executorId, env, handler)
                if (!env.isLengthyTask()) return
                logger.warn("executor: $executorId :: ${env.executionTarget.displayName} :: ${taskTypeOf(env)}\n")
                play("drumroll_faded")
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
                    }
                )
            }
        }

        private val timeCards = arrayOf("eternity_later", "moments_later", "pair_of_pants_later", "inches_later")
        private val success = arrayOf("Ba-Dum-Tss!", "wow", "boring") + timeCards
        private val error = arrayOf("failure", "doh", "oh_geez", "better_call_saul", "metal_gear")

        fun notifyState(state: BuildState) {
//            debounce(state) {
            play(
                when (state) {
                    BuildState.Success -> success.random()
                    else -> error.random()
                }
            )
//            }
        }
    }
}

