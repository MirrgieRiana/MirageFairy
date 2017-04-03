package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityNeedleFloor extends TileEntity implements ITickable
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

	@Override
	public void update()
	{
		if (world.isRemote) return;

		data.duration--;
		if (data.duration < 0) {
			getWorld().destroyBlock(getPos(), false);
		}
	}

	public static class Data
	{

		public double damage = 0;
		public int duration = 20;
		public boolean poison = false;
		public boolean enemyOnly = false;

		public void readFromNBT(TileEntityNeedleFloor tileEntity, NBTTagCompound compound)
		{
			damage = (compound.hasKey("Damage", NBT.TAG_DOUBLE) ? compound.getDouble("Damage") : 0);
			duration = compound.hasKey("Duration", NBT.TAG_INT) ? compound.getInteger("Duration") : 0;
			poison = compound.hasKey("Poison", NBT.TAG_BYTE) ? compound.getBoolean("Poison") : false;
			enemyOnly = compound.hasKey("EnemyOnly", NBT.TAG_BYTE) ? compound.getBoolean("EnemyOnly") : false;
		}

		public void writeToNBT(TileEntityNeedleFloor tileEntity, NBTTagCompound compound)
		{
			compound.setDouble("Damage", damage);
			compound.setInteger("Duration", duration);
			compound.setBoolean("Poison", poison);
			compound.setBoolean("EnemyOnly", enemyOnly);
		}

	}

}
