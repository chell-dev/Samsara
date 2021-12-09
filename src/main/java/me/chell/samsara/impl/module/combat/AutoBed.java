package me.chell.samsara.impl.module.combat;

import me.chell.samsara.api.event.PlayerUpdateEvent;
import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.value.Value;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoBed extends Module {
    public AutoBed() {
        super("AutoBed", Category.COMBAT);
    }

    private final Value<Boolean> hotbarRefill = builder("Refill", false).build();
    private final Value<Double> hitRange = builder("Hit Range", 5.5d).bounds(0d, 6d).build();
    private final Value<Double> hitWallRange = builder("Hit Wall Range", 5d).bounds(0d, 6d).name("Wall Range").build();
    private final Value<Double> placeRange = builder("Place Range", 4.5d).bounds(0d, 6d).build();
    private final Value<Double> placeWallRange = builder("Place Wall Range", 3.2d).bounds(0d, 6d).name("Wall Range").build();
    private final Value<Double> minDamage = builder("Min Damage", 4d).bounds(0d, 20d).build();
    private final Value<Double> maxSelfDamage = builder("Self Damage", 6d).bounds(0d, 20d).build();
    private final Value<Double> playerRange = builder("Player Range", 10d).bounds(0d, 20d).build();
    private final Value<Boolean> autoSwitch = builder("Auto Switch", false).build();

    private BlockPos placePos;

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if(hotbarRefill.getValue()) {
            boolean emptySlot = false;

            for(int i=0; i<9;i++) {
                if(getPlayer().inventory.mainInventory.get(i).isEmpty()) {
                    emptySlot = true;
                    break;
                }
            }

            if(emptySlot && !(getMinecraft().currentScreen instanceof GuiContainer)) {
                for(int i=9;i<36;i++) {
                    ItemStack stack = getPlayer().inventory.mainInventory.get(i);
                    if(stack.getItem() instanceof ItemBed) {
                        getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, i, 0, ClickType.QUICK_MOVE, getPlayer());
                        break;
                    }
                }
            }
        }

        if(hitRange.getValue() > 0d) {
            for(TileEntity e : getWorld().loadedTileEntityList) {
                if(!(e instanceof TileEntityBed)) continue;

                BlockPos pos = e.getPos();
                RayTraceResult result = getWorld().rayTraceBlocks(getPlayer().getPositionEyes(0f), new Vec3d(pos));
                boolean raytrace = result != null && result.getBlockPos().equals(pos);
                double dist = getPlayer().getDistance(pos.getX(), pos.getY(), pos.getZ());

                if(raytrace ? dist <= hitRange.getValue() : dist <= hitWallRange.getValue()) {
                    getPlayer().connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0f, 0f, 0f));
                    return;
                }
            }
        }

        if(placeRange.getValue() > 0d) {
            boolean holdingBed = getPlayer().getHeldItemMainhand().getItem() instanceof ItemBed || getPlayer().getHeldItemOffhand().getItem() instanceof ItemBed;

            if(placePos != null) {
                if(holdingBed) {

                    EnumFacing direction = null;
                    for(EnumFacing facing : EnumFacing.HORIZONTALS) {
                        BlockPos blockPos = placePos.offset(facing);
                        if(blockCheck(blockPos)) {
                            direction = facing;
                            break;
                        }
                    }

                    if(direction != null) {
                        //int i = MathHelper.floor((double)(getPlayer().rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                        //EnumFacing enumfacing = EnumFacing.byHorizontalIndex(i);

                        float f, f1, f2;

                        RayTraceResult rayTraceResult = getWorld().rayTraceBlocks(getPlayer().getPositionEyes(0f), new Vec3d(placePos));
                        if(rayTraceResult == null) {
                            f = f1 = f2 = 0f;
                        } else {
                            Vec3d vec = rayTraceResult.hitVec;
                            f = (float)(vec.x - (double)placePos.getX());
                            f1 = (float)(vec.y - (double)placePos.getY());
                            f2 = (float)(vec.z - (double)placePos.getZ());
                        }

                        float yaw = getPlayer().rotationYaw;
                        getPlayer().rotationYaw = direction.getHorizontalAngle();
                        getPlayer().connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, EnumFacing.UP, EnumHand.MAIN_HAND, f, f1, f2));
                        getPlayer().rotationYaw = yaw;

                        placePos = null;
                        return;
                    }
                }
                placePos = null;
            }

            int bedSlot = -1;
            if(holdingBed) {
                bedSlot = getPlayer().inventory.currentItem;
            } else if(autoSwitch.getValue()){
                for(int i=0; i<9;i++) {
                    if(getPlayer().inventory.mainInventory.get(i).getItem() instanceof ItemBed) {
                        bedSlot = i;
                        break;
                    }
                }
                if(bedSlot == -1) return;
            } else return;

            List<EntityPlayer> list = new ArrayList<>();
            for(EntityPlayer player : getWorld().playerEntities) {
                if(player.equals(getPlayer())) continue;
                if(player.isDead || player.getHealth() <= 0) continue;
                if(player.hurtTime > 0 || player.hurtResistantTime > 0) continue;
                if(getPlayer().getDistance(player) > playerRange.getValue()) continue;
                list.add(player);
            }
            if(list.isEmpty()) return;
            list.sort(Comparator.comparing(e -> getPlayer().getDistance(e)));
            EntityPlayer target = list.get(0);
            if(target == null) return;

            double damage = 0d;
            BlockPos pos = null;
            for(BlockPos blockPos : getPlacePositions(getPlayer().getPosition(), placeRange.getValue(), placeWallRange.getValue())) {
                if(calculateDamage(blockPos.up(), getPlayer()) > maxSelfDamage.getValue()) continue;
                double d = calculateDamage(blockPos.up(), target);
                if(d > damage) {
                    damage = d;
                    pos = blockPos;
                }
            }

            if(damage > minDamage.getValue()) {
                placePos = pos;
                getPlayer().inventory.currentItem = bedSlot;
            }
        }
    }

    private List<BlockPos> getPlacePositions(BlockPos origin, double range, double wallRange) {
        List<BlockPos> list = new ArrayList<>();
        double x = origin.getX();
        double y = origin.getY();
        double z = origin.getZ();

        for(double xx = x-range;xx <= x+range; xx++) {
            for(double yy = y-range;yy <= y+range; yy++) {
                for(double zz = z-range;zz <= z+range; zz++) {
                    BlockPos blockPos = new BlockPos(xx, yy, zz);
                    RayTraceResult result = getWorld().rayTraceBlocks(getPlayer().getPositionEyes(0f), new Vec3d(blockPos));

                    boolean raytrace = result != null && result.getBlockPos().equals(blockPos);
                    boolean dist = raytrace ? origin.distanceSq(xx+0.5D, yy+0.5D, zz+0.5D) <= range * range
                            : origin.distanceSq(xx+0.5D, yy+0.5D, zz+0.5D) <= wallRange * wallRange;

                    if(dist && canPlaceBed(blockPos))
                        list.add(blockPos);
                }
            }
        }

        return list;
    }

    /*
    private List<BlockPos> getBlocksInRange(BlockPos origin, double range) {
        List<BlockPos> list = new ArrayList<>();
        double x = origin.getX();
        double y = origin.getY();
        double z = origin.getZ();

        for(double xx = x-range;xx <= x+range; xx++) {
            for(double yy = y-range;yy <= y+range; yy++) {
                for(double zz = z-range;zz <= z+range; zz++) {
                    if(origin.distanceSq(xx+0.5D, yy+0.5D, zz+0.5D) <= range * range)
                        list.add(new BlockPos(xx, yy, zz));
                }
            }
        }

        return list;
    }
    */

    private boolean canPlaceBed(BlockPos blockPos) {
        if(!blockCheck(blockPos)) return false;
        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos neighbour = blockPos.offset(facing);
            if(blockCheck(neighbour)) {
                return true;
            }
        }
        return false;
    }

    private boolean blockCheck(BlockPos blockPos) {
        boolean block = getWorld().getBlockState(blockPos).getMaterial() == Material.AIR;
        boolean up = getWorld().getBlockState(blockPos.up()).getMaterial() == Material.AIR;
        boolean down = getWorld().getBlockState(blockPos.down()).isSideSolid(getWorld(), blockPos, EnumFacing.UP);
        return block && up && down;
    }

    private double calculateDamage(BlockPos pos, EntityLivingBase entity) {
        float explosionSize = 5.0F;
        double x = (double)pos.getX() + 0.5D;
        double y = (double)pos.getY() + 0.5D;
        double z = (double)pos.getZ() + 0.5D;
        Explosion explosion = new Explosion(getWorld(), null, x, y, z, explosionSize, true, true);

        float f3 = explosionSize * 2.0F;
        Vec3d vec3d = new Vec3d(x, y, z);

        if (!entity.isImmuneToExplosions())
        {
            double d12 = entity.getDistance(x, y, z) / (double)f3;

            if (d12 <= 1.0D)
            {
                double d5 = entity.posX - x;
                double d7 = entity.posY + (double)entity.getEyeHeight() - y;
                double d9 = entity.posZ - z;
                double d13 = MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                if (d13 != 0.0D)
                {
                    double d14 = getWorld().getBlockDensity(vec3d, entity.getEntityBoundingBox());
                    double d10 = (1.0D - d12) * d14;
                    DamageSource source = DamageSource.causeExplosionDamage(explosion);
                    float amount = (float)((int)((d10 * d10 + d10) / 2.0D * 7.0D * (double)f3 + 1.0D));
                    //entity.attackEntityFrom(source, amount);

                    if(entity instanceof EntityPlayer) {
                        switch (getWorld().getDifficulty()) {
                            case PEACEFUL:
                                return 0.0D;
                            case EASY:
                                amount = Math.min(amount / 2.0F + 1.0F, amount);
                                break;
                            case HARD:
                                amount = amount * 3.0F / 2.0F;
                                break;
                        }
                    }
                    //if(entity.canBlockDamageSource(source)) {
                    //    return 0d;
                    //}

                    if ((float)entity.hurtResistantTime > (float)entity.maxHurtResistantTime / 2.0F) {
                        return 0.0D;
                    } else {
                        amount = applyArmorCalculations(source, amount, entity);
                        amount = applyPotionDamageCalculations(source, amount, entity);
                        amount = Math.max(amount - entity.getAbsorptionAmount(), 0.0F);
                        return amount;
                    }


                    //double d11 = d10;
                    //d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d10);
                }
            }
        }

        return 0.0D;
    }

    private float applyArmorCalculations(DamageSource source, float damage, EntityLivingBase entity) {
        if (!source.isUnblockable())
        {
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        }

        return damage;
    }

    private float applyPotionDamageCalculations(DamageSource source, float damage, EntityLivingBase entity) {
        if (source.isDamageAbsolute())
        {
            return damage;
        }
        else
        {
            if (entity.isPotionActive(MobEffects.RESISTANCE) && source != DamageSource.OUT_OF_WORLD)
            {
                int i = (entity.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = damage * (float)j;
                damage = f / 25.0F;
            }

            if (damage <= 0.0F)
            {
                return 0.0F;
            }
            else
            {
                int k = EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(), source);

                if (k > 0)
                {
                    damage = CombatRules.getDamageAfterMagicAbsorb(damage, (float)k);
                }

                return damage;
            }
        }
    }
}
