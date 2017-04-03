package mirrg.minecraft.mod.miragefairy.modules.main;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityFairyHouseCube extends TileEntity
{

	public Data data = new Data();

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		data.readFromNBT(this, compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		data.writeToNBT(this, compound);
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	//

	public static class Data
	{

		public EnumFacing facing = EnumFacing.NORTH;

		public void readFromNBT(TileEntityFairyHouseCube tileEntity, NBTTagCompound compound)
		{
			facing = null;
			if (compound.hasKey("Facing", NBT.TAG_STRING)) facing = EnumFacing.byName(compound.getString("Facing"));
			if (facing == null) facing = EnumFacing.NORTH;
		}

		public void writeToNBT(TileEntityFairyHouseCube tileEntity, NBTTagCompound compound)
		{
			compound.setString("Facing", facing.getName());
		}

	}

}
