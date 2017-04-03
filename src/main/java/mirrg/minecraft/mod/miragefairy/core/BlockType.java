package mirrg.minecraft.mod.miragefairy.core;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

public class BlockType
{

	public final int meta;
	public final String unlocalizedName;
	public final String modelName;
	public final float hardness;
	public final int harvestLevel;
	public final Supplier<ItemStack> sDropItem;

	public BlockType(int meta, String unlocalizedName, String modelName, float hardness, int harvestLevel, Supplier<ItemStack> sDropItem)
	{
		this.meta = meta;
		this.unlocalizedName = unlocalizedName;
		this.modelName = modelName;
		this.hardness = hardness;
		this.harvestLevel = harvestLevel;
		this.sDropItem = sDropItem;
	}

}
