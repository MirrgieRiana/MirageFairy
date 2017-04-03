package mirrg.minecraft.mod.miragefairy.api;

public class FairyDummy implements IFairy
{

	@Override
	public String getUnlocalizedName()
	{
		return "dummy";
	}

	private FairyColorset fairyColorset = new FairyColorset(0, 0, 0, 0);

	@Override
	public FairyColorset getColor()
	{
		return fairyColorset;
	}

	@Override
	public int getRarity()
	{
		return 1;
	}

	@Override
	public double getCost()
	{
		return 100;
	}

	private FairyPotential fairyPotential = new FairyPotential(0, 0, 0, 0, 0, 0);

	@Override
	public FairyPotential getPotential()
	{
		return fairyPotential;
	}

}
