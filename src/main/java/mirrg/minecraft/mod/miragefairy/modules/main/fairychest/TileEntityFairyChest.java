package mirrg.minecraft.mod.miragefairy.modules.main.fairychest;

import javax.annotation.Nullable;

import mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.util.Color;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityFairyChest extends TileEntityLockableLoot implements ITickable
{

	public Data data = new Data();

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		data.readFromNBT(this, compound);
		if (compound.hasKey("CustomName", 8)) customName = compound.getString("CustomName");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		data.writeToNBT(this, compound);
		if (hasCustomName()) compound.setString("CustomName", customName);
		return compound;
	}

	public NBTTagCompound writeToNBTToDrop(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		data.writeToNBTToDrop(this, compound);
		if (hasCustomName()) compound.setString("CustomName", customName);
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

	//

	public static class Data
	{

		public EnumFacing facing = EnumFacing.NORTH;
		public NonNullList<ItemStack> stacks = NonNullList.withSize(54, ItemStack.EMPTY);

		public void readFromNBT(TileEntityFairyChest tileEntity, NBTTagCompound compound)
		{
			facing = null;
			if (compound.hasKey("Facing", NBT.TAG_STRING)) facing = EnumFacing.byName(compound.getString("Facing"));
			if (facing == null) facing = EnumFacing.NORTH;

			stacks = NonNullList.withSize(stacks.size(), ItemStack.EMPTY);
			if (!tileEntity.checkLootAndRead(compound)) ItemStackHelper.loadAllItems(compound, stacks);
		}

		public void writeToNBT(TileEntityFairyChest tileEntity, NBTTagCompound compound)
		{
			compound.setString("Facing", facing.getName());

			if (!tileEntity.checkLootAndWrite(compound)) ItemStackHelper.saveAllItems(compound, stacks);
		}

		public void writeToNBTToDrop(TileEntityFairyChest tileEntity, NBTTagCompound compound)
		{
			if (!tileEntity.checkLootAndWrite(compound)) ItemStackHelper.saveAllItems(compound, stacks);
		}

	}

	//

	@Override
	public int getSizeInventory()
	{
		return data.stacks.size();
	}

	@Override
	public boolean isEmpty()
	{
		for (ItemStack itemStack : data.stacks) {
			if (!itemStack.isEmpty()) return false;
		}
		return true;
	}

	public int addItemStack(ItemStack stack)
	{
		for (int i = 0; i < data.stacks.size(); ++i) {
			if (data.stacks.get(i).isEmpty()) {
				setInventorySlotContents(i, stack);
				return i;
			}
		}
		return -1;
	}

	@Override
	public String getName()
	{
		return hasCustomName() ? customName : "container.fairyChest";
	}

	public static void registerFixes(DataFixer fixer)
	{
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityDispenser.class, new String[] {
			"Items"
		}));
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public String getGuiID()
	{
		return "miragefairy:fairy_chest";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		fillWithLoot(playerIn);
		return new ContainerFairyChest(playerInventory, this);
	}

	@Override
	protected NonNullList<ItemStack> getItems()
	{
		return data.stacks;
	}

	public int getItemCount()
	{
		int t = 0;
		for (int i = 0; i < getSizeInventory(); i++) {
			if (!getStackInSlot(i).isEmpty()) t++;
		}
		return t;
	}

	public int getColor()
	{
		return getColor(0, getSizeInventory());
	}

	public int getColor(int begin, int count)
	{
		if (begin + count > getSizeInventory()) count = getSizeInventory() - begin;
		int r = 0;
		int g = 0;
		int b = 0;
		int c = 0;
		for (int i = begin; i < begin + count; i++) {
			ItemStack itemStack = getStackInSlot(i);
			if (itemStack.getItem() instanceof ItemFairyBase) {
				EnumFairy fairy = ((ItemFairyBase) itemStack.getItem()).getFairy(itemStack);
				int color;

				color = fairy.colorDark;
				r += (color >> 16) & 0xff;
				g += (color >> 8) & 0xff;
				b += (color >> 0) & 0xff;
				c++;

				color = fairy.colorBright;
				r += (color >> 16) & 0xff;
				g += (color >> 8) & 0xff;
				b += (color >> 0) & 0xff;
				c++;

				color = fairy.colorHair;
				r += (color >> 16) & 0xff;
				g += (color >> 8) & 0xff;
				b += (color >> 0) & 0xff;
				c++;

			}
		}
		return c == 0 ? 0xffffff : Color.rgb(r / c, g / c, b / c);
	}

	public int getLitLevel()
	{
		int t = getItemCount();
		if (t >= getSizeInventory() * 0.75) return 2;
		if (t > 0) return 1;
		return 0;
	}

	public int getLightValue()
	{
		return (int) (10.0 * getItemCount() / getSizeInventory());
	}

	private long tick = -2;
	private int litLevel = -1;
	private int lightValue = -1;

	@Override
	public void update()
	{
		if (!world.isRemote) {
			tick++;
			if (tick % 100 == 0) {
				boolean flag = false;

				if (litLevel != getLitLevel()) {
					litLevel = getLitLevel();
					flag = true;
				}

				if (lightValue != getLightValue()) {
					lightValue = getLightValue();
					flag = true;
				}

				if (flag) {
					((WorldServer) world).getPlayerChunkMap().getEntry(pos.getX() >> 4, pos.getZ() >> 4).sendPacket(getUpdatePacket());
					update2();
				}
			}
			if (tick % 10000 == 0) {
				((WorldServer) world).getPlayerChunkMap().getEntry(pos.getX() >> 4, pos.getZ() >> 4).sendPacket(getUpdatePacket());
				update2();
			}
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
		update2();
	}

	private void update2()
	{
		world.setBlockState(pos, world.getBlockState(pos), 10);
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.checkLight(pos);
	}

}
