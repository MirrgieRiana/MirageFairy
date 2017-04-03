package mirrg.minecraft.mod.miragefairy.util;

import java.util.Random;

public class Color
{

	public final double r;
	public final double g;
	public final double b;

	/**
	 * @param rgb
	 *            "0" ~ "ffffff"
	 */
	public Color(String rgb)
	{
		this(Integer.parseInt(rgb, 16));
	}

	/**
	 * @param rgb
	 *            0x000000 ~ 0xffffff
	 */
	public Color(int rgb)
	{
		this((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
	}

	/**
	 * @param r
	 *            0 ~ 255
	 * @param g
	 *            0 ~ 255
	 * @param b
	 *            0 ~ 255
	 */
	public Color(int r, int g, int b)
	{
		this(r / 255.0, g / 255.0, b / 255.0);
	}

	/**
	 * @param r
	 *            0 ~ 1
	 * @param g
	 *            0 ~ 1
	 * @param b
	 *            0 ~ 1
	 */
	public Color(double r, double g, double b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public Color brighter(double min)
	{
		return mul(1 - min).add(min);
	}

	public Color darker(double max)
	{
		return mul(max);
	}

	public Color mul(double a)
	{
		return mul(a, a, a);
	}

	public Color mul(double r, double g, double b)
	{
		return new Color(this.r * r, this.g * g, this.b * b);
	}

	public Color add(double a)
	{
		return add(a, a, a);
	}

	public Color add(double r, double g, double b)
	{
		return new Color(this.r + r, this.g + g, this.b + b);
	}

	public int toInt()
	{
		return rgb(r, g, b);
	}

	/**
	 * @param r
	 *            0 ~ 1
	 * @param g
	 *            0 ~ 1
	 * @param b
	 *            0 ~ 1
	 * @return
	 */
	public static int rgb(double r, double g, double b)
	{
		return rgb((int) (r * 255), (int) (g * 255), (int) (b * 255));
	}

	/**
	 * @param r
	 *            0 ~ 255
	 * @param g
	 *            0 ~ 255
	 * @param b
	 *            0 ~ 255
	 * @return
	 */
	public static int rgb(int r, int g, int b)
	{
		return (Util.trim(r, 0, 255) << 16)
			| (Util.trim(g, 0, 255) << 8)
			| Util.trim(b, 0, 255);
	}

	public static Color random()
	{
		return new Color(Math.random(), Math.random(), Math.random());
	}

	public static Color random(Random random)
	{
		return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

}
