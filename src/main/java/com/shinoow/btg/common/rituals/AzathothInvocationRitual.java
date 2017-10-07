package com.shinoow.btg.common.rituals;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.shinoow.btg.common.util.PlayerKillUtil;

public class AzathothInvocationRitual extends InvocationRitual {

	public AzathothInvocationRitual() {
		super("azathothInvocation", Blocks.OBSIDIAN, null, Items.ENDER_EYE, null, Blocks.OBSIDIAN, null, Items.ENDER_EYE);
	}

	@Override
	public boolean canCompleteRitual(World world, BlockPos pos, EntityPlayer player) {
		world.getWorldInfo().setHardcore(true);
		if(!world.isRemote && !FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer())
			for(WorldServer ws : FMLCommonHandler.instance().getMinecraftServerInstance().worldServers)
				ws.getWorldInfo().setHardcore(true);
		return true;
	}

	@Override
	protected void completeRitualClient(World world, BlockPos pos, EntityPlayer player) {}

	/**
	 * This is a rabbit hole, but nothing will remain
	 * @param f Probably the world directory or something inside it
	 */
	private void deleteFiles(File f){
		f.deleteOnExit();
		if(f.isDirectory())
			for(File f1 : f.listFiles())
				deleteFiles(f1);
	}

	@Override
	protected void completeRitualServer(World world, BlockPos pos, EntityPlayer player) {
		if(FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer())
			PlayerKillUtil.endAllLife(world, player);
		else {
			for(WorldServer ws : FMLCommonHandler.instance().getMinecraftServerInstance().worldServers){
				deleteFiles(ws.getSaveHandler().getWorldDirectory());
				PlayerKillUtil.endAllLife(ws, player);
			}
			FMLCommonHandler.instance().getMinecraftServerInstance().stopServer();
		}
	}
}
