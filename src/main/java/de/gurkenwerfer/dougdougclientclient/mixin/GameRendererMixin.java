package de.gurkenwerfer.dougdougclientclient.mixin;

import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import de.gurkenwerfer.dougdougclientclient.modules.Gurkreach;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;


/*
@Mixin(value = GameRenderer.class, priority = 1000)
public abstract class GameRendererMixin {

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 6))
    private double updateTargetedEntityModifySurvivalReach(double d) {
        Gurkreach gurkReach = new Gurkreach();
        return gurkReach.isEnabled() ? gurkReach.getReach() : d;
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9))
    private double updateTargetedEntityModifySquaredMaxReach(double d) {
        Gurkreach gurkReach = new Gurkreach();
        return gurkReach.isEnabled() ? gurkReach.getReach() * gurkReach.getReach() : d;
    }
}
 */

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