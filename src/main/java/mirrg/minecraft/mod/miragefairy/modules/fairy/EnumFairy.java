package mirrg.minecraft.mod.miragefairy.modules.fairy;

import static mirrg.minecraft.mod.miragefairy.modules.fairy.EnumFairyPotentialType.*;

import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import mirrg.minecraft.mod.miragefairy.core.IItemTypeProvider;
import mirrg.minecraft.mod.miragefairy.core.ItemType;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicHaste;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicInstantFarming;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicItemCollecting;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicMagicArrow;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicMeteorStrike;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicNeedleFloor;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicNeedleShot;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicPotionSimple;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicStrongBody;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicThunder;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicWaterFlowGeneration;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magics.FairyMagicWorkbench;

public enum EnumFairy implements IFairy
{
	air(0, fc(0xFFBE80, 0xDEFFFF, 0xDEFFFF, 0xB0FFFF), 1, 80, po(10, 0, 0, 0, 0, 1), null, kw(), 1),
	water(1, fc(0x5469F2, 0x172AD3, 0x5985FF, 0x2D40F4), 1, 90, po(10, 1, 2, 0, 0, 1), new FairyMagicWaterFlowGeneration(), kw(), 1),
	lava(2, fc(0xCD4208, 0xCC4108, 0xEDB54A, 0x4C1500), 2, 90, po(10, 1, 0, 0, 0, 0), null, kw(), 1),
	fire(3, fc(0xFF6C01, 0xFF7324, 0xF9DFA4, 0xFF4000), 1, 70, po(10, 2, 1, 0, 0, 0), null, kw(), 1),
	sun(4, fc(0xff2f00, 0xff7500, 0xff972b, 0xffe7b2), 5, 260, po(23, 1, 0, 0, 0, 10), null, kw("celestial:%s"), 1),
	moon(5, fc(0xD9E4FF, 0x0C121F, 0x747D93, 0x2D4272), 5, 260, po(30, 0, 1, 0, 4, 10), null, kw("celestial:%s"), 1),
	star(6, fc(0xffffff, 0x0E0E10, 0x2C2C2E, 0x191919), 4, 260, po(57, 0, 1, 0, 2, 10), new FairyMagicMeteorStrike(), kw("celestial:%s"), 1),
	daytime(7, fc(0xFFE260, 0x84B5EF, 0xAACAEF, 0xffe7b2), 2, 130, po(99, 0, 1, 0, 0, 10), null, kw("timezone:%s"), 1),
	night(8, fc(0xFFE260, 0x0E0E10, 0x2C2C2E, 0x2D4272), 2, 130, po(86, 0, 0, 0, 1, 10), null, kw("timezone:%s"), 1),
	sunny(9, fc(0xB4FFFF, 0x84B5EF, 0xAACAEF, 0xffe7b2), 1, 220, po(99, 1, 1, 0, 0, 10), null, kw("weather:%s"), 1),
	rain(10, fc(0xB4FFFF, 0x4D5670, 0x4D5670, 0x2D40F4), 1, 220, po(92, 0, 5, 0, 0, 10), null, kw("weather:%s"), 1),
	thunder(11, fc(0xB4FFFF, 0x4D5670, 0x4D5670, 0xFFEB00), 3, 220, po(45, 1, 0, 2, 4, 10), new FairyMagicThunder(), kw("weather:%s"), 1),
	snowy(12, fc(0xB4FFFF, 0x4D5670, 0x4D5670, 0xffffff), 2, 220, po(38, 1, 0, 0, 1, 10), null, kw("weather:%s"), 1),
	plains(13, fc(0x80FF00, 0x86C91C, 0xD4FF82, 0xBB5400), 1, 160, po(10, 0, 3, 0, 0, 0), null, kw("biome:%s"), 0),
	ocean(14, fc(0x80FF00, 0x1771D3, 0x59C6FF, 0x2D84F4), 1, 160, po(10, 1, 2, 0, 0, 1), null, kw("biome:%s"), 0),
	desert(15, fc(0x80FF00, 0xD9C26B, 0xFFE9AC, 0xD2A741), 2, 160, po(10, 1, 0, 0, 0, 0), null, kw("biome:%s"), 0),
	nether(16, fc(0x80FF00, 0x972226, 0xFE7F00, 0xCF000D), 2, 160, po(10, 1, 4, 0, 3, 0), null, kw("biome:hell"), 0),
	end(17, fc(0x80FF00, 0x363636, 0xD1D175, 0x1E0D2E), 3, 160, po(10, 1, 1, 0, 5, 3), null, kw("biome:theend"), 0),
	stone(18, fc(0x333333, 0x686868, 0x8F8F8F, 0x747474), 1, 230, po(10, 0, 0, 0, 0, 0), null, kw(), 1),
	cobblestone(19, fc(0x333333, 0x4A4A4A, 0xC8C8C8, 0x747474), 1, 230, po(10, 3, 0, 0, 0, 0), null, kw(), 1),
	mossycobblestone(20, fc(0x333333, 0x4A4A4A, 0xC8C8C8, 0x278E27), 3, 230, po(10, 0, 3, 0, 1, 0), null, kw(), 1),
	bedrock(21, fc(0x333333, 0x1E1E1E, 0xEEEEEE, 0x747474), 3, 230, po(10, 0, 0, 0, 0, 1), null, kw(), 1),
	endstone(22, fc(0x333333, 0xC3BD89, 0xFAFAD0, 0xF5FF75), 3, 230, po(10, 0, 0, 0, 5, 0), null, kw(), 0),
	dirt(23, fc(0xB87440, 0x593D29, 0xB9855C, 0x914A18), 1, 210, po(10, 0, 2, 0, 0, 0), null, kw(), 1),
	grass(24, fc(0xB87440, 0x7FBF55, 0x97C667, 0x5BC117), 1, 210, po(10, 0, 5, 0, 0, 0), null, kw(), 1),
	sand(25, fc(0xB87440, 0xC2BC84, 0xEEE4B6, 0xD8D09B), 1, 210, po(10, 2, 0, 0, 0, 0), null, kw(), 1),
	ice(26, fc(0xFFFFFF, 0xB0CCFF, 0xB0CCFF, 0xB0CCFF), 2, 130, po(10, 0, 0, 0, 1, 1), null, kw(), 1),
	snow(27, fc(0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF), 2, 130, po(10, 0, 0, 0, 1, 0), null, kw(), 1),
	packedice(28, fc(0xFFFFFF, 0xC4D3EE, 0xC4D3EE, 0xC4D3EE), 4, 130, po(10, 0, 0, 0, 0, 2), null, kw(), 0),
	netherquartz(29, fc(0xEDE9E4, 0x5D4A3F, 0xDFD8CF, 0xCCAB90), 3, 130, po(0, 10, 0, 5, 1, 4), null, kw(), 0),
	emerald(30, fc(0x9FF9B5, 0x17DD62, 0x81F99E, 0x008A25), 4, 130, po(0, 10, 0, 1, 1, 5), null, kw(), 1),
	diamond(31, fc(0x97FFE3, 0x70FFD9, 0xD1FAF3, 0x30DBBD), 4, 130, po(0, 10, 0, 2, 2, 6), null, kw(), 1),
	iron(32, fc(0xA0A0A0, 0x727272, 0xD8D8D8, 0xD8AF93), 2, 110, po(0, 10, 1, 1, 0, 0), null, kw(), 1),
	gold(33, fc(0xA0A0A0, 0xDC7613, 0xFFFF0B, 0xDEDE00), 3, 110, po(0, 10, 2, 3, 3, 0), null, kw(), 1),
	obsidian(34, fc(0x0F0F1A, 0x09090E, 0x463A60, 0x532B76), 3, 120, po(3, 10, 0, 0, 0, 1), null, kw(), 1),
	redstone(35, fc(0xFF5959, 0xCD0000, 0xFF0000, 0xBA0000), 3, 120, po(0, 10, 0, 5, 3, 0), null, kw(), 1),
	lapislazuli(36, fc(0xA2B7E8, 0x224BD5, 0x4064EC, 0x0A33C2), 3, 120, po(0, 10, 2, 1, 3, 2), null, kw(), 1),
	glowstone(37, fc(0xFFFF7D, 0xA28C22, 0xFFF448, 0xECB861), 3, 120, po(0, 10, 1, 1, 5, 1), null, kw(), 1),
	coal(38, fc(0x363636, 0x030303, 0x101010, 0x000000), 2, 120, po(0, 10, 5, 0, 0, 0), null, kw(), 1),
	human(39, fc(0xFFBE80, 0x322976, 0x00AAAA, 0x281B0B), 4, 180, po(0, 5, 10, 5, 0, 0), null, kw("player"), 1),
	zombie(40, fc(0x2B4219, 0x322976, 0x00AAAA, 0x2B4219), 1, 160, po(10, 1, 8, 1, 10, 0), null, kw(), 0),
	skeleton(41, fc(0xCACACA, 0xCFCFCF, 0xCFCFCF, 0x494949), 1, 160, po(8, 3, 1, 3, 10, 0), null, kw(), 0),
	creeper(42, fc(0x5BAA53, 0x5EE74C, 0xD6FFCF, 0x000000), 1, 160, po(6, 0, 10, 0, 0, 0), null, kw(), 0),
	spider(43, fc(0x494422, 0x52483F, 0x61554A, 0xA80E0E), 1, 160, po(4, 0, 10, 0, 1, 0), null, kw(), 0),
	blaze(44, fc(0xFFE753, 0xDC8801, 0xFFFE37, 0x882900), 3, 160, po(0, 0, 10, 0, 12, 0), null, kw(), 0),
	witherskeleton(45, fc(0x505252, 0x1C1C1C, 0x1C1C1C, 0x060606), 4, 160, po(0, 0, 1, 0, 10, 0), null, kw(), 0),
	ghast(46, fc(0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0x565656), 3, 160, po(0, 0, 10, 0, 4, 0), null, kw(), 0),
	wither(47, fc(0x181818, 0x141414, 0x3C3C3C, 0x557272), 5, 220, po(0, 0, 10, 0, 32, 2), null, kw(), 0),
	enderman(48, fc(0x000000, 0x161616, 0x161616, 0xEF84FA), 3, 180, po(0, 4, 10, 1, 20, 1), null, kw(), 0),
	enderdragon(49, fc(0x000000, 0x181818, 0x181818, 0xA500E2), 5, 220, po(0, 0, 10, 0, 20, 1), null, kw(), 0),
	cow(50, fc(0xDE9D9D, 0x3B2E22, 0x969696, 0x000000), 1, 120, po(3, 0, 10, 0, 0, 0), null, kw(), 0),
	villager(51, fc(0xBD8B72, 0x3C2A23, 0x71544D, 0xBD8B72), 3, 120, po(0, 10, 10, 4, 0, 0), new FairyMagicPotionSimple("scapegoat", 1, "absorption", 25, 5, Vi), kw(), 0),
	golem(52, fc(0xDBCDC1, 0x8B7260, 0xDDC9B9, 0x46750B), 3, 180, po(0, 10, 10, 1, 10, 0), new FairyMagicStrongBody(), kw(), 0),
	mooshroom(53, fc(0x940E0F, 0x940E0F, 0x940E0F, 0xA1A1A1), 4, 120, po(0, 0, 10, 0, 1, 0), null, kw(), 0),
	feather(54, fc(0xFFBE80, 0xAAAAAA, 0xFFFFFF, 0x585858), 1, 100, po(1, 2, 10, 2, 0, 0), new FairyMagicPotionSimple("levitation", 2, "levitation", 50, 0.04, Vi), kw(), 0),
	leather(55, fc(0xFFBE80, 0xC65C35, 0xC65C35, 0x542716), 2, 100, po(0, 4, 10, 0, 0, 0), null, kw(), 0),
	string(56, fc(0xFFBE80, 0x000000, 0x8C8C8C, 0xFFFFFF), 2, 100, po(0, 5, 10, 0, 0, 0), null, kw(), 0),
	bonemeal(57, fc(0xFFBE80, 0xEAEAEA, 0xEAEAEA, 0xB9B9CB), 1, 100, po(2, 10, 3, 0, 0, 0), null, kw(), 0),
	milk(58, fc(0xFFBE80, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF), 2, 100, po(0, 0, 10, 0, 0, 0), null, kw(), 0),
	fish(59, fc(0xFFBE80, 0x6B9F93, 0x6B9F93, 0xADBEDB), 2, 70, po(3, 0, 10, 0, 0, 0), new FairyMagicPotionSimple("waterBreathing", 1, "water_breathing", 1000, 10, Vi), kw(), 0),
	enderpearl(60, fc(0xFFBE80, 0x258474, 0x8CF4E2, 0x00725F), 3, 100, po(0, 5, 10, 2, 4, 3), null, kw(), 0),
	netherstar(61, fc(0xFFBE80, 0xCBD6D6, 0x88A4A4, 0xD2D200), 5, 100, po(0, 6, 10, 3, 18, 5), null, kw(), 0),
	redmushroom(62, fc(0x7C4042, 0xFE2A2A, 0xFFFFFF, 0x9A171C), 1, 60, po(0, 0, 10, 0, 3, 0), null, kw(), 0),
	tallgrass(63, fc(0x168700, 0x7FBF55, 0x97C667, 0x5BC117), 1, 60, po(8, 0, 10, 0, 0, 0), null, kw(), 0),
	rose(64, fc(0x168700, 0x910205, 0xF7070F, 0x3E4400), 2, 110, po(0, 0, 10, 0, 3, 0), new FairyMagicNeedleShot(), kw(), 0),
	poppy(65, fc(0x168700, 0xF7070F, 0xF7070F, 0x3A0102), 1, 80, po(2, 1, 10, 0, 1, 0), null, kw(), 0),
	cactus(66, fc(0x168700, 0x128C21, 0x148D24, 0x000000), 3, 100, po(1, 0, 10, 0, 0, 0), new FairyMagicNeedleFloor(), kw(), 0),
	wheat(67, fc(0x168700, 0x716125, 0xD5DA45, 0x9E8714), 1, 100, po(1, 0, 10, 0, 0, 0), null, kw(), 0),
	log(68, fc(0x168700, 0x59472C, 0x977748, 0xBB9761), 1, 100, po(2, 0, 10, 0, 0, 0), null, kw(), 0),
	apple(69, fc(0x168700, 0xFF1C2B, 0xFF1C2B, 0xFFAAAF), 2, 100, po(1, 0, 10, 0, 3, 0), null, kw(), 0),
	pickaxe(70, fc(0x957546, 0x9A9A9A, 0x9A9A9A, 0x333333), 1, 90, po(2, 10, 0, 0, 0, 0), null, kw(), 0),
	shovel(71, fc(0x957546, 0x9A9A9A, 0x9A9A9A, 0xB87440), 1, 90, po(4, 10, 0, 0, 0, 0), null, kw(), 0),
	axe(72, fc(0x957546, 0x9A9A9A, 0x9A9A9A, 0x59472C), 1, 90, po(3, 10, 3, 0, 0, 0), null, kw(), 0),
	hoe(73, fc(0x957546, 0x9A9A9A, 0x9A9A9A, 0x168700), 1, 90, po(5, 10, 5, 0, 0, 0), new FairyMagicInstantFarming(), kw(), 0),
	bucket(74, fc(0x957546, 0x9A9A9A, 0x9A9A9A, 0x5469F2), 2, 90, po(1, 10, 0, 0, 0, 0), null, kw(), 0),
	flintandsteel(75, fc(0x957546, 0x9A9A9A, 0x9A9A9A, 0xFF4A00), 2, 90, po(0, 10, 0, 0, 0, 0), null, kw(), 0),
	shears(76, fc(0x957546, 0xD5D5D5, 0xD5D5D5, 0x634235), 2, 90, po(0, 10, 1, 0, 0, 0), null, kw(), 0),
	compass(77, fc(0x957546, 0x9A9A9A, 0x9A9A9A, 0xFF1414), 2, 90, po(0, 10, 0, 1, 1, 0), null, kw(), 0),
	sword(78, fc(0x957546, 0x70FFD9, 0xD1FAF3, 0xE60000), 1, 120, po(2, 10, 0, 0, 0, 0), null, kw(), 0),
	bow(79, fc(0x957546, 0x493615, 0x493615, 0xFFFFFF), 2, 120, po(0, 10, 0, 0, 0, 0), null, kw(), 0),
	arrow(80, fc(0x957546, 0x493615, 0x493615, 0x969696), 2, 120, po(0, 10, 1, 0, 0, 0), new FairyMagicMagicArrow(), kw(), 0),
	chestplate(81, fc(0x957546, 0xC1C1C1, 0xC1C1C1, 0xFF6578), 2, 140, po(1, 10, 3, 0, 0, 0), new FairyMagicPotionSimple("protect", 2, "resistance", 100, 5, In), kw(), 0),
	workbench(82, fc(0x957546, 0xB17449, 0xB17449, 0x2B2315), 2, 150, po(2, 10, 0, 0, 0, 0), new FairyMagicWorkbench(), kw(), 0),
	enchantmenttable(83, fc(0x957546, 0x322042, 0x2BDED6, 0x8A1512), 4, 150, po(0, 10, 1, 0, 4, 1), null, kw(), 0),
	glass(84, fc(0x957546, 0xE8FAFE, 0xFFFFFF, 0xC4F7FF), 2, 160, po(4, 10, 0, 0, 1, 0), new FairyMagicPotionSimple("invisible", 3, "invisibility", 1000, 2, In), kw(), 0),
	torch(85, fc(0x957546, 0xFFB800, 0xFFC42C, 0xFFF8C3), 2, 150, po(0, 10, 1, 0, 1, 0), null, kw(), 0),
	stair(86, fc(0x957546, 0xBB9761, 0xBB9761, 0x005500), 1, 150, po(4, 10, 0, 1, 1, 0), null, kw(), 0),
	chest(87, fc(0x957546, 0xA76E1F, 0xA76E1F, 0x413B2F), 2, 190, po(2, 10, 0, 0, 0, 0), new FairyMagicItemCollecting(), kw(), 0),
	bed(88, fc(0x957546, 0x9C2626, 0xEBEBEB, 0x413421), 2, 190, po(1, 10, 4, 0, 0, 0), null, kw(), 0),
	spawner(89, fc(0x957546, 0x1B2A35, 0x1B2A35, 0xFF3E00), 4, 100, po(0, 10, 15, 0, 5, 0), null, kw(), 0),
	beacon(90, fc(0x957546, 0xC0F5FE, 0x2BDED6, 0x322042), 5, 190, po(0, 10, 2, 1, 10, 4), null, kw(), 0),
	netherportal(91, fc(0x957546, 0x670ECC, 0x670ECC, 0x322042), 3, 170, po(0, 10, 0, 0, 8, 0), null, kw(), 0),
	endportal(92, fc(0x957546, 0x427367, 0xF9F9C5, 0x258474), 4, 170, po(0, 10, 0, 0, 8, 0), null, kw(), 0),
	dispenser(93, fc(0x957546, 0x616161, 0xC5C5C5, 0x000000), 2, 70, po(0, 5, 0, 10, 0, 0), null, kw(), 0),
	tnt(94, fc(0x957546, 0xDB441A, 0xCECECE, 0x000000), 3, 100, po(10, 10, 0, 1, 0, 0), null, kw(), 0),
	book(95, fc(0x957546, 0x654B17, 0xFFFFFF, 0x000000), 2, 100, po(0, 10, 2, 1, 1, 0), null, kw(), 0),
	sugar(96, fc(0xCC850C, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF), 1, 90, po(2, 5, 5, 0, 1, 0), new FairyMagicHaste(), kw(), 0),
	bread(97, fc(0xCC850C, 0x654B17, 0x9E7325, 0x3F2E0E), 2, 90, po(2, 5, 5, 0, 0, 0), null, kw(), 0),
	cake(98, fc(0xCC850C, 0xB85D27, 0xEAE9EB, 0xEA1D1D), 2, 90, po(0, 5, 5, 0, 1, 0), null, kw(), 0),
	goldenapple(99, fc(0xCC850C, 0xDBA213, 0xEAEE57, 0x351705), 3, 90, po(0, 5, 5, 0, 5, 0), null, kw(), 1),
	//clowdy(0x82FFFF, 0x4D5670, 0x4D5670, 0x4D5670, 2, 220, po(74, 0, 4, 0, 1, 10), null, kw("weather:%s")),
	;

