package com.shinoow.btg.client;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.shinoow.btg.client.gui.GuiEULA;
import com.shinoow.btg.client.render.RenderNyarlathotepTNT;
import com.shinoow.btg.common.CommonProxy;
import com.shinoow.btg.common.entity.EntityNyarlathotepTNT;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(){
		RenderingRegistry.registerEntityRenderingHandler(EntityNyarlathotepTNT.class, manager -> new RenderNyarlathotepTNT(manager));

		GuiEULA.init();
	}

	@Override
	public void init(){

	}

	@Override
	public void postInit(){

	}
}
