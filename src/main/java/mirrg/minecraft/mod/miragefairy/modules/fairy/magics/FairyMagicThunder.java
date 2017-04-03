package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import java.util.List;
import java.util.Optional;

import mirrg.minecraft.mod.miragefairy.api.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicBoolean;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicDouble;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class FairyMagicThunder extends FairyMagic
{

	public PropertyFairyMagicDouble propertyReach = new PropertyFairyMagicDouble("Reach",
		(p, Co, In, Vi, Lo, Ma, Et) -> 10 + Et / 10,
		"10 + Et / 10");
	public PropertyFairyMagicDouble propertyAdditionalDamage = new PropertyFairyMagicDouble("Additional Damage",
		(p, Co, In, Vi, Lo, Ma, Et) -> 5 + Ma / 25,
		"5 + Ma / 25");
	public PropertyFairyMagicInteger propertyCount = new PropertyFairyMagicInteger("Count",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (1 + (In + Vi) / 100),
		"(int) (1 + (In + Vi) / 100)");
	public PropertyFairyMagicBoolean propertyBypassesArmor = new PropertyFairyMagicBoolean("Bypasses Armor",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 50,
		"Lo > 50");
	public PropertyFairyMagicInteger propertyRedrawing = new PropertyFairyMagicInteger("Redrawing",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (Lo / 3),
		"(int) (Lo / 3)");
	public PropertyFairyMagicInteger propertyRegion = new PropertyFairyMagicInteger("Region",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (5 + Ma / 50),
		"(int) (5 + Ma / 50)");
	public PropertyFairyMagicInteger propertyCooltime = new PropertyFairyMagicInteger("Cooltime",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (100 / (1 + Co / 100)),
		"(int) (100 / (1 + Co / 100))");
	public PropertyFairyMagicBoolean propertyMobOnly = new PropertyFairyMagicBoolean("Mob Only",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 15,
		"Lo > 15");

	public FairyMagicThunder()
	{
		super("thunder", 5);
		properties.add(propertyReach);
		properties.add(propertyAdditionalDamage);
		properties.add(propertyCount);
		properties.add(propertyBypassesArmor);
		properties.add(propertyRedrawing);
		properties.add(propertyRegion);
		properties.add(propertyCooltime);
		properties.add(propertyMobOnly);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		return onItemRightClickOnBlock(propertyReach.get(fairy), propertyCooltime.get(fairy), fairy, itemStack, playerIn, worldIn, r -> {

			// マナがない場合は失敗
			if (!ItemFairyBase.tryUseMana(playerIn, getCost(fairy))) return EnumActionResult.FAIL;

			BlockPos pos = r.getBlockPos().offset(r.sideHit);

			// Count回試行
			for (int i = 0; i < propertyCount.get(fairy); i++) {
				int region = propertyRegion.get(fairy);

				// 座標決定
				int x;
				int z;
				{
					x = i == 0 ? 0 : worldIn.rand.nextInt(region * 2 + 1) - region;
					z = i == 0 ? 0 : worldIn.rand.nextInt(region * 2 + 1) - region;

					// Redrawing回再抽選
					a:
					for (int j = 0; j < propertyRedrawing.get(fairy); j++) {

						// 指定位置にダメージ対象が居るならOK
						Optional<BlockPos> pos2 = adjustPos(pos.add(x, 0, z), worldIn, 10);
						if (pos2.isPresent()) {
							List<Entity> entities = worldIn.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(
								pos2.get().getX() - 3.0D + x,
								pos2.get().getY() - 3.0D,
								pos2.get().getZ() - 3.0D + z,
								pos2.get().getX() + 3.0D + x,
								pos2.get().getY() + 6.0D + 3.0D,
								pos2.get().getZ() + 3.0D + z));
							for (Entity entity : entities) {
								if (!(entity instanceof EntityLivingBase)) continue;
								if (entity instanceof EntityPlayer) continue;

								break a;
							}
						}

						// 再抽選
						x = i == 0 ? 0 : worldIn.rand.nextInt(region * 2 + 1) - region;
						z = i == 0 ? 0 : worldIn.rand.nextInt(region * 2 + 1) - region;

					}
				}

				// 発射
				Optional<BlockPos> pos2 = adjustPos(r.getBlockPos().add(x, 0, z), worldIn, 10);
				if (pos2.isPresent()) {
					List<Entity> entities = worldIn.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(
						pos2.get().getX() - 3.0D + x,
						pos2.get().getY() - 3.0D,
						pos2.get().getZ() - 3.0D + z,
						pos2.get().getX() + 3.0D + x,
						pos2.get().getY() + 6.0D + 3.0D,
						pos2.get().getZ() + 3.0D + z));

					// エフェクトだけ追加
					EntityLightningBolt entityLightningBolt = new EntityLightningBolt(worldIn, pos2.get().getX() + x, pos2.get().getY(), pos2.get().getZ() + z, true);
					worldIn.spawnEntity(entityLightningBolt);
					//worldIn.addWeatherEffect(entityLightningBolt);

					if (!worldIn.isRemote) {

						// ダメージ
						for (Entity entity : entities) {
							if (propertyMobOnly.get(fairy)) {
								if (!(entity instanceof EntityLivingBase)) continue;
								if (entity instanceof EntityPlayer) continue;
							}
							if (!ForgeEventFactory.onEntityStruckByLightning(entity, entityLightningBolt)) {
								DamageSource damageSource = new DamageSource(DamageSource.LIGHTNING_BOLT.damageType);
								if (propertyBypassesArmor.get(fairy)) damageSource.setDamageBypassesArmor();
								entity.attackEntityFrom(damageSource, (float) (double) propertyAdditionalDamage.get(fairy));
								entity.onStruckByLightning(entityLightningBolt);
							}
						}

					}

				}

			}

			spawnParticle(EnumParticleTypes.SPELL, fairy, 20, worldIn, new Vec3d(pos).addVector(0.5, 0.5, 0.5));
			return EnumActionResult.SUCCESS;
		});
	}

	protected Optional<BlockPos> adjustPos(BlockPos pos, World world, int limit)
	{
		BlockPos pos2;

		if (isSolid(pos, world)) {
			// 地中なので上へ

			while (pos.getY() < world.getHeight()) {
				pos2 = pos.up();

				IBlockState blockState = world.getBlockState(pos2);
				if (!isSolid(pos2, world)) return Optional.of(pos2);

				limit--;
				if (limit <= 0) return Optional.empty();

				pos = pos2;
			}

			return Optional.empty();
		} else {
			// 空気なので下へ

			while (pos.getY() >= 0) {
				pos2 = pos.down();

				IBlockState blockState = world.getBlockState(pos2);
				if (isSolid(pos2, world)) return Optional.of(pos);

				limit--;
				if (limit <= 0) return Optional.empty();

				pos = pos2;
			}

			return Optional.empty();
		}
	}

}
