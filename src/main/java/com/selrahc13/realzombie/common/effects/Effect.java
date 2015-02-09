package com.selrahc13.realzombie.common.effects;

import net.minecraft.potion.Potion;
import cpw.mods.fml.common.registry.LanguageRegistry;

import com.selrahc13.realzombie.common.misc.PotionUtils;

public class Effect extends Potion {
  public static Effect bleeding;
  public static int bleedingId;
  public static Effect zombification;
  public static int zombificationId;

  @SuppressWarnings("deprecation")
public Effect(int id, boolean isBadEffect, int color, String name) {
    super(id, isBadEffect, color);
    setPotionName("potion." + name);
    LanguageRegistry.instance().addStringLocalization(getName(), name);
  }

  @Override
  public Potion setIconIndex(int x, int y) {
    super.setIconIndex(x, y);
    return this;
  }

  public static void loadEffects() {
    bleeding = new Effect(PotionUtils.getNextPotionID(), true, 5149489, "Bleeding");
    zombification = new Effect(PotionUtils.getNextPotionID(), true, 5149489, "Zombification");
  }

  // Deprecated, using PotionUtils to retrieve next available potion ID to avoid conflicts
  /*
  public static void effectConfig(Configuration config) {
    bleedingId = config.get("effect", "bleedingId", 29, "Bleeding Effect ID").getInt();
    zombificationId = config.get("effect", "zombificationId", 30, "Zombification Effect ID").getInt();
  }
*/
  public static void register() {
	
    Potion.potionTypes[bleeding.getId()] = bleeding;
    Potion.potionTypes[zombification.getId()] = zombification;
  }
}