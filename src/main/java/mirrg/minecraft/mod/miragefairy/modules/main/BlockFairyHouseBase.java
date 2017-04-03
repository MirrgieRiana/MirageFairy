package mirrg.minecraft.mod.miragefairy.modules.main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ModuleFairy;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockFairyHouseBase extends Block
{

	public BlockFairyHouseBase(Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(blockMaterialIn, blockMapColorIn);
	}

	public BlockFairyHouseBase(Material materialIn)
	{
		super(materialIn);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> list = new ArrayList<>();
		for (int i = 0; i < 1 + fortune; i++) {
			list.add(ModuleFairy.itemFairy.getItemStack(FairyGatcha.getGatchaResult(getGatchaWeight(), RANDOM)));
		}
		return list;
	}

	public abstract Hashtable<EnumFairy, Double> getGatchaWeight();

}
