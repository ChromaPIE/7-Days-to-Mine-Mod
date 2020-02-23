package com.nuparu.sevendaystomine.client.renderer.entity;

import com.nuparu.sevendaystomine.entity.EntityMountableBlock;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class RenderMountableBlock extends Render<EntityMountableBlock>{

	public RenderMountableBlock(RenderManager renderManager) {
		super(renderManager);
		this.shadowSize = 0f;
	}

	@Override
	public void doRender(EntityMountableBlock entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
		
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityMountableBlock entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
