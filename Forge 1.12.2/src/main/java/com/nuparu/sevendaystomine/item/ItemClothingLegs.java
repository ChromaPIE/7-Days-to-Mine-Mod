package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemClothingLegs extends ItemClothing {

	public ItemClothingLegs(boolean isDyeable, boolean hasOverlay, String fileName) {
		super(SevenDaysToMine.CLOTHING, 0, EntityEquipmentSlot.LEGS);
		this.isDyeable = isDyeable;
		this.hasOverlay = hasOverlay;
		this.texture = new ResourceLocation(SevenDaysToMine.MODID,"textures/models/clothing/" + fileName +".png");
		if(hasOverlay) {
			this.overlay = new ResourceLocation(SevenDaysToMine.MODID,"textures/models/clothing/" + fileName +"_overlay.png");
		}
	}
	
	
	@SideOnly(Side.CLIENT)
	public ModelPlayer getModel(EntityPlayer player, ItemStack stack) {
		if(model == null) {
			model = new ModelPlayer(0.1f,false);
		}
		return model;
	}

}
