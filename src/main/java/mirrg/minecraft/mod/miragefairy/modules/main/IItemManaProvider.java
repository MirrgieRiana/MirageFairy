package mirrg.minecraft.mod.miragefairy.modules.main;

import net.minecraft.item.ItemStack;

public interface IItemManaProvider
{

	public long getMana(ItemStack itemStack);

	public void setMana(ItemStack itemStack, long mana);

}
