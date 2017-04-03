package mirrg.minecraft.mod.miragefairy.modules.main.fairychest;

import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFairyChest extends Slot
{

	public SlotFairyChest(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stack.getItem() instanceof ItemFairyBase;
	}

}
