package com.nuparu.sevendaystomine.client.renderer.tileentity;

import java.util.List;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.config.ModConfig;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.tileentity.TileEntityCalendar;
import com.nuparu.sevendaystomine.tileentity.TileEntityStreetSign;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityCalendarRenderer extends TileEntitySpecialRenderer<TileEntityCalendar> {
	public void render(TileEntityCalendar te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		FontRenderer fontrenderer = this.getFontRenderer();
		EntityPlayer player = Minecraft.getMinecraft().player;

		int mod = Utils.getDay(player.world) % ModConfig.world.bloodmoonFrequency;
		int i = mod == 0 ? 0 : ModConfig.world.bloodmoonFrequency - (mod);

		GlStateManager.pushMatrix();

		int k = te.getBlockMetadata();
		float f2 = 0.0F;
		if (k == 0) {
			f2 = 0.0F;
		}

		if (k == 1) {
			f2 = 90.0F;
		}

		if (k == 2) {
			f2 = 180.0F;
		}

		if (k == 3) {
			f2 = 270.0F;
		}

		/*
		 * GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F); GlStateManager.translate(x +
		 * 0.5F, y + 0.5F, z + 0.5F); GlStateManager.translate(-0.43725,0,0.03125);
		 */
		GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(0.03125F, 0.4F, -0.437F);

		GlStateManager.scale(0.0625, -0.0625, -0.0625);
		fontrenderer.drawString(i + "", -fontrenderer.getStringWidth(i + "") / 2, 0, 0x000000);
		GlStateManager.popMatrix();

	}
}