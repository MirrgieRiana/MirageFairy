package mirrg.minecraft.mod.miragefairy.modules.fairy;

import mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairy.ItemTypeProvider;

public class ItemFairy extends ItemFairyBase
{

	public ItemFairy(ItemTypeProvider[] types)
	{
		super(types);
	}

	@Override
	public boolean canBeMagicOperator()
	{
		return true;
	}

	@Override
	public boolean canUseMagic()
	{
		return true;
	}

}
