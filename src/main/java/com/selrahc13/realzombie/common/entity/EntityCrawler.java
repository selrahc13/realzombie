package com.selrahc13.realzombie.common.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import com.selrahc13.realzombie.RealZombie;
import com.selrahc13.realzombie.common.effects.Effect;
import com.selrahc13.realzombie.common.effects.EnactEffect;

public class EntityCrawler extends EntityMob {
  public EntityCrawler(World world) {
    super(world);
    setHealth(RealZombie.random(2, 14));
    float moveSpeed = RealZombie.random(1, 3) / 10f;
    getNavigator().setBreakDoors(true);
    //tasks.addTask(0, new EntityAISwimming(this));
    //tasks.addTask(1, new AIBreakDoors(this));
    tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayerMP.class, moveSpeed, false));
    tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, moveSpeed, false));
    //tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityRealZombie.class, moveSpeed, true));
    tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityVillager.class, moveSpeed, false));
    tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityAnimal.class, moveSpeed, false));
    tasks.addTask(2, new EntityAIFindFood(this, EntityItem.class, moveSpeed, 32));
    tasks.addTask(5, new EntityAIMoveThroughVillage(this, moveSpeed, false));
    tasks.addTask(6, new EntityAIWander(this, moveSpeed));
    tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
    tasks.addTask(7, new EntityAIWatchClosest(this, EntityAnimal.class, 8F));
    tasks.addTask(7, new EntityAILookIdle(this));
    targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayerMP.class, 0, true));
    targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityAnimal.class, 0, true));
    targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, true));
  }

  @Override
  public void collideWithNearbyEntities() {
      List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

      if (list != null && !list.isEmpty())
      {
          for (int i = 0; i < list.size(); ++i)
          {
        	  if (list.get(i).getClass() == EntityItem.class) {
        		  EntityItem item = (EntityItem)list.get(i);
    			  ItemStack itemStack = item.getEntityItem();
    			  // Zombies should eat food items
    			  if (itemStack.getItem() instanceof ItemFood) {
    				  item.setDead();
    			  }
              }
          }
      }	  
  }

  @Override
  public boolean getCanSpawnHere() {
	  return true;
  }
  
  @Override
  protected boolean canDespawn() {
    return false;
  }

  @Override
  protected boolean isAIEnabled() {
    return true;
  }

  @Override
  protected String getLivingSound() {
    return "mob.zombie.say";
  }

  @Override
  protected String getHurtSound() {
    return "mob.zombie.hurt";
  }

  @Override
  protected String getDeathSound() {
    return "mob.zombie.death";
  }

  @Override
  protected Entity findPlayerToAttack() {
    EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 16D);

    if (entityplayer != null && canEntityBeSeen(entityplayer)) {
      return entityplayer;
    } else {
      return null;
    }
  }

  @Override
  public boolean attackEntityFrom(DamageSource damageSource, float damage) {
    if (super.attackEntityFrom(damageSource, damage)) {
      Entity entity = damageSource.getEntity();

      if (riddenByEntity == entity || ridingEntity == entity) {
        return true;
      }

      if (entity != this) {
        entityToAttack = entity;
      }

      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean attackEntityAsMob(Entity entity) {
    if (worldObj.difficultySetting.equals(EnumDifficulty.EASY)) {
      int j = rand.nextInt(10);
      int k = rand.nextInt(20);
      if (j == 0) {
        ((EntityLivingBase)entity).addPotionEffect(new EnactEffect(Effect.bleeding.getId(), 20 * 300, 1));
      }
      if (k == 0) {
        ((EntityLivingBase)entity).addPotionEffect(new EnactEffect(Effect.zombification.getId(), 20 * 300, 1));
      }
      return entity.attackEntityFrom(DamageSource.causeMobDamage(this), RealZombie.random(0,2));
    } else if (worldObj.difficultySetting.equals(EnumDifficulty.NORMAL)) {
      int j = rand.nextInt(5);
      int k = rand.nextInt(10);
      if (j == 0) {
        ((EntityLivingBase)entity).addPotionEffect(new EnactEffect(Effect.bleeding.getId(), 20 * 300, 1));
      }
      if (k == 0) {
        ((EntityLivingBase)entity).addPotionEffect(new EnactEffect(Effect.zombification.getId(), 20 * 300, 1));
      }
      return entity.attackEntityFrom(DamageSource.causeMobDamage(this), RealZombie.random(1,3));
    } else if (worldObj.difficultySetting.equals(EnumDifficulty.HARD)) {
      int j = rand.nextInt(3);
      int k = rand.nextInt(6);
      if (j == 0) {
        ((EntityLivingBase)entity).addPotionEffect(new EnactEffect(Effect.bleeding.getId(), 20 * 300, 1));
      }
      if (k == 0) {
        ((EntityLivingBase)entity).addPotionEffect(new EnactEffect(Effect.zombification.getId(), 20 * 300, 1));
      }
      return entity.attackEntityFrom(DamageSource.causeMobDamage(this), RealZombie.random(0,6));
    } else {
      return false;
    }
  }

  @Override
  protected void attackEntity(Entity entity, float distanceToEntity) {
	// don't attack other zombies
	if (entity instanceof EntityWalker || entity instanceof EntityCrawler) {
		return;
	}
    if (attackTime <= 0 && distanceToEntity < 2.0F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY) {
      attackTime = 20;
      attackEntityAsMob(entity);
    }
  }

  @Override
  public float getBlockPathWeight(int xCoord, int yCoord, int zCoord) {
    return 0.5F - worldObj.getLightBrightness(xCoord, yCoord, zCoord);
  }

  @Override
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
  }
}