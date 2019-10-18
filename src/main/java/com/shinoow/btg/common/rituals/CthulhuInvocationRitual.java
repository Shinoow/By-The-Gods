package com.shinoow.btg.common.rituals;

import com.shinoow.btg.common.util.PlayerKillUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CthulhuInvocationRitual extends InvocationRitual {

	public CthulhuInvocationRitual() {
		super("cthulhuInvocation", Items.BED, null, Items.WATER_BUCKET, null, Items.BED, null, Items.WATER_BUCKET);

	}

	@Override
	public boolean canCompleteRitual(World world, BlockPos pos, EntityPlayer player) {

		return true;
	}

	@Override
	protected void completeRitualClient(World world, BlockPos pos, EntityPlayer player) {}

	@Override
	protected void completeRitualServer(World world, BlockPos pos, EntityPlayer player) {
		player.inventory.clear();
		PlayerKillUtil.killEntity(player, PlayerKillUtil.cthulhu);
	}
}
