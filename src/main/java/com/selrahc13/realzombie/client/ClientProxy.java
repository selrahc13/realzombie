package com.selrahc13.realzombie.client;

import com.selrahc13.realzombie.client.render.RenderCrawler;
import com.selrahc13.realzombie.client.render.RenderWalker;
import com.selrahc13.realzombie.common.CommonProxy;
import com.selrahc13.realzombie.common.entity.EntityCrawler;
import com.selrahc13.realzombie.common.entity.EntityWalker;
import com.selrahc13.realzombie.models.ModelCrawler;
import com.selrahc13.realzombie.models.ModelWalker;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;

public class ClientProxy extends CommonProxy {
  @Override
  public void preload(FMLPreInitializationEvent event) {
    super.preload(event);
  }

  @Override
  public void load(FMLInitializationEvent event) {
    super.load(event);
    RenderingRegistry.registerEntityRenderingHandler(EntityWalker.class, new RenderWalker(new ModelWalker(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(EntityCrawler.class, new RenderCrawler(new ModelCrawler(), 0.5F));
  }

  @Override
  public void postload(FMLPostInitializationEvent event) {
    super.postload(event);
  }

  @Override
  public void serverStarted(FMLServerStartedEvent event) {
    super.serverStarted(event);
  }
}