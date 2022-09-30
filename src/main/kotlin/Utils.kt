/**
 * @author Muhammad Hashim (mhashim6) (<a href="https://mhashim6.me">https://mhashim6.me</a>) on 17/9/22
 */

enum class BuildState { Starting, Success, Error, Warning, None }
enum class Context { ProjectTask, Execution, None }

fun String.containsAny(vararg words: String) = words.any { contains(it) }