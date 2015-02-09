/*
Copyright (c) jglrxavpok, All rights reserved.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3.0 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library.
*/
package com.selrahc13.realzombie.common.entity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFollowEntity extends EntityAIBase
{
    private Class<? extends Entity> target;
    private EntityLiving follower;
    private EntityLivingBase theTarget;
    private double followSpeed;
    private float max;

    public EntityAIFollowEntity(EntityLiving taskOwner, Class<? extends Entity> entityToFollow, double speed, float maxDist)
    {
        follower = taskOwner;
        this.target = entityToFollow;
        this.setMutexBits(3);
        followSpeed = speed;
        max = maxDist;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        List<?> list = this.follower.worldObj.getEntitiesWithinAABB(this.target, this.follower.boundingBox.expand(this.max, this.max, this.max));

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            Iterator<?> iterator = list.iterator();

            while (iterator.hasNext())
            {
                theTarget = (EntityLivingBase)iterator.next();
            }

            return this.theTarget != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.follower.getDistanceSqToEntity(this.theTarget) < max && !theTarget.isDead;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        if(theTarget instanceof EntityLiving)
        ((EntityLiving)theTarget).getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theTarget = null;
        this.follower.getNavigator().clearPathEntity();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.follower.getLookHelper().setLookPositionWithEntity(this.theTarget, 30.0F, 30.0F);
        this.follower.getNavigator().tryMoveToEntityLiving(this.theTarget, followSpeed);
    }
}