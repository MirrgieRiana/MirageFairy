package mirrg.minecraft.mod.miragefairy.modules.main;

import java.util.function.Predicate;

import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ModuleFairy;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class RecipeMagicSphereWithFairy implements IRecipe
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

		ItemStack itemStackFairy = pick(list, s -> !s.isEmpty() && s.getItem() == ModuleFairy.itemFairy);
		if (itemStackFairy.isEmpty()) return false;
		if (!(itemStackFairy.getItem() instanceof ItemFairyBase)) return false;
		ItemFairyBase itemFairyFairy = (ItemFairyBase) itemStackFairy.getItem();

		ItemStack itemStackMagicSphere = pick(list, s -> !s.isEmpty() && s.getItem() == ModuleFairy.itemMagicSphere);
		if (itemStackMagicSphere.isEmpty()) return false;
		if (!(itemStackMagicSphere.getItem() instanceof ItemFairyBase)) return false;
		ItemFairyBase itemFairyMagicSphere = (ItemFairyBase) itemStackMagicSphere.getItem();

		// 残っているならレシピ破綻
		for (int i = 0; i < list.size(); ++i) {
			if (!list.get(i).isEmpty()) return false;
		}

		// 結果アイテム生成
		resultItem = ModuleFairy.itemMagicSphereWithFairy.getItemStack(itemFairyMagicSphere.getFairy(itemStackMagicSphere));
		ModuleFairy.itemMagicSphereWithFairy.setItemStackStored(resultItem, itemStackFairy);

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
