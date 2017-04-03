package mirrg.minecraft.mod.miragefairy.modules.main;

import mirrg.minecraft.mod.miragefairy.core.ItemType;
import mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ModuleFairy;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTypeDustMirage extends ItemType
{

	public ItemTypeDustMirage(int meta, String unlocalizedName, String modelName)
	{
		super(meta, unlocalizedName, modelName);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemStack = player.getHeldItem(hand);

		// effect
		for (int i = 0; i < 5; i++) {
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
				pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ,
				(Math.random() - 0.5) * 0.1, Math.random() * 0.1, (Math.random() - 0.5) * 0.1,
				new int[0]);
		}

		// 減らす
		if (!worldIn.isRemote) itemStack.shrink(1);

		// 増やす
		EnumFairy fairy = FairyGatcha.getGatchaResult(FairyGatcha.getGatchaWeight(player, worldIn, pos, new FairyGatchaSettings(1, 5, 0.1)), worldIn.rand);
		if (fairy != null) {

			if (!worldIn.isRemote) {
				ItemStack newItemStack = ModuleFairy.itemFairySpirit.getItemStack(fairy);
				if (newItemStack != null) {
					if (itemStack.isEmpty()) {
						player.setHeldItem(hand, newItemStack);
					} else {

						EntityItem entityitem = new EntityItem(worldIn,
							pos.getX() + hitX + facing.getFrontOffsetX() * 0.1,
							pos.getY() + hitY + facing.getFrontOffsetY() * 0.1,
							pos.getZ() + hitZ + facing.getFrontOffsetZ() * 0.1, newItemStack);
						entityitem.setDefaultPickupDelay();
						worldIn.spawnEntity(entityitem);

						/*
						if (!player.inventory.addItemStackToInventory(newItemStack)) {
							player.dropItem(newItemStack, false);
						}
						*/

					}
				}
			}

			// effect
			worldIn.playSound(player,
				pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ,
				SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1.0F, 1.0F);

		}

		return EnumActionResult.SUCCESS;
	}

}
