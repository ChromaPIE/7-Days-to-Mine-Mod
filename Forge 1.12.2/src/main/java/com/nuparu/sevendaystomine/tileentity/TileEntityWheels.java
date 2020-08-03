package com.nuparu.sevendaystomine.tileentity;

import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.item.ItemQuality;
import com.nuparu.sevendaystomine.util.MathUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityWheels extends TileEntity {

	protected ItemStack stack = ItemStack.EMPTY;

	public TileEntityWheels() {

	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("stack", Constants.NBT.TAG_COMPOUND)) {
			this.stack = new ItemStack(compound.getCompoundTag("stack"));
		} else {
			this.stack = generateItemStack();
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);
		compound.setTag("stack", this.stack.writeToNBT(new NBTTagCompound()));

		return compound;
	}

	public void setItemStack(ItemStack stack) {
		this.stack = stack.copy();
		this.stack.setCount(1);
	}

	public ItemStack getItemStack() {
		return this.stack;
	}

	public ItemStack generateItemStack() {
		ItemStack is = new ItemStack(ModBlocks.WHEELS);
		this.stack = ItemQuality.setQualityForStack(is, MathUtils.getIntInRange(1, 600));
		return is;
	}

}
