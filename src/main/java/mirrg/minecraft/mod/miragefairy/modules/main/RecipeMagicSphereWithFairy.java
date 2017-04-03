package mirrg.minecraft.mod.miragefairy.modules.main;

import java.util.Optional;

import mirrg.minecraft.mod.miragefairy.core.RecipeMirageFairyShapeless;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ModuleFairy;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeMagicSphereWithFairy extends RecipeMirageFairyShapeless
{

	@Override
	protected Optional<ItemStack> matches(NonNullList<ItemStack> list, InventoryCrafting inv, World worldIn)
	{
		ItemStack itemStackFairy = pick(list, s -> !s.isEmpty() && s.getItem() == ModuleFairy.itemFairy);
		if (itemStackFairy.isEmpty()) return Optional.empty();
		if (!(itemStackFairy.getItem() instanceof ItemFairyBase)) return Optional.empty();
		ItemFairyBase itemFairyFairy = (ItemFairyBase) itemStackFairy.getItem();

		ItemStack itemStackMagicSphere = pick(list, s -> !s.isEmpty() && s.getItem() == ModuleFairy.itemMagicSphere);
		if (itemStackMagicSphere.isEmpty()) return Optional.empty();
		if (!(itemStackMagicSphere.getItem() instanceof ItemFairyBase)) return Optional.empty();
		ItemFairyBase itemFairyMagicSphere = (ItemFairyBase) itemStackMagicSphere.getItem();

		ItemStack itemStack = ModuleFairy.itemMagicSphereWithFairy.getItemStack(itemFairyMagicSphere.getFairy(itemStackMagicSphere));
		ModuleFairy.itemMagicSphereWithFairy.setItemStackStored(itemStack, itemStackFairy);
		return Optional.of(itemStack);
	}

}
