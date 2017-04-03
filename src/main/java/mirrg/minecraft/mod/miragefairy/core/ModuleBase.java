package mirrg.minecraft.mod.miragefairy.core;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModuleBase
{

	public void preInit(FMLPreInitializationEvent event)
	{

	}

	public void init(FMLInitializationEvent event)
	{

	}

	public static <B extends Block, I extends ItemBlock> Tuple<B, I> registerBlock(B block, Function<B, I> fItem, String registryName, String unlocalizedName)
	{
		block.setRegistryName(registryName);
		block.setUnlocalizedName(unlocalizedName);
		GameRegistry.findRegistry(Block.class).register(block);

		I item = fItem.apply(block);
		item.setRegistryName(registryName);
		item.setUnlocalizedName(unlocalizedName);
		GameRegistry.findRegistry(Item.class).register(item);

		return new Tuple<>(block, item);
	}

	public static <B extends Block> Tuple<B, ItemBlock> registerBlock(B block, String registryName, String unlocalizedName)
	{
		return registerBlock(block, ItemBlock::new, registryName, unlocalizedName);
	}

	public static <T extends Block> Tuple<T, ItemMultiTexture> registerBlockMetadata(T block, String registryName, String unlocalizedName, ItemMultiTexture.Mapper mapper)
	{
		return registerBlock(block, b -> new ItemMultiTexture(b, b, mapper), registryName, unlocalizedName);
	}

}
