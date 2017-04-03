package mirrg.minecraft.mod.miragefairy.core;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMetadata<T extends Enum<T> & IBlockTypeProvider & IStringSerializable> extends Block
{

	public final PropertyEnum<T> propertyType;
	private ArrayList<T> types = new ArrayList<>();

	private static PropertyEnum<?> _PROPERTY_TYPE;

	public static <T extends BlockMetadata<E>, E extends Enum<E> & IBlockTypeProvider & IStringSerializable> T createInstance(
		Class<E> clazz, Function<PropertyEnum<E>, T> constuctor)
	{
		PropertyEnum<E> property = PropertyEnum.create("variant", clazz);
		_PROPERTY_TYPE = property;
		return constuctor.apply(property);
	}

	public BlockMetadata(Material material, PropertyEnum<T> propertyType, T[] types2, T defaultType)
	{
		super(material);
		this.propertyType = propertyType;
		setDefaultState(blockState.getBaseState().withProperty(propertyType, defaultType));

		// TODO pickaxe以外のharvestLevelを設定できない
		for (T type : types2) {
			setHarvestLevel("pickaxe", type.getBlockType().harvestLevel, getState(type));
		}

		types = new ArrayList<>();
		for (T type : types2) {
			types.add(null);
		}
		for (T type : types2) {
			types.set(type.getBlockType().meta, type);
		}

	}

	public T getType(int index)
	{
		if (index >= types.size()) index = 0;
		return types.get(index);
	}

	public Iterable<T> getTypes()
	{
		return types;
	}

	public ItemStack getItemStack(T type)
	{
		return getItemStack(type, 1);
	}

	public ItemStack getItemStack(T type, int count)
	{
		return new ItemStack(this, count, type.getBlockType().meta);
	}

	public T getType(ItemStack itemStack)
	{
		return getType(itemStack.getMetadata());
	}

	public IBlockState getState(T type)
	{
		return getDefaultState().withProperty(propertyType, type);
	}

	//

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return blockState.getValue(propertyType).getBlockType().hardness;
	}

	//

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		Supplier<ItemStack> sDropItem = state.getValue(propertyType).getBlockType().sDropItem;
		return sDropItem != null ? sDropItem.get().getItem() : super.getItemDropped(state, rand, fortune);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		Supplier<ItemStack> sDropItem = state.getValue(propertyType).getBlockType().sDropItem;
		return sDropItem != null ? sDropItem.get().getMetadata() : state.getValue(propertyType).getBlockType().meta;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		Supplier<ItemStack> sDropItem = state.getValue(propertyType).getBlockType().sDropItem;
		if (sDropItem != null) {
			return sDropItem.get().getCount() + random.nextInt(fortune + 1);
		} else {
			return super.quantityDroppedWithBonus(fortune, random);
		}
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(this, 1, state.getValue(propertyType).getBlockType().meta);
	}

	//

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (T type : getTypes()) {
			list.add(new ItemStack(itemIn, 1, type.getBlockType().meta));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(propertyType, getType(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(propertyType).getBlockType().meta;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {
			propertyType == null ? _PROPERTY_TYPE : propertyType
		});
	}

}
