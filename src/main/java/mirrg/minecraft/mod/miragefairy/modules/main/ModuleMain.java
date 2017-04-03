package mirrg.minecraft.mod.miragefairy.modules.main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Optional;

import mirrg.minecraft.mod.miragefairy.MirageFairyMod;
import mirrg.minecraft.mod.miragefairy.core.ItemMetadata;
import mirrg.minecraft.mod.miragefairy.core.ModuleBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ModuleFairy;
import mirrg.minecraft.mod.miragefairy.util.Color;
import mirrg.minecraft.mod.miragefairy.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModuleMain extends ModuleBase
{

	public static CreativeTabs creativeTabMirageFairy;

	public static ItemMetadata<EnumItemMaterial> itemMaterial;
	public static ItemFairyWand itemFairyWand;
	public static ItemManaChargingCrystal itemManaChargingCrystal;
	public static BlockFairyHousePlant blockFairyHousePlant;
	public static ItemBlock itemFairyHousePlant;
	public static BlockFairyHouseCube blockFairyHouseCube;
	public static ItemMultiTexture itemFairyHouseCube;
	public static BlockFairyChest blockFairyChest;
	public static ItemFairyChest itemFairyChest;

	public static SoundEvent soundFairyWandUse;
	public static SoundEvent soundFairyWandCharge;

	public static ResourceLocation resourceLocationParticleFairy = new ResourceLocation(MirageFairyMod.MODID + ":particle/fairy");
	public static TextureAtlasSprite textureParticleFairy;

	public ModuleMain()
	{
		creativeTabMirageFairy = new CreativeTabs("mirageFairy") {
			@Override
			@SideOnly(Side.CLIENT)
			public ItemStack getTabIconItem()
			{
				return new ItemStack(itemFairyWand);
			}
		};
	}

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{

		// アイテムとブロック登録
		{
			itemMaterial = new ItemMetadata<>(EnumItemMaterial.values());
			itemMaterial.setRegistryName("material").setUnlocalizedName("material").setCreativeTab(ModuleMain.creativeTabMirageFairy);
			GameRegistry.findRegistry(Item.class).register(itemMaterial);
			itemMaterial.initModel();

			itemFairyWand = new ItemFairyWand(ToolMaterial.IRON);
			itemFairyWand.setRegistryName("fairy_wand").setUnlocalizedName("fairyWand").setCreativeTab(ModuleMain.creativeTabMirageFairy);
			GameRegistry.findRegistry(Item.class).register(itemFairyWand);
			ModelLoader.setCustomModelResourceLocation(itemFairyWand, 0, new ModelResourceLocation(MirageFairyMod.MODID + ":fairy_wand", "inventory"));

			itemManaChargingCrystal = new ItemManaChargingCrystal();
			itemManaChargingCrystal.setRegistryName("mana_charging_crystal").setUnlocalizedName("manaChargingCrystal").setCreativeTab(ModuleMain.creativeTabMirageFairy);
			GameRegistry.findRegistry(Item.class).register(itemManaChargingCrystal);
			ModelLoader.setCustomModelResourceLocation(itemManaChargingCrystal, 0, new ModelResourceLocation(MirageFairyMod.MODID + ":mana_charging_crystal", "inventory"));

			// fairy house plant
			{
				Tuple<BlockFairyHousePlant, ItemBlock> tuple = registerBlock(new BlockFairyHousePlant(), "fairy_house_plant", "fairyHousePlant");

				blockFairyHousePlant = tuple.getFirst();
				blockFairyHousePlant
					.setCreativeTab(ModuleMain.creativeTabMirageFairy);

				itemFairyHousePlant = tuple.getSecond();
				ModelLoader.setCustomModelResourceLocation(itemFairyHousePlant, 0, new ModelResourceLocation(MirageFairyMod.MODID + ":fairy_house_plant", "inventory"));
			}

			// fairy house cube
			{
				Tuple<BlockFairyHouseCube, ItemMultiTexture> tuple = registerBlockMetadata(new BlockFairyHouseCube(), "fairy_house_cube", "fairyHouseCube",
					stack -> BlockPlanks.EnumType.byMetadata(stack.getItemDamage()).getName());

				blockFairyHouseCube = tuple.getFirst();
				blockFairyHouseCube
					.setCreativeTab(ModuleMain.creativeTabMirageFairy);
				ModelLoader.setCustomStateMapper(
					blockFairyHouseCube,
					new StateMap.Builder().withName(blockFairyHouseCube.VARIANT).withSuffix("_fairy_house_cube").build());
				GameRegistry.registerTileEntity(TileEntityFairyHouseCube.class, "fairy_house_cube");

				itemFairyHouseCube = tuple.getSecond();
				for (int i = 0; i < BlockPlanks.EnumType.values().length; i++) {
					ModelLoader.setCustomModelResourceLocation(itemFairyHouseCube, i,
						new ModelResourceLocation(MirageFairyMod.MODID + ":" + BlockPlanks.EnumType.byMetadata(i).getName() + "_fairy_house_cube", "inventory"));
				}
			}

			// fairy chest
			{
				Tuple<BlockFairyChest, ItemFairyChest> tuple = registerBlock(new BlockFairyChest(), ItemFairyChest::new, "fairy_chest", "fairyChest");

				blockFairyChest = tuple.getFirst();
				blockFairyChest
					.setCreativeTab(ModuleMain.creativeTabMirageFairy);
				GameRegistry.registerTileEntity(TileEntityFairyChest.class, "fairy_chest");

				itemFairyChest = tuple.getSecond();
				ModelLoader.setCustomModelResourceLocation(itemFairyChest, 0, new ModelResourceLocation(MirageFairyMod.MODID + ":fairy_chest", "inventory"));
			}

		}

		// 音登録
		{
			soundFairyWandUse = new SoundEvent(new ResourceLocation(MirageFairyMod.MODID + ":fairyWand.use")).setRegistryName("fairyWand.use");
			GameRegistry.findRegistry(SoundEvent.class).register(soundFairyWandUse);
			soundFairyWandCharge = new SoundEvent(new ResourceLocation(MirageFairyMod.MODID + ":fairyWand.charge")).setRegistryName("fairyWand.charge");
			GameRegistry.findRegistry(SoundEvent.class).register(soundFairyWandCharge);
		}

		// GUI
		{
			NetworkRegistry.INSTANCE.registerGuiHandler(MirageFairyMod.MODID, new IGuiHandler() {
				@Override
				public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
				{
					return EnumGUI.values()[ID].guiHandler.getServerGuiElement(ID, player, world, x, y, z);
				}

				@Override
				public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
				{
					return EnumGUI.values()[ID].guiHandler.getClientGuiElement(ID, player, world, x, y, z);
				}
			});
		}

		// 妖精の宝ドロップ
		{
			Hashtable<ResourceLocation, EnumChestQuality> chestQualityTable = new Hashtable<>();
			chestQualityTable.put(LootTableList.CHESTS_SPAWN_BONUS_CHEST, EnumChestQuality.NONE);
			chestQualityTable.put(LootTableList.CHESTS_END_CITY_TREASURE, EnumChestQuality.HIGH);
			chestQualityTable.put(LootTableList.CHESTS_SIMPLE_DUNGEON, EnumChestQuality.LOW);
			chestQualityTable.put(LootTableList.CHESTS_VILLAGE_BLACKSMITH, EnumChestQuality.LOW);
			chestQualityTable.put(LootTableList.CHESTS_ABANDONED_MINESHAFT, EnumChestQuality.NORMAL);
			chestQualityTable.put(LootTableList.CHESTS_NETHER_BRIDGE, EnumChestQuality.NORMAL);
			chestQualityTable.put(LootTableList.CHESTS_STRONGHOLD_LIBRARY, EnumChestQuality.HIGH);
			chestQualityTable.put(LootTableList.CHESTS_STRONGHOLD_CROSSING, EnumChestQuality.HIGH);
			chestQualityTable.put(LootTableList.CHESTS_STRONGHOLD_CORRIDOR, EnumChestQuality.HIGH);
			chestQualityTable.put(LootTableList.CHESTS_DESERT_PYRAMID, EnumChestQuality.LOW);
			chestQualityTable.put(LootTableList.CHESTS_JUNGLE_TEMPLE, EnumChestQuality.LOW);
			chestQualityTable.put(LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER, EnumChestQuality.NONE);
			chestQualityTable.put(LootTableList.CHESTS_IGLOO_CHEST, EnumChestQuality.LOW);
			chestQualityTable.put(LootTableList.CHESTS_WOODLAND_MANSION, EnumChestQuality.NORMAL);
			MinecraftForge.EVENT_BUS.register(new Object() {
				@SubscribeEvent
				public void hook(LootTableLoadEvent event)
				{
					if (chestQualityTable.containsKey(event.getName())) {
						EnumChestQuality chestQuality = chestQualityTable.get(event.getName());
						if (chestQuality.enable) {
							for (int rarity = 1; rarity <= EnumFairy.RARITY_MAX; rarity++) {

								// ドロップリスト生成
								ArrayList<LootEntry> list = new ArrayList<>();
								{
									int sum = 0;
									for (EnumFairy fairy : EnumFairy.values()) {
										if (fairy.rarity == rarity) {
											if (fairy.treasureWeight > 0) {
												list.add(new LootEntryItem(ModuleFairy.itemFairy, fairy.treasureWeight, 0, new LootFunction[] {
													new SetMetadata(new LootCondition[0], new RandomValueRange(fairy.meta)),
												}, new LootCondition[0], fairy.getUnlocalizedName()));
												sum += fairy.treasureWeight;
											}
										}
									}
									double ratio = 0.3 * Math.pow(chestQuality.ratioPerRate, rarity - 1);
									double d = sum * (1 / ratio - 1);
									list.add(new LootEntryEmpty((int) d, 0, new LootCondition[0], "null"));
								}

								// 登録
								event.getTable().addPool(new LootPool(
									list.stream().toArray(LootEntry[]::new),
									new LootCondition[0],
									new RandomValueRange(3),
									new RandomValueRange(0),
									"mirageFairy." + rarity));

							}
						}
					}
				}
			});
		}

		// 地形生成登録
		{
			MinecraftForge.EVENT_BUS.register(new Object() {
				@SubscribeEvent
				public void init(DecorateBiomeEvent.Post event)
				{
					double cubes = event.getWorld().getHeight() / 16.0;
					double chance = cubes * 0.4;
					int count = (int) chance;
					if (event.getRand().nextDouble() < chance - (int) chance) count++;

					for (int i = 0; i < count; i++) {
						int x = event.getRand().nextInt(16) + 8;
						int y = event.getRand().nextInt(event.getWorld().getHeight());
						int z = event.getRand().nextInt(16) + 8;
						BlockPos pos2 = event.getPos().add(x, y, z);
						if (ModuleMain.blockFairyHousePlant.canPlaceBlockAt(event.getWorld(), pos2)) {
							event.getWorld().setBlockState(pos2, ModuleMain.blockFairyHousePlant.getDefaultState(), 2);
						}
					}
				}
			});
			MinecraftForge.EVENT_BUS.register(new Object() {
				@SubscribeEvent
				public void init(DecorateBiomeEvent.Post event)
				{
					double cubes = event.getWorld().getHeight() / 16.0;
					double chance = cubes * 50;
					int count = (int) chance;
					if (event.getRand().nextDouble() < chance - (int) chance) count++;

					for (int i = 0; i < count; i++) {
						int x = event.getRand().nextInt(16) + 8;
						int y = event.getRand().nextInt(event.getWorld().getHeight());
						int z = event.getRand().nextInt(16) + 8;
						EnumFacing facing = EnumFacing.HORIZONTALS[event.getRand().nextInt(4)];
						BlockPos posLog = event.getPos().add(x, y, z);
						BlockPos posAir = posLog.offset(facing);
						IBlockState blockStateLog = event.getWorld().getBlockState(posLog);
						IBlockState blockStateAir = event.getWorld().getBlockState(posAir);
						if (!blockStateAir.isSideSolid(event.getWorld(), posAir, facing.getOpposite())) {
							if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK))) {
								event.getWorld().setBlockState(posLog, ModuleMain.blockFairyHouseCube.getDefaultState()
									.withProperty(BlockFairyHouseCube.VARIANT, BlockPlanks.EnumType.OAK)
									.withProperty(BlockFairyHouseCube.FACING, facing), 2);
							} else if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE))) {
								event.getWorld().setBlockState(posLog, ModuleMain.blockFairyHouseCube.getDefaultState()
									.withProperty(BlockFairyHouseCube.VARIANT, BlockPlanks.EnumType.SPRUCE)
									.withProperty(BlockFairyHouseCube.FACING, facing), 2);
							} else if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH))) {
								event.getWorld().setBlockState(posLog, ModuleMain.blockFairyHouseCube.getDefaultState()
									.withProperty(BlockFairyHouseCube.VARIANT, BlockPlanks.EnumType.BIRCH)
									.withProperty(BlockFairyHouseCube.FACING, facing), 2);
							} else if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE))) {
								event.getWorld().setBlockState(posLog, ModuleMain.blockFairyHouseCube.getDefaultState()
									.withProperty(BlockFairyHouseCube.VARIANT, BlockPlanks.EnumType.JUNGLE)
									.withProperty(BlockFairyHouseCube.FACING, facing), 2);
							} else if (blockStateLog.equals(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA))) {
								event.getWorld().setBlockState(posLog, ModuleMain.blockFairyHouseCube.getDefaultState()
									.withProperty(BlockFairyHouseCube.VARIANT, BlockPlanks.EnumType.ACACIA)
									.withProperty(BlockFairyHouseCube.FACING, facing), 2);
							} else if (blockStateLog.equals(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK))) {
								event.getWorld().setBlockState(posLog, ModuleMain.blockFairyHouseCube.getDefaultState()
									.withProperty(BlockFairyHouseCube.VARIANT, BlockPlanks.EnumType.DARK_OAK)
									.withProperty(BlockFairyHouseCube.FACING, facing), 2);
							}
						}
					}
				}
			});
		}

		// テクスチャ登録
		{
			MinecraftForge.EVENT_BUS.register(new Object() {
				@SubscribeEvent
				public void init(TextureStitchEvent.Pre event)
				{
					if (event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
						textureParticleFairy = event.getMap().registerSprite(resourceLocationParticleFairy);
					}
				}
			});
		}

	}

	@Override
	public void init(FMLInitializationEvent event)
	{

		// 鉱石辞書
		{
			OreDictionary.registerOre("craftingToolFairyWand", new ItemStack(itemFairyWand, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("craftingDoll", itemMaterial.getItemStack(EnumItemMaterial.DOLL));
		}

		// レシピ
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemFairyWand),
				"  2",
				" 1 ",
				"1  ",
				'1', "stickWood",
				'2', "gemBloodstone").setMirrored(true));
			GameRegistry.addRecipe(new ShapelessOreRecipe(itemMaterial.getItemStack(EnumItemMaterial.DOLL),
				"craftingToolFairyWand",
				"gemBloodstone",
				new ItemStack(Items.ROTTEN_FLESH, 1, 0),
				new ItemStack(Items.SPIDER_EYE, 1, 0),
				new ItemStack(Items.FEATHER, 1, 0),
				new ItemStack(Items.BONE, 1, 0),
				new ItemStack(Items.LEATHER, 1, 0),
				new ItemStack(Blocks.TALLGRASS, 1, BlockTallGrass.EnumType.GRASS.getMeta())));
			GameRegistry.addRecipe(new ShapedOreRecipe(itemManaChargingCrystal.createItemStack(8000),
				"111",
				"121",
				"111",
				'1', "gemBloodstone",
				'2', "craftingToolFairyWand"));
			GameRegistry.addRecipe(new ShapelessOreRecipe(itemMaterial.getItemStack(EnumItemMaterial.MIRAGE_DUST),
				"craftingToolFairyWand",
				"ingotMiragium"));
			for (EnumFairy fairy : EnumFairy.values()) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(ModuleFairy.itemFairy.getItemStack(fairy),
					"craftingDoll",
					fairy.getUnlocalizedName("fairySpirit")));
				GameRegistry.addRecipe(new ShapedOreRecipe(ModuleFairy.itemMagicSphere.getItemStack(fairy),
					" 1 ",
					"121",
					" 1 ",
					'1', "blockGlass",
					'2', fairy.getUnlocalizedName("fairySpirit")));
				GameRegistry.addRecipe(new ShapelessOreRecipe(ModuleFairy.itemMagicSphere.getItemStack(fairy),
					fairy.getUnlocalizedName("magicSphereWithFairy")));
			}
			GameRegistry.addRecipe(new RecipeMagicSphereWithFairy());
			RecipeSorter.register("magic_shpere_with_fairy", RecipeMagicSphereWithFairy.class, RecipeSorter.Category.SHAPELESS, "");
			GameRegistry.addRecipe(new RecipeRepairManaChargingCrystal());
			RecipeSorter.register("rapair_mana_charging_crystal", RecipeRepairManaChargingCrystal.class, RecipeSorter.Category.SHAPELESS, "");
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemFairyChest),
				"111",
				"121",
				"111",
				'1', "logWood",
				'2', "craftingToolFairyWand"));
		}

		// 色登録
		{
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
				@Override
				public int getColorFromItemstack(ItemStack stack, int tintIndex)
				{
					if (stack.getMetadata() == EnumItemMaterial.DOLL.itemType.meta) {
						if (tintIndex == 0) return 0xFDCFBC;
						if (tintIndex == 1) return 0x7CD142;
						if (tintIndex == 2) return 0x37F22B;
					}
					return 0xffffff;
				}
			}, new Item[] {
				itemMaterial,
			});
			Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
				@Override
				public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
				{
					Optional<TileEntityFairyChest> oTileEntity = blockFairyChest.getTileEntity(worldIn, pos);
					if (!oTileEntity.isPresent()) return 0xffffff;

					int litLevel = oTileEntity.get().getLitLevel();
					int a = oTileEntity.get().getSizeInventory();
					if (tintIndex == 0) return litLevel >= 1 ? new Color(oTileEntity.get().getColor(0, a / 2)).brighter(0.75).toInt() : 0x251B16;
					if (tintIndex == 1) return litLevel >= 2 ? new Color(oTileEntity.get().getColor(a / 2, a / 2)).brighter(0.75).toInt() : 0x251B16;

					return 0xffffff;
				}
			}, new Block[] {
				blockFairyChest,
			});
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
				@Override
				public int getColorFromItemstack(ItemStack stack, int tintIndex)
				{
					TileEntityFairyChest tileEntity = Util.getTileEntityFromItemStack(new TileEntityFairyChest(), stack);

					int litLevel = tileEntity.getLitLevel();
					int a = tileEntity.getSizeInventory();
					if (tintIndex == 0) return litLevel >= 1 ? new Color(tileEntity.getColor(0, a / 2)).brighter(0.75).toInt() : 0x251B16;
					if (tintIndex == 1) return litLevel >= 2 ? new Color(tileEntity.getColor(a / 2, a / 2)).brighter(0.75).toInt() : 0x251B16;

					return 0xffffff;
				}
			}, new Item[] {
				itemFairyChest,
			});
		}

	}

	public static void spawnParticleFairy(World world, Vec3d vec3d, int color)
	{
		Particle particle = new ParticleFairy(world, vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 0, 0, 0);
		particle.setParticleTexture(ModuleMain.textureParticleFairy);
		particle.setRBGColorF(
			((color >> 16) & 0xff) / 255.0f,
			((color >> 8) & 0xff) / 255.0f,
			((color >> 0) & 0xff) / 255.0f);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

}
