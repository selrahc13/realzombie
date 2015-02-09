package com.selrahc13.realzombie.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
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

public class EntityWalker extends EntityMob {
  public String texture;

  public EntityWalker(World par1World) {
    super(par1World);
    texture = getRandomZombieTexture();
    setHealth(RealZombie.random(6, 26));
    float moveSpeed = RealZombie.random(2, 4) / 10f;
    getNavigator().setBreakDoors(true);
    //tasks.addTask(0, new EntityAISwimming(this));
    tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityPlayerMP.class, moveSpeed, false));
    tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityPlayer.class, moveSpeed, false));
    //tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityRealZombie.class, moveSpeed, false));
    tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityAnimal.class, moveSpeed, false));    
    //tasks.addTask(4, new EntityAIAttackOnCollide(this, moveSpeed, false));
    tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityVillager.class, moveSpeed, true));
    tasks.addTask(4, new EntityAIAttackOnCollide(this, BlockDoor.class, moveSpeed, true));
    if (RealZombie.usingCarpentersBlocks) {
    	try {
    		tasks.addTask(4, new EntityAIAttackOnCollide(this, Class.forName("com.carpentersblocks.block.BlockCarpentersDoor"), moveSpeed, false));
    	} catch (Exception e) {}
    }
    if (RealZombie.usingMalisisDoors) {
    	try {
    		tasks.addTask(4, new EntityAIAttackOnCollide(this, Class.forName("net.malisis.doors.door.block.Door"), moveSpeed, false));
    	} catch (Exception e) {}
    }        
    tasks.addTask(3, new EntityAIFindFood(this, EntityItem.class, moveSpeed, 32));
    tasks.addTask(5, new EntityAIMoveThroughVillage(this, moveSpeed, false));
    tasks.addTask(6, new EntityAIWander(this, moveSpeed));
    tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
    tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 8F));
    tasks.addTask(8, new EntityAILookIdle(this));
    //tasks.addTask(8, new AIDoorInteract(this));
    tasks.addTask(9, new AIBreakDoors(this));
    targetTasks.addTask(10, new EntityAIHurtByTarget(this, false));
    targetTasks.addTask(11, new EntityAINearestAttackableTarget(this, EntityPlayerMP.class, 0, true));
    targetTasks.addTask(11, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    targetTasks.addTask(11, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, true));
    targetTasks.addTask(11, new EntityAINearestAttackableTarget(this, EntityAnimal.class, 0, true));
  }

  private String getRandomZombieTexture() {
    return "zombie" + rand.nextInt(7) + ".png";
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
    return "realzombie:mob.zombie.say";
  }

  @Override
  protected String getHurtSound() {
    return "realzombie:mob.zombie.hurt";
  }

  @Override
  protected String getDeathSound() {
    return "realzombie:mob.zombie.death";
  }

  @Override
  protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
    this.playSound("mob.zombie.step", 0.15F, 1.0F);
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
      return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 1);
    } else if (worldObj.difficultySetting.equals(EnumDifficulty.NORMAL)) {
      int j = rand.nextInt(5);
      int k = rand.nextInt(10);
      if (j == 0) {
        ((EntityLivingBase)entity).addPotionEffect(new EnactEffect(Effect.bleeding.getId(), 20 * 300, 1));
      }
      if (k == 0) {
        ((EntityLivingBase)entity).addPotionEffect(new EnactEffect(Effect.zombification.getId(), 20 * 300, 1));
      }
      return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
    } else if (worldObj.difficultySetting.equals(EnumDifficulty.HARD)) {
      int j = rand.nextInt(3);
      int k = rand.nextInt(6);
      if (j == 0) {
        ((EntityLivingBase)entity).addPotionEffect(new EnactEffect(Effect.bleeding.getId(), 20 * 300, 1));
      }
      if (k == 0) {
        ((EntityLivingBase)entity).addPotionEffect(new EnactEffect(Effect.zombification.getId(), 20 * 300, 1));
      }
      return entity.attackEntityFrom(DamageSource.causeMobDamage(this), RealZombie.random(1, 8));
    } else {
      return false;
    }
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (!worldObj.isRemote && worldObj.difficultySetting.equals(EnumDifficulty.PEACEFUL)) {
      setDead();
    }
  }

  @Override
  protected void attackEntity(Entity entity, float distanceToEntity) {
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

  @Override
  public EnumCreatureAttribute getCreatureAttribute()
  {
	  return EnumCreatureAttribute.UNDEAD;
  }

}