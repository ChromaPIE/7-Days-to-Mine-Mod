package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.entity.EntityFlame;
import com.nuparu.sevendaystomine.entity.EntityShot;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.ItemGun.EnumWield;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFlamethrower extends ItemGun {

	public ItemFlamethrower() {
		super();
		setMaxAmmo(500);
		setFullDamage(20f);
		setSpeed(0.18f);
		setRecoil(0.15f);
		setCounterDef(0);
		setCross(22);
		setReloadTime(1500);
		setDelay(1);
		setProjectiles(10);
		setShotsPerAmmo(50);
		setType(EnumGun.RIFLE);
		setLength(EnumLength.LONG);
		setWield(EnumWield.TWO_HAND);
	}

	@Override
	public Item getReloadItem(ItemStack stack) {
		return ModItems.GAS_CANISTER;
	}

	@Override
	public SoundEvent getReloadSound() {
		return SoundHelper.AK47_RELOAD;
	}

	@Override
	public SoundEvent getShotSound() {
		return SoundHelper.FLAMETHROWER_SHOT;
	}

	@Override
	public SoundEvent getDrySound() {
		return SoundHelper.PISTOL_DRYSHOT;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

		ItemStack itemstack = playerIn.getHeldItem(handIn);

		/*
		 * if (handIn == EnumHand.OFF_HAND && (gunWield != EnumWield.DUAL &&
		 * playerIn.getHeldItemMainhand() != ItemStack.EMPTY)) { return new
		 * ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack); }
		 */

		if ((getWield() == EnumWield.TWO_HAND && !playerIn.getHeldItem(getOtherHand(handIn)).isEmpty())) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
		if (itemstack.isEmpty() || itemstack.getTagCompound() == null) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (!nbt.hasKey("Ammo") || !nbt.hasKey("Capacity") || !nbt.hasKey("NextFire")) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
		if (itemstack.getTagCompound().getLong("NextFire") > worldIn.getTotalWorldTime()
				|| itemstack.getTagCompound().getBoolean("Reloading")) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}

		int ammo = nbt.getInteger("Ammo");
		boolean flag = playerIn.isCreative();
		if (ammo > 0 || flag) {
			float velocity = getSpeed() * (1f + ((float) getQuality(itemstack) / (float) ItemQuality.MAX_QUALITY));
			for (int i = 0; i < getProjectiles(); i++) {
				EntityFlame shot = new EntityFlame(worldIn, playerIn, velocity,
						((float) getSpread(playerIn, handIn) / (playerIn.isSneaking() ? 1.5f : 1f)),
						(int) Math.ceil(30 * (1f + ((float) getQuality(itemstack) / (float) ItemQuality.MAX_QUALITY))));
				if (!worldIn.isRemote) {
					worldIn.spawnEntity(shot);
				}
			}
			itemstack.damageItem(1, playerIn);
			worldIn.playSound(null, new BlockPos(playerIn), getShotSound(), SoundCategory.PLAYERS, getShotSoundVolume(),
					getShotSoundPitch());
			playerIn.swingArm(handIn);
			if (worldIn.isRemote) {
				SevenDaysToMine.proxy.addRecoil(getRecoil(), playerIn);
			}

			if (!flag) {
				itemstack.getTagCompound().setInteger("Ammo", ammo - 1);
			}

			itemstack.getTagCompound().setLong("NextFire", worldIn.getTotalWorldTime() + getDelay());

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		} else {
			worldIn.playSound(null, new BlockPos(playerIn), getDrySound(), SoundCategory.PLAYERS, 0.3F,
					1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
			itemstack.getTagCompound().setLong("NextFire", worldIn.getTotalWorldTime() + (getDelay() / 2));
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
	}

}
