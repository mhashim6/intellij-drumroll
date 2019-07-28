import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompileTask

/**
 *@author mhashim6 on 20/03/19
 */
class BeforeCompile : CompileTask {
    override fun execute(context: CompileContext): Boolean {
        play("drumroll_faded")
        return true
    }
}