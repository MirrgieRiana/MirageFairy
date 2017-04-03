package mirrg.minecraft.mod.miragefairy.modules.main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.oredict.OreDictionary;

public class FairyGatcha
{

	public static EnumFairy getGatchaResult(Hashtable<EnumFairy, Double> fairiesWithWeight, Random random)
	{
		double value = random.nextDouble();
		for (Entry<EnumFairy, Double> tuple : fairiesWithWeight.entrySet()) {
			value -= tuple.getValue();
			if (value < 0) return tuple.getKey();
		}
		return EnumFairy.air;
	}

	public static Hashtable<EnumFairy, Double> getGatchaWeight(EntityPlayer player, World worldIn, BlockPos pos, FairyGatchaSettings settings)
	{

		// レア度に関係なく出現可能妖精の列挙＆重みづけ
		ArrayList<EnumFairy> fairies = new ArrayList<>();
		{
			int range = 2;

			// デフォルト
			fairies.add(EnumFairy.air);

			// 近隣ブロック
			for (int x = -range; x <= range; x++) {
				for (int y = -range; y <= range; y++) {
					for (int z = -range; z <= range; z++) {
						getFairiesFromNames(getNamesFromBlock(worldIn, pos.add(x, y, z))).forEach(fairies::add);
					}
				}
			}

			// 近隣モブ
			for (EntityLivingBase entity : worldIn.getEntitiesWithinAABB(EntityLivingBase.class,
				new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)))) {
				for (int i = 0; i < 9; i++) {
					getFairiesFromNames(getNamesFromEntityLiving(entity)).forEach(fairies::add);
				}
			}

