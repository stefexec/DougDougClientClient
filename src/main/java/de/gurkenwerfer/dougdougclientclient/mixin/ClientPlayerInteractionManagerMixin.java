package de.gurkenwerfer.dougdougclientclient.mixin;

import de.gurkenwerfer.dougdougclientclient.classes.ModuleManager;
import de.gurkenwerfer.dougdougclientclient.modules.Gurkreach;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

	@Unique
	Gurkreach gurkReach = new Gurkreach();

	@Shadow
	protected abstract void syncSelectedSlot();

	@Inject(method = "getReachDistance", at = @At("HEAD"), cancellable = true)
	private void onGetReachDistance(CallbackInfoReturnable<Float> info) {
		if (ModuleManager.isModuleEnabled("Gurkreach")) {
			info.setReturnValue(gurkReach.getReach());
			///System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXX" + gurkReach.getReach());
		} else {
			info.setReturnValue(5.0f);
		}
	}

	@Inject(method = "hasExtendedReach", at = @At("HEAD"), cancellable = true)
	private void onHasExtendedReach(CallbackInfoReturnable<Boolean> info) {
		if (ModuleManager.isModuleEnabled("Gurkreach")) info.setReturnValue(false);
	}

	@Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
	private void onAttackBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> info) {
		MinecraftClient mc = MinecraftClient.getInstance();
		BlockState state = mc.world.getBlockState(blockPos);
		if (state.calcBlockBreakingDelta(mc.player, mc.world, blockPos) > 0.5f) {
			mc.world.breakBlock(blockPos, true, mc.player);
			((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, direction), null);
			((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, direction), null);
			info.setReturnValue(true);
		}
	}

	@Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
	private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo info) {
		MinecraftClient mc = MinecraftClient.getInstance();
		mc.player.sendMessage(Text.of("ATTACK!"));

		if (ModuleManager.isModuleEnabled("Gurkreach")) {

			if (target == null) return;
			Vec3d oldPos = player.getPos();
			Vec3d newPos = target.getPos();

			mc.player.sendMessage(Text.of("pos: " + oldPos + newPos));

			teleportFromTo(MinecraftClient.getInstance(), oldPos, newPos);

			Packet<?> packet = PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking());
			((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(packet, null);

			teleportFromTo(MinecraftClient.getInstance(), newPos, oldPos);
		}
	}

	@Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
	public void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {

		MinecraftClient mc = MinecraftClient.getInstance();

		if (ModuleManager.isModuleEnabled("Gurkreach")) {

			Vec3d oldPos = player.getPos();
			Vec3d newPos = hitResult.getPos();

			teleportFromTo(MinecraftClient.getInstance(), oldPos, newPos);

			Packet<?> packet = new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, hitResult, 0);
			((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(packet, null);

			teleportFromTo(MinecraftClient.getInstance(), newPos, oldPos);

			cir.setReturnValue(ActionResult.SUCCESS);
		}
	}

	@Unique
	void teleportFromTo(MinecraftClient client, Vec3d from, Vec3d to) {
		double maxDist = 7;
		double targetDist = Math.ceil(from.distanceTo(to) / maxDist);

		MinecraftClient mc = MinecraftClient.getInstance();

		for (int i = 1; i <= targetDist; i++) {

			double x = from.x + (to.x - from.x) / targetDist * i;
			double y = from.y + (to.y - from.y) / targetDist * i;
			double z = from.z + (to.z - from.z) / targetDist * i;

			Packet<?> packet = new PlayerMoveC2SPacket.PositionAndOnGround(
					x,
					y + 1,
					z,
					mc.player.isOnGround()
			);

			// Send packet immediately using accessor
			((ClientConnectionAccessor) mc.getNetworkHandler().getConnection())._send(packet, null);

		}
	}
}
