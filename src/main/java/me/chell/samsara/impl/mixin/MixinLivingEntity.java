package me.chell.samsara.impl.mixin;

import me.chell.samsara.api.event.EventManager;
import me.chell.samsara.impl.event.EntityKnockbackEvent;
import me.chell.samsara.impl.event.EntityPushedEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "takeKnockback", at = @At("HEAD"), cancellable = true)
    private void knockback(double strength, double x, double z, CallbackInfo ci) {
        EntityKnockbackEvent event = new EntityKnockbackEvent((LivingEntity) (Object) this);
        EventManager.INSTANCE.post(event);
        if(event.getCanceled()) ci.cancel();
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    private void push(Entity entity, CallbackInfo ci) {
        EntityPushedEvent event = new EntityPushedEvent((LivingEntity) (Object) this, entity);
        EventManager.INSTANCE.post(event);
        if(event.getCanceled()) ci.cancel();
    }
}
