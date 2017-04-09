package mirrg.minecraft.mod.miragefairy.modules.main.fairychest;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import mirrg.minecraft.mod.miragefairy.MirageFairyMod;
import mirrg.minecraft.mod.miragefairy.modules.main.EnumGUI;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import mirrg.minecraft.mod.miragefairy.util.Color;
import mirrg.minecraft.mod.miragefairy.util.Util;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFairyChest extends BlockContainer
{

	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);
	public static final PropertyBool IS_ROOT = PropertyBool.create("is_root");

	public BlockFairyChest()
	{
		super(Material.WOOD);
		setDefaultState(blockState.getBaseState()
			.withProperty(FACING, EnumFacing.NORTH)
			.withProperty(IS_ROOT, true));
		setHardness(0.5f);
		setSoundType(SoundType.WOOD);
	}

	//

	protected static final AxisAlignedBB AABB = new AxisAlignedBB(2 / 16.0, 0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0);

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return AABB;
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

	//

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote) return true;

		Util.getTileEntity(TileEntityFairyChest.class, worldIn, pos)
			.ifPresent(t -> FMLNetworkHandler.openGui(playerIn, MirageFairyMod.MODID, EnumGUI.FAIRY_CHEST.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ()));
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		TileEntityFairyChest tileEntity = new TileEntityFairyChest();
		tileEntity.data.facing = state.getValue(FACING);
		return tileEntity;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
			.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (stack.hasDisplayName()) {
			Util.getTileEntity(TileEntityFairyChest.class, worldIn, pos)
				.ifPresent(t -> t.setCustomName(stack.getDisplayName()));
		}
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
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

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {
			FACING,
			IS_ROOT,
		});
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		Optional<TileEntityFairyChest> oTileEntity = Util.getTileEntity(TileEntityFairyChest.class, worldIn, pos);
		if (oTileEntity.isPresent()) {
			state = state.withProperty(FACING, oTileEntity.get().data.facing);
		}
		if (worldIn.getBlockState(pos.down()).getBlock() == this) state = state.withProperty(IS_ROOT, false);
		return state;
	}

	public TileEntityFairyChest.Data getData(IBlockAccess world, BlockPos pos)
	{
		return Util.getTileEntity(TileEntityFairyChest.class, world, pos)
			.map(t -> t.data)
			.orElseGet(() -> new TileEntityFairyChest.Data());
	}

	//

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, player, tooltip, advanced);
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound != null && nbttagcompound.hasKey("BlockEntityTag", 10)) {
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag");

			if (nbttagcompound1.hasKey("LootTable", 8)) {
				tooltip.add("???????");
			}

			if (nbttagcompound1.hasKey("Items", 9)) {
				NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack> withSize(54, ItemStack.EMPTY);
				ItemStackHelper.loadAllItems(nbttagcompound1, nonnulllist);
				int i = 0;
				int j = 0;

				for (ItemStack itemstack : nonnulllist) {
					if (!itemstack.isEmpty()) {
						++j;

						if (i <= 4) {
							++i;
							tooltip.add(String.format("%s x%d", new Object[] {
								itemstack.getDisplayName(), Integer.valueOf(itemstack.getCount())
							}));
						}
					}
				}

				if (j - i > 0) {
					tooltip.add(String.format(TextFormatting.ITALIC + I18n.translateToLocal("container.shulkerBox.more"), new Object[] {
						Integer.valueOf(j - i)
					}));
				}
			}
		}
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return EnumPushReaction.DESTROY;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return Util.getTileEntity(TileEntityFairyChest.class, world, pos)
			.map(t -> t.getLightValue())
			.orElse(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		Util.getTileEntity(TileEntityFairyChest.class, worldIn, pos)
			.ifPresent(t -> {
				if (Math.random() < 0.5 * t.getLightValue() / 15) {
					ModuleMain.spawnParticleFairy(worldIn, new Vec3d(pos).addVector(0.5, 0.5, 0.5),
						new Color(t.getColor()).brighter(0.75).toInt());
				}
			});
	}

	//

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		Util.getTileEntity(TileEntityFairyChest.class, worldIn, pos)
			.ifPresent(t -> {
				spawnAsEntity(worldIn, pos, getItem(t));
				worldIn.updateComparatorOutputLevel(pos, this);
			});
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{

	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return getItem((TileEntityFairyChest) worldIn.getTileEntity(pos));
	}

	public ItemStack getItem(TileEntityFairyChest tileEntity)
	{
		ItemStack itemStack = new ItemStack(this);
		itemStack.setTagInfo("BlockEntityTag", tileEntity.writeToNBTToDrop(new NBTTagCompound()));
		if (tileEntity.hasCustomName()) itemStack.setStackDisplayName(tileEntity.getName());
		return itemStack;
	}

}
