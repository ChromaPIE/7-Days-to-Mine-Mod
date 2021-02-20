package com.nuparu.sevendaystomine.client.renderer.entity;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.model.ModelCar;
import com.nuparu.sevendaystomine.client.model.ModelMinibike;
import com.nuparu.sevendaystomine.entity.EntityCar;
import com.nuparu.sevendaystomine.entity.EntityMinibike;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCar extends Render<EntityCar> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/car.png");
	protected ModelCar modelCar = new ModelCar();

	public RenderCar(RenderManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0;

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCar entity) {
		return TEXTURE;
	}

	@Override
	public void doRender(EntityCar entity, double x, double y, double z, float entityYaw, float partialTicks) {
		preRenderCallback(entity,partialTicks);
		
		GlStateManager.pushMatrix();
		this.setupTranslation(x, y+0.1, z);
		this.setupRotation(entity, entityYaw, partialTicks);
		this.bindEntityTexture(entity);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		this.modelCar.render(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	protected void preRenderCallback(EntityCar entity, float partialTicks) {
		ModelCar model = modelCar;
		float rotation = entity.wheelAnglePrev + (entity.wheelAngle -
		entity.wheelAnglePrev) * partialTicks;
		if (rotation != 0) {
			model.setRotationAngle(model.getFrontWheels(), rotation, 0, 0);
			model.setRotationAngle(model.getRearWheels(), rotation, 0, 0);
		}
	}

	public void setupTranslation(double p_188309_1_, double p_188309_3_, double p_188309_5_) {
		GlStateManager.translate((float) p_188309_1_, (float) p_188309_3_ + 1.375F, (float) p_188309_5_);
	}

	public void setupRotation(EntityCar p_188311_1_, float p_188311_2_, float p_188311_3_) {
		GlStateManager.rotate(180.0F - p_188311_2_, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
	}

}