			// 近隣アイテム
			for (EntityItem entity : worldIn.getEntitiesWithinAABB(EntityItem.class,
				new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)))) {
				for (int i = 0; i < 3; i++) {
					getFairiesFromNames(getNamesFromItemStack(entity.getEntityItem())).forEach(fairies::add);
				}
			}

			// 環境
			getFairiesFromNames(getNamesFromPos(worldIn, pos, player)).forEach(fairies::add);

		}

		return getFairiesWithWeight(fairies, settings);
	}

	public static Hashtable<EnumFairy, Double> getGatchaWeight(FairyGatchaSettings settings)
	{
		return getFairiesWithWeight(Stream.of(EnumFairy.values())
			.collect(Collectors.toCollection(ArrayList::new)), settings);
	}

	public static Hashtable<EnumFairy, Double> getFairiesWithWeight(ArrayList<EnumFairy> fairies, FairyGatchaSettings settings)
	{
		// fairies: 出現可能な妖精が列挙されているだけの配列

		Hashtable<EnumFairy, int[]> fairiesWithWeight = getFairiesWithWeight2(fairies);

		// fairiesWithWeight: 妖精の種類ごとに比重が整理された配列

		ArrayList<Hashtable<EnumFairy, double[]>> tableFairy = new ArrayList<>();
		for (int i = 1; i <= EnumFairy.RARITY_MAX; i++) {
			tableFairy.add(new Hashtable<>());
		}
		for (Entry<EnumFairy, int[]> entry : fairiesWithWeight.entrySet()) {
			tableFairy.get(entry.getKey().getRarity() - 1).put(entry.getKey(), new double[] {
				entry.getValue()[0]
			});
		}
		for (Hashtable<EnumFairy, double[]> hash : tableFairy) {
			if (hash.isEmpty()) hash.put(EnumFairy.air, new double[] {
				1
			});
		}

		// tableFairy: 妖精をレア度ごとに振り分けた

		for (Hashtable<EnumFairy, double[]> hash : tableFairy) {
			double sum = hash.values().stream().mapToDouble(i -> i[0]).sum();
			hash.keySet().stream().forEach(k -> hash.get(k)[0] /= sum);
		}

		// tableFairy: 同一レア度内で比重の合計が1になるようにした

		// suitableRarity = 3
		// ratioPerRarity = 0.2
		//
		// R We Graph
		// 1 50 --------------------------------------------------||||||||||||||||||||||||||||||||||||||||||||||||||
		// 2 25 -------------------------|||||||||||||||||||||||||
		// 3 20 -----||||||||||||||||||||
		// 4  4 -||||
		// 5  1 |
		//
		// 1 -> 2 -> 3 -> 4 -> 5
		//   0.5  0.5  0.2  0.2
		double rate = 1; // 確率
		int limit = Math.min(EnumFairy.RARITY_MAX, settings.limitRarity); // 上限
		for (int rarity = 1; rarity <= EnumFairy.RARITY_MAX; rarity++) {

			// 次のレア度以降で占有する確率
			double nextRate;
			if (rarity == limit) {
				// 現在のレア度が最高なら占有しない
				nextRate = 0;
			} else if (rarity <= settings.suitableRarity) {
				// 現在のレア度が適正上限以上なら通常倍率
				nextRate = rate * settings.ratioPerRarity;
			} else {
				// 現在のレア度が適正上限未満なら通常倍率
				nextRate = rate * 0.5;
			}

			// 利用可能な確率
			double availableRate = rate - nextRate;

			Hashtable<EnumFairy, double[]> hash = tableFairy.get(rarity - 1);
			hash.keySet().stream().forEach(k -> hash.get(k)[0] *= availableRate);

			// 更新
			rate = nextRate;
		}

		// tableFairy: 全レア度で比重の合計が1

		ArrayList<Tuple<EnumFairy, Double>> fairiesWithWeight2 = new ArrayList<>();
		for (Hashtable<EnumFairy, double[]> hash : tableFairy) {
			hash.keySet().stream().forEach(k -> fairiesWithWeight2.add(new Tuple<>(k, hash.get(k)[0])));
		}

		// fairiesWithWeight2: レア度を混合した妖精の出現比

		Hashtable<EnumFairy, Double> fairiesWithWeight3 = new Hashtable<>();
		for (Tuple<EnumFairy, Double> tuple : fairiesWithWeight2) {
			if (fairiesWithWeight3.containsKey(tuple.getFirst())) {
				fairiesWithWeight3.put(tuple.getFirst(), fairiesWithWeight3.get(tuple.getFirst()) + tuple.getSecond());
			} else {
				fairiesWithWeight3.put(tuple.getFirst(), tuple.getSecond());
			}
		}

		return fairiesWithWeight3;
	}

	private static Hashtable<EnumFairy, int[]> getFairiesWithWeight2(ArrayList<EnumFairy> fairies)
	{
		Hashtable<EnumFairy, int[]> fairiesWithWeight = new Hashtable<>();
		for (EnumFairy fairy : fairies) {
			if (fairiesWithWeight.containsKey(fairy)) {
				fairiesWithWeight.get(fairy)[0]++;
			} else {
				fairiesWithWeight.put(fairy, new int[] {
					1
				});
			}
		}
		return fairiesWithWeight;
	}

	public static ArrayList<String> getNamesFromPos(World worldIn, BlockPos pos, EntityPlayer player)
	{
		ArrayList<String> names = new ArrayList<>();
		BlockPos posPlayer = new BlockPos(player.posX, player.posY, player.posZ);

		if (worldIn.canSeeSky(posPlayer)) {
			names.add("underground");
			names.add("building");
		}

		if (worldIn.provider.isSurfaceWorld()) {
			if (worldIn.canSeeSky(posPlayer)) {
				if (worldIn.isDaytime()) {
					names.add("timezone:daytime");
				} else {
					names.add("timezone:night");
				}
			}
		}

		if (worldIn.provider.isSurfaceWorld()) {
			if (!worldIn.isRaining()) {
				if (worldIn.canSeeSky(posPlayer)) {
					if (worldIn.isDaytime()) {
						names.add("celestial:sun");
					} else {
						names.add("celestial:moon");
						names.add("celestial:star");
					}
				}
			}
		}

		if (worldIn.canSeeSky(posPlayer)) {
			if (worldIn.getPrecipitationHeight(posPlayer).getY() <= posPlayer.getY()) {
				if (worldIn.isRaining()) {
					Biome biome = worldIn.getBiome(posPlayer);
					if (worldIn.isThundering()) {
						names.add("weather:thunder");
					}
					if (biome.getEnableSnow()) {
						names.add("weather:snowy");
					} else {
						if (worldIn.canSnowAt(posPlayer, false)) {
							names.add("weather:snowy");
						} else {
							if (biome.canRain()) {
								names.add("weather:rain");
							} else {
								names.add("weather:cloudy");
							}
						}
					}
				} else {
					if (worldIn.provider.isSurfaceWorld()) {
						names.add("weather:sunny");
					}
				}
			}
		}

		names.add("biome:" + worldIn.getBiome(pos).getBiomeName());

		return names;
	}

	public static ArrayList<String> getNamesFromBlock(World worldIn, BlockPos blockPos)
	{
		ArrayList<String> names = new ArrayList<>();

		IBlockState blockState = worldIn.getBlockState(blockPos);
		blockState = blockState.getActualState(worldIn, blockPos);

		names.add(blockState.getBlock().getUnlocalizedName());

		ItemStack itemStack = blockState.getBlock().getPickBlock(blockState, null, worldIn, blockPos, null);
		if (itemStack == null || itemStack.isEmpty()) return names;

		names.addAll(getNamesFromItemStack(itemStack));

		return names;
	}

	public static ArrayList<String> getNamesFromEntityLiving(EntityLivingBase entityLiving)
	{
		ArrayList<String> names = new ArrayList<>();

		String entityString = EntityList.getEntityString(entityLiving);
		if (entityString != null) names.add(entityString);

		return names;
	}

	public static ArrayList<String> getNamesFromItemStack(ItemStack itemStack)
	{
		ArrayList<String> names = new ArrayList<>();

		// TODO 妖精アイテムはガチャに影響しない
		{
			if (itemStack.getItem() instanceof ItemFairyBase) return names;
		}

		names.add(itemStack.getUnlocalizedName());

		int[] ids = OreDictionary.getOreIDs(itemStack);
		for (int id : ids) {
			names.add(OreDictionary.getOreName(id));
		}

		return names;
	}

	public static Stream<EnumFairy> getFairiesFromNames(ArrayList<String> names)
	{
		return Stream.of(EnumFairy.values())
			.filter(f -> isHit(f, names));
	}

	public static boolean isHit(EnumFairy fairy, ArrayList<String> names)
	{
		return names.stream()
			.anyMatch(n -> Stream.of(fairy.keywords)
				.anyMatch(k -> n.replace(" ", "")
					.replace("_", "")
					.toLowerCase().indexOf(k.toLowerCase()) != -1));
	}

	public static String getString(Hashtable<EnumFairy, Double> gatchaWeight)
	{
		ArrayList<String> strings = new ArrayList<>();
		strings.add("--------------------");
		double[] sum = {
			0
		};
		gatchaWeight.entrySet().stream()
			.sorted((a, b) -> (int) Math.signum(b.getValue() - a.getValue()))
			.forEach(e -> {
				strings.add(String.format("  %7.3f%%: %s %s",
					e.getValue() * 100,
					e.getKey().getRarity(),
					e.getKey().getUnlocalizedName()));
				sum[0] += e.getValue();
			});
		strings.add(String.format("# %7.3f%% #", sum[0] * 100));
		strings.add("--------------------");
		return String.join("\n", strings);
	}

	// TODO testに移行
	public static void main(String[] args)
	{
		System.out.println(getString(FairyGatcha.getGatchaWeight(new FairyGatchaSettings(2, 3, 0.2))));
	}

}
