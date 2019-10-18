package com.shinoow.btg.common.rituals;

import java.util.Random;

import com.shinoow.abyssalcraft.common.entity.EntityShubOffspring;
import com.shinoow.abyssalcraft.common.entity.demon.EntityEvilSheep;
import com.shinoow.btg.common.util.PlayerKillUtil;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ShubNiggurathInvocationRitual extends InvocationRitual {

	public ShubNiggurathInvocationRitual() {
		super("shubniggurathInvocation", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE), null, new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE),
				null, new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE), null, new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));

	}

	@Override
	public boolean canCompleteRitual(World world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	private EntityLiving getRandomSheep(World world){
		return world.rand.nextInt(4) == 0 ? new EntityShubOffspring(world) : world.rand.nextBoolean() ? new EntitySheep(world) : new EntityEvilSheep(world);
	}

	private int posNeg(Random rand){
		return rand.nextInt(10) * (rand.nextBoolean() ? -1 : 1);
	}

	@Override
	protected void completeRitualClient(World world, BlockPos pos, EntityPlayer player) {}

	@Override
	protected void completeRitualServer(World world, BlockPos pos, EntityPlayer player) {
		int n = world.rand.nextInt(30) + 10;
		for(int i = 0; i < n; i++){
			EntityLiving sheep = getRandomSheep(world);
			sheep.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			sheep.setLocationAndAngles(pos.getX() + posNeg(world.rand), pos.getY() + world.rand.nextInt(5), pos.getZ() + posNeg(world.rand), 0, 0);
			world.spawnEntity(sheep);
		}
		PlayerKillUtil.killEntity(player, PlayerKillUtil.shub_niggurath);
	}

}
