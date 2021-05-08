package com.nuparu.sevendaystomine.dispenser;

import com.nuparu.sevendaystomine.entity.EntityFlare;
import com.nuparu.sevendaystomine.entity.EntityFragmentationGrenade;
import com.nuparu.sevendaystomine.entity.EntityProjectileVomit;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BehaviorThrowVomit implements IBehaviorDispenseItem {

	@Override
	public ItemStack dispense(IBlockSource source, final ItemStack stack)
    {
        return (new BehaviorProjectileDispense()
        {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                return new EntityProjectileVomit(worldIn, position.getX(), position.getY(), position.getZ());
            }
            protected float getProjectileInaccuracy()
            {
                return super.getProjectileInaccuracy() * 0.5F;
            }
            protected float getProjectileVelocity()
            {
                return super.getProjectileVelocity() * 1.25F;
            }
        }).dispense(source, stack);
    }

}
