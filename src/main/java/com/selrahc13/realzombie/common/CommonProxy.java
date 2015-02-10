package com.selrahc13.realzombie.common;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.biome.BiomeGenBase;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.selrahc13.realzombie.RealZombie;
import com.selrahc13.realzombie.common.effects.Effect;
import com.selrahc13.realzombie.common.entity.EntityCrawler;
import com.selrahc13.realzombie.common.entity.EntityWalker;
import com.selrahc13.realzombie.common.misc.Config;
import com.selrahc13.realzombie.common.misc.DamageType;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;

public class CommonProxy {
  public void preload(FMLPreInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(RealZombie.proxy);
    Config.init(event);

    Effect.loadEffects();
    Effect.register();
    EntityRegistry.registerGlobalEntityID(EntityWalker.class, "Walker", EntityRegistry.findGlobalUniqueEntityId(), 1, 2);
    EntityRegistry.registerGlobalEntityID(EntityCrawler.class, "Crawler", EntityRegistry.findGlobalUniqueEntityId(), 1, 2);

    BiomeGenBase[] allBiomes = Iterators.toArray(Iterators.filter(Iterators.forArray(BiomeGenBase.getBiomeGenArray()), Predicates.notNull()), BiomeGenBase.class);
    // TODO: Make removing all other hostile spawns optional by config
    EntityRegistry.removeSpawn(EntityZombie.class, EnumCreatureType.monster, allBiomes);
    EntityRegistry.removeSpawn(EntitySkeleton.class, EnumCreatureType.monster, allBiomes);
    EntityRegistry.removeSpawn(EntitySpider.class, EnumCreatureType.monster, allBiomes);
    EntityRegistry.removeSpawn(EntityEnderman.class, EnumCreatureType.monster, allBiomes);
    EntityRegistry.removeSpawn(EntityCaveSpider.class, EnumCreatureType.monster, allBiomes);
    EntityRegistry.removeSpawn(EntityCreeper.class, EnumCreatureType.monster, allBiomes);
    EntityRegistry.removeSpawn(EntitySlime.class, EnumCreatureType.monster, allBiomes);
    EntityRegistry.removeSpawn(EntityWitch.class, EnumCreatureType.monster, allBiomes);

    EntityRegistry.addSpawn(EntityWalker.class, 300, 1, 10, EnumCreatureType.monster, allBiomes);
    EntityRegistry.addSpawn(EntityCrawler.class, 200, 1, 4, EnumCreatureType.monster, allBiomes);

  }

  public void load(FMLInitializationEvent event) {
 }

  public void postload(FMLPostInitializationEvent event) {
  }

  public void serverStarted(FMLServerStartedEvent event) {
  }


  @SubscribeEvent
  public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
    if (event.entityLiving.isPotionActive(Effect.bleeding)) {
      if (event.entityLiving.getActivePotionEffect(Effect.bleeding).getDuration() == 0) {
        event.entityLiving.removePotionEffect(Effect.bleeding.id);
        return;
      }
      if (event.entityLiving.worldObj.rand.nextInt(100) == 0) {
        event.entityLiving.attackEntityFrom(DamageType.bleedOut, 2);
      }
    }
    // TODO: Do we want zombification to auto-expire?
    if (event.entityLiving.isPotionActive(Effect.zombification)) {
      if (event.entityLiving.getActivePotionEffect(Effect.zombification).getDuration() == 0) {
    	event.entityLiving.addPotionEffect(new PotionEffect(Effect.zombification.id, 3000, 10));
        //event.entityLiving.removePotionEffect(Effect.zombification.id);
        return;
      }
      if (event.entityLiving.worldObj.rand.nextInt(100) == 0) {
        if (event.entityLiving.getHealth() > 1.0F) {
          event.entityLiving.attackEntityFrom(DamageType.zombieInfection, 1.0F);
          event.entityLiving.addPotionEffect(new PotionEffect(Potion.hunger.id, 1800, 10));
        } else {
          EntityWalker var2 = new EntityWalker(event.entityLiving.worldObj);
          var2.setLocationAndAngles(event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, event.entityLiving.rotationYaw, event.entityLiving.rotationPitch);
          event.entityLiving.worldObj.spawnEntityInWorld(var2);
          event.entityLiving.attackEntityFrom(DamageType.zombieInfection, 1.0F);
        }
      }
    }
  }

}