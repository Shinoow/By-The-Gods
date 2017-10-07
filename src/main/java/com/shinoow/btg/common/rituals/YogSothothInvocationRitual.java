package com.shinoow.btg.common.rituals;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.shinoow.btg.common.util.PlayerKillUtil;

public class YogSothothInvocationRitual extends InvocationRitual {

	public YogSothothInvocationRitual() {
		super("yogsothothInvocation", Items.CLOCK, null, new ItemStack[]{new ItemStack(Items.OAK_DOOR),
				new ItemStack(Items.SPRUCE_DOOR), new ItemStack(Items.BIRCH_DOOR), new ItemStack(Items.JUNGLE_DOOR),
				new ItemStack(Items.ACACIA_DOOR), new ItemStack(Items.DARK_OAK_DOOR)}, null, Items.CLOCK, null, new ItemStack[]{new ItemStack(Items.OAK_DOOR),
				new ItemStack(Items.SPRUCE_DOOR), new ItemStack(Items.BIRCH_DOOR), new ItemStack(Items.JUNGLE_DOOR),
				new ItemStack(Items.ACACIA_DOOR), new ItemStack(Items.DARK_OAK_DOOR)});
	}

	@Override
	public boolean canCompleteRitual(World world, BlockPos pos, EntityPlayer player) {

		return true;
	}

	@Override
	protected void completeRitualClient(World world, BlockPos pos, EntityPlayer player) {}

	@Override
	protected void completeRitualServer(World world, BlockPos pos, EntityPlayer player) {
		BlockPos p = player.getPosition();

		player.inventory.clear();
		player.setPositionAndUpdate(p.getX(), -10, p.getZ());
		PlayerKillUtil.killEntity(player, PlayerKillUtil.yog_sothoth);

		world.addWeatherEffect(new EntityLightningBolt(world, p.getX(), p.getY(), p.getZ(), false));
	}

}
