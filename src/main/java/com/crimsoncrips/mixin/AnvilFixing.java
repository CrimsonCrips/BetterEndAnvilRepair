package com.crimsoncrips.mixin;

import com.crimsoncrips.BEAnvilTags;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import static net.minecraft.block.AnvilBlock.FACING;
import static net.minecraft.sound.SoundCategory.BLOCKS;
import static org.betterx.bclib.blocks.BaseAnvilBlock.DESTRUCTION;

@Mixin(AnvilBlock.class)
public abstract class AnvilFixing extends FallingBlock {

	public AnvilFixing(Settings settings) {
		super(settings);
	}
	public abstract int getMaxDurability();

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ActionResult actionResult;
		if (state.isIn(BEAnvilTags.BETTERANVILS)) {
			ItemStack offhandstack = player.getOffHandStack();
			ItemStack mainhandstack = player.getMainHandStack();
			Block block = world.getBlockState(pos).getBlock();
			int destruction = (Integer) state.get(DESTRUCTION);
			Direction direction = state.get(FACING);
			if (offhandstack.getItem() == Items.IRON_INGOT && destruction > 0 && mainhandstack.isIn(BEAnvilTags.HAMMER)) {
				if (!player.isCreative()) offhandstack.setCount(offhandstack.getCount() - 1);
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_PLACE, BLOCKS, 0.5F, 1.0F);
				world.setBlockState(pos, block.getDefaultState().with(DESTRUCTION, destruction - 1).with(FACING, direction), 4);
				actionResult = ActionResult.SUCCESS;
			} else if (offhandstack.getItem() == Items.OBSIDIAN && destruction < 2 && mainhandstack.isIn(BEAnvilTags.HAMMER)) {
				if (!player.isCreative()) offhandstack.setCount(offhandstack.getCount() - 1);
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_PLACE, BLOCKS, 0.5F, 0F);
				world.setBlockState(pos, block.getDefaultState().with(DESTRUCTION, destruction + 1).with(FACING, direction), 4);
				actionResult = ActionResult.SUCCESS;
			} else {
				player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
				player.incrementStat(Stats.INTERACT_WITH_ANVIL);
				actionResult = ActionResult.CONSUME;
			}
		} else {
			player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
			player.incrementStat(Stats.INTERACT_WITH_ANVIL);
			actionResult = ActionResult.CONSUME;
		}
		return actionResult;
	}
}
