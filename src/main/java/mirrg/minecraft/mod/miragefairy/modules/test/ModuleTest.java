package mirrg.minecraft.mod.miragefairy.modules.test;

import mirrg.minecraft.mod.miragefairy.MirageFairyMod;
import mirrg.minecraft.mod.miragefairy.core.ModuleBase;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModuleTest extends ModuleBase
{

	public static Tuple<Block, ItemBlock> tupleBlock2;
	public static Tuple<Block, ItemBlock> tupleBlock3;

	public static Item itemItem2;

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		tupleBlock2 = registerBlock(new Block2(), "block2", "block2");
		ModelLoader.setCustomModelResourceLocation(tupleBlock2.getSecond(), 0, new ModelResourceLocation(MirageFairyMod.MODID + ":block2", "inventory"));

		tupleBlock3 = registerBlock(new Block3(), "block3", "block3");
		ModelLoader.setCustomModelResourceLocation(tupleBlock3.getSecond(), 0, new ModelResourceLocation(MirageFairyMod.MODID + ":block3", "inventory"));

		itemItem2 = new Item2().setRegistryName("item2").setUnlocalizedName("item2");
		GameRegistry.findRegistry(Item.class).register(itemItem2);
		ModelLoader.setCustomModelResourceLocation(itemItem2, 0, new ModelResourceLocation(MirageFairyMod.MODID + ":item2", "inventory"));
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.addShapedRecipe(new ItemStack(tupleBlock2.getFirst()),
			"A ", " A",
			'A', itemItem2);
		GameRegistry.addShapedRecipe(new ItemStack(itemItem2),
			"A ", " A",
			'A', Items.STICK);

		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex)
			{
				return tintIndex == 0 ? 0xff0000 : -1;
			}
		}, new Item[] {
			itemItem2
		});
	}

}
