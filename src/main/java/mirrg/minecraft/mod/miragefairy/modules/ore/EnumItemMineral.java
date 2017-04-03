package mirrg.minecraft.mod.miragefairy.modules.ore;

import mirrg.minecraft.mod.miragefairy.MirageFairyMod;
import mirrg.minecraft.mod.miragefairy.core.IItemTypeProvider;
import mirrg.minecraft.mod.miragefairy.core.ItemType;

public enum EnumItemMineral implements IItemTypeProvider
{
	BLOODSTONE(new ItemType(0, "bloodstone", MirageFairyMod.MODID + ":bloodstone")),
	MIRAGIUM_INGOT(new ItemType(1, "ingotMiragium", MirageFairyMod.MODID + ":miragium_ingot"));

	public final ItemType itemType;

	private EnumItemMineral(ItemType itemType)
	{
		this.itemType = itemType;
	}

	@Override
	public ItemType getItemType()
	{
		return itemType;
	}

}
