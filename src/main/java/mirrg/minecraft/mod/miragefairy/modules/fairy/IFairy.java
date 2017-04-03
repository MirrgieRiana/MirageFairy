package mirrg.minecraft.mod.miragefairy.modules.fairy;

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
