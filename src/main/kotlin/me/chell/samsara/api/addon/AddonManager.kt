package me.chell.samsara.api.addon

import me.chell.samsara.api.Loadable
import me.chell.samsara.api.util.Globals
import java.io.File
import java.net.URLClassLoader
import java.util.zip.ZipFile

object AddonManager: Loadable, Globals {

    val addons = mutableListOf<Addon>()

    init {
        val folder = File("mods/")

        for(f in folder.listFiles()!!) {
            if(f.name.endsWith(".jar", true)) {

                for(entry in ZipFile(f).entries()) {

                    if(entry.name.endsWith(".class")) {

                        val classLoader = URLClassLoader.newInstance(arrayOf(f.toURI().toURL()), javaClass.classLoader); // thank you seppuku
                        val clazz = classLoader.loadClass(entry.name.dropLast(".class".length).replace('/', '.'))

                        if(Addon::class.java.isAssignableFrom(clazz)) {
                            val a = clazz.getDeclaredConstructor().newInstance() as Addon
                            addons.add(a)
                            LOG.info("Found addon in ${f.name}.")
                            break
                        }

                    }
                }

            }
        }
    }

    override fun load() {
        for(a in addons) {
            a.load()
        }
    }

    override fun unload() {
        for(a in addons) {
            a.unload()
        }
    }
}