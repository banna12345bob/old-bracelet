package com.idiotss.isaac.animation;

import net.minecraft.util.math.Vec3d;

public interface AnimatablePlayer {
    void playRollAnimation(String animationName, Vec3d direction);
}
