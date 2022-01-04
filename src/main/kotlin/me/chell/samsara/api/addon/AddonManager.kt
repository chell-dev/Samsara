package me.chell.samsara.api.addon

import me.chell.samsara.api.Loadable
import java.io.File
import java.net.URLClassLoader
import java.util.zip.ZipFile

object AddonManager: Loadable {
    override fun load() {
        invokeMethod("load")
    }

    override fun unload() {
        invokeMethod("unload")
    }

    private fun invokeMethod(name: String) {
        val folder = File("Samsara/Addons/")

        for(f in folder.listFiles()!!) {
            if(f.name.endsWith(".jar", true)) {
                val zip = ZipFile(f)

                for(entry in zip.entries()) {
                    val kt = entry.name.endsWith(".kt")
                    if(entry.name.endsWith(".class") || kt) {
                        val classLoader = URLClassLoader.newInstance(arrayOf(f.toURI().toURL()), javaClass.classLoader); // thank you seppuku
                        val clazz = classLoader.loadClass(entry.name.dropLast(if(kt) ".kt".length else ".class".length).replace('/', '.'))

                        if(Addon::class.java.isAssignableFrom(clazz)) {
                            clazz.getDeclaredMethod("load").invoke(clazz.getDeclaredConstructor().newInstance())
                        }
                    }
                }
            }
        }
    }
}