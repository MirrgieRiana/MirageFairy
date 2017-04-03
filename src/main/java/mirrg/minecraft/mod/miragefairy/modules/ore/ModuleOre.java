package mirrg.minecraft.mod.miragefairy.modules.ore;

import java.util.Random;

import mirrg.minecraft.mod.miragefairy.MirageFairyMod;
import mirrg.minecraft.mod.miragefairy.core.BlockMetadata;
import mirrg.minecraft.mod.miragefairy.core.ItemMetadata;
import mirrg.minecraft.mod.miragefairy.core.ModuleBase;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleOre extends ModuleBase
{

	public static BlockOre<EnumBlockOre> blockOre;
	public static ItemMultiTexture itemOre;
	public static ItemMetadata<EnumItemMineral> itemMineral;

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{

		// アイテムとブロック登録
		{

			// 鉱石ブロック
			{
				Tuple<BlockOre<EnumBlockOre>, ItemMultiTexture> tuple = registerBlockMetadata(
					BlockMetadata.createInstance(
						EnumBlockOre.class,
						p -> new BlockOre<>(Material.ROCK, p, EnumBlockOre.values(), EnumBlockOre.MIRAGIUM)),
					"ore",
					"ore",
					stack -> blockOre.getType(stack).blockType.unlocalizedName);

				blockOre = tuple.getFirst();
				blockOre.setCreativeTab(ModuleMain.creativeTabMirageFairy);
				ModelLoader.setCustomStateMapper(
					blockOre,
					new StateMap.Builder().withName(blockOre.propertyType).withSuffix("_ore").build());

				itemOre = tuple.getSecond();
				ModelLoader.setCustomModelResourceLocation(itemOre, 0, new ModelResourceLocation(MirageFairyMod.MODID + ":miragium_ore", "inventory"));
				ModelLoader.setCustomModelResourceLocation(itemOre, 1, new ModelResourceLocation(MirageFairyMod.MODID + ":bloodstone_ore", "inventory"));
			}

			// インゴット他
			itemMineral = new ItemMetadata<>(EnumItemMineral.values());
			itemMineral.setRegistryName("mineral").setUnlocalizedName("mineral").setCreativeTab(ModuleMain.creativeTabMirageFairy);
			GameRegistry.findRegistry(Item.class).register(itemMineral);
			itemMineral.initModel();

		}

		// 鉱石生成
		{
			MinecraftForge.ORE_GEN_BUS.register(new Object() {

				@SubscribeEvent
				public void init(OreGenEvent.Post event)
				{
					this.event = event;
					genStandardOre2(8,
						new WorldGenMinable(ore(EnumBlockOre.MIRAGIUM), 9),
						0, 128, 128);
					genStandardOre2(8,
						new WorldGenMinable(ore(EnumBlockOre.BLOODSTONE), 9),
						0, 32, 64);
				}

				private OreGenEvent.Post event;

				private IBlockState ore(EnumBlockOre type)
				{
					return blockOre.getState(type);
				}

				// ￣￣￣￣￣￣
				//
				//
				private void genStandardOre1(int blockCount, WorldGenerator generator, int minHeight, int maxHeight)
				{
					for (int i = 0; i < blockCount; i++) {
						BlockPos blockpos = event.getPos().add(
							event.getRand().nextInt(16),
							event.getRand().nextInt(maxHeight - minHeight) + minHeight,
							event.getRand().nextInt(16));
						generator.generate(event.getWorld(), event.getRand(), blockpos);
					}
				}

				//     ／＼
				//   ／    ＼
				// ／        ＼
				private void genStandardOre2(int blockCount, WorldGenerator generator, int minHeight, int middleHeight, int maxHeight)
				{
					for (int i = 0; i < blockCount; i++) {
						BlockPos blockpos = event.getPos().add(
							event.getRand().nextInt(16),
							getRandomHeight(event.getRand(), minHeight, middleHeight, maxHeight),
							event.getRand().nextInt(16));
						generator.generate(event.getWorld(), event.getRand(), blockpos);
					}
				}

			});
		}

	}

	@Override
	public void init(FMLInitializationEvent event)
	{

		// 鉱石辞書登録
		{
			OreDictionary.registerOre("oreMiragium", new ItemStack(itemOre, 1, EnumBlockOre.MIRAGIUM.blockType.meta));
			OreDictionary.registerOre("oreBloodstone", new ItemStack(itemOre, 1, EnumBlockOre.BLOODSTONE.blockType.meta));

			OreDictionary.registerOre("gemBloodstone", itemMineral.getItemStack(EnumItemMineral.BLOODSTONE));
			OreDictionary.registerOre("ingotMiragium", itemMineral.getItemStack(EnumItemMineral.MIRAGIUM_INGOT));
		}

		// レシピ
		{
			GameRegistry.addSmelting(
				new ItemStack(ModuleOre.blockOre, 1, EnumBlockOre.MIRAGIUM.blockType.meta),
				new ItemStack(itemMineral, 1, EnumItemMineral.MIRAGIUM_INGOT.itemType.meta),
				1.0f);
		}

	}

	private static int getRandomHeight(Random rand, int minHeight, int middleHeight, int maxHeight)
	{
		double a = maxHeight - minHeight;
		double b = middleHeight - minHeight;
		double c = b / a;

		double r1 = rand.nextDouble();
		double r2 = rand.nextDouble();
		if (r2 > r1) {
			r1 = 1 - r1;
			r2 = 1 - r2;
		}

		int h;
		if (rand.nextDouble() < c) {
			// low
			double h2 = middleHeight - minHeight;
			h = (int) (minHeight + r1 * h2);
		} else {
			// high
			double h2 = middleHeight - maxHeight;
			h = (int) (maxHeight + r1 * h2);
		}
		if (h < minHeight) h = minHeight;
		if (h >= maxHeight) h = maxHeight;
		return h;
	}

	// TODO testに移行
	public static void main(String[] args)
	{
		test_getRandomHeight();
	}

	private static void test_getRandomHeight()
	{
		int[] a = new int[100];
		for (int i = 0; i < 1000000; i++) {
			a[getRandomHeight(new Random(), 10, 30, 30)]++;
		}
		for (int i = 0; i < a.length; i++) {
			System.out.println(i + ": " + a[i]);
		}
	}

}
