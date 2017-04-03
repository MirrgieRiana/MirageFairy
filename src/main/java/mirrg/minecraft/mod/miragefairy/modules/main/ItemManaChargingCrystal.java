package mirrg.minecraft.mod.miragefairy.modules.main;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import mirrg.minecraft.mod.miragefairy.api.IItemManaProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

public class ItemManaChargingCrystal extends Item implements IItemManaProvider
{

	public ItemManaChargingCrystal()
	{
		super();
		setMaxStackSize(1);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		subItems.add(createItemStack(0));
		subItems.add(createItemStack(8000));
		subItems.add(createItemStack(100000000));
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		return MathHelper.hsvToRGB(Math.max(0.0F, (float) getHealth(stack)) / 3.0F, 1.0F, 1.0F);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1 - getHealth(stack);
	}

	private double getHealth(ItemStack stack)
	{
		return Math.max(Math.min(getMana(stack) / 8000.0, 1), 0);
	}

	public ItemStack createItemStack(long mana)
	{
		ItemStack itemStack = new ItemStack(this);
		setMana(itemStack, mana);
		return itemStack;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add("Mana: " + ChatFormatting.YELLOW + getMana(stack));
	}

	@Override
	public long getMana(ItemStack itemStack)
	{
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound == null) {
			return 0;
		}

		return tagCompound.getLong("mana");
	}

	@Override
	public void setMana(ItemStack itemStack, long mana)
	{
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			itemStack.setTagCompound(tagCompound);
		}

		tagCompound.setLong("mana", mana);
	}

}
