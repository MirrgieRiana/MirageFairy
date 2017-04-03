package mirrg.minecraft.mod.miragefairy.modules.main.fairyhouse;

import java.util.Hashtable;
import java.util.Random;

import javax.annotation.Nullable;

import mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairy;
import mirrg.minecraft.mod.miragefairy.modules.main.FairyGatcha;
import mirrg.minecraft.mod.miragefairy.modules.main.FairyGatchaSettings;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import mirrg.minecraft.mod.miragefairy.util.Color;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFairyHousePlant extends BlockFairyHouseBase
{

	public BlockFairyHousePlant()
	{
		super(Material.PLANTS);
		setHardness(0.25F);
		setLightLevel(0.2f);
		setSoundType(SoundType.PLANT);
	}

	//

	protected static final AxisAlignedBB AABB = new AxisAlignedBB(1 / 16.0, 0, 1 / 16.0, 15 / 16.0, 11 / 16.0, 15 / 16.0);

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public Block.EnumOffsetType getOffsetType()
	{
		return Block.EnumOffsetType.XZ;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = pos.getX() + 8 / 16.0;
		double d1 = pos.getY() + 11 / 16.0;
		double d2 = pos.getZ() + 8 / 16.0;
		worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);

		if (Math.random() < 0.2) {
			ModuleMain.spawnParticleFairy(worldIn, new Vec3d(pos).addVector(0.5, 0.5, 0.5),
				Color.random().brighter(0.75).toInt());
		}
	}

	//

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return super.canPlaceBlockAt(worldIn, pos) ? canBlockStay(worldIn, pos) : false;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if (!canBlockStay(worldIn, pos)) worldIn.destroyBlock(pos, false);
	}

	public boolean canBlockStay(World worldIn, BlockPos pos)
	{
		IBlockState state = worldIn.getBlockState(pos.down());
		return state.getBlock().isSideSolid(state, worldIn, pos.down(), EnumFacing.UP);
	}

	//

	@Override
	public Hashtable<EnumFairy, Double> getGatchaWeight()
	{
		return FairyGatcha.getGatchaWeight(new FairyGatchaSettings(1, 3, 0.1));
	}

}
