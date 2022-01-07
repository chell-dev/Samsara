package me.chell.samsara.impl.event

import me.chell.samsara.api.event.Event
import net.minecraft.client.particle.Particle

class ParticleAddedEvent(val particle: Particle): Event.Cancelable()