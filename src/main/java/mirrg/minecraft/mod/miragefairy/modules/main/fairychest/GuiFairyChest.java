package mirrg.minecraft.mod.miragefairy.modules.main.fairychest;

import mirrg.minecraft.mod.miragefairy.MirageFairyMod;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFairyChest extends GuiContainer
{

	public static final ResourceLocation TEXTURE = new ResourceLocation(MirageFairyMod.MODID + ":textures/gui/container.png");
	public static final int WIDTH = 256;
	public static final int HEIGHT = 256;
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
		mc.getTextureManager().bindTexture(TEXTURE);

		GlStateManager.pushMatrix();
		GlStateManager.translate((width - xSize) / 2, (height - ySize) / 2, 0);
		{

			drawTexturedModalRect(0, 0, 4, 4, 0, 0, 4, 4);
			drawTexturedModalRect(4, 0, 8, 4, 4, 0, xSize - 4, 4);
			drawTexturedModalRect(8, 0, 12, 4, xSize - 4, 0, xSize, 4);

			drawTexturedModalRect(0, 4, 4, 8, 0, 4, 4, ySize - 4);
			drawTexturedModalRect(4, 4, 8, 8, 4, 4, xSize - 4, ySize - 4);
			drawTexturedModalRect(8, 4, 12, 8, xSize - 4, 4, xSize, ySize - 4);

			drawTexturedModalRect(0, 8, 4, 12, 0, ySize - 4, 4, ySize);
			drawTexturedModalRect(4, 8, 8, 12, 4, ySize - 4, xSize - 4, ySize);
			drawTexturedModalRect(8, 8, 12, 12, xSize - 4, ySize - 4, xSize, ySize);

			for (Slot slot : inventorySlots.inventorySlots) {
				drawTexturedModalRect(32, 0, 50, 18, slot.xPos - 1, slot.yPos - 1, slot.xPos + 17, slot.yPos + 17);
			}

		}
		GlStateManager.popMatrix();
	}

	public void drawTexturedModalRect(int sx1, int sy1, int sx2, int sy2, int dx1, int dy1, int dx2, int dy2)
	{
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(
			GlStateManager.SourceFactor.SRC_ALPHA,
			GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
			GlStateManager.SourceFactor.SRC_ALPHA,
			GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		VertexBuffer vertexBuffer = Tessellator.getInstance().getBuffer();
		vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(dx1, dy2, zLevel).tex((float) sx1 / WIDTH, (float) sy2 / HEIGHT).endVertex();
		vertexBuffer.pos(dx2, dy2, zLevel).tex((float) sx2 / WIDTH, (float) sy2 / HEIGHT).endVertex();
		vertexBuffer.pos(dx2, dy1, zLevel).tex((float) sx2 / WIDTH, (float) sy1 / HEIGHT).endVertex();
		vertexBuffer.pos(dx1, dy1, zLevel).tex((float) sx1 / WIDTH, (float) sy1 / HEIGHT).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.disableBlend();
	}

	public static void drawRectAlpha(int left, int top, int right, int bottom, int color)
	{
		if (left < right) {
			int i = left;
			left = right;
			right = i;
		}

		if (top < bottom) {
			int j = top;
			top = bottom;
			bottom = j;
		}

		float f3 = (color >> 24 & 255) / 255.0F;
		float f = (color >> 16 & 255) / 255.0F;
		float f1 = (color >> 8 & 255) / 255.0F;
		float f2 = (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(
			GlStateManager.SourceFactor.SRC_ALPHA,
			GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
			GlStateManager.SourceFactor.SRC_ALPHA,
			GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(f, f1, f2, f3);
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(left, bottom, 0.0D).endVertex();
		vertexbuffer.pos(right, bottom, 0.0D).endVertex();
		vertexbuffer.pos(right, top, 0.0D).endVertex();
		vertexbuffer.pos(left, top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

}
