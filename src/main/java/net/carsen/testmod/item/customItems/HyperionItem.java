package net.carsen.testmod.item.customItems;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HyperionItem extends Item {

    public HyperionItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        if (pLevel.isClientSide()) return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));

        // Teleportation Logic
        double[] cords = getTeleportCoordinates(pPlayer);

        if (!canTeleport(pLevel, new BlockPos((int) (pPlayer.getX() + cords[0]), (int) (pPlayer.getY() + cords[1]), (int) (pPlayer.getZ() + cords[2])))) {
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));
        }

        pPlayer.teleportTo(
                Math.floor(pPlayer.getX() + cords[0]) + 0.5,
                pPlayer.getY() + cords[1],
                Math.floor(pPlayer.getZ() + cords[2]) + 0.5);


        // Create an explosion at the new location of the player

        Level level = pPlayer.getCommandSenderWorld();

        Level.ExplosionInteraction interaction = Level.ExplosionInteraction.NONE;

        level.explode(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), 6.0F, false, interaction);

        return InteractionResultHolder.pass(pPlayer.getItemInHand(pHand));
    }

    // Gets the coordinates that the player will teleport to relative to where the player is looking
    private double[] getTeleportCoordinates(Player pPlayer) {
        float yaw = pPlayer.getYRot();
        float pitch = pPlayer.getXRot();
        double yBlocks = 10 * -Math.sin(pitch * Math.PI / 180); // Use 10 to travel 10 blocks total
        double xBlocks = 10 * Math.cos(pitch * Math.PI / 180) * -Math.sin(yaw * Math.PI / 180);
        double zBlocks = 10 * Math.cos(pitch * Math.PI / 180) * Math.cos(yaw * Math.PI / 180);
        return new double[] { xBlocks, yBlocks, zBlocks };
    }

    private boolean canTeleport(Level pLevel, BlockPos blockPos) {
        return pLevel.getBlockState(blockPos).isAir();
    }
}
