package me.chell.samsara.api.util

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import me.chell.samsara.Samsara
import me.chell.samsara.api.feature.Feature
import me.chell.samsara.api.module.Module
import me.chell.samsara.api.module.ModuleManager
import me.chell.samsara.api.value.Bind
import me.chell.samsara.api.value.Value
import me.chell.samsara.api.feature.Widget
import me.chell.samsara.api.widget.WidgetManager
import java.io.File
import java.lang.reflect.Modifier
import java.net.URL

object FileUtils: Globals {

    private val gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation().excludeFieldsWithModifiers(Modifier.TRANSIENT)
        .registerTypeAdapter(File::class.java, FileAdapter())
        .setPrettyPrinting().create()

    fun saveConfig() {
        saveSettings()
        saveFeatures(Samsara.modulesFile.value.absolutePath, ModuleManager.modules)
        saveFeatures(Samsara.widgetsFile.value.absolutePath, WidgetManager.widgets)
        //saveWaypoints(Samsara.waypointFile.value.absolutePath)
        LOG.info("Config saved.")
    }

    fun loadConfig() {
        loadSettings()
        loadModules(Samsara.modulesFile.value.absolutePath)
        loadWidgets(Samsara.widgetsFile.value.absolutePath)
        //loadWaypoints(Samsara.waypointFile.value.absolutePath)
        LOG.info("Config loaded.")
    }

    fun createDefaultFiles() {
        createFile("${Globals.NAME}/Modules.json")
        createFile("${Globals.NAME}/Widgets.json")
        createFile("${Globals.NAME}/Waypoints.json")
        createFile("${Globals.NAME}/Settings.json")
        File("${Globals.NAME}/Addons/").mkdirs()
        downloadTheme("Default")
    }

    fun downloadTheme(name: String) {
        val theme = File("${Globals.NAME}/Themes/$name.lua")
        if(!theme.exists()) {
            createFile(theme.absolutePath)
            try {
                theme.writeBytes(URL("https://raw.githubusercontent.com/chell-dev/Samsara/master/Themes/$name.lua").readBytes())
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    private fun saveFeatures(fileName: String, list: List<Feature>) {
        val file = createFile(fileName)

        file.writeText(gson.toJson(list))
    }

    private fun saveSettings() {
        val file = createFile("${Globals.NAME}/Settings.json")

        file.writeText(gson.toJson(Samsara.values))
    }

    private fun loadSettings() {
        val file = checkFile("${Globals.NAME}/Settings.json") ?: return

        val list: List<Value<*>> = gson.fromJson(file.reader(), object: TypeToken<List<Value<*>>>() {}.type) ?: return

        for(v in list) {
            parseValue(v, Samsara)
        }
    }

    private fun loadModules(fileName: String) {
        val file = checkFile(fileName) ?: return

        val moduleList: List<Module> = gson.fromJson(file.reader(), object: TypeToken<List<Module>>() {}.type) ?: return

        for(m in moduleList) {
            val module = ModuleManager.getModule<Module>(m.name) ?: continue
            for(v in m.values) {
                parseValue(v, module)
            }
            if(module.isEnabled()) module.onEnable()
        }
    }

    private fun loadWidgets(fileName: String) {
        val file = checkFile(fileName) ?: return

        val widgetList: List<Widget> = gson.fromJson(file.reader(), object: TypeToken<List<Widget>>() {}.type) ?: return

        for(w in widgetList) {
            val widget = WidgetManager.getWidget<Widget>(w.name) ?: continue
            for(v in w.values) {
                parseValue(v, widget)
            }
            if(widget.isEnabled()) widget.onEnable()
        }
    }

    /*
    fun loadWaypoints(fileName: String) {
        val file = checkFile(fileName) ?: return

        val waypointList: List<Waypoint> = gson.fromJson(file.reader(), object: TypeToken<List<Waypoint>>() {}.type) ?: return

        for(w in waypointList) {
            val waypoint = WaypointManager.getWaypoint<Waypoint>(w.name) ?: continue
            for(v in w.values) {
                parseValue(v, waypoint)
            }
        }
    }
    */

    private fun checkFile(fileName: String): File? {
        val file = File(fileName)
        if(!file.exists() || file.isDirectory) {
            LOG.error("File $fileName doesn't exist or is a directory.")
            return null
        }
        return file
    }

    private fun createFile(fileName: String): File {
        val file = File(fileName)
        file.parentFile?.mkdirs()
        file.createNewFile()

        return file
    }

    // TODO color
    private fun parseValue(parse: Value<*>, feature: Feature) {
        when(parse.value) {
            is Boolean -> {
                val v = feature.getValue<Boolean>(parse.name) ?: return
                v.value = (parse.value as Boolean)
                v.displayName = parse.displayName
            }

            is Int -> {
                val v = feature.getValue<Int>(parse.name) ?: return
                v.value = (parse.value as Int)
                v.displayName = parse.displayName
            }
            is Double -> {
                val v = feature.getValue<Double>(parse.name) ?: return
                v.value = (parse.value as Double)
                v.displayName = parse.displayName
            }
            is Float -> {
                val v = feature.getValue<Float>(parse.name) ?: return
                v.value = (parse.value as Float)
                v.displayName = parse.displayName
            }

            is LinkedTreeMap<*, *> -> {
                val v0 = feature.getValue<Any>(parse.name) ?: return
                if(v0.value is Bind) {
                    val v = feature.getValue<Bind>(parse.name) ?: return

                    val map = (parse.value as LinkedTreeMap<*, *>)
                    v.value.key = (map["key"] as Double).toInt()
                    v.value.enabled = map["enabled"] as Boolean
                    v.value.hold = map["hold"] as Boolean

                    v.displayName = parse.displayName
                } else if(v0.value is File) {
                    val v = feature.getValue<File>(parse.name) ?: return

                    val map = (parse.value as LinkedTreeMap<*, *>)
                    v.value = File(map["file"].toString())

                    v.displayName = parse.displayName
                }
            }

            is String -> {
                val v = feature.getValue<Any>(parse.name) ?: return
                when (v.value) {
                    is Enum<*> -> {
                        val enum = v as Value<Enum<*>>
                        val clazz = enum.value::class.java
                        v.value = (java.lang.Enum.valueOf(clazz, (parse.value as String)))
                    }
                    is Char -> {
                        v.value = parse.value.toString()[0]
                    }
                    is String -> {
                        v.value = parse.value.toString()
                    }
                }
                v.displayName = parse.displayName
            }

            else -> {
                val v = feature.getValue<Any>(parse.name) ?: return
                v.displayName = parse.displayName
            }
        }
    }

    class FileAdapter: TypeAdapter<File>() {
        override fun write(writer: JsonWriter, file: File?) {
            writer.beginObject()
            writer.name("file")
            writer.value(if(file == null) "/" else file.absolutePath)
            writer.endObject()
        }

        override fun read(reader: JsonReader): File {
            reader.beginObject()
            if (reader.hasNext()) {
                return File(reader.nextString())
            }
            return File("/")
        }
    }
}