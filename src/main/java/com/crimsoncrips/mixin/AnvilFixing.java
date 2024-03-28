package com.crimsoncrips.mixin;


import com.crimsoncrips.HammerTag;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.betterx.bclib.blocks.BaseAnvilBlock;
import org.betterx.bclib.interfaces.BlockModelProvider;
import org.betterx.bclib.interfaces.CustomItemProvider;
import org.betterx.bclib.interfaces.tools.AddMineablePickaxe;
import org.betterx.betterend.blocks.AeterniumAnvil;
import org.betterx.betterend.registry.EndBlocks;
import org.betterx.betterend.registry.EndItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.sound.SoundCategory.BLOCKS;

@Mixin (BaseAnvilBlock.class)
public abstract class AnvilFixing extends AnvilBlock implements AddMineablePickaxe, BlockModelProvider, CustomItemProvider {


	@Shadow @Final public static IntProperty DESTRUCTION;

	@Shadow public abstract int getMaxDurability();

	public AnvilFixing(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack offhandstack = player.getOffHandStack();
		ItemStack mainhandstack = player.getMainHandStack();
		Block block = world.getBlockState(pos).getBlock();
		int destruction = (Integer)state.get(DESTRUCTION);
		Direction direction = state.get(FACING);
			if (offhandstack.getItem() == Items.IRON_INGOT && destruction > 0 && mainhandstack.isIn(HammerTag.HAMMER)) {
				if (!player.isCreative()) offhandstack.setCount(offhandstack.getCount() - 1);
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_PLACE, BLOCKS, 0.5F, 1.0F);
				world.setBlockState(pos, block.getDefaultState().with(DESTRUCTION, destruction - 1).with(FACING, direction), 4);
				mainhandstack.setDamage(getMaxDurability() - 5);
				return ActionResult.SUCCESS;
			} else if (offhandstack.getItem() == Items.OBSIDIAN && destruction < 2 && mainhandstack.isIn(HammerTag.HAMMER)) {
				if (!player.isCreative()) offhandstack.setCount(offhandstack.getCount() - 1);
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_PLACE, BLOCKS, 0.5F, 0F);
				world.setBlockState(pos, block.getDefaultState().with(DESTRUCTION, destruction + 1).with(FACING, direction), 4);
				mainhandstack.setDamage(getMaxDurability() - 5);
				return ActionResult.SUCCESS;
			} else {
			player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
			player.incrementStat(Stats.INTERACT_WITH_ANVIL);
			return ActionResult.CONSUME ;
		}
	}


}
