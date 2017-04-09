package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import java.util.Random;

import mirrg.minecraft.mod.miragefairy.util.Util;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFairyTorch extends BlockTorch implements ITileEntityProvider
{

	public BlockFairyTorch()
	{
		setHardness(0.0F);
		setLightLevel(14 / 15f);
		setSoundType(SoundType.WOOD);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityFairyTorch();
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return Util.getTileEntity(TileEntityFairyTorch.class, world, pos)
			.map(t -> t.data.lightValue)
			.orElse(15);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.AIR;
	}

}
