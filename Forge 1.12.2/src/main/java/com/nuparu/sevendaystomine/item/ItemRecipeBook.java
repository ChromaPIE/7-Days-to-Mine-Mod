package com.nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRecipeBook extends ItemGuide {
	private String recipe;

	public ItemRecipeBook(ResourceLocation data, String recipe) {
		super(data);
		this.recipe = recipe;
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(playerIn);
		if (playerIn.isSneaking())
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);

		if (!iep.hasRecipe(recipe) && !isRead(stack)) {
			if (worldIn.isRemote) {
				SevenDaysToMine.proxy.openClientOnlyGui(2, stack);
			}
			iep.unlockRecipe(recipe);
			setRead(stack, true);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		} else {
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}
	}

	public boolean isRead(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null) {
			if (nbt.hasKey("read")) {
				return nbt.getBoolean("read");
			}
		}
		return false;
	}

	public void setRead(ItemStack stack, boolean read) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setBoolean("read", read);
	}

	public String getRecipe() {
		return this.recipe;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(!GuiScreen.isShiftKeyDown()) return;
		if(Minecraft.getMinecraft().player == null) return;
		boolean known = CapabilityHelper.getExtendedPlayer(Minecraft.getMinecraft().player).hasRecipe(recipe);
		tooltip.add(known ? TextFormatting.GREEN +  SevenDaysToMine.proxy.localize("stat.known.name") : TextFormatting.RED + SevenDaysToMine.proxy.localize("stat.unknown.name"));
	}

}
