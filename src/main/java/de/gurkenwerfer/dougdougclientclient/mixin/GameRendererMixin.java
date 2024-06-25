package de.gurkenwerfer.dougdougclientclient.mixin;

import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import de.gurkenwerfer.dougdougclientclient.modules.Gurkreach;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
@Mixin(value = GameRenderer.class, priority = 1000)
public abstract class GameRendererMixin {

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 6))
    private double updateTargetedEntityModifySurvivalReach(double d) {
        Gurkreach gurkReach = new Gurkreach();
        return ModuleManager.isModuleEnabled("Gurkreach") ? gurkReach.getReach() : d;
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9))
    private double updateTargetedEntityModifySquaredMaxReach(double d) {
        Gurkreach gurkReach = new Gurkreach();
        return ModuleManager.isModuleEnabled("Gurkreach") ? gurkReach.getReach() * gurkReach.getReach() : d;
    }
}

 */


/*

THESE DO NOT FUCK THEM


@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @ModifyVariable(
            method = "updateTargetedEntity",
            at = @At(value = "STORE", ordinal = -1), // The first double variable assigned (6.0)
            ordinal = 0
    )

    private double modifySurvivalReach(double d) {
        Gurkreach gurkReach = new Gurkreach();
        //System.out.println("REEEEEAAAAAACCCHHHHHHHHHH  " + gurkReach.getReach());
        return ModuleManager.isModuleEnabled("Gurkreach") ? gurkReach.getReach() : d;
    }

    @ModifyVariable(
            method = "updateTargetedEntity",
            at = @At(value = "STORE", ordinal = -1), // The second double variable assigned (9.0)
            ordinal = 1
    )
    private double modifySquaredMaxReach(double d) {
        Gurkreach gurkReach = new Gurkreach();
        return ModuleManager.isModuleEnabled("Gurkreach") ? gurkReach.getReach() * gurkReach.getReach() : d;
    }
}

 */

// go big or go play vanilla  (TO BE IMPROVED)
@Mixin(value = GameRenderer.class, priority = 1010)
public class GameRendererMixin {

    @Inject(method = "updateTargetedEntity", at = @At("HEAD"), cancellable = true)
    private void onUpdateTargetedEntity(float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity entity = client.getCameraEntity();
        if (entity != null) {
            if (client.world != null) {
                client.getProfiler().push("pick");
                client.targetedEntity = null;
                double d = 120;
                client.crosshairTarget = entity.raycast(d, tickDelta, false);
                Vec3d vec3d = entity.getCameraPosVec(tickDelta);
                boolean bl = false;
                int i = 3;
                double e = d;
                if (client.interactionManager.hasExtendedReach()) {
                    e = 120.0; // Replacing 6.0 with 120.0
                    d = e;
                } else {
                    if (d > 3.0) {
                        bl = true;
                    }

                    d = d;
                }

                e *= e;
                if (client.crosshairTarget != null) {
                    e = client.crosshairTarget.getPos().squaredDistanceTo(vec3d);
                }

                Vec3d vec3d2 = entity.getRotationVec(1.0F);
                Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
                float f = 1.0F;
                Box box = entity.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
                EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, vec3d, vec3d3, box, entityx -> !entityx.isSpectator() && entityx.canHit(), e);
                if (entityHitResult != null) {
                    Entity entity2 = entityHitResult.getEntity();
                    Vec3d vec3d4 = entityHitResult.getPos();
                    double g = vec3d.squaredDistanceTo(vec3d4);
                    if (bl && g > 14400.0) { // Replacing 9.0 with 14400.0
                        client.crosshairTarget = BlockHitResult.createMissed(vec3d4, Direction.getFacing(vec3d2.x, vec3d2.y, vec3d2.z), BlockPos.ofFloored(vec3d4));
                    } else if (g < e || client.crosshairTarget == null) {
                        client.crosshairTarget = entityHitResult;
                        if (entity2 instanceof LivingEntity || entity2 instanceof ItemFrameEntity) {
                            client.targetedEntity = entity2;
                        }
                    }
                }

                client.getProfiler().pop();
                ci.cancel(); // Cancel the original method to replace it with our modified one
            }
        }
    }
}
