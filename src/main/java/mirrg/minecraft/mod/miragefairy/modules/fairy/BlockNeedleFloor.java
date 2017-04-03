package mirrg.minecraft.mod.miragefairy.modules.fairy;

import javax.annotation.Nullable;

import mirrg.minecraft.mod.miragefairy.modules.fairy.TileEntityNeedleFloor.Data;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNeedleFloor extends Block implements ITileEntityProvider
{

	protected BlockNeedleFloor()
	{
		super(Material.CACTUS);
		setSoundType(SoundType.PLANT);
	}

	//

	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0 / 16.0, 0, 0 / 16.0, 16 / 16.0, 4 / 16.0, 16 / 16.0);

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
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (worldIn.isRemote) return;

		if (entityIn instanceof EntityLivingBase) {
			EntityLivingBase entityLiving = (EntityLivingBase) entityIn;
			if (entityLiving.getEntityBoundingBox().intersectsWith(AABB.offset(pos))) {
				Data data = getData(worldIn, pos);

				// プレイヤー保護
				if (data.enemyOnly && entityLiving instanceof EntityPlayer) return;

				// 攻撃
				entityLiving.attackEntityFrom(DamageSource.CACTUS, (float) data.damage);
				if (data.poison) entityLiving.addPotionEffect(new PotionEffect(MobEffects.POISON, 20 * 10));

				// 消滅
				worldIn.destroyBlock(pos, false);

			}
		}
	}

	public TileEntityNeedleFloor.Data getData(IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityNeedleFloor) {
			return ((TileEntityNeedleFloor) tileEntity).data;
		} else {
			return new TileEntityNeedleFloor.Data();
		}
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
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
	{
		return true;
	}

	//

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
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityNeedleFloor();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

}
