package com.nuparu.sevendaystomine.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotWorkbenchScrap extends SlotItemHandler {
	ContainerWorkbenchUncrafting container;

	public SlotWorkbenchScrap(IItemHandler itemHandler, int index, int xPosition, int yPosition,
			ContainerWorkbenchUncrafting container) {
		super(itemHandler, index, xPosition, yPosition);
		this.container = container;
	}

	@Nullable
	@SideOnly(Side.CLIENT)
	public String getSlotTexture() {
		return SevenDaysToMine.MODID + ":items/empty_scrap_slot";
	}

	public boolean isItemValid(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() == ModItems.IRON_SCRAP;
	}

	@Override
	public void onSlotChange(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {
		super.onSlotChange(p_75220_1_, p_75220_2_);
		container.onScrapChanged(p_75220_2_);
		System.out.println("FFF");
	}

	@Override
	public void putStack(ItemStack stack) {
		container.onScrapChanged(stack);
		super.putStack(stack);
		System.out.println("FfdfdfFF");
	}

	@Override
	@Nonnull
	public ItemStack decrStackSize(int amount) {

		int amountPrev = this.getStack().getCount();
		ItemStack stack = super.decrStackSize(amount);
		System.out.println("PREDICTION " + (amountPrev-amount));
		if(amountPrev-amount < 1) {
			container.onScrapChanged(ItemStack.EMPTY);
		}else {
			container.onScrapChanged(stack);
		}
		System.out.println("Ffdfd0000 fFF " + stack.toString());
		return stack;
	}

}
