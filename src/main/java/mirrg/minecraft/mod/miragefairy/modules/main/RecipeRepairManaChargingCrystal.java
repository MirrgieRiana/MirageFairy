package mirrg.minecraft.mod.miragefairy.modules.main;

import java.util.function.Predicate;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class RecipeRepairManaChargingCrystal implements IRecipe
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

		ItemManaChargingCrystal item = ModuleMain.itemManaChargingCrystal;

		ItemStack itemStack1 = pick(list, s -> !s.isEmpty() && s.getItem() == item);
		if (itemStack1.isEmpty()) return false;
		if (!(itemStack1.getItem() instanceof ItemManaChargingCrystal)) return false;

		ItemStack itemStack2 = pick(list, s -> !s.isEmpty() && s.getItem() == item);
		if (itemStack2.isEmpty()) return false;
		if (!(itemStack2.getItem() instanceof ItemManaChargingCrystal)) return false;

		// 残っているならレシピ破綻
		for (int i = 0; i < list.size(); ++i) {
			if (!list.get(i).isEmpty()) return false;
		}

		// 結果アイテム生成
		resultItem = item.createItemStack(item.getMana(itemStack1) + item.getMana(itemStack2));

		return true;
	}

	private ItemStack pick(NonNullList<ItemStack> list, Predicate<ItemStack> predicate)
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
