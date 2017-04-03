package mirrg.minecraft.mod.miragefairy.modules.fairy.magic;

import mirrg.minecraft.mod.miragefairy.api.IFairy;

public class PropertyFairyMagicDouble extends PropertyFairyMagic<Double>
{

	public PropertyFairyMagicDouble(String name, PropertyFairyMagic.IPropertyFairyMagic<Double> function, String hint)
	{
		super(name, function, hint);
	}

	public double getAsDouble(IFairy fairy)
	{
		return super.get(fairy);
	}

	@Override
	public String toString(IFairy fairy)
	{
		return String.format("%.2f", get(fairy));
	}

}
