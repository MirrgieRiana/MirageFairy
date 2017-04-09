package mirrg.minecraft.mod.miragefairy.modules.fairy;

import java.text.MessageFormat;

import mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairy.ItemTypeProvider;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;

public class ItemMagicSphere extends ItemFairyBase
{

	public ItemMagicSphere(ItemTypeProvider[] types)
	{
		super(types);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		FairyMagic fairyMagic = getFairy(stack).fairyMagic;
		String fairyMagic2 = fairyMagic == null ? "Invalid" : fairyMagic.getLocalizedName();
		String infix = fairyMagic == null ? ".invalid" : "";
		String unlocalizedName = I18n.translateToLocal(getUnlocalizedName() + infix + ".name").trim();
		return MessageFormat.format(unlocalizedName, fairyMagic2);
	}

	@Override
	public boolean showGeneralInformation()
	{
		return false;
	}

	@Override
	public EnumFairy findMagicOperator(ItemStack itemStack, EntityPlayer playerIn, EnumHand handIn)
	{
		return Util.findItemNullable(playerIn, handIn, this::getFairy2);
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
