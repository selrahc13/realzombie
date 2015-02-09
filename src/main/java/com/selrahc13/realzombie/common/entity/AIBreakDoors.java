package com.selrahc13.realzombie.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.EnumDifficulty;

public class AIBreakDoors extends AIDoorInteract {
  private int breakingTime;
  private int field_75358_j = -1;

  public AIBreakDoors(EntityLiving entityLiving) {
    super(entityLiving);
  }
  
  /**
   * Execute a one shot task or start executing a continuous task
   */
  @Override
  public void startExecuting()
  {
      super.startExecuting();
      this.breakingTime = 0;
  }

  @Override
  public boolean continueExecuting()
  {
	  boolean exec = false;
      double d0 = this.theEntity.getDistanceSq((double)this.entityPosX, (double)this.entityPosY, (double)this.entityPosZ);
      if (this.breakingTime <= 240 && d0 < 4.0D) {
    	   exec = (isClosed(theDoor));
      }
      return exec;
  }

  @Override
  public void updateTask() {
    super.updateTask();

    if (this.theEntity.getRNG().nextInt(20) == 0) {
      int damagesound = (theDoor.getUnlocalizedName().toLowerCase().contains("iron")) ? 1011 : 1010;
      this.theEntity.worldObj.playAuxSFX(damagesound, this.entityPosX, this.entityPosY, this.entityPosZ, 0);
    }

    ++this.breakingTime;
    int i = (int)((float)this.breakingTime / 240.0F * 10.0F);

    if (i != this.field_75358_j) {
      this.theEntity.worldObj.destroyBlockInWorldPartially(this.theEntity.getEntityId(), this.entityPosX, this.entityPosY, this.entityPosZ, i);
      this.field_75358_j = i;
    }

    if (this.breakingTime == 240 && this.theEntity.worldObj.difficultySetting == EnumDifficulty.HARD) {
      int breaksound = (theDoor.getUnlocalizedName().toLowerCase().contains("iron")) ? 1012 : 1020;
      this.theEntity.worldObj.setBlockToAir(this.entityPosX, this.entityPosY, this.entityPosZ);
      this.theEntity.worldObj.playAuxSFX(breaksound, this.entityPosX, this.entityPosY, this.entityPosZ, 0);
      this.theEntity.worldObj.playAuxSFX(2001, this.entityPosX, this.entityPosY, this.entityPosZ, Block.getIdFromBlock(this.theDoor));
    }
  }
}