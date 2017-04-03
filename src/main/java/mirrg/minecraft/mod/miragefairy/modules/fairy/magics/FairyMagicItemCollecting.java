package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import java.util.List;

import mirrg.minecraft.mod.miragefairy.api.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicDouble;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FairyMagicItemCollecting extends FairyMagic
{

	public PropertyFairyMagicDouble propertyRange = new PropertyFairyMagicDouble("Range",
		(p, Co, In, Vi, Lo, Ma, Et) -> 10 + (Lo + Vi + Et) / 5,
		"10 + (Lo + Vi + Et) / 5");
	public PropertyFairyMagicInteger propertyCount = new PropertyFairyMagicInteger("Count",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (10 + (Ma + In + Et) / 5),
		"(int) (10 + (Ma + In + Et) / 5)");
	public PropertyFairyMagicInteger propertyCooltime = new PropertyFairyMagicInteger("Cooltime",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (20 / (1 + (Co + Et) / 100)),
		"(int) (20 / (1 + (Co + Et) / 100))");

	public FairyMagicItemCollecting()
	{
		super("itemCollecting", 1);
		properties.add(propertyRange);
		properties.add(propertyCount);
		properties.add(propertyCooltime);
	}

	@Override
	public EnumActionResult onItemUse(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX,
		float hitY, float hitZ)
	{

		// アイテムを置く場所が埋まっている
		BlockPos pos2 = pos.offset(facing);
		if (isSolid(pos2, worldIn)) return EnumActionResult.FAIL;

		// コスト消費
		if (!ItemFairyBase.tryUseMana(player, getCost(fairy))) return EnumActionResult.FAIL;

		// 実行
		double range = propertyRange.get(fairy);
		List<EntityItem> entities = worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(
			pos2.getX() - range,
			pos2.getY() - range,
			pos2.getZ() - range,
			pos2.getX() + range + 1,
			pos2.getY() + range + 1,
			pos2.getZ() + range + 1));
		int lim = propertyCount.get(fairy);
		boolean flag = false;
		for (EntityItem entity : entities) {
			if (!entity.getEntityBoundingBox().intersectsWith(new AxisAlignedBB(pos2))) {

				// エフェクト
				for (int i = 0; i < 8; i++) {
					double ratio = (double) i / 8;
					spawnParticle(EnumParticleTypes.SPELL, fairy, 1, worldIn, new Vec3d(
						entity.posX * (1 - ratio) + (pos2.getX() + 0.5) * ratio,
						entity.posY * (1 - ratio) + (pos2.getY() + 0.5) * ratio,
						entity.posZ * (1 - ratio) + (pos2.getZ() + 0.5) * ratio));
				}

				entity.setPosition(pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5);

				flag = true;

				lim--;
				if (lim <= 0) break;

			}
		}

		// エフェクト
		if (flag) worldIn.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);

		addCooltime(player, propertyCooltime.get(fairy));
		play(ModuleMain.soundFairyWandUse, player, worldIn);
		return EnumActionResult.SUCCESS;
	}

}
