package com.idiotss.isaac;

import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Holder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OldBracelet implements ModInitializer {
	public static final String MOD_ID = "old-bracelet";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// From AmyMialeeMods' Collisions Lib
	public static final Holder<EntityAttribute> TANGIBLE = registerEntityAttribute("tangibility", new ClampedEntityAttribute(
			"attribute.name." + MOD_ID + '.' + "tangibility", 1, 0, 1).setTracked(true));

	public static final Holder<EntityAttribute> INVINCIBILITY = registerEntityAttribute("invincibility", new ClampedEntityAttribute(
			"attribute.name." + MOD_ID + '.' + "invincibility", 0, 0, 1).setTracked(true));

	private static Holder<EntityAttribute> registerEntityAttribute(String id, EntityAttribute attribute) {
		return Registry.registerHolder(Registries.ENTITY_ATTRIBUTE, Identifier.of(MOD_ID, id), attribute);
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

	@Override
	public void onInitialize() {
	}
}