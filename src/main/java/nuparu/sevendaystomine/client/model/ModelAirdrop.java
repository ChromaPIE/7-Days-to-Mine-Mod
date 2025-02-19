package nuparu.sevendaystomine.client.model;


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.entity.EntityAirdrop;

@SideOnly(Side.CLIENT)
public class ModelAirdrop extends ModelBase {
	ModelRenderer Box;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	ModelRenderer Shape5;
	ModelRenderer Shape6;
	ModelRenderer Shape7;
	ModelRenderer Shape8;
	ModelRenderer Shape1;
	ModelRenderer Shape9;
	ModelRenderer Shape10;
	ModelRenderer Shape11;

	public ModelAirdrop() {
		textureWidth = 128;
		textureHeight = 64;

		Box = new ModelRenderer(this, 0, 0);
		Box.addBox(0F, 0F, 0F, 16, 16, 16);
		Box.setRotationPoint(-8F, 8F, -8F);
		Box.setTextureSize(128, 64);
		Box.mirror = true;
		setRotation(Box, 0F, 0F, 0F);
		Shape2 = new ModelRenderer(this, 122, 0);
		Shape2.addBox(1F, -33F, 0F, 1, 33, 1);
		Shape2.setRotationPoint(6F, 8F, 6F);
		Shape2.setTextureSize(128, 64);
		Shape2.mirror = true;
		setRotation(Shape2, -0.0349066F, 0F, 0.1745329F);
		Shape3 = new ModelRenderer(this, 122, 0);
		Shape3.addBox(-1F, -33F, -1F, 1, 33, 1);
		Shape3.setRotationPoint(-6F, 8F, -6F);
		Shape3.setTextureSize(128, 64);
		Shape3.mirror = true;
		setRotation(Shape3, 0.0349066F, 0F, -0.1745329F);
		Shape4 = new ModelRenderer(this, 122, 0);
		Shape4.addBox(-1F, -33F, 0F, 1, 33, 1);
		Shape4.setRotationPoint(-6F, 8F, 6F);
		Shape4.setTextureSize(128, 64);
		Shape4.mirror = true;
		setRotation(Shape4, -0.0349066F, 0F, -0.1745329F);
		Shape5 = new ModelRenderer(this, 122, 0);
		Shape5.addBox(0F, -33F, -1F, 1, 33, 1);
		Shape5.setRotationPoint(6F, 8F, -6F);
		Shape5.setTextureSize(128, 64);
		Shape5.mirror = true;
		setRotation(Shape5, 0.0349066F, 0F, 0.1745329F);
		Shape6 = new ModelRenderer(this, 0, 33);
		Shape6.addBox(0F, 0F, 0F, 24, 1, 18);
		Shape6.setRotationPoint(-12F, -25F, -9F);
		Shape6.setTextureSize(128, 64);
		Shape6.mirror = true;
		setRotation(Shape6, 0F, 0F, 0F);
		Shape7 = new ModelRenderer(this, 3, 33);
		Shape7.addBox(0F, 0F, 0F, 12, 1, 18);
		Shape7.setRotationPoint(12F, -25F, -9F);
		Shape7.setTextureSize(128, 64);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, 0F, 0.4101524F);
		Shape8 = new ModelRenderer(this, 3, 33);
		Shape8.addBox(-12F, 0F, 0F, 12, 1, 18);
		Shape8.setRotationPoint(-12F, -25F, -9F);
		Shape8.setTextureSize(128, 64);
		Shape8.mirror = true;
		setRotation(Shape8, 0F, 0F, -0.4101524F);
		Shape1 = new ModelRenderer(this, 118, 0);
		Shape1.addBox(-1F, -32F, -1F, 1, 32, 1);
		Shape1.setRotationPoint(-6F, 8F, -6F);
		Shape1.setTextureSize(128, 64);
		Shape1.mirror = true;
		setRotation(Shape1, 0.0349066F, 0F, -0.4974188F);
		Shape9 = new ModelRenderer(this, 118, 0);
		Shape9.addBox(0F, -32F, -1F, 1, 32, 1);
		Shape9.setRotationPoint(6F, 8F, -6F);
		Shape9.setTextureSize(128, 64);
		Shape9.mirror = true;
		setRotation(Shape9, 0.0349066F, 0F, 0.4974188F);
		Shape10 = new ModelRenderer(this, 118, 0);
		Shape10.addBox(0F, -32F, 0F, 1, 32, 1);
		Shape10.setRotationPoint(6F, 8F, 6F);
		Shape10.setTextureSize(128, 64);
		Shape10.mirror = true;
		setRotation(Shape10, -0.0349066F, 0F, 0.4974188F);
		Shape11 = new ModelRenderer(this, 118, 0);
		Shape11.addBox(-1F, -32F, 0F, 1, 32, 1);
		Shape11.setRotationPoint(-6F, 8F, 6F);
		Shape11.setTextureSize(128, 64);
		Shape11.mirror = true;
		setRotation(Shape11, -0.0349066F, 0F, -0.4974188F);
	}

	public void render(EntityAirdrop entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Box.render(f5);
		if (!entity.onGround && !entity.getLanded()) {
			Shape2.render(f5);
			Shape3.render(f5);
			Shape4.render(f5);
			Shape5.render(f5);
			Shape6.render(f5);
			Shape7.render(f5);
			Shape8.render(f5);
			Shape1.render(f5);
			Shape9.render(f5);
			Shape10.render(f5);
			Shape11.render(f5);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
