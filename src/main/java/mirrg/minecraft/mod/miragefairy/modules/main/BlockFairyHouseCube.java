package mirrg.minecraft.mod.miragefairy.modules.main;

import java.util.Hashtable;
import java.util.Random;

import mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairy;
import mirrg.minecraft.mod.miragefairy.util.Color;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFairyHouseCube extends BlockFairyHouseBase implements ITileEntityProvider
{

	public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);

	public BlockFairyHouseCube()
	{
		super(Material.WOOD);
		setDefaultState(blockState.getBaseState()
			.withProperty(VARIANT, BlockPlanks.EnumType.OAK)
			.withProperty(FACING, EnumFacing.NORTH));
		setHardness(2.0F);
		setSoundType(SoundType.WOOD);
		setHarvestLevel("axe", 0);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {
			VARIANT,
			FACING,
		});
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(VARIANT).getMetadata();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (!(tileEntity instanceof TileEntityFairyHouseCube)) return state;
		TileEntityFairyHouseCube tileEntityFairyHouseCube = (TileEntityFairyHouseCube) tileEntity;
		return state
			.withProperty(FACING, tileEntityFairyHouseCube.data.facing);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(VARIANT).getMetadata();
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(this, 1, state.getValue(VARIANT).getMetadata());
	}

	//

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
			list.add(new ItemStack(itemIn, 1, type.getMetadata()));
		}
	}

	//

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		EnumFacing facing = getActualState(stateIn, worldIn, pos).getValue(FACING);
		Vec3d vec3d = new Vec3d(pos)
			.addVector(0.5, 0.5, 0.5)
			.addVector(
				facing.getFrontOffsetX() * 0.5,
				facing.getFrontOffsetY() * 0.5,
				facing.getFrontOffsetZ() * 0.5);
		worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 0.0D, 0.0D, 0.0D, new int[0]);

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
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		TileEntityFairyHouseCube tileEntity = new TileEntityFairyHouseCube();
		tileEntity.data.facing = state.getValue(FACING);
		return tileEntity;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityFairyHouseCube();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	//

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
			.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	//

	@Override
	public Hashtable<EnumFairy, Double> getGatchaWeight()
	{
		return FairyGatcha.getGatchaWeight(new FairyGatchaSettings(2, 3, 0.1));
	}

}
