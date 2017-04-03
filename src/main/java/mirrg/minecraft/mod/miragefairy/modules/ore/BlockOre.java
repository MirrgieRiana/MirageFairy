package mirrg.minecraft.mod.miragefairy.modules.ore;

import mirrg.minecraft.mod.miragefairy.core.BlockMetadata;
import mirrg.minecraft.mod.miragefairy.core.IBlockTypeProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOre<T extends Enum<T> & IBlockTypeProvider & IStringSerializable> extends BlockMetadata<T>
{

	public BlockOre(Material material, PropertyEnum<T> propertyType, T[] types2, T defaultType)
	{
		super(material, propertyType, types2, defaultType);
		setResistance(5.0f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

}
