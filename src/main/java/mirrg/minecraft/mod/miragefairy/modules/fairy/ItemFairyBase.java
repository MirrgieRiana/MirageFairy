package mirrg.minecraft.mod.miragefairy.modules.fairy;

import java.util.List;
import java.util.Optional;

import com.mojang.realmsclient.gui.ChatFormatting;

import mirrg.minecraft.mod.miragefairy.core.ItemMetadata;
import mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairy.ItemTypeProvider;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.main.IItemManaProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public abstract class ItemFairyBase extends ItemMetadata<EnumFairy.ItemTypeProvider>
{

	public ItemFairyBase(ItemTypeProvider[] types)
	{
		super(types);
	}

	public ItemStack getItemStack(EnumFairy fairy)
	{
		return getItemStack(fairy, 1);
	}

	public ItemStack getItemStack(EnumFairy fairy, int count)
	{
		return new ItemStack(this, count, fairy.meta);
	}

	public EnumFairy getFairy(ItemStack stack)
	{
		return getType(stack).getFairy();
	}

	//

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		String fairy = getFairy(stack).getLocalizedName();
		String item = I18n.translateToLocal(getUnlocalizedName() + ".name").trim();
		return String.format(item, fairy);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		EnumFairy fairy = getFairy(stack);

		if (showTypeInformation()) {
			tooltip.add(String.format("Type: %s%s(%s)",
				ChatFormatting.YELLOW,
				fairy.unlocalizedName,
				fairy.meta));
		}

		if (showGeneralInformation()) {
			tooltip.add("Rarity: " + ChatFormatting.GOLD + repeat("★", fairy.rarity));
			tooltip.add("Cost: " + ChatFormatting.GREEN + ((int) fairy.getCost()));
			tooltip.add(String.format("    %s%s%3d",
				advanced ? ChatFormatting.GRAY + "Et" : "", EnumFairyPotentialType.Et.color, (int) fairy.getEt()));
			tooltip.add(String.format("%s%s%3d    %s%s%3d",
				advanced ? ChatFormatting.GRAY + "Lo" : "", EnumFairyPotentialType.Lo.color, (int) fairy.getLo(),
				advanced ? ChatFormatting.GRAY + "Ma" : "", EnumFairyPotentialType.Ma.color, (int) fairy.getMa()));
			tooltip.add(String.format("%s%s%3d    %s%s%3d",
				advanced ? ChatFormatting.GRAY + "In" : "", EnumFairyPotentialType.In.color, (int) fairy.getIn(),
				advanced ? ChatFormatting.GRAY + "Vi" : "", EnumFairyPotentialType.Vi.color, (int) fairy.getVi()));
			tooltip.add(String.format("    %s%s%3d",
				advanced ? ChatFormatting.GRAY + "Co" : "", EnumFairyPotentialType.Co.color, (int) fairy.getCo()));
		}

		if (showMagicInformation()) {
			FairyMagic fairyMagic = fairy.fairyMagic;
			if (fairyMagic != null) {
				EnumFairy fairyOperator = findMagicOperator(stack, playerIn, stack == playerIn.getHeldItemOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
				if (fairyOperator != null) {
					tooltip.add(String.format("Magic: %s%s%s(%s%s%s)",
						ChatFormatting.YELLOW,
						fairyMagic.getLocalizedName(),
						ChatFormatting.RESET,
						ChatFormatting.GREEN,
						fairyMagic.getInformation(fairyOperator),
						ChatFormatting.RESET));
					tooltip.add("  Operator: " + fairyOperator.getLocalizedName());
					fairyMagic.addInformation(Optional.of(fairyOperator), tooltip, advanced);
				} else {
					tooltip.add(String.format("Magic: %s%s",
						ChatFormatting.YELLOW,
						fairyMagic.getLocalizedName()));
					fairyMagic.addInformation(Optional.empty(), tooltip, advanced);
				}
			}
		}

	}

	public boolean showTypeInformation()
	{
		return true;
	}

	public boolean showGeneralInformation()
	{
		return true;
	}

	public boolean showMagicInformation()
	{
		return true;
	}

	//

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!canUseMagic()) return EnumActionResult.PASS;
		ItemStack itemStack = player.getHeldItem(hand);
		FairyMagic fairyMagic = getFairy(itemStack).fairyMagic;
		if (fairyMagic == null) return EnumActionResult.PASS;
		EnumFairy fairyOperator = findMagicOperator(itemStack, player, hand);
		if (fairyOperator == null) return EnumActionResult.PASS;

		return fairyMagic.onItemUse(fairyOperator, itemStack, this, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if (!(entityLiving instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) entityLiving;
		if (!canUseMagic()) return;
		FairyMagic fairyMagic = getFairy(stack).fairyMagic;
		if (fairyMagic == null) return;
		EnumHand hand = stack == player.getHeldItemOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		EnumFairy fairyOperator = findMagicOperator(stack, player, hand);
		if (fairyOperator == null) return;

		fairyMagic.onPlayerStoppedUsing(fairyOperator, this, player, hand, stack, worldIn, entityLiving, timeLeft);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemStack = playerIn.getHeldItem(handIn);
		if (!canUseMagic()) return new ActionResult<>(EnumActionResult.PASS, itemStack);
		FairyMagic fairyMagic = getFairy(itemStack).fairyMagic;
		if (fairyMagic == null) return new ActionResult<>(EnumActionResult.PASS, itemStack);
		EnumFairy fairyOperator = findMagicOperator(itemStack, playerIn, handIn);
		if (fairyOperator == null) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

		return fairyMagic.onItemRightClick(fairyOperator, itemStack, this, worldIn, playerIn, handIn);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		if (!canUseMagic()) return;
		FairyMagic fairyMagic = getFairy(stack).fairyMagic;
		if (fairyMagic == null) return;
		if (!(player instanceof EntityPlayer)) return;
		EnumFairy fairyOperator = findMagicOperator(stack, (EntityPlayer) player, stack == player.getHeldItemOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
		if (fairyOperator == null) return;

		fairyMagic.onUsingTick(fairyOperator, this, (EntityPlayer) player, stack, player, count);
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		if (!canUseMagic()) return 0;
		FairyMagic fairyMagic = getFairy(stack).fairyMagic;
		if (fairyMagic == null) return 0;
		return fairyMagic.getMaxItemUseDuration(stack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		if (!canUseMagic()) return EnumAction.NONE;
		FairyMagic fairyMagic = getFairy(stack).fairyMagic;
		if (fairyMagic == null) return EnumAction.NONE;
		return fairyMagic.getItemUseAction(stack);
	}

	public boolean canBeMagicOperator()
	{
		return false;
	}

	public EnumFairy findMagicOperator(ItemStack itemStack, EntityPlayer playerIn, EnumHand handIn)
	{
		return getFairy(itemStack);
	}

	public boolean canUseMagic()
	{
		return false;
	}

	//

	public static boolean tryUseMana(EntityPlayer player, long cost)
	{
		if (tryUseMana(player.getHeldItem(EnumHand.OFF_HAND), cost)) return true;
		if (tryUseMana(player.getHeldItem(EnumHand.MAIN_HAND), cost)) return true;
		for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
			if (tryUseMana(player.inventory.getStackInSlot(i), cost)) return true;
		}
		return false;
	}

	public static boolean tryUseMana(ItemStack itemStack, long cost)
	{
		if (itemStack.getItem() instanceof IItemManaProvider) {
			IItemManaProvider itemManaProvider = (IItemManaProvider) itemStack.getItem();
			long mana = itemManaProvider.getMana(itemStack);
			if (mana >= cost) {
				itemManaProvider.setMana(itemStack, mana - cost);
				return true;
			}
		}
		return false;
	}

	private static String repeat(String string, int times)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < times; i++) {
			sb.append(string);
		}
		return sb.toString();
	}

}
