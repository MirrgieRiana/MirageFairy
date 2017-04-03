package mirrg.minecraft.mod.miragefairy.modules.main;

import mirrg.minecraft.mod.miragefairy.modules.main.fairychest.ContainerFairyChest;
import mirrg.minecraft.mod.miragefairy.modules.main.fairychest.GuiFairyChest;
import mirrg.minecraft.mod.miragefairy.modules.main.fairychest.TileEntityFairyChest;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public enum EnumGUI
{
	FAIRY_CHEST(new IGuiHandler() {
		@Override
		public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
		{
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileEntityFairyChest) {
				TileEntityFairyChest tileEntityFairyChest = (TileEntityFairyChest) tileEntity;
				return new ContainerFairyChest(player.inventory, tileEntityFairyChest);
			}
			return null;
		}

		@Override
		public GuiScreen getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
		{
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileEntityFairyChest) {
				TileEntityFairyChest tileEntityFairyChest = (TileEntityFairyChest) tileEntity;
				return new GuiFairyChest(player.inventory, tileEntityFairyChest);
			}
			return null;
		}
	});

	public final IGuiHandler guiHandler;

	private EnumGUI(IGuiHandler guiHandler)
	{
		this.guiHandler = guiHandler;
	}

}
