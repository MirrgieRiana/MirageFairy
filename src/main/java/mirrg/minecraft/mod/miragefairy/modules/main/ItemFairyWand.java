package mirrg.minecraft.mod.miragefairy.modules.main;

import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemFairyWand extends ItemTool
{

	public ItemFairyWand(Item.ToolMaterial material)
	{
		super(1.5F, -3.0F, material, new HashSet<>());
		setMaxDamage(16 - 1);
		setNoRepair();
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote) {
			player.sendStatusMessage(new TextComponentString(FairyGatcha.getString(FairyGatcha.getGatchaWeight(player, worldIn, pos, new FairyGatchaSettings(1, 5, 0.1)))), false);
		}
		if (worldIn.isRemote) {
			worldIn.playSound(player, player.posX, player.posY, player.posZ, ModuleMain.soundFairyWandUse, SoundCategory.PLAYERS, 0.5F, 1.0F);
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		itemStack = itemStack.copy();

		// damage
		itemStack.setItemDamage(itemStack.getItemDamage() + 1);
		if (itemStack.getItemDamage() > itemStack.getMaxDamage()) {
			itemStack.shrink(1);
			itemStack.setItemDamage(0);
		}

		return itemStack;
	}

}
