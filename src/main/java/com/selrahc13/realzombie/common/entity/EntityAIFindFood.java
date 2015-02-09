package com.selrahc13.realzombie.common.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class EntityAIFindFood extends EntityAIBase {
    private Class<? extends Entity> target;
    private EntityLiving follower;
    private EntityItem theTarget;
    private double followSpeed;
    private float max;

    // AI to cause target to seek out food on the ground
	public EntityAIFindFood(EntityLiving taskOwner,
			Class<? extends Entity> entityToFollow, double speed, float maxDist) {
        follower = taskOwner;
        this.target = entityToFollow;
        this.setMutexBits(3);
        followSpeed = speed;
        max = maxDist;
	}

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
	@SuppressWarnings("rawtypes")
	@Override
    public boolean shouldExecute()
    {
        List list = this.follower.worldObj.getEntitiesWithinAABB(this.target, this.follower.boundingBox.expand(this.max, this.max, this.max));

        if (list.isEmpty())
        {
            return false;
        }
        else
        {

            for (int i = 0; i < list.size(); ++i)
            {
            	if (list.get(i).getClass() == EntityItem.class) {
            		EntityItem item = (EntityItem)list.get(i);
            		ItemStack itemStack = item.getEntityItem();
            		if (itemStack.getItem() instanceof ItemFood) {
            			theTarget = item;
            		}
            	}
            }
            return this.theTarget != null;
        }
    }

	@Override
    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        if(this.theTarget instanceof EntityItem) {
        	this.follower.getNavigator().tryMoveToEntityLiving(theTarget, followSpeed);
        }
    }
	
	@Override
    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.follower.getLookHelper().setLookPositionWithEntity(this.theTarget, 30.0F, 30.0F);
    	this.follower.getNavigator().tryMoveToEntityLiving(theTarget, followSpeed);
    }

	@Override
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
		if(this.follower.getDistanceSqToEntity(this.theTarget) < 2) {
			this.theTarget.setDead();
			return false;
		}
        return this.follower.getDistanceSqToEntity(this.theTarget) < max && !theTarget.isDead;
    }

    /**
     * Resets the task
     */
	@Override
    public void resetTask()
    {
        this.theTarget = null;
        this.follower.getNavigator().clearPathEntity();
    }

}
