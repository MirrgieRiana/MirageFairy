package mirrg.minecraft.mod.miragefairy.core;

import java.util.ArrayList;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemMetadata<T extends IItemTypeProvider> extends Item
{

	private ArrayList<T> types = new ArrayList<>();

	public ItemMetadata(T[] types2)
	{

		setHasSubtypes(true);
		setMaxDamage(0);

		types = new ArrayList<>();
		for (T type : types2) {
			types.add(null);
		}
		for (T type : types2) {
			types.set(type.getItemType().meta, type);
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
		return new ItemStack(this, count, type.getItemType().meta);
	}

	public T getType(ItemStack itemStack)
	{
		return getType(itemStack.getMetadata());
	}

	//

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for (T type : getTypes()) {
			subItems.add(new ItemStack(this, 1, type.getItemType().meta));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "item." + getType(stack.getMetadata()).getItemType().unlocalizedName;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemStack = playerIn.getHeldItem(handIn);
		if (itemStack != null) {
			T type = getType(itemStack.getMetadata());
			return type.getItemType().onItemRightClick(worldIn, playerIn, handIn);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemStack = player.getHeldItem(hand);
		if (itemStack != null) {
			T type = getType(itemStack.getMetadata());
			return type.getItemType().onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	//

	public void initModel()
	{
		for (T type : getTypes()) {
			ModelLoader.setCustomModelResourceLocation(this, type.getItemType().meta, new ModelResourceLocation(type.getItemType().modelName, "inventory"));
		}
	}

}
