package mirrg.minecraft.mod.miragefairy.modules.main.fairychest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFairyChest extends Container
{

	private final IInventory inventoryPlayer;
	private final IInventory inventoryTile;

	public ContainerFairyChest(IInventory inventoryPlayer, IInventory inventoryTile)
	{
		this.inventoryPlayer = inventoryPlayer;
		this.inventoryTile = inventoryTile;

		// Tile
		for (int y = 0; y < 6; ++y) {
			for (int x = 0; x < 9; ++x) {
				addSlotToContainer(new SlotFairyChest(inventoryTile, x + y * 9, 8 + x * 18, 18 + y * 18));
			}
		}

		// プレイヤー
		{
			for (int y = 0; y < 3; ++y) {
				for (int x = 0; x < 9; ++x) {
					addSlotToContainer(new Slot(inventoryPlayer, x + y * 9 + 9, 8 + x * 18, 104 + y * 18 + 2 * 18));
				}
			}

			for (int x = 0; x < 9; ++x) {
				addSlotToContainer(new Slot(inventoryPlayer, x, 8 + x * 18, 162 + 2 * 18));
			}
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return inventoryTile.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < 6 * 9) {
				if (!this.mergeItemStack(itemstack1, 6 * 9, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, 6 * 9, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

}
