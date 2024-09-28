package com.idiotss.isaac.camera;

import io.socol.betterthirdperson.api.CustomCamera;
import io.socol.betterthirdperson.api.util.Rotation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class OldBraceletCamera {
    private CustomCamera camera;

    public OldBraceletCamera(CustomCamera camera) {
        this.camera = camera;
    }

    public void lookAt(Vec3d currentPos, Vec3d target) {
        double d = target.x - currentPos.x;
        double e = target.y - currentPos.y;
        double f = target.z - currentPos.z;
        double g = Math.sqrt(d * d + f * f);
        float yaw = MathHelper.wrapDegrees((float)(MathHelper.atan2(f, d) * 57.2957763671875) - 90.0F); // Side to side
//        float pitch = MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * 57.2957763671875))); // up and down
        Rotation rotation = new Rotation(yaw, this.camera.getPitch());
        this.camera.setCameraRotation(rotation);
    }
}
