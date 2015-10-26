package jeb

import java.io.File

open class Io {

    open fun lastModified(dir: File, predicate: (File) -> Boolean) = dir.
            listFiles().
            filter { predicate(it) }.
            maxBy { it.lastModified() }

    open fun fileExists(dir: File, predicate: (File) -> Boolean) = dir.
            listFiles().
            filter(predicate).
            any()

    open fun fileExists(file: File) = file.exists()

    open fun remove(f: File) = f.deleteRecursively()

    open fun copy(from: File, to: File) =
            !"cp -r ${from.absolutePath} ${to.absolutePath}"

    open fun sync(from: File, base: File, to: File) =
            !"rsync -avh --delete --link-dest=${base.absolutePath} ${from.absolutePath}/ ${to.absolutePath}"

    open fun move(from: File, to: File) =
            !"mv ${from.absolutePath} ${to.absolutePath}"

    private operator fun String.not() {
        val proc = Runtime.getRuntime().exec(this)
        val returnCode = proc.waitFor()
        val stdout = proc.inputStream.bufferedReader().readText()
        val stderr = proc.errorStream.bufferedReader().readText()
        if (returnCode != 0 || stderr.isNotBlank()) {
            throw JebExecException(
                    cmd = this,
                    stdout = stdout,
                    stderr = stderr,
                    returnCode = returnCode)
        }
    }

}
