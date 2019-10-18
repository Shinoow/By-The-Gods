package com.shinoow.btg.common.rituals;

import com.shinoow.btg.common.util.PlayerKillUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class HasturInvocationRitual extends InvocationRitual {

	public HasturInvocationRitual() {
		super("hasturInvocation", new ItemStack(Items.NAME_TAG).setStackDisplayName("Hastur"), null, new ItemStack(Items.NAME_TAG).setStackDisplayName("Hastur"),
				null, new ItemStack(Items.NAME_TAG).setStackDisplayName("Hastur"), null, new ItemStack(Items.NAME_TAG).setStackDisplayName("Hastur"));
	}

	@Override
	public boolean canCompleteRitual(World world, BlockPos pos, EntityPlayer player) {

		return true;
	}

	@Override
	protected void completeRitualClient(World world, BlockPos pos, EntityPlayer player) {
		player.sendMessage(new TextComponentString("He whose name should not be said."));
	}

	@Override
	protected void completeRitualServer(World world, BlockPos pos, EntityPlayer player) {
		world.createExplosion(player, player.posX, player.posY, player.posZ, 16, true);
		PlayerKillUtil.killEntity(player, PlayerKillUtil.hastur_explosion);
	}
}
