package com.selrahc13.realzombie.common.misc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.potion.Potion;

import com.selrahc13.realzombie.RealZombie;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class PotionUtils {

    public static void extendArray(int newSlots) {
        RealZombie.logger.info("Extending potions array by " + newSlots);
        Potion[] potionTypes = new Potion[Potion.potionTypes.length + newSlots];
        System.arraycopy(Potion.potionTypes, 0, potionTypes, 0, Potion.potionTypes.length);
        Field potionArray = ReflectionHelper.findField(Potion.class, ObfuscationReflectionHelper.remapFieldNames(Potion.class.getName(), "potionArray", "field_76425_a", "a"));
        try {
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(potionArray, potionArray.getModifiers() & ~Modifier.FINAL);
            potionArray.set(null, potionTypes);
        } catch(Exception e) {
            RealZombie.logger.error("There was an error in extending the potions array", e);
        }
    }

    public static int getNextPotionID() {
        int firstOpen = 24; //The first open index in vanilla
        for(int i = firstOpen; i < Potion.potionTypes.length; i++) {
            if(Potion.potionTypes[i] == null) {
                return i;
            }
        }
        extendArray(1);
        return getNextPotionID();
    }
}