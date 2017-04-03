package mirrg.minecraft.mod.miragefairy.modules.fairy;

import net.minecraft.util.text.translation.I18n;

public interface IFairy
{

	public static final IFairy DUMMY = new FairyDummy();

	public String getUnlocalizedName();

	public default String getUnlocalizedName(String unlocalizedNamePrefix)
	{
		return unlocalizedNamePrefix + toUpperCaseHead(getUnlocalizedName());
	}

	// TODO move to util
	public static String toUpperCaseHead(String str)
	{
		if (str.length() == 0) return str;
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public default String getLocalizedName()
	{
		return I18n.translateToLocal("fairy." + getUnlocalizedName() + ".name").trim();
	}

	public int getColorSkin();

	public int getColorDark();

	public int getColorBright();

	public int getColorHair();

	public int getRarity();

	public double getCost();

	public double getCo();

	public double getIn();

	public double getVi();

	public double getLo();

	public double getMa();

	public double getEt();

	public default double getSum()
	{
		return getCo() + getIn() + getVi() + getLo() + getMa() + getEt();
	}

}
