package me.chell.samsara.api.util

import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import me.chell.samsara.api.module.Module
import me.chell.samsara.api.module.ModuleManager
import me.chell.samsara.api.value.Bind
import me.chell.samsara.api.value.Value
import java.io.File
import java.lang.reflect.Modifier

// TODO widgets and other values
object Config {

    fun save(fileName: String) {
        val file = File(fileName)
        file.createNewFile()

        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().excludeFieldsWithModifiers(Modifier.TRANSIENT).setPrettyPrinting().create()
        file.writeText(gson.toJson(ModuleManager.modules))
    }

    fun load(fileName: String) {
        val file = File(fileName)
        if(!file.exists() || file.isDirectory) return

        val gson = GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create()
        val moduleList: List<Module> = gson.fromJson(file.reader(), object: TypeToken<List<Module>>() {}.type)

        for(m in moduleList) {
            val module = ModuleManager.getModule<Module>(m.name) ?: continue
            for(v in m.values) {
                parseValue(v, module)
            }
        }
    }

    private fun parseValue(parse: Value<*>, module: Module) {
        when(parse.value) {
            is Boolean -> {
                val v = module.getValue<Boolean>(parse.name) ?: return
                v.value = (parse.value as Boolean)
                v.displayName = parse.displayName
            }
            is Int -> {
                val v = module.getValue<Int>(parse.name) ?: return
                v.value = (parse.value as Int)
                v.displayName = parse.displayName
            }
            is Double -> {
                val v = module.getValue<Double>(parse.name) ?: return
                v.value = (parse.value as Double)
                v.displayName = parse.displayName
            }
            is Float -> {
                val v = module.getValue<Float>(parse.name) ?: return
                v.value = (parse.value as Float)
                v.displayName = parse.displayName
            }
            is LinkedTreeMap<*, *> -> {
                val v = module.getValue<Bind>(parse.name) ?: return
                val map = (parse.value as LinkedTreeMap<*, *>)
                v.value.key = (map["key"] as Double).toInt()
                v.value.enabled = map["enabled"] as Boolean
                v.value.hold = map["hold"] as Boolean
                v.displayName = parse.displayName
            }
            is Color -> { // TODO probably doesn't work
                val v = module.getValue<Color>(parse.name) ?: return
                v.value = (parse.value as Color)
                v.displayName = parse.displayName
            }
            is String -> {
                val v = module.getValue<Any>(parse.name) ?: return
                if(v.value is Enum<*>) {
                    val enum = v.value as Value<Enum<*>>
                    val clazz = enum.value::class.java
                    v.value = (java.lang.Enum.valueOf(clazz, (parse.value as String)))
                } else {
                    v.value = (parse.value as String)
                }
                v.displayName = parse.displayName
            }
            is Char -> {
                val v = module.getValue<Char>(parse.name) ?: return
                v.value = (parse.value as Char)
                v.displayName = parse.displayName
            }
            else -> {
                val v = module.getValue<Any>(parse.name) ?: return
                v.value = parse.value!!
                v.displayName = parse.displayName
            }
        }
    }
}