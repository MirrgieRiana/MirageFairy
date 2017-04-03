package mirrg.minecraft.mod.miragefairy.modules.main;

import mirrg.minecraft.mod.miragefairy.MirageFairyMod;
import mirrg.minecraft.mod.miragefairy.core.IItemTypeProvider;
import mirrg.minecraft.mod.miragefairy.core.ItemType;

public enum EnumItemMaterial implements IItemTypeProvider
{
	DOLL(new ItemType(0, "doll", MirageFairyMod.MODID + ":doll")),
	MIRAGE_DUST(new ItemTypeDustMirage(1, "dustMirage", MirageFairyMod.MODID + ":mirage_dust"));

	public final ItemType itemType;

	private EnumItemMaterial(ItemType itemType)
	{
		this.itemType = itemType;
	}

	@Override
	public ItemType getItemType()
	{
		return itemType;
	}

}
