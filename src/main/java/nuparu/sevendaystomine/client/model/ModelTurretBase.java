package nuparu.sevendaystomine.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTurretBase extends ModelBase {
	ModelRenderer BodyTop;
	ModelRenderer RotorBase;
	ModelRenderer RotorA;
	ModelRenderer RotorB;
	ModelRenderer RotorC;
	ModelRenderer RotorD;
	ModelRenderer BodyBottom;
	ModelRenderer LegTopA;
	ModelRenderer LegTopB;
	ModelRenderer LegTopC;
	ModelRenderer LegTopD;
	ModelRenderer LegBottomA;
	ModelRenderer LegBottomB;
	ModelRenderer LegBottomC;
	ModelRenderer LegBottomD;
	ModelRenderer HeadLidA;
	ModelRenderer HeadLidB;
	ModelRenderer HeadBase;
	ModelRenderer BarrelBase;
	ModelRenderer BarrelA;
	ModelRenderer BarrelB;
	ModelRenderer BarrelC;
	ModelRenderer BarrelD;
	ModelRenderer BarrelRing;
	ModelRenderer BarrelEnd;
	ModelRenderer AmmoBoxBaseA;
	ModelRenderer AmmoBoxBaseB;
	ModelRenderer AmmoBoxBaseC;
	ModelRenderer AmmoBox;
	ModelRenderer BarrelE;
	ModelRenderer BarrelF;
	ModelRenderer BarrelG;
	ModelRenderer BarrelH;

	public ModelTurretBase() {
		textureWidth = 64;
		textureHeight = 64;
		BodyTop = new ModelRenderer(this, 0, 45);
		BodyTop.addBox(-1.5F, 0F, -1.5F, 3, 2, 3);
		BodyTop.setRotationPoint(0F, 10F, 0F);
		BodyTop.setTextureSize(64, 64);
		BodyTop.mirror = true;
		setRotation(BodyTop, 0F, 0F, 0F);
		RotorBase = new ModelRenderer(this, 0, 37);
		RotorBase.addBox(-3F, 0F, -3F, 6, 1, 6);
		RotorBase.setRotationPoint(0F, 9F, 0F);
		RotorBase.setTextureSize(64, 64);
		RotorBase.mirror = true;
		setRotation(RotorBase, 0F, 0F, 0F);
		RotorA = new ModelRenderer(this, 0, 12);
		RotorA.addBox(-4F, 0F, -2F, 1, 1, 4);
		RotorA.setRotationPoint(0F, 9F, 0F);
		RotorA.setTextureSize(64, 64);
		RotorA.mirror = true;
		setRotation(RotorA, 0F, 0F, 0F);
		RotorB = new ModelRenderer(this, 0, 12);
		RotorB.addBox(3F, 0F, -2F, 1, 1, 4);
		RotorB.setRotationPoint(0F, 9F, 0F);
		RotorB.setTextureSize(64, 64);
		RotorB.mirror = true;
		setRotation(RotorB, 0F, 0F, 0F);
		RotorC = new ModelRenderer(this, 0, 5);
		RotorC.addBox(-2F, 0F, -4F, 4, 1, 1);
		RotorC.setRotationPoint(0F, 9F, 0F);
		RotorC.setTextureSize(64, 64);
		RotorC.mirror = true;
		setRotation(RotorC, 0F, 0F, 0F);
		RotorD = new ModelRenderer(this, 0, 5);
		RotorD.addBox(-2F, 0F, 3F, 4, 1, 1);
		RotorD.setRotationPoint(0F, 9F, 0F);
		RotorD.setTextureSize(64, 64);
		RotorD.mirror = true;
		setRotation(RotorD, 0F, 0F, 0F);
		BodyBottom = new ModelRenderer(this, 0, 24);
		BodyBottom.addBox(-2F, 0F, -2F, 4, 4, 4);
		BodyBottom.setRotationPoint(0F, 12F, 0F);
		BodyBottom.setTextureSize(64, 64);
		BodyBottom.mirror = true;
		setRotation(BodyBottom, 0F, 0F, 0F);
		LegTopA = new ModelRenderer(this, 17, 25);
		LegTopA.addBox(0F, 0F, 0F, 2, 5, 2);
		LegTopA.setRotationPoint(1F, 15F, 1F);
		LegTopA.setTextureSize(64, 64);
		LegTopA.mirror = true;
		setRotation(LegTopA, 0.3926991F, 0F, -0.3926991F);
		LegTopB = new ModelRenderer(this, 26, 25);
		LegTopB.addBox(-2F, 0F, 0F, 2, 5, 2);
		LegTopB.setRotationPoint(-1F, 15F, 1F);
		LegTopB.setTextureSize(64, 64);
		LegTopB.mirror = true;
		setRotation(LegTopB, 0.3926991F, 0F, 0.3926991F);
		LegTopC = new ModelRenderer(this, 35, 25);
		LegTopC.addBox(0F, 0F, -2F, 2, 5, 2);
		LegTopC.setRotationPoint(1F, 15F, -1F);
		LegTopC.setTextureSize(64, 64);
		LegTopC.mirror = true;
		setRotation(LegTopC, -0.3926991F, 0F, -0.3926991F);
		LegTopD = new ModelRenderer(this, 44, 25);
		LegTopD.addBox(-2F, 0F, -2F, 2, 5, 2);
		LegTopD.setRotationPoint(-1F, 15F, -1F);
		LegTopD.setTextureSize(64, 64);
		LegTopD.mirror = true;
		setRotation(LegTopD, -0.3926991F, 0F, 0.3926991F);
		LegBottomA = new ModelRenderer(this, 17, 16);
		LegBottomA.addBox(0.5F, 5F, 0.5F, 1, 7, 1);
		LegBottomA.setRotationPoint(1F, 15F, 1F);
		LegBottomA.setTextureSize(64, 64);
		LegBottomA.mirror = true;
		setRotation(LegBottomA, 0.3926991F, 0F, -0.3926991F);
		LegBottomB = new ModelRenderer(this, 22, 16);
		LegBottomB.addBox(-1.5F, 5F, 0.5F, 1, 7, 1);
		LegBottomB.setRotationPoint(-1F, 15F, 1F);
		LegBottomB.setTextureSize(64, 64);
		LegBottomB.mirror = true;
		setRotation(LegBottomB, 0.3926991F, 0F, 0.3926991F);
		LegBottomC = new ModelRenderer(this, 27, 16);
		LegBottomC.addBox(0.5F, 5F, -1.5F, 1, 7, 1);
		LegBottomC.setRotationPoint(1F, 15F, -1F);
		LegBottomC.setTextureSize(64, 64);
		LegBottomC.mirror = true;
		setRotation(LegBottomC, -0.3926991F, 0F, -0.3926991F);
		LegBottomD = new ModelRenderer(this, 32, 16);
		LegBottomD.addBox(-1.5F, 5F, -1.5F, 1, 7, 1);
		LegBottomD.setRotationPoint(-1F, 15F, -1F);
		LegBottomD.setTextureSize(64, 64);
		LegBottomD.mirror = true;
		setRotation(LegBottomD, -0.3926991F, 0F, 0.3926991F);
		HeadLidA = new ModelRenderer(this, 50, 0);
		HeadLidA.addBox(2F, -5F, -3F, 1, 5, 6);
		HeadLidA.setRotationPoint(0F, 9F, 0F);
		HeadLidA.setTextureSize(64, 64);
		HeadLidA.mirror = true;
		setRotation(HeadLidA, 0F, 0F, 0F);
		HeadLidB = new ModelRenderer(this, 50, 0);
		HeadLidB.addBox(-3F, -5F, -3F, 1, 5, 6);
		HeadLidB.setRotationPoint(0F, 9F, 0F);
		HeadLidB.setTextureSize(64, 64);
		HeadLidB.mirror = true;
		setRotation(HeadLidB, 0F, 0F, 0F);
		HeadBase = new ModelRenderer(this, 0, 51);
		HeadBase.addBox(-2F, -6F, -4F, 4, 5, 8);
		HeadBase.setRotationPoint(0F, 9F, 0F);
		HeadBase.setTextureSize(64, 64);
		HeadBase.mirror = true;
		setRotation(HeadBase, 0F, 0F, 0F);
		BarrelBase = new ModelRenderer(this, 38, 11);
		BarrelBase.addBox(-0.5F, -4F, -16F, 1, 1, 12);
		BarrelBase.setRotationPoint(0F, 9F, 0F);
		BarrelBase.setTextureSize(64, 64);
		BarrelBase.mirror = true;
		setRotation(BarrelBase, 0F, 0F, 0F);
		BarrelA = new ModelRenderer(this, 44, 17);
		BarrelA.addBox(-0.5F, -3F, -16F, 1, 1, 6);
		BarrelA.setRotationPoint(0F, 9F, 0F);
		BarrelA.setTextureSize(64, 64);
		BarrelA.mirror = true;
		setRotation(BarrelA, 0F, 0F, 0F);
		BarrelB = new ModelRenderer(this, 44, 17);
		BarrelB.addBox(-0.5F, -5F, -16F, 1, 1, 6);
		BarrelB.setRotationPoint(0F, 9F, 0F);
		BarrelB.setTextureSize(64, 64);
		BarrelB.mirror = true;
		setRotation(BarrelB, 0F, 0F, 0F);
		BarrelC = new ModelRenderer(this, 44, 17);
		BarrelC.addBox(-1.5F, -4F, -16F, 1, 1, 6);
		BarrelC.setRotationPoint(0F, 9F, 0F);
		BarrelC.setTextureSize(64, 64);
		BarrelC.mirror = true;
		setRotation(BarrelC, 0F, 0F, 0F);
		BarrelD = new ModelRenderer(this, 44, 17);
		BarrelD.addBox(0.5F, -4F, -16F, 1, 1, 6);
		BarrelD.setRotationPoint(0F, 9F, 0F);
		BarrelD.setTextureSize(64, 64);
		BarrelD.mirror = true;
		setRotation(BarrelD, 0F, 0F, 0F);
		BarrelRing = new ModelRenderer(this, 56, 28);
		BarrelRing.addBox(-1.5F, -5F, -10F, 3, 3, 1);
		BarrelRing.setRotationPoint(0F, 9F, 0F);
		BarrelRing.setTextureSize(64, 64);
		BarrelRing.mirror = true;
		setRotation(BarrelRing, 0F, 0F, 0F);
		BarrelEnd = new ModelRenderer(this, 0, 18);
		BarrelEnd.addBox(-1.5F, -5F, -18F, 3, 3, 2);
		BarrelEnd.setRotationPoint(0F, 9F, 0F);
		BarrelEnd.setTextureSize(64, 64);
		BarrelEnd.mirror = true;
		setRotation(BarrelEnd, 0F, 0F, 0F);
		AmmoBoxBaseA = new ModelRenderer(this, 35, 0);
		AmmoBoxBaseA.addBox(-3F, -4F, 4F, 6, 4, 1);
		AmmoBoxBaseA.setRotationPoint(0F, 9F, 0F);
		AmmoBoxBaseA.setTextureSize(64, 64);
		AmmoBoxBaseA.mirror = true;
		setRotation(AmmoBoxBaseA, 0F, 0F, 0F);
		AmmoBoxBaseB = new ModelRenderer(this, 34, 58);
		AmmoBoxBaseB.addBox(-3F, -1F, 5F, 6, 1, 4);
		AmmoBoxBaseB.setRotationPoint(0F, 9F, 0F);
		AmmoBoxBaseB.setTextureSize(64, 64);
		AmmoBoxBaseB.mirror = true;
		setRotation(AmmoBoxBaseB, 0F, 0F, 0F);
		AmmoBoxBaseC = new ModelRenderer(this, 35, 0);
		AmmoBoxBaseC.addBox(-3F, -4F, 9F, 6, 4, 1);
		AmmoBoxBaseC.setRotationPoint(0F, 9F, 0F);
		AmmoBoxBaseC.setTextureSize(64, 64);
		AmmoBoxBaseC.mirror = true;
		setRotation(AmmoBoxBaseC, 0F, 0F, 0F);
		AmmoBox = new ModelRenderer(this, 44, 40);
		AmmoBox.addBox(-3F, -6F, 5F, 6, 5, 4);
		AmmoBox.setRotationPoint(0F, 9F, 0F);
		AmmoBox.setTextureSize(64, 64);
		AmmoBox.mirror = true;
		setRotation(AmmoBox, 0F, 0F, 0F);
		BarrelE = new ModelRenderer(this, 37, 15);
		BarrelE.addBox(-0.5F, -3F, -9F, 1, 1, 5);
		BarrelE.setRotationPoint(0F, 9F, 0F);
		BarrelE.setTextureSize(64, 64);
		BarrelE.mirror = true;
		setRotation(BarrelE, 0F, 0F, 0F);
		BarrelF = new ModelRenderer(this, 37, 15);
		BarrelF.addBox(-0.5F, -5F, -9F, 1, 1, 5);
		BarrelF.setRotationPoint(0F, 9F, 0F);
		BarrelF.setTextureSize(64, 64);
		BarrelF.mirror = true;
		setRotation(BarrelF, 0F, 0F, 0F);
		BarrelG = new ModelRenderer(this, 37, 15);
		BarrelG.addBox(0.5F, -4F, -9F, 1, 1, 5);
		BarrelG.setRotationPoint(0F, 9F, 0F);
		BarrelG.setTextureSize(64, 64);
		BarrelG.mirror = true;
		setRotation(BarrelG, 0F, 0F, 0F);
		BarrelH = new ModelRenderer(this, 37, 15);
		BarrelH.addBox(-1.5F, -4F, -9F, 1, 1, 5);
		BarrelH.setRotationPoint(0F, 9F, 0F);
		BarrelH.setTextureSize(64, 64);
		BarrelH.mirror = true;
		setRotation(BarrelH, 0F, 0F, 0F);
	}

	public void render(float headRotation) {
		rotateHead(headRotation);
		BodyTop.render(0.0625F);
		RotorBase.render(0.0625F);
		RotorA.render(0.0625F);
		RotorB.render(0.0625F);
		RotorC.render(0.0625F);
		RotorD.render(0.0625F);
		BodyBottom.render(0.0625F);
		LegTopA.render(0.0625F);
		LegTopB.render(0.0625F);
		LegTopC.render(0.0625F);
		LegTopD.render(0.0625F);
		LegBottomA.render(0.0625F);
		LegBottomB.render(0.0625F);
		LegBottomC.render(0.0625F);
		LegBottomD.render(0.0625F);
		HeadLidA.render(0.0625F);
		HeadLidB.render(0.0625F);
		HeadBase.render(0.0625F);
		BarrelBase.render(0.0625F);
		BarrelA.render(0.0625F);
		BarrelB.render(0.0625F);
		BarrelC.render(0.0625F);
		BarrelD.render(0.0625F);
		BarrelRing.render(0.0625F);
		BarrelEnd.render(0.0625F);
		AmmoBoxBaseA.render(0.0625F);
		AmmoBoxBaseB.render(0.0625F);
		AmmoBoxBaseC.render(0.0625F);
		AmmoBox.render(0.0625F);
		BarrelE.render(0.0625F);
		BarrelF.render(0.0625F);
		BarrelG.render(0.0625F);
		BarrelH.render(0.0625F);
	}

	public void rotateHead(float rot) {
		// this.head.rotateAngleY = f3 / (180F / (float)Math.PI);

		this.HeadBase.rotateAngleY = rot / (180F / (float) Math.PI);
		this.HeadLidA.rotateAngleY = this.HeadBase.rotateAngleY;
		this.HeadLidB.rotateAngleY = this.HeadBase.rotateAngleY;
		this.BarrelBase.rotateAngleY = this.HeadBase.rotateAngleY;
		this.BarrelA.rotateAngleY = this.HeadBase.rotateAngleY;
		this.BarrelB.rotateAngleY = this.HeadBase.rotateAngleY;
		this.BarrelC.rotateAngleY = this.HeadBase.rotateAngleY;
		this.BarrelD.rotateAngleY = this.HeadBase.rotateAngleY;
		this.BarrelE.rotateAngleY = this.HeadBase.rotateAngleY;
		this.BarrelF.rotateAngleY = this.HeadBase.rotateAngleY;
		this.BarrelG.rotateAngleY = this.HeadBase.rotateAngleY;
		this.BarrelH.rotateAngleY = this.HeadBase.rotateAngleY;
		this.BarrelEnd.rotateAngleY = this.HeadBase.rotateAngleY;
		this.BarrelRing.rotateAngleY = this.HeadBase.rotateAngleY;
		this.AmmoBoxBaseA.rotateAngleY = this.HeadBase.rotateAngleY;
		this.AmmoBoxBaseB.rotateAngleY = this.HeadBase.rotateAngleY;
		this.AmmoBoxBaseC.rotateAngleY = this.HeadBase.rotateAngleY;
		this.AmmoBox.rotateAngleY = this.HeadBase.rotateAngleY;
		this.RotorBase.rotateAngleY = this.HeadBase.rotateAngleY;
		this.RotorA.rotateAngleY = this.HeadBase.rotateAngleY;
		this.RotorB.rotateAngleY = this.HeadBase.rotateAngleY;
		this.RotorC.rotateAngleY = this.HeadBase.rotateAngleY;
		this.RotorD.rotateAngleY = this.HeadBase.rotateAngleY;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}