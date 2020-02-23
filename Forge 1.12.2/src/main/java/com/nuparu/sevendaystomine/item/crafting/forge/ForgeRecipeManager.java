package com.nuparu.sevendaystomine.item.crafting.forge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.EnumMaterial;

import net.minecraft.item.ItemStack;

public class ForgeRecipeManager {
private static ForgeRecipeManager INSTANCE;
	
	private ArrayList<IForgeRecipe> recipes = new ArrayList<IForgeRecipe>();
	
	public ForgeRecipeManager() {
		INSTANCE = this;
		addRecipes();
	}
	
	public static ForgeRecipeManager getInstance() {
		return INSTANCE;
	}
	
	public ArrayList<IForgeRecipe> getRecipes(){
		return this.recipes;
	}
	
	public void addRecipes() {
		HashMap<EnumMaterial,Integer> ingotiron = new HashMap<EnumMaterial,Integer>();
		ingotiron.put(EnumMaterial.IRON,6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_IRON),new ItemStack(ModItems.MOLD_INGOT),ingotiron));
		HashMap<EnumMaterial,Integer> ingotbrass1 = new HashMap<EnumMaterial,Integer>();
		ingotbrass1.put(EnumMaterial.BRASS,6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_BRASS),new ItemStack(ModItems.MOLD_INGOT),ingotbrass1));
		HashMap<EnumMaterial,Integer> ingotlead = new HashMap<EnumMaterial,Integer>();
		ingotlead.put(EnumMaterial.LEAD,6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_LEAD),new ItemStack(ModItems.MOLD_INGOT),ingotlead));
		HashMap<EnumMaterial,Integer> ingotcopper = new HashMap<EnumMaterial,Integer>();
		ingotcopper.put(EnumMaterial.COPPER,6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_COPPER),new ItemStack(ModItems.MOLD_INGOT),ingotcopper));
		HashMap<EnumMaterial,Integer> ingottin = new HashMap<EnumMaterial,Integer>();
		ingottin.put(EnumMaterial.TIN,6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_TIN),new ItemStack(ModItems.MOLD_INGOT),ingottin));
		HashMap<EnumMaterial,Integer> ingotzinc = new HashMap<EnumMaterial,Integer>();
		ingotzinc.put(EnumMaterial.ZINC,6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_ZINC),new ItemStack(ModItems.MOLD_INGOT),ingotzinc));
		HashMap<EnumMaterial,Integer> ingotgold = new HashMap<EnumMaterial,Integer>();
		ingotgold.put(EnumMaterial.GOLD,6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_GOLD),new ItemStack(ModItems.MOLD_INGOT),ingotgold));
		HashMap<EnumMaterial,Integer> ingotsteel = new HashMap<EnumMaterial,Integer>();
		ingotsteel.put(EnumMaterial.STEEL,6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_STEEL),new ItemStack(ModItems.MOLD_INGOT),ingotsteel));
		HashMap<EnumMaterial,Integer> ingotbronze1 = new HashMap<EnumMaterial,Integer>();
		ingotbronze1.put(EnumMaterial.BRONZE,6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_BRONZE),new ItemStack(ModItems.MOLD_INGOT),ingotbronze1));
		HashMap<EnumMaterial,Integer> ingotbronze2 = new HashMap<EnumMaterial,Integer>();
		ingotbronze2.put(EnumMaterial.COPPER,5);
		ingotbronze2.put(EnumMaterial.TIN,1);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_BRONZE),new ItemStack(ModItems.MOLD_INGOT),ingotbronze2));
		HashMap<EnumMaterial,Integer> ingotbrass2 = new HashMap<EnumMaterial,Integer>();
		ingotbrass2.put(EnumMaterial.COPPER,4);
		ingotbrass2.put(EnumMaterial.ZINC,2);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_BRASS),new ItemStack(ModItems.MOLD_INGOT),ingotbrass2));
		
		
		
		
		//addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.STICK),new ItemStack(Items.ARROW),new ArrayList(Arrays.asList(new ItemStack(Items.COAL),new ItemStack(Items.PAPER)))));
	    //addRecipe(new CampfireRecipeShaped(new ItemStack(Items.CAKE),new ItemStack(Items.ARROW),new ItemStack[][]{{new ItemStack(Items.BONE),new ItemStack(Items.ARROW)},{new ItemStack(Items.APPLE),new ItemStack(Items.COOKIE)}}));
	}
	
	public void addRecipe(IForgeRecipe recipe) {
		recipes.add(recipe);
	}
}
