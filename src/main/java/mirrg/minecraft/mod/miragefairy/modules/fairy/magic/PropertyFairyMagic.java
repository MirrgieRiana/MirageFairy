package mirrg.minecraft.mod.miragefairy.modules.fairy.magic;

import com.mojang.realmsclient.gui.ChatFormatting;

import mirrg.minecraft.mod.miragefairy.api.IFairy;

public abstract class PropertyFairyMagic<T>
{

	public String name;
	public IPropertyFairyMagic<T> function;
	public String hint;

	public PropertyFairyMagic(String name, IPropertyFairyMagic<T> function, String hint)
	{
		this.name = name;
		this.function = function;
		this.hint = hint;
	}

	public T get(IFairy fairy)
	{
		return function.get(new double[] {
			fairy.getPotential().co(),
			fairy.getPotential().in(),
			fairy.getPotential().vi(),
			fairy.getPotential().lo(),
			fairy.getPotential().ma(),
			fairy.getPotential().et(),
		},
			fairy.getPotential().co(),
			fairy.getPotential().in(),
			fairy.getPotential().vi(),
			fairy.getPotential().lo(),
			fairy.getPotential().ma(),
			fairy.getPotential().et());
	}

	public String toString(IFairy fairy)
	{
		return get(fairy).toString();
	}

	public static interface IPropertyFairyMagic<T>
	{

		public T get(double[] p, double Co, double In, double Vi, double Lo, double Ma, double Et);

	}

	public ChatFormatting getColor(IFairy fairy)
	{
		return !function.get(new double[] {
			0, 0, 0, 0, 0, 0,
		}, 0, 0, 0, 0, 0, 0).equals(get(fairy)) ? ChatFormatting.GREEN : ChatFormatting.RED;
	}

}
