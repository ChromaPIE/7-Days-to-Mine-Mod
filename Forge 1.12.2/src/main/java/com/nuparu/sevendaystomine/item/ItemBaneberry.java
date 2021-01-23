package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.init.ModBlocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class ItemBaneberry extends ItemFood implements net.minecraftforge.common.IPlantable {

	public ItemBaneberry() {
		super(1, 0.3f, false);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack itemstack = player.getHeldItem(hand);
        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
        if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up()))
        {
            worldIn.setBlockState(pos.up(),  ModBlocks.BANEBERRY_PLANT.getDefaultState());

            if (player instanceof EntityPlayerMP)
            {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos.up(), itemstack);
            }

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return ModBlocks.BANEBERRY_PLANT.getDefaultState();
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		if(worldIn.isRemote) return;
		if(worldIn.rand.nextDouble() < 0.75) {
			PotionEffect effect = new PotionEffect(MobEffects.WEAKNESS,3000);
			effect.getCurativeItems().clear();
			player.addPotionEffect(effect);
		}
		if(worldIn.rand.nextDouble() < 0.65) {
			PotionEffect effect = new PotionEffect(MobEffects.SLOWNESS,3000);
			effect.getCurativeItems().clear();
			player.addPotionEffect(effect);
		}
		if(worldIn.rand.nextDouble() < 0.45) {
			PotionEffect effect = new PotionEffect(MobEffects.NAUSEA,1200);
			effect.getCurativeItems().clear();
			player.addPotionEffect(effect);
		}
		if(worldIn.rand.nextDouble() < 0.45) {
			PotionEffect effect = new PotionEffect(MobEffects.BLINDNESS,1200);
			effect.getCurativeItems().clear();
			player.addPotionEffect(effect);
		}
		if(worldIn.rand.nextDouble() < 0.33) {
			PotionEffect effect = new PotionEffect(MobEffects.BLINDNESS,600);
			effect.getCurativeItems().clear();
			player.addPotionEffect(effect);
		}
	}
}
