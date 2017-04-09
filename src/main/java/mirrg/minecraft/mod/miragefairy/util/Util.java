package mirrg.minecraft.mod.miragefairy.util;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.Constants.NBT;

public class Util
{

	public static int trim(int value, int min, int max)
	{
		return value > max ? max : value < min ? min : value;
	}

	public static long trim(long value, long min, long max)
	{
		return value > max ? max : value < min ? min : value;
	}

	public static double trim(double value, double min, double max)
	{
		return value > max ? max : value < min ? min : value;
	}

	public static <T extends TileEntity> T getTileEntityFromItemStack(T tileEntity, ItemStack itemStack)
	{
		if (itemStack.hasTagCompound()) {
			NBTTagCompound nbt = itemStack.getTagCompound();
			if (nbt.hasKey("BlockEntityTag", NBT.TAG_COMPOUND)) {
				tileEntity.readFromNBT(nbt.getCompoundTag("BlockEntityTag"));
			}
		}
		return tileEntity;
	}

	public static String toUpperCaseHead(String str)
	{
		if (str.length() == 0) return str;
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static <T extends TileEntity> Optional<T> getTileEntity(Class<T> clazz, IBlockAccess blockAccess, BlockPos pos)
	{
		TileEntity tileEntity = blockAccess.getTileEntity(pos);
		if (tileEntity != null && clazz.isInstance(tileEntity)) {
			return Optional.of((T) tileEntity);
		}
		return Optional.empty();
	}

	public static <T> Optional<T> findItemOptional(EntityPlayer player, EnumHand hand, Function<ItemStack, Optional<T>> predicate)
	{
		Optional<T> res;

		if (hand == EnumHand.MAIN_HAND) {

			res = predicate.apply(player.getHeldItem(EnumHand.OFF_HAND));
			if (res.isPresent()) return res;

			res = predicate.apply(player.getHeldItem(EnumHand.MAIN_HAND));
			if (res.isPresent()) return res;

		} else {

			res = predicate.apply(player.getHeldItem(EnumHand.MAIN_HAND));
			if (res.isPresent()) return res;

			res = predicate.apply(player.getHeldItem(EnumHand.OFF_HAND));
			if (res.isPresent()) return res;

		}

		for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {

			res = predicate.apply(player.inventory.getStackInSlot(i));
			if (res.isPresent()) return res;

		}

		return Optional.empty();
	}

	public static <T> Optional<ItemStack> findItemPredicate(EntityPlayer player, EnumHand hand, Predicate<ItemStack> predicate)
	{
		return findItemOptional(player, hand, s -> Optional.ofNullable(predicate.test(s) ? s : null));
	}

	public static <T> T findItemNullable(EntityPlayer player, EnumHand hand, Function<ItemStack, T> predicate)
	{
		return findItemOptional(player, hand, s -> Optional.ofNullable(predicate.apply(s))).orElse(null);
	}

}
