package com.shinoow.btg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import org.apache.logging.log4j.Level;

import com.shinoow.abyssalcraft.api.ritual.RitualRegistry;
import com.shinoow.btg.common.CommonProxy;
import com.shinoow.btg.common.entity.EntityNyarlathotepTNT;
import com.shinoow.btg.common.rituals.*;

@Mod(modid = ByTheGods.modid, name = ByTheGods.name, version = ByTheGods.version, dependencies = "required-after:forge@[forgeversion,);required-after:abyssalcraft@[1.9.4,)", useMetadata = false, acceptedMinecraftVersions = "[1.12.2]", updateJSON = "https://raw.githubusercontent.com/Shinoow/By-The-Gods/master/version.json", certificateFingerprint = "cert_fingerprint")
public class ByTheGods {

	public static final String version = "btg_version";
	public static final String modid = "btg";
	public static final String name = "By The Gods";

	@Metadata(modid)
	public static ModMetadata metadata;

	@Instance(modid)
	public static ByTheGods instance;

	@SidedProxy(clientSide = "com.shinoow.btg.client.ClientProxy",
			serverSide = "com.shinoow.btg.common.CommonProxy")
	public static CommonProxy proxy;

	public static Configuration cfg;

	public static boolean acceptedEULA;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		metadata = event.getModMetadata();
		metadata.description = metadata.description +"\n\n\u00a76Supporters: "+getSupporterList()+"\u00a7r";

		cfg = new Configuration(event.getSuggestedConfigurationFile());
		syncConfig();

		EntityRegistry.registerModEntity(new ResourceLocation(modid, "nyarlathoteptnt"), EntityNyarlathotepTNT.class, "nyarlathoteptnt", 1, instance, 80, 3, true);

		proxy.preInit();

		if(event.getSide().isServer() && !acceptedEULA){
			FMLLog.log("By The Gods", Level.WARN, "EULA has not been accepted! Either remove the mod or accept the EULA (set the value in btg.cfg to true) before the next launch!");
			FMLCommonHandler.instance().exitJava(1, true);
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event){
		RitualRegistry.instance().registerRitual(new CthulhuInvocationRitual());
		RitualRegistry.instance().registerRitual(new NyarlathotepInvocationRitual());
		RitualRegistry.instance().registerRitual(new HasturInvocationRitual());
		RitualRegistry.instance().registerRitual(new DagonInvocationRitual());
		RitualRegistry.instance().registerRitual(new AzathothInvocationRitual());
		RitualRegistry.instance().registerRitual(new ShubNiggurathInvocationRitual());
		RitualRegistry.instance().registerRitual(new YogSothothInvocationRitual());

		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit();
	}

	@EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		FMLLog.log("By The Gods", Level.WARN, "Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
	}

	private static void syncConfig(){

		acceptedEULA = cfg.get(Configuration.CATEGORY_GENERAL, "Accepted EULA", false, "Whether or not the mod's EULA has been accepted. The game won't finish loading as long as it's false.").getBoolean();

		if(cfg.hasChanged())
			cfg.save();
	}

	private String getSupporterList(){
		BufferedReader nameFile;
		String names = "";
		try {
			nameFile = new BufferedReader(new InputStreamReader(new URL("https://raw.githubusercontent.com/Shinoow/AbyssalCraft/master/supporters.txt").openStream()));

			names = nameFile.readLine();
			nameFile.close();

		} catch (IOException e) {
			FMLLog.log("By The Gods", Level.ERROR, "Failed to fetch supporter list, using local version!");
			names = "Tedyhere";
		}

		return names;
	}
}
