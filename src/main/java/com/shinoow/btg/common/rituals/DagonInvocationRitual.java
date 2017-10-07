package com.shinoow.btg.common.rituals;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.shinoow.btg.common.util.PlayerKillUtil;

public class DagonInvocationRitual extends InvocationRitual {

	public DagonInvocationRitual() {
		super("dagonInvocation", new ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE), null, Items.PRISMARINE_SHARD, null,
				new ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE), null, Items.PRISMARINE_SHARD);
	}

	@Override
	public boolean canCompleteRitual(World world, BlockPos pos, EntityPlayer player) {

		return true;
	}

	@Override
	protected void completeRitualClient(World world, BlockPos pos, EntityPlayer player) {}

	@Override
	protected void completeRitualServer(World world, BlockPos pos, EntityPlayer player) {
		PlayerKillUtil.killEntity(player, PlayerKillUtil.dagon);

		IBlockState prism = Blocks.PRISMARINE.getDefaultState();
		IBlockState brick = prism.withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.BRICKS);
		IBlockState dark = prism.withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.DARK);

		for(int x = -5; x < 6; x++)
			for(int z = -3; z < 4; z++)
				for(int y = -5; y < 21; y++){
					boolean flag = y == 17 || y < 8 && y > 4;
					boolean flag1 = y == 16 || y == 18 || y == 8 || y == 4;
					if(z == -3 || z == 3)
						if(x > -5 && x < 5){
							world.setBlockState(player.getPosition().add(x, y, z), flag ? dark : flag1 ? brick : prism);
							world.setBlockState(player.getPosition().add(z, y, x), flag ? dark : flag1 ? brick : prism);
						}
					if(z == -2 || z == 2)
						if(x > -6 && x < 6){
							world.setBlockState(player.getPosition().add(x, y, z), flag ? dark : flag1 ? brick : prism);
							world.setBlockState(player.getPosition().add(z, y, x), flag ? dark : flag1 ? brick : prism);
						}
					if(z > -2 && z < 2){
						world.setBlockState(player.getPosition().add(x, y, z), flag ? dark : flag1 ? brick : prism);
						world.setBlockState(player.getPosition().add(z, y, x), flag ? dark : flag1 ? brick : prism);
					}
				}
	}
}
