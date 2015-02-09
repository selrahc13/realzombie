package com.selrahc13.realzombie.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.selrahc13.realzombie.RealZombie;
import com.selrahc13.realzombie.common.entity.EntityWalker;

public class RenderWalker extends RenderLiving {
  public RenderWalker(ModelBase par1ModelBase, float shadowSize) {
    super(par1ModelBase, shadowSize);
  }

  public void renderZombie(EntityWalker entityZombie, double x, double y, double z, float yaw, float partialTickTime) {
    super.doRender(entityZombie, x, y, z, yaw, partialTickTime);
  }

  @Override
  public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
    renderZombie((EntityWalker)entity, x, y, z, yaw, partialTickTime);
  }

  @Override
  protected ResourceLocation getEntityTexture(Entity entity) {
    if (entity instanceof EntityWalker) {
      EntityWalker zombie = (EntityWalker)entity;
      return new ResourceLocation(RealZombie.meta.modId.toLowerCase() + ":textures/entities/" + zombie.texture);
    }
    return null;
  }
}