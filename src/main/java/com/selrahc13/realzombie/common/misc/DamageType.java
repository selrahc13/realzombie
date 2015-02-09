package com.selrahc13.realzombie.common.misc;

import net.minecraft.util.DamageSource;

public class DamageType extends DamageSource {
  public static final DamageSource bleedOut = new DamageType("bleedOut").setDamageBypassesArmor();
  public static final DamageSource zombieInfection = new DamageType("zombieInfection").setDamageBypassesArmor();

  public DamageType(String damageType) {
    super(damageType);
  }
}