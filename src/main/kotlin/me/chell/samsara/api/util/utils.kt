package me.chell.samsara.api.util

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

fun World.getBlockState(pos: Vec3d?): BlockState = getBlockState(BlockPos(pos))