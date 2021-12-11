package me.chell.samsara.api.event

import me.chell.samsara.api.Loadable
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.jvmErasure

class EventManager : Loadable {
    private val map: MutableMap<KClass<out Event>, MutableList<Any>> = mutableMapOf()

    fun post(e: Event) {
        val list = map[e::class] ?: return
        for(any in list) {
            for(f in any::class.functions) {
                if(f.annotations.isEmpty()) continue
                if(f.annotations[0].annotationClass == EventHandler::class) {
                    if(f.parameters[1].type == e::class.createType())
                        f.call(any, e)
                }
            }
        }
    }

    fun register(a: Any) {
        for(f in a::class.functions) {
            if(f.annotations.isEmpty()) continue
            if(f.annotations[0].annotationClass == EventHandler::class) {
                val e: KClass<out Event> = f.parameters[1].type.jvmErasure as KClass<out Event>
                if(map.containsKey(e)) {
                    map[e]?.add(a)
                } else {
                    map[e] = mutableListOf(a)
                }
            }
        }
    }

    fun unregister(a: Any) {
        for(f in a::class.functions) {
            if(f.annotations.isEmpty()) continue
            if(f.annotations[0].annotationClass == EventHandler::class) {
                val e: KClass<out Event> = f.parameters[1].type.jvmErasure as KClass<out Event>
                map[e]?.remove(a)
            }
        }
    }

    override fun load() {}

    override fun unload() = map.clear()
}