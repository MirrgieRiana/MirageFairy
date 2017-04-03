package mirrg.minecraft.mod.miragefairy.modules.main;

import java.util.ArrayList;
import java.util.Optional;

import mirrg.minecraft.mod.miragefairy.core.RecipeMirageFairyShapeless;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeRepairManaChargingCrystal extends RecipeMirageFairyShapeless
{

	@Override
	protected Optional<ItemStack> matches(NonNullList<ItemStack> list, InventoryCrafting inv, World worldIn)
	{
		ItemManaChargingCrystal item = ModuleMain.itemManaChargingCrystal;

		ArrayList<ItemStack> itemStacks = new ArrayList<>();
		while (true) {
			ItemStack itemStack = pick(list, s -> !s.isEmpty() && s.getItem() == item);
			if (itemStack.isEmpty()) break;
			if (!(itemStack.getItem() instanceof ItemManaChargingCrystal)) break;
			itemStacks.add(itemStack);
		}
		if (itemStacks.size() == 0) return Optional.empty();

		return Optional.of(item.createItemStack(itemStacks.stream()
			.mapToLong(s -> item.getMana(s))
			.sum()));
	}

}
