package nuparu.sevendaystomine.item;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemEmptyJar extends ItemScrapable {

	public ItemEmptyJar() {
		super(EnumMaterial.GLASS, 3);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if (raytraceresult == null) {
			return new ActionResult(EnumActionResult.PASS, itemstack);
		} else {
			if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos blockpos = raytraceresult.getBlockPos();

				if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn
						.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
					return new ActionResult(EnumActionResult.PASS, itemstack);
				}

				IBlockState state = worldIn.getBlockState(blockpos);
				if (state.getMaterial() == Material.WATER) {
					worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ,
							SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
					return new ActionResult(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, playerIn,
							new ItemStack(nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER)));
				}
				if (state.getBlock() instanceof BlockCauldron) {
					int level = state.getValue(BlockCauldron.LEVEL);
					if (level > 0) {
						if (!worldIn.isRemote) {
							worldIn.setBlockState(blockpos, state.withProperty(BlockCauldron.LEVEL, level - 1));
						}
						worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ,
								SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
						return new ActionResult(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, playerIn,
								new ItemStack(nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER)));
					}
				}
			}

			return new ActionResult(EnumActionResult.PASS, itemstack);
		}

	}

	protected ItemStack turnBottleIntoItem(ItemStack p_185061_1_, EntityPlayer player, ItemStack stack) {
		p_185061_1_.shrink(1);
		player.addStat(StatList.getObjectUseStats(this));

		if (p_185061_1_.isEmpty()) {
			return stack;
		} else {
			if (!player.inventory.addItemStackToInventory(stack)) {
				player.dropItem(stack, false);
			}

			return p_185061_1_;
		}
	}

}
