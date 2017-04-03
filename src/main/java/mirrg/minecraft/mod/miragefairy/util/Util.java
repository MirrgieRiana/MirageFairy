package mirrg.minecraft.mod.miragefairy.util;

import java.util.Optional;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.Constants.NBT;

public class Util
{

	public static int trim(int value, int min, int max)
	{
		if (value > max) value = max;
		if (value < min) value = min;
		return value;
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

}
