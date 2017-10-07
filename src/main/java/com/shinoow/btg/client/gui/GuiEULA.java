package com.shinoow.btg.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.logging.log4j.Level;

import com.shinoow.btg.ByTheGods;

public class GuiEULA extends GuiScreen {

	private GuiMainMenu parent;


	public GuiEULA(GuiMainMenu parent){
		this.parent = parent;
	}

	@Override
	public void initGui(){
		super.initGui();
		int center = width / 2;
		int y = height / 2;

		buttonList.add(new GuiButton(0, center - 150, y + 20, 132, 20, "Accept EULA"));
		buttonList.add(new GuiButton(1, center + 150 - 132, y + 20, 132, 20, "Quit Game"));
	}


	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		int center = width / 2;
		int y = height / 2;
		super.drawScreen(mouseX, mouseY, partialTicks);
		fontRendererObj.drawSplitString("Thank you for installing By The Gods!\nDue to the destructive things this mod can do to the world (and anything inside it), I (Shinoow) need you to accept the mod's EULA (a copy can be found in the mod's jar file, but also on the GitHub repository and the CurseForge page), which in short includes you accepting to take all responsibility for anything bad happening due to usage of this mod, while also revoking your right to blame me for anything (as it's your responsibility, not mine)\n\nIf you do not wish to comply to these terms, please quit the game and remove the mod.", center - 140, y - 90, 300, 0xFFFFFF);
	}

	@Override
	protected void actionPerformed(GuiButton button){
		if(button.id == 0){
			ByTheGods.acceptedEULA = true;
			ByTheGods.cfg.get(Configuration.CATEGORY_GENERAL, "Accepted EULA", false, "Whether or not the mod's EULA has been accepted. The game won't finish loading as long as it's false.").setValue(true);
			ByTheGods.cfg.save();
			mc.displayGuiScreen(parent);
		} else if(button.id == 1){
			FMLLog.log("By The Gods", Level.WARN, "EULA declined, shutting down game! Either remove the mod or accept the EULA on next launch!");
			FMLCommonHandler.instance().exitJava(1, true);
		}
	}

	public static void init(){
		if(!ByTheGods.acceptedEULA)
			MinecraftForge.EVENT_BUS.register(new GuiEULA(null));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onGuiOPened(GuiOpenEvent event){
		if(event.getGui() instanceof GuiMainMenu && !ByTheGods.acceptedEULA)
			event.setGui(new GuiEULA((GuiMainMenu) event.getGui()));
	}
}
