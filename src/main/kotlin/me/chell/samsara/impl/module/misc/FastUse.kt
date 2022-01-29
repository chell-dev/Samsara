package me.chell.samsara.impl.module.misc

import me.chell.samsara.api.event.EventHandler
import me.chell.samsara.api.feature.Module
import me.chell.samsara.api.value.Register
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.event.PlayerTickEvent
import me.chell.samsara.impl.mixin.AccessorClientPlayerInteractionManager
import me.chell.samsara.impl.mixin.AccessorMinecraftClient
import net.minecraft.item.BlockItem
import net.minecraft.item.EndCrystalItem
import net.minecraft.item.ExperienceBottleItem

class FastUse: Module("FastUse", Category.MISC, "Removes item use delay.") {

    @Register val pvp = Value("PVP Mode", true, "Only for XP bottles and crystals.")

    @EventHandler
    fun onPlayerTick(event: PlayerTickEvent) {
        val accessor = mc as AccessorMinecraftClient

        (interactionManager as AccessorClientPlayerInteractionManager).setBlockBreakingCooldown(0)
        accessor.setAttackCooldown(0)
        if(!pvp.value || mainHand() || offHand())
            accessor.setItemUseCooldown(0)
    }

    private fun mainHand(): Boolean {
        return (player.mainHandStack.item is ExperienceBottleItem
                || player.mainHandStack.item is EndCrystalItem)
    }

    private fun offHand(): Boolean {
        val item = (player.offHandStack.item is ExperienceBottleItem
                || player.offHandStack.item is EndCrystalItem)
        return item && player.mainHandStack.item !is BlockItem
    }

}