package de.gurkenwerfer.dougdougclientclient.mixin;

import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import de.gurkenwerfer.dougdougclientclient.modules.Gurkreach;
import net.minecraft.client.render.GameRenderer;
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

THESE WORK AND SHOULD BE PROTECTED AT ALL COST!!!!!!!!!!!!!
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

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "updateTargetedEntity", at = @At("HEAD"), cancellable = true)
    private void injectUpdateTargetedEntity(CallbackInfo ci) {
        // You can modify the logic of the entire method if necessary
    }

    @ModifyVariable(method = "updateTargetedEntity", at = @At("STORE"), ordinal = 0)
    private double modifySurvivalReach(double d) {
        Gurkreach gurkReach = new Gurkreach();
        return ModuleManager.isModuleEnabled("Gurkreach") ? gurkReach.getReach() : d;
    }

    @ModifyVariable(method = "updateTargetedEntity", at = @At("STORE"), ordinal = 1)
    private double modifySquaredMaxReach(double d) {
        Gurkreach gurkReach = new Gurkreach();
        return ModuleManager.isModuleEnabled("Gurkreach") ? gurkReach.getReach() * gurkReach.getReach() : d;
    }
}