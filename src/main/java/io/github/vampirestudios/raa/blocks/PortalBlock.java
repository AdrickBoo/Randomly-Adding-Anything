package io.github.vampirestudios.raa.blocks;

import io.github.vampirestudios.raa.generation.dimensions.DimensionData;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class PortalBlock extends Block {
    private DimensionType dimensionType;
    private DimensionData dimensionData;

    public PortalBlock(DimensionData  dimensionData, DimensionType dimensionType) {
        super(Block.Settings.of(Material.PORTAL));
        this.dimensionType = dimensionType;
        this.dimensionData = dimensionData;
    }

    @Override
    public Text getName() {
        return new TranslatableText("text.raa.block.portal" , new LiteralText(dimensionData.getName()));
    }

    @Override
    public ActionResult onUse(BlockState blockState_1, World world_1, BlockPos pos, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1) {
        if (!world_1.isClient) {
            BlockPos playerPos = playerEntity_1.getBlockPos();
            if (playerPos.getX() == pos.getX() && playerPos.getZ() == pos.getZ() && playerPos.getY() == pos.getY() + 1) {
                if(playerEntity_1.world.dimension.getType() == this.dimensionType) {
                    // coming from our custom dimension
                    FabricDimensions.teleport(playerEntity_1, DimensionType.OVERWORLD, null);
                } else {
                    // going to our custom dimension
                    FabricDimensions.teleport(playerEntity_1, this.dimensionType, null);
                }
            }
        }
        return super.onUse(blockState_1, world_1, pos, playerEntity_1, hand_1, blockHitResult_1);
    }
}
