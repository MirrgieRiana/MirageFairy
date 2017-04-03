package mirrg.minecraft.mod.miragefairy.modules.fairy;

import java.text.MessageFormat;
import java.util.Optional;

import mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairy.ItemTypeProvider;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.util.Constants.NBT;

public class ItemMagicSphereWithFairy extends ItemFairyBase
{

	public ItemMagicSphereWithFairy(ItemTypeProvider[] types)
	{
		super(types);
		setMaxStackSize(1);
	}

	//

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{

	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		FairyMagic fairyMagic = getFairy(stack).fairyMagic;
		String fairyMagic2 = fairyMagic == null ? "Invalid" : fairyMagic.getLocalizedName();
		String fairyOperator = getFairyStored(stack).map(f -> f.getLocalizedName()).orElse("Invalid");
		String infix = fairyMagic == null ? ".invalid" : "";
		String unlocalizedName = I18n.translateToLocal(getUnlocalizedName() + infix + ".name").trim();
		return MessageFormat.format(unlocalizedName, fairyMagic2, fairyOperator);
	}

	@Override
	public boolean showGeneralInformation()
	{
		return false;
	}

	private EnumFairy getFairy2(ItemStack itemStack)
	{
		if (itemStack.getItem() instanceof ItemFairy) {
			ItemFairy itemFairy = (ItemFairy) itemStack.getItem();
			if (itemFairy.canBeMagicOperator()) {
				return itemFairy.getFairy(itemStack);
			}
		}
		return null;
	}

	@Override
	public boolean canUseMagic()
	{
		return true;
	}

	@Override
	public EnumFairy findMagicOperator(ItemStack itemStack, EntityPlayer playerIn, EnumHand handIn)
	{
		return getFairyStored(itemStack).orElse(EnumFairy.air);
	}

	public void setItemStackStored(ItemStack itemStack, ItemStack itemStackFairy)
	{
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			itemStack.setTagCompound(tagCompound);
		}

		NBTTagCompound tagCompoundFairy = new NBTTagCompound();
		NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
		list.set(0, itemStackFairy);
		ItemStackHelper.saveAllItems(tagCompoundFairy, list);
		tagCompound.setTag("fairy", tagCompoundFairy);
	}

	public ItemStack getItemStackStored(ItemStack itemStack)
	{
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound == null) return ItemStack.EMPTY;

		if (!tagCompound.hasKey("fairy", NBT.TAG_COMPOUND)) return ItemStack.EMPTY;
		NBTTagCompound tagCompoundFairy = tagCompound.getCompoundTag("fairy");

		NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(tagCompoundFairy, list);
		ItemStack itemStackFairy = list.get(0);

		return itemStackFairy;
	}

	public Optional<EnumFairy> getFairyStored(ItemStack itemStack)
	{
		ItemStack itemStackStored = getItemStackStored(itemStack);
		if (itemStackStored.isEmpty()) return Optional.empty();
		if (!(itemStackStored.getItem() instanceof ItemFairyBase)) return Optional.empty();
		ItemFairyBase itemFairy = (ItemFairyBase) itemStackStored.getItem();
		return Optional.of(itemFairy.getFairy(itemStackStored));
	}

	//

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		return getItemStackStored(itemStack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return getFairy(stack).fairyMagic == null;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		return 0xff0000;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 0;
	}

	/*
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return getFairy(stack).fairyMagic != null;
	}
	*/

}
