package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import mirrg.minecraft.mod.miragefairy.api.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class FairyMagicWorkbench extends FairyMagic
{

	public FairyMagicWorkbench()
	{
		super("workbench", 0.1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if (!ItemFairyBase.tryUseMana(playerIn, getCost(fairy))) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

		playerIn.displayGui(new InterfaceCraftingTable(worldIn, playerIn.getPosition()));

		spawnParticle(fairy, 10, worldIn, new Vec3d(playerIn.posX, playerIn.posY + playerIn.height / 2, playerIn.posZ));
		play(ModuleMain.soundFairyWandUse, playerIn, worldIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

	public static class InterfaceCraftingTable implements IInteractionObject
	{

		private final World world;
		private final BlockPos position;

		public InterfaceCraftingTable(World worldIn, BlockPos pos)
		{
			this.world = worldIn;
			this.position = pos;
		}

		@Override
		public String getName()
		{
			return "crafting_table";
		}

		@Override
		public boolean hasCustomName()
		{
			return false;
		}

		@Override
		public ITextComponent getDisplayName()
		{
			return new TextComponentTranslation(Blocks.CRAFTING_TABLE.getUnlocalizedName() + ".name", new Object[0]);
		}

		@Override
		public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
		{
			return new ContainerWorkbench(playerInventory, this.world, this.position) {
				@Override
				public boolean canInteractWith(EntityPlayer playerIn)
				{
					return true;
				}
			};
		}

		@Override
		public String getGuiID()
		{
			return "minecraft:crafting_table";
		}

	}

}