	// --------------------------------------------------------------

	public static int RARITY_MAX = 5;

	public final int meta;
	public final String unlocalizedName;
	public final int colorSkin;
	public final int colorDark;
	public final int colorBright;
	public final int colorHair;
	public final int rarity;
	private final double rate;
	private final double[] potentials;
	public final FairyMagic fairyMagic;
	public final String[] keywords;
	public final int treasureWeight;

	private EnumFairy(
		int meta,
		FairyColorset fairyColorset,
		int rarity,
		double rate,
		double[] potentials,
		FairyMagic fairyMagic,
		Stream<String> keywords,
		int treasureWeight)
	{
		this.meta = meta;
		this.unlocalizedName = name();
		this.colorSkin = fairyColorset.skin;
		this.colorDark = fairyColorset.dark;
		this.colorBright = fairyColorset.bright;
		this.colorHair = fairyColorset.hair;
		this.rarity = rarity;
		this.rate = rate;
		this.potentials = potentials;
		this.fairyMagic = fairyMagic;
		this.keywords = keywords.map(k -> String.format(k, name())).toArray(String[]::new);
		this.treasureWeight = treasureWeight;
	}

	@Deprecated
	private static FairyColorset fc()
	{
		return fc(0xffffff, 0xffffff, 0xffffff, 0xffffff);
	}

