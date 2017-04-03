package mirrg.minecraft.mod.miragefairy.modules.main.fairychest;

import mirrg.minecraft.mod.miragefairy.util.UtilGUI;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFairyChest extends GuiContainer
{

	public IInventory inventoryPlayer;
	public IInventory inventoryTile;

	public GuiFairyChest(IInventory inventoryPlayer, IInventory inventoryTile)
	{
		super(new ContainerFairyChest(inventoryPlayer, inventoryTile));
		this.inventoryPlayer = inventoryPlayer;
		this.inventoryTile = inventoryTile;
		ySize = 114 + 6 * 18;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = inventoryTile.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(inventoryPlayer.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(UtilGUI.TEXTURE);

		GlStateManager.pushMatrix();
		GlStateManager.translate((width - xSize) / 2, (height - ySize) / 2, 0);
		{
			UtilGUI.drawContainer(0, 0, xSize, ySize);
			inventorySlots.inventorySlots.forEach(UtilGUI::drawSlot);
		}
		GlStateManager.popMatrix();
	}

}
