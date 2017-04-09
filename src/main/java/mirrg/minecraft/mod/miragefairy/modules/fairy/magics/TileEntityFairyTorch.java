package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityFairyTorch extends TileEntity
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
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	public void sendPacket()
	{
		((WorldServer) world).getPlayerChunkMap().getEntry(pos.getX() >> 4, pos.getZ() >> 4).sendPacket(getUpdatePacket());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
		world.checkLight(pos);
	}

	//

	public static class Data
	{

		public int lightValue = 15;
		public int colorBody = 0xffffff;
		public int colorHead = 0xffffff;

		public void readFromNBT(TileEntityFairyTorch tileEntity, NBTTagCompound compound)
		{
			lightValue = compound.hasKey("LightValue", NBT.TAG_INT) ? compound.getInteger("LightValue") : 15;
			colorBody = compound.hasKey("ColorBody", NBT.TAG_INT) ? compound.getInteger("ColorBody") : 0xffffff;
			colorHead = compound.hasKey("ColorHead", NBT.TAG_INT) ? compound.getInteger("ColorHead") : 0xffffff;
		}

		public void writeToNBT(TileEntityFairyTorch tileEntity, NBTTagCompound compound)
		{
			compound.setInteger("LightValue", lightValue);
			compound.setInteger("ColorBody", colorBody);
			compound.setInteger("ColorHead", colorHead);
		}

	}

}
