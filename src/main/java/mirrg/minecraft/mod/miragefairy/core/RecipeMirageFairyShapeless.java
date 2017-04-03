package mirrg.minecraft.mod.miragefairy.core;

import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public abstract class RecipeMirageFairyShapeless implements IRecipe
{

	private ItemStack resultItem = ItemStack.EMPTY;

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		this.resultItem = ItemStack.EMPTY;

		// アイテムを取得
		NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for (int i = 0; i < list.size(); ++i) {
			list.set(i, inv.getStackInSlot(i));
		}

		// アイテムを検証
		Optional<ItemStack> oItemStack = matches(list, inv, worldIn);
		if (!oItemStack.isPresent()) return false;

		// 残っているならレシピ破綻
		for (int i = 0; i < list.size(); ++i) {
			if (!list.get(i).isEmpty()) return false;
		}

		// 結果アイテム生成
		resultItem = oItemStack.get();

		return true;
	}

	protected abstract Optional<ItemStack> matches(NonNullList<ItemStack> list, InventoryCrafting inv, World worldIn);

	protected ItemStack pick(NonNullList<ItemStack> list, Predicate<ItemStack> predicate)
	{
		for (int i = 0; i < list.size(); ++i) {
			ItemStack itemStack = list.get(i);
			if (predicate.test(itemStack)) {
				list.set(i, ItemStack.EMPTY);
				return itemStack;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		return this.resultItem.copy();
	}

	@Override
	public int getRecipeSize()
	{
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return this.resultItem;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for (int i = 0; i < list.size(); ++i) {
			list.set(i, ForgeHooks.getContainerItem(inv.getStackInSlot(i)));
		}
		return list;
	}

}
