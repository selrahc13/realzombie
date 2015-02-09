package com.selrahc13.realzombie.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.selrahc13.realzombie.RealZombie;
import com.selrahc13.realzombie.common.entity.EntityCrawler;

public class RenderCrawler extends RenderLiving {
  public RenderCrawler(ModelBase par1ModelBase, float shadowSize) {
    super(par1ModelBase, shadowSize);
  }

  public void renderCrawler(EntityCrawler entityCrawler, double x, double y, double z, float yaw, float partialTickTime) {
    super.doRender(entityCrawler, x, y, z, yaw, partialTickTime);
  }

  @Override
  public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
    renderCrawler((EntityCrawler)entity, x, y, z, yaw, partialTickTime);
  }

  @Override
  protected ResourceLocation getEntityTexture(Entity entity) {
	  //TODO: Replace texture
    return new ResourceLocation(RealZombie.meta.modId.toLowerCase() + ":textures/entities/crawler.png");
  }
}