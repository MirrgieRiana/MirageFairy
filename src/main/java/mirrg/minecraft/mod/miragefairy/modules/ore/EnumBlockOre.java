package mirrg.minecraft.mod.miragefairy.modules.ore;

import mirrg.minecraft.mod.miragefairy.core.BlockType;
import mirrg.minecraft.mod.miragefairy.core.IBlockTypeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public enum EnumBlockOre implements IBlockTypeProvider, IStringSerializable
{
	MIRAGIUM(new BlockType(0, "miragium", "miragium", 3.0f, 1, null)),
	BLOODSTONE(new BlockType(1, "bloodstone", "bloodstone", 3.0f, 2, () -> new ItemStack(ModuleOre.itemMineral, 4, 0)));

	public final BlockType blockType;

	private EnumBlockOre(BlockType blockType)
	{
		this.blockType = blockType;
	}

	@Override
	public String getName()
	{
		return blockType.modelName;
	}

	@Override
	public BlockType getBlockType()
	{
		return blockType;
	}

}
