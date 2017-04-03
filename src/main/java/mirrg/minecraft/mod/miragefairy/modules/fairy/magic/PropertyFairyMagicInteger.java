package mirrg.minecraft.mod.miragefairy.modules.fairy.magic;

import mirrg.minecraft.mod.miragefairy.modules.fairy.IFairy;

public class PropertyFairyMagicInteger extends PropertyFairyMagic<Integer>
{

	public PropertyFairyMagicInteger(String name, PropertyFairyMagic.IPropertyFairyMagic<Integer> function, String hint)
	{
		super(name, function, hint);
	}

	public double getAsInt(IFairy fairy)
	{
		return super.get(fairy);
	}

}
