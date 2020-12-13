package com.nuparu.sevendaystomine.potions;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.util.DamageSources;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.EnumDifficulty;

public class PotionChlorinePoisoning extends PotionBase {

	public PotionChlorinePoisoning(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(0,1);
		setRegistryName(SevenDaysToMine.MODID, "chlorine_poisoning");
		setPotionName("effect." + getRegistryName().getResourcePath());
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_) {
		if (entityLivingBaseIn.world.getDifficulty() == EnumDifficulty.PEACEFUL)
			return;
		if (entityLivingBaseIn.world.rand.nextInt(10) == 0) {
			entityLivingBaseIn.attackEntityFrom(DamageSources.chlorinePoison, 1);

		}
	}
	
}
