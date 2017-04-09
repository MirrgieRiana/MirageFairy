package mirrg.minecraft.mod.miragefairy.modules.fairy;

import java.util.ArrayList;
import java.util.List;

import mirrg.minecraft.mod.miragefairy.MirageFairyMod;
import mirrg.minecraft.mod.miragefairy.core.ModuleBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.BlockFairyTorch;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.BlockNeedleFloor;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.TileEntityFairyTorch;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.TileEntityNeedleFloor;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import mirrg.minecraft.mod.miragefairy.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleFairy extends ModuleBase
{

	public static ItemFairy itemFairy;
	public static ItemFairySpirit itemFairySpirit;
	public static ItemMagicSphere itemMagicSphere;
	public static ItemMagicSphereWithFairy itemMagicSphereWithFairy;

	public static BlockNeedleFloor blockNeedleFloor;
	public static ItemBlock itemNeedleFloor;

	public static BlockFairyTorch blockFairyTorch;
	public static ItemBlock itemFairyTorch;

	public static Potion potionStepHeight;

	public static CreativeTabs creativeTabMirageFairyFairy;
	public static CreativeTabs creativeTabMirageFairyFairySpirit;
	public static CreativeTabs creativeTabMirageFairyMagicSphere;

	public ModuleFairy()
	{
		creativeTabMirageFairyFairy = new CreativeTabs("mirageFairy.fairy") {
			@Override
			@SideOnly(Side.CLIENT)
			public ItemStack getTabIconItem()
			{
				return ModuleFairy.itemFairy.getItemStack(EnumFairy.air);
			}
		};
		creativeTabMirageFairyFairySpirit = new CreativeTabs("mirageFairy.fairySpirit") {
			@Override
			@SideOnly(Side.CLIENT)
			public ItemStack getTabIconItem()
			{
				return ModuleFairy.itemFairySpirit.getItemStack(EnumFairy.sun);
			}
		};
		creativeTabMirageFairyMagicSphere = new CreativeTabs("mirageFairy.magicSphere") {
			@Override
			@SideOnly(Side.CLIENT)
			public ItemStack getTabIconItem()
			{
				return ModuleFairy.itemMagicSphere.getItemStack(EnumFairy.thunder);
			}
		};
	}

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{

		// アイテムとブロック登録
		{
			itemFairy = new ItemFairy(EnumFairy.getItemTypeProviders("fairy", MirageFairyMod.MODID + ":fairy"));
			itemFairy
				.setRegistryName("fairy")
				.setUnlocalizedName("fairy")
				.setCreativeTab(creativeTabMirageFairyFairy);
			GameRegistry.findRegistry(Item.class).register(itemFairy);
			itemFairy.initModel();

			itemFairySpirit = new ItemFairySpirit(EnumFairy.getItemTypeProviders("fairySpirit", MirageFairyMod.MODID + ":fairy_spirit"));
			itemFairySpirit
				.setRegistryName("fairy_spirit")
				.setUnlocalizedName("fairySpirit")
				.setCreativeTab(creativeTabMirageFairyFairySpirit);
			GameRegistry.findRegistry(Item.class).register(itemFairySpirit);
			itemFairySpirit.initModel();

			itemMagicSphere = new ItemMagicSphere(EnumFairy.getItemTypeProviders("magicSphere", MirageFairyMod.MODID + ":magic_sphere"));
			itemMagicSphere
				.setRegistryName("magic_sphere")
				.setUnlocalizedName("magicSphere")
				.setCreativeTab(creativeTabMirageFairyMagicSphere);
			GameRegistry.findRegistry(Item.class).register(itemMagicSphere);
			itemMagicSphere.initModel();

			itemMagicSphereWithFairy = new ItemMagicSphereWithFairy(EnumFairy.getItemTypeProviders("magicSphereWithFairy", MirageFairyMod.MODID + ":magic_sphere_with_fairy"));
			itemMagicSphereWithFairy
				.setRegistryName("magic_sphere_with_fairy")
				.setUnlocalizedName("magicSphereWithFairy");
			GameRegistry.findRegistry(Item.class).register(itemMagicSphereWithFairy);
			itemMagicSphereWithFairy.initModel();

			// needle floor
			{
				Tuple<BlockNeedleFloor, ItemBlock> tuple = registerBlock(new BlockNeedleFloor(), "needle_floor", "needleFloor");

				blockNeedleFloor = tuple.getFirst();
				blockNeedleFloor
					.setCreativeTab(ModuleMain.creativeTabMirageFairy);
				GameRegistry.registerTileEntity(TileEntityNeedleFloor.class, "needle_floor");

				itemNeedleFloor = tuple.getSecond();
				ModelLoader.setCustomModelResourceLocation(itemNeedleFloor, 0, new ModelResourceLocation(MirageFairyMod.MODID + ":needle_floor", "inventory"));
			}

			// fairy torch
			{
				Tuple<BlockFairyTorch, ItemBlock> tuple = registerBlock(new BlockFairyTorch(), "fairy_torch", "fairyTorch");

				blockFairyTorch = tuple.getFirst();
				blockFairyTorch
					.setCreativeTab(ModuleMain.creativeTabMirageFairy);
				GameRegistry.registerTileEntity(TileEntityFairyTorch.class, "fairy_torch");

				itemFairyTorch = tuple.getSecond();
				ModelLoader.setCustomModelResourceLocation(itemFairyTorch, 0, new ModelResourceLocation(MirageFairyMod.MODID + ":fairy_torch", "inventory"));
			}
		}

		// 翻訳登録
		{
			((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new IResourceManagerReloadListener() {
				@Override
				public void onResourceManagerReload(IResourceManager resourceManager)
				{
					ArrayList<String> list = new ArrayList<>();
					list.add("en_us");

					if (!"en_us".equals(Minecraft.getMinecraft().gameSettings.language)) {
						list.add(Minecraft.getMinecraft().gameSettings.language);
					}

					for (String string : list) {
						try {
							List<IResource> resources;
							resources = resourceManager.getAllResources(new ResourceLocation(MirageFairyMod.MODID + ":lang/fairy/" + string + ".lang"));
							for (IResource resource : resources) {
								LanguageMap.inject(resource.getInputStream());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		}

		// ポーション効果登録
		{
			potionStepHeight = new Potion(false, 0xffaa22) {
				{
					setPotionName("effect.mirageFairy.stepHeight");
					setIconIndex(2, 1);
					setBeneficial();
					setRegistryName("stepHeight");
				}

				@Override
				public boolean isReady(int duration, int amplifier)
				{
					return true;
				}

				@Override
				public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_)
				{
					PotionEffect potionEffect = entityLivingBaseIn.getActivePotionEffect(this);
					if (potionEffect == null) return;

					if (potionEffect.getDuration() <= 20) {
						entityLivingBaseIn.stepHeight = 0.6f;
					} else {
						float stepHeight = 1.1f + potionEffect.getAmplifier() * 0.5f;
						if (stepHeight > entityLivingBaseIn.stepHeight) {
							entityLivingBaseIn.stepHeight = stepHeight;
						}
					}
				}
			};
			((FMLControlledNamespacedRegistry<Potion>) Potion.REGISTRY).register(potionStepHeight);
		}

	}

	@Override
	public void init(FMLInitializationEvent event)
	{

		// 鉱石辞書登録
		{
			for (EnumFairy fairy : EnumFairy.values()) {
				OreDictionary.registerOre(fairy.getUnlocalizedName("fairySpirit"), itemFairySpirit.getItemStack(fairy));
				OreDictionary.registerOre(fairy.getUnlocalizedName("fairy"), itemFairy.getItemStack(fairy));
				OreDictionary.registerOre(fairy.getUnlocalizedName("magicSphere"), itemMagicSphere.getItemStack(fairy));
				OreDictionary.registerOre(fairy.getUnlocalizedName("magicSphereWithFairy"), itemMagicSphereWithFairy.getItemStack(fairy));
			}
		}

		// 色登録
		{
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
				@Override
				public int getColorFromItemstack(ItemStack stack, int tintIndex)
				{
					if (tintIndex == 0) return itemFairySpirit.getFairy(stack).getColor().dark;
					if (tintIndex == 1) return itemFairySpirit.getFairy(stack).getColor().skin;
					if (tintIndex == 2) return itemFairySpirit.getFairy(stack).getColor().bright;
					if (tintIndex == 3) return itemFairySpirit.getFairy(stack).getColor().hair;
					return 0xffffff;
				}
			}, new Item[] {
				itemFairySpirit,
			});
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
				@Override
				public int getColorFromItemstack(ItemStack stack, int tintIndex)
				{
					if (tintIndex == 0) return itemFairy.getFairy(stack).getColor().skin;
					if (tintIndex == 1) return 0x00BE00;
					if (tintIndex == 2) return itemFairy.getFairy(stack).getColor().dark;
					if (tintIndex == 3) return itemFairy.getFairy(stack).getColor().bright;
					if (tintIndex == 4) return itemFairy.getFairy(stack).getColor().hair;
					return 0xffffff;
				}
			}, new Item[] {
				itemFairy,
			});
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
				@Override
				public int getColorFromItemstack(ItemStack stack, int tintIndex)
				{
					if (tintIndex == 0) return itemMagicSphere.getFairy(stack).getColor().dark;
					if (tintIndex == 1) return itemMagicSphere.getFairy(stack).getColor().hair;
					if (tintIndex == 2) return itemMagicSphere.getFairy(stack).getColor().skin;
					if (tintIndex == 3) return itemMagicSphere.getFairy(stack).getColor().bright;
					return 0xffffff;
				}
			}, new Item[] {
				itemMagicSphere,
			});
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
				@Override
				public int getColorFromItemstack(ItemStack stack, int tintIndex)
				{
					EnumFairy fairyOperator = itemMagicSphereWithFairy.getFairyStored(stack).orElse(EnumFairy.air);
					if (tintIndex == 0) return itemMagicSphereWithFairy.getFairy(stack).getColor().dark;
					if (tintIndex == 1) return itemMagicSphereWithFairy.getFairy(stack).getColor().hair;
					if (tintIndex == 2) return fairyOperator.getColor().skin;
					if (tintIndex == 3) return 0x00BE00;
					if (tintIndex == 4) return fairyOperator.getColor().dark;
					if (tintIndex == 5) return fairyOperator.getColor().bright;
					if (tintIndex == 6) return fairyOperator.getColor().hair;
					if (tintIndex == 7) return itemMagicSphereWithFairy.getFairy(stack).getColor().skin;
					if (tintIndex == 8) return itemMagicSphereWithFairy.getFairy(stack).getColor().bright;
					return 0xffffff;
				}
			}, new Item[] {
				itemMagicSphereWithFairy,
			});
			Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
				@Override
				public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
				{
					TileEntityFairyTorch tileEntity = Util.getTileEntity(TileEntityFairyTorch.class, worldIn, pos).orElse(null);
					if (tileEntity == null) return 0xffffff;

					if (tintIndex == 0) return tileEntity.data.colorBody;
					if (tintIndex == 1) return tileEntity.data.colorHead;

					return 0xffffff;
				}
			}, new Block[] {
				blockFairyTorch,
			});
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
				@Override
				public int getColorFromItemstack(ItemStack stack, int tintIndex)
				{
					TileEntityFairyTorch tileEntity = Util.getTileEntityFromItemStack(new TileEntityFairyTorch(), stack);

					if (tintIndex == 0) return tileEntity.data.colorBody;
					if (tintIndex == 1) return tileEntity.data.colorHead;

					return 0xffffff;
				}
			}, new Item[] {
				itemFairyTorch,
			});
		}

	}

}
