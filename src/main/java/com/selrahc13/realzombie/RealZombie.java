package com.selrahc13.realzombie;

import java.util.Random;

import org.apache.logging.log4j.Logger;

import com.selrahc13.realzombie.common.CommonProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;

@Mod(
	modid=Reference.MODID, 
	dependencies=Reference.DEPEND, 
	name=Reference.NAME, 
	version=Reference.VERSION
)
public class RealZombie {
	/*
  public static CreativeTabs creativeTab = new CreativeTabs("creativeTabRealZombie") {
    @Override public Item getTabIconItem() {
    	//TODO: Replace icon
      return Items.egg;
    }
  };*/

  @Instance("RealZombie")
  public static RealZombie INSTANCE;

  @Metadata
  public static ModMetadata meta;

  public static Logger logger;  
  
  public static Random rand = new Random();
  
  public static boolean usingMalisisDoors = false;
  public static boolean usingCarpentersBlocks = false;
  
  @SidedProxy(clientSide = "com.selrahc13.realzombie.client.ClientProxy", serverSide = "com.selrahc13.realzombie.common.CommonProxy")
  public static CommonProxy proxy;

  @EventHandler
  public void preload(FMLPreInitializationEvent event) {
    logger = event.getModLog();
    proxy.preload(event);
  }

  @EventHandler
  public void load(FMLInitializationEvent event) {
    proxy.load(event);
  }

  @EventHandler
  public void postload(FMLPostInitializationEvent event) {
    proxy.postload(event);
    if (usingMalisisDoors = Loader.isModLoaded("malisisdoors")) {
    	logger.info("MalsisDoors is loaded - Break all the doors!");
    }
    if (usingCarpentersBlocks = Loader.isModLoaded("CarpentersBlocks")) {
    	logger.info("Carpenter's Blocks is loaded - Break even more doors!");
    }
  }

  @EventHandler
  public void serverStarted(FMLServerStartedEvent event) {
    proxy.serverStarted(event);
  }

  public static boolean isServer() {
    return FMLCommonHandler.instance().getEffectiveSide().isServer();
  }

  public static boolean isClient() {
    return FMLCommonHandler.instance().getEffectiveSide().isClient();
  }
  
  public static float random (int min, int max) {
	  return (float) rand.nextInt(max - min + 1) + min;
  }
  
}