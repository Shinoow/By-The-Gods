package com.shinoow.btg.common.rituals;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.shinoow.abyssalcraft.lib.util.SpecialTextUtil;
import com.shinoow.btg.common.entity.EntityNyarlathotepTNT;

public class NyarlathotepInvocationRitual extends InvocationRitual {

	public NyarlathotepInvocationRitual() {
		super("nyarlathotepInvocation", Blocks.TNT, null, Blocks.TNT, null, Blocks.TNT, null, Blocks.TNT);

	}

	@Override
	public boolean canCompleteRitual(World world, BlockPos pos, EntityPlayer player) {

		return true;
	}

	private int posNeg(Random rand){
		return rand.nextInt(10) * (rand.nextBoolean() ? -1 : 1);
	}

	@Override
	protected void completeRitualClient(World world, BlockPos pos, EntityPlayer player) {}

	@Override
	protected void completeRitualServer(World world, BlockPos pos, EntityPlayer player) {

		int n = world.rand.nextInt(30) + 10;
		SpecialTextUtil.customGroup(world, "Nyarlathotep", "Gnark gnark! Didn't see that coming now, did you "+player.getName()+"? Foolish human.");
		for(int i = 0; i < n; i++){
			EntityNyarlathotepTNT tnt = new EntityNyarlathotepTNT(world, pos.getX() + posNeg(world.rand), pos.getY() + world.rand.nextInt(5), pos.getZ() + posNeg(world.rand), null);
			world.spawnEntity(tnt);
		}
	}
}
