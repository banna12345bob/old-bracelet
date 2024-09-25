package com.idiotss.isaac;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Holder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class OldBraceletAttributes {
    // From AmyMialeeMods' Collisions Lib
    public static final Holder<EntityAttribute> TANGIBLE = registerEntityAttribute("tangibility", new ClampedEntityAttribute(
            "attribute.name." + OldBracelet.MOD_ID + '.' + "tangibility", 1, 0, 1).setTracked(true));

    public static final Holder<EntityAttribute> INVINCIBILITY = registerEntityAttribute("invincibility", new ClampedEntityAttribute(
            "attribute.name." + OldBracelet.MOD_ID + '.' + "invincibility", 0, 0, 1).setTracked(true));

    private static Holder<EntityAttribute> registerEntityAttribute(String id, EntityAttribute attribute) {
        return Registry.registerHolder(Registries.ENTITY_ATTRIBUTE, Identifier.of(OldBracelet.MOD_ID, id), attribute);
    }

    public static boolean getTangibility(final LivingEntity entity) {
        return entity.getAttributeInstance(TANGIBLE) != null && (entity.getAttributeInstance(TANGIBLE).getValue() > 0);
    }

    public static boolean getInvincibility(final LivingEntity entity) {
        return entity.getAttributeInstance(INVINCIBILITY) != null && (entity.getAttributeInstance(INVINCIBILITY).getValue() > 0);
    }

    public static void setInvincibility(final LivingEntity entity, boolean value) {
        entity.getAttributeInstance(INVINCIBILITY).setBaseValue(value ? 1 : 0);
    }

    public static void register() {
        OldBracelet.LOGGER.info("Registering Attributes for " + OldBracelet.MOD_ID);
    }
}
