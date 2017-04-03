package mirrg.minecraft.mod.miragefairy.api;

import mirrg.minecraft.mod.miragefairy.util.Util;
import net.minecraft.util.text.translation.I18n;

public interface IFairy
{

	public static final IFairy DUMMY = new FairyDummy();

	public String getUnlocalizedName();

	public default String getUnlocalizedName(String unlocalizedNamePrefix)
	{
		return unlocalizedNamePrefix + Util.toUpperCaseHead(getUnlocalizedName());
	}

	public default String getLocalizedName()
	{
		return I18n.translateToLocal("fairy." + getUnlocalizedName() + ".name").trim();
	}

	public FairyColorset getColor();

	public int getRarity();

	public double getCost();

	public FairyPotential getPotential();

}
