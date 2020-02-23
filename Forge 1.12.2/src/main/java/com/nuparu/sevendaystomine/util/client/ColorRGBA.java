package com.nuparu.sevendaystomine.util.client;

import com.nuparu.sevendaystomine.util.MathUtils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ColorRGBA {

	public double R;
	public double G;
	public double B;
	public double A;

	public ColorRGBA(double r, double g, double b, double a) {
		this.R = r;
		this.G = g;
		this.B = b;
		this.A = a;
	}

	public ColorRGBA(double r, double g, double b) {
		this.R = r;
		this.G = g;
		this.B = b;
		this.A = 1;
	}

	public ColorRGBA darken(double delta) {

		double r = MathUtils.clamp(R - delta, 0d, 1d);
		double g = MathUtils.clamp(G - delta, 0d, 1d);
		double b = MathUtils.clamp(B - delta, 0d, 1d);

		return new ColorRGBA(r, g, b, A);
	}

	public ColorRGBA lighten(double delta) {

		double r = MathUtils.clamp(R + delta, 0d, 1d);
		double g = MathUtils.clamp(G + delta, 0d, 1d);
		double b = MathUtils.clamp(B + delta, 0d, 1d);

		return new ColorRGBA(r, g, b, A);
	}
}
