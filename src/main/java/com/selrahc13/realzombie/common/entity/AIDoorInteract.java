package com.selrahc13.realzombie.common.entity;

import com.selrahc13.realzombie.RealZombie;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIDoorInteract;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;

public class AIDoorInteract extends EntityAIDoorInteract
{
    protected EntityLiving theEntity;
    protected int entityPosX;
    protected int entityPosY;
    protected int entityPosZ;
    protected Block theDoor;
    protected BlockDoor field_151504_e;
    /** If is true then the Entity has stopped Door Interaction and completed the task. */
    boolean hasStoppedDoorInteraction;
    float entityPositionX;
    float entityPositionZ;

    public AIDoorInteract(EntityLiving entity)
    {
    	super(entity);
        this.theEntity = entity;
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if (!this.theEntity.isCollidedHorizontally)
        {
            return false;
        }
        else
        {
            PathNavigate pathnavigate = this.theEntity.getNavigator();
            PathEntity pathentity = pathnavigate.getPath();

            if (pathentity != null && !pathentity.isFinished() && pathnavigate.getCanBreakDoors())
            {
                for (int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2, pathentity.getCurrentPathLength()); ++i)
                {
                    PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
                    this.entityPosX = pathpoint.xCoord;
                    this.entityPosY = pathpoint.yCoord + 1;
                    this.entityPosZ = pathpoint.zCoord;

                    if (this.theEntity.getDistanceSq((double)this.entityPosX, this.theEntity.posY, (double)this.entityPosZ) <= 2.25D)
                    {
                        this.theDoor = this.getDoor(this.entityPosX, this.entityPosY, this.entityPosZ);

                        if (this.theDoor != null)
                        {
                            return true;
                        }
                    }
                }

                this.entityPosX = MathHelper.floor_double(this.theEntity.posX);
                this.entityPosY = MathHelper.floor_double(this.theEntity.posY + 1.0D);
                this.entityPosZ = MathHelper.floor_double(this.theEntity.posZ);
                this.theDoor = this.getDoor(this.entityPosX, this.entityPosY, this.entityPosZ);
                return this.theDoor != null;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return !this.hasStoppedDoorInteraction;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.hasStoppedDoorInteraction = false;
        this.entityPositionX = (float)((double)((float)this.entityPosX + 0.5F) - this.theEntity.posX);
        this.entityPositionZ = (float)((double)((float)this.entityPosZ + 0.5F) - this.theEntity.posZ);
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
        float f = (float)((double)((float)this.entityPosX + 0.5F) - this.theEntity.posX);
        float f1 = (float)((double)((float)this.entityPosZ + 0.5F) - this.theEntity.posZ);
        float f2 = this.entityPositionX * f + this.entityPositionZ * f1;

        if (f2 < 0.0F)
        {
            this.hasStoppedDoorInteraction = true;
        }
    }

    public boolean isClosed(Block block) {
    	if (block == Blocks.wooden_door 
    			|| block == Blocks.iron_door) {

    		return ((BlockDoor)block).func_150015_f(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ);
    	} else if (block == Blocks.iron_bars 
        			|| block == Blocks.fence
        			|| block == Blocks.fence_gate) {
    		return true;
    	}
        if (RealZombie.usingCarpentersBlocks) {
        	try {
        		if (Class.forName("com.carpentersblocks.block.BlockCarpentersDoor").isInstance(block)) {
        			//TODO: Is there a way to read door state without code cross-contamination?
        			return true;
        		}
        	} catch (Exception e) {}
        }
        if (RealZombie.usingMalisisDoors) {
        	try {
        		if (Class.forName("net.malisis.doors.door.block.Door").isInstance(block)) {
        			//TODO: Is there a way to read door state without code cross-contamination?
        			return true;
        		}
        	} catch (Exception e) {}
        }        
    	return false;
    }
    
    /*
     * Extended to recognize more types of doors as breakable
     */
    private Block getDoor(int posX, int posY, int posZ)
    {
        Block block = this.theEntity.worldObj.getBlock(posX, posY, posZ);
        if (block != Blocks.air){ 
        	if (block == Blocks.wooden_door 
        			|| block == Blocks.iron_bars 
        			|| block == Blocks.iron_door 
        			|| block == Blocks.fence
        			|| block == Blocks.fence_gate) {
        		return block;
        	}
        }
        
        
        if (RealZombie.usingCarpentersBlocks) {
        	try {
        		if (Class.forName("com.carpentersblocks.block.BlockCarpentersDoor").isInstance(block)) {
        			return block;
        		}
        	} catch (Exception e) {}
        }
        if (RealZombie.usingMalisisDoors) {
        	try {
        		if (Class.forName("net.malisis.doors.door.block.Door").isInstance(block)) {
        			return block;
        		}
        	} catch (Exception e) {}
        }        
        
        // No door found here
        return null;
    }
    
    @SuppressWarnings("unused")
	private BlockDoor func_151503_a(int p_151503_1_, int p_151503_2_, int p_151503_3_)
    {
        Block block = this.theEntity.worldObj.getBlock(p_151503_1_, p_151503_2_, p_151503_3_);
        return block != Blocks.wooden_door ? null : (BlockDoor)block;
    }    
}