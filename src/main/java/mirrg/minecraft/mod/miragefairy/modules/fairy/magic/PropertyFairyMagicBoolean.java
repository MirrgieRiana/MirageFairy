package mirrg.minecraft.mod.miragefairy.modules.fairy.magic;

import mirrg.minecraft.mod.miragefairy.modules.fairy.IFairy;

public class PropertyFairyMagicBoolean extends PropertyFairyMagic<Boolean>
{

	public PropertyFairyMagicBoolean(String name, PropertyFairyMagic.IPropertyFairyMagic<Boolean> function, String hint)
	{
		super(name, function, hint);
	}

	public boolean getAsBoolean(IFairy fairy)
	{
		return super.get(fairy);
	}

	@Override
	public String toString(IFairy fairy)
	{
		return get(fairy) ? "Yes" : "No";
	}

}
