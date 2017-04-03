package mirrg.minecraft.mod.miragefairy.api;

import java.util.stream.DoubleStream;

public class FairyPotential
{

	private double[] potentials;
	private double sum;

	public FairyPotential(double co, double in, double vi, double lo, double ma, double et)
	{
		this(new double[] {
			co, in, vi, lo, ma, et,
		});
	}

	public FairyPotential(double[] potentials)
	{
		if (potentials.length != EnumFairyPotentialType.values().length) throw new IllegalArgumentException();
		this.potentials = potentials.clone();
		this.sum = DoubleStream.of(potentials).sum();
	}

	public double get(EnumFairyPotentialType fairyPotentialType)
	{
		return potentials[fairyPotentialType.ordinal()];
	}

	public double co()
	{
		return get(EnumFairyPotentialType.CO);
	}

	public double in()
	{
		return get(EnumFairyPotentialType.IN);
	}

	public double vi()
	{
		return get(EnumFairyPotentialType.VI);
	}

	public double lo()
	{
		return get(EnumFairyPotentialType.LO);
	}

	public double ma()
	{
		return get(EnumFairyPotentialType.MA);
	}

	public double et()
	{
		return get(EnumFairyPotentialType.ET);
	}

	public double sum()
	{
		return sum;
	}

}
