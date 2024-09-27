

// Adapted from AmyMialeeMods' Collisions Lib
// https://github.com/AmyMialeeMods/collisions-lib/blob/main/src/main/java/amymialee/collisionslib/mixin/LivingEntityMixin.java

package com.idiotss.isaac.mixin;

import com.idiotss.isaac.OldBraceletAttributes;
import com.idiotss.isaac.OldBracelet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

//    @Inject(method = "tick", at = @At("HEAD"))
//    private void tick(CallbackInfo ci) {
//        // Invincibility switches back and forth due to desync with the server
//        boolean inv = OldBraceletAttributes.getInvincibility(((LivingEntity) ((Object) this)));
//        if (OldBraceletAttributes.getInvincibility(((LivingEntity) ((Object) this))) == true) {
//            OldBracelet.LOGGER.info("TRUE");
//        }
//        OldBracelet.LOGGER.info(this.uuid.toString() + ": " + inv);
//    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        if (OldBraceletAttributes.getInvincibility(((LivingEntity) ((Object) this)))) {
            cir.cancel();
        }
    }

    @Inject(method = "pushAway", at = @At("HEAD"), cancellable = true)
    protected void pushAway(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity && (OldBraceletAttributes.getTangibility(((LivingEntity) ((Object) this))) || OldBraceletAttributes.getTangibility((LivingEntity) entity))) {
            ci.cancel();
        }
    }

    @Unique
    public boolean collidesWith(Entity other) {
        if (other instanceof LivingEntity) {
            return (this.isCollidable() && other.isCollidable());
        }
        return other.isCollidable() && !this.isConnectedThroughVehicle(other);
    }

    @Unique
    public boolean isCollidable() {
        return OldBraceletAttributes.getTangibility(((LivingEntity) ((Object) this)));
    }

    @Inject(method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", at = @At("RETURN"))
    private static void addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(OldBraceletAttributes.TANGIBLE);
        info.getReturnValue().add(OldBraceletAttributes.INVINCIBILITY);
    }
}