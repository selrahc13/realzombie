package com.selrahc13.realzombie.common.misc;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Config {
  public static boolean debug;
  public static boolean canSpawnZombiesInDefaultWorld;

  public static void init(FMLPreInitializationEvent event) {
    Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "RealZombie Config.txt"));

    config.load();

    // Deprecated, using PotionUtils to manage ids
    //Effect.effectConfig(config);

    debug = config.get(Configuration.CATEGORY_GENERAL, "debug", false, "Should RealZombie log extra information?").getBoolean(false);
    canSpawnZombiesInDefaultWorld = config.get(Configuration.CATEGORY_GENERAL, "spawn zombies-in-default-world", false, "Should Real Zombies generate in the surface world?").getBoolean(true);

    config.save();
  }
}