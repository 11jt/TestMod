package net.carsen.testmod.item.customItems;

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
        if (!pLevel.isClientSide()) {
            double[] cords = getTeleportCoordinates(pPlayer);
            pPlayer.teleportTo(
                    Math.floor(pPlayer.getX() + cords[0]) + 0.5,
                    pPlayer.getY() + cords[1],
                    Math.floor(pPlayer.getZ() + cords[2]) + 0.5);
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pHand));
    }

    private double[] getTeleportCoordinates(Player pPlayer) {
        float yaw = pPlayer.getYRot();
        float pitch = pPlayer.getXRot();
        double yBlocks = 10 * -Math.sin(pitch * Math.PI / 180);
        double xBlocks = 10 * Math.cos(pitch * Math.PI / 180) * -Math.sin(yaw * Math.PI / 180) + 0.5;
        double zBlocks = 10 * Math.cos(pitch * Math.PI / 180) * Math.cos(yaw * Math.PI / 180) + 0.5;
        return new double[] { xBlocks, yBlocks, zBlocks };
    }
}