	private static FairyColorset fc(int skin, int dark, int bright, int hair)
	{
		return new FairyColorset(skin, dark, bright, hair);
	}

	@Deprecated
	private static double[] po()
	{
		return po(10, 0, 0, 0, 0, 0);
	}

	private static double[] po(double d1, double d2, double d3, double d4, double d5, double d6)
	{
		return new double[] {
			d1, d2, d3, d4, d5, d6,
		};
	}

	private static Stream<String> kw()
	{
		return Stream.of("%s");
	}

	private static Stream<String> kw(String... keywords)
	{
		return Stream.of(keywords);
	}

	// --------------------------------------------------------------

	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public int getColorSkin()
	{
		return colorSkin;
	}

	@Override
	public int getColorDark()
	{
		return colorDark;
	}

	@Override
	public int getColorBright()
	{
		return colorBright;
	}

	@Override
	public int getColorHair()
	{
		return colorHair;
	}

	@Override
	public int getRarity()
	{
		return rarity;
	}

	@Override
	public double getCost()
	{
		return rate * Math.pow(1.2, rarity - 1);
	}

	public double getPotential()
	{
		return rate * Math.pow(1.33, rarity - 1);
	}

	private double getSum1()
	{
		return DoubleStream.of(potentials).sum();
	}

	private double getMax()
	{
		return DoubleStream.of(potentials).max().getAsDouble();
	}

	private double getDeviation()
	{
		return getMax() / getSum1();
	}

	private double getDeviationRate()
	{
		return (getDeviation() - 1) * (-6.0 / 5) * 0.5 + 1;
	}

	@Override
	public double getCo()
	{
		return getPotential() * potentials[0] / getSum1() * getDeviationRate();
	}

	@Override
	public double getIn()
	{
		return getPotential() * potentials[1] / getSum1() * getDeviationRate();
	}

	@Override
	public double getVi()
	{
		return getPotential() * potentials[2] / getSum1() * getDeviationRate();
	}

	@Override
	public double getLo()
	{
		return getPotential() * potentials[3] / getSum1() * getDeviationRate();
	}

	@Override
	public double getMa()
	{
		return getPotential() * potentials[4] / getSum1() * getDeviationRate();
	}

	@Override
	public double getEt()
	{
		return getPotential() * potentials[5] / getSum1() * getDeviationRate();
	}

	public ItemTypeProvider getItemTypeProvider(String unlocalizedNamePrefix, String modelName)
	{
		return new ItemTypeProvider(new ItemType(meta, getUnlocalizedName(unlocalizedNamePrefix), modelName));
	}

	public static ItemTypeProvider[] getItemTypeProviders(String unlocalizedNamePrefix, String modelName)
	{
		return Stream.of(values())
			.map(f -> f.getItemTypeProvider(unlocalizedNamePrefix, modelName))
			.toArray(ItemTypeProvider[]::new);
	}

	public class ItemTypeProvider implements IItemTypeProvider
	{

		public final ItemType itemType;

		private ItemTypeProvider(ItemType itemType)
		{
			this.itemType = itemType;
		}

		@Override
		public ItemType getItemType()
		{
			return itemType;
		}

		public EnumFairy getFairy()
		{
			return EnumFairy.this;
		}

	}

}
