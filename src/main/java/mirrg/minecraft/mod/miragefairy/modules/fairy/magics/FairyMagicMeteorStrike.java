package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import java.util.Optional;

import mirrg.minecraft.mod.miragefairy.api.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicDouble;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FairyMagicMeteorStrike extends FairyMagic
{

	public PropertyFairyMagicDouble propertyReach = new PropertyFairyMagicDouble("Reach",
		(p, Co, In, Vi, Lo, Ma, Et) -> 20 + Et / 5,
		"20 + Et / 5");
	public PropertyFairyMagicInteger propertyExplosionPower = new PropertyFairyMagicInteger("Explosion Power",
		(p, Co, In, Vi, Lo, Ma, Et) -> Math.min((int) (1 + (Co + Ma * 2) / 100), 20),
		"Math.min((int) (3 + Ma / 50), 20)");
	public PropertyFairyMagicInteger propertyCooltime = new PropertyFairyMagicInteger("Cooltime",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (200 + 400 / (1 + (Co + In + Vi) / 100)),
		"(int) (200 + 400 / (1 + (Co + In + Vi) / 100))");

	public FairyMagicMeteorStrike()
	{
		super("meteorStrike", 20);
		properties.add(propertyReach);
		properties.add(propertyExplosionPower);
		properties.add(propertyCooltime);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		return onItemRightClickOnBlock(propertyReach.get(fairy), propertyCooltime.get(fairy), fairy, itemStack, playerIn, worldIn, r -> {

			// マナがない場合は失敗
			if (!ItemFairyBase.tryUseMana(playerIn, getCost(fairy))) return EnumActionResult.FAIL;

			for (int i = 0; i < 10; i++) {

				// 空が見えるか
				float a = (float) (worldIn.rand.nextDouble() * 2 * Math.PI);
				float b = (float) ((worldIn.rand.nextDouble() * 30 + 60) / 180 * Math.PI);
				double distance = 250;
				BlockPos pos = r.getBlockPos().offset(r.sideHit);
				Vec3d start = new Vec3d(
					pos.getX() + 0.5,
					pos.getY() + 0.5,
					pos.getZ() + 0.5);
				Vec3d end = new Vec3d(
					pos.getX() + 0.5 + MathHelper.cos(b) * MathHelper.cos(a) * distance,
					pos.getY() + 0.5 + MathHelper.sin(b) * distance,
					pos.getZ() + 0.5 + MathHelper.cos(b) * MathHelper.sin(a) * distance);
				RayTraceResult rayTraceResult2 = worldIn.rayTraceBlocks(start, end);

				// 見えないなら再試行
				if (rayTraceResult2 != null) continue;

				// メテオ
				if (!worldIn.isRemote) {
					EntityLargeFireball entitylargefireball = new EntityLargeFireball(worldIn);
					entitylargefireball.shootingEntity = playerIn;
					entitylargefireball.explosionPower = propertyExplosionPower.get(fairy);
					entitylargefireball.posX = end.xCoord;
					entitylargefireball.posY = end.yCoord;
					entitylargefireball.posZ = end.zCoord;
					entitylargefireball.accelerationX = (start.xCoord - end.xCoord) / distance / 5;
					entitylargefireball.accelerationY = (start.yCoord - end.yCoord) / distance / 5;
					entitylargefireball.accelerationZ = (start.zCoord - end.zCoord) / distance / 5;
					worldIn.spawnEntity(entitylargefireball);
				}

				spawnParticle(EnumParticleTypes.SPELL, fairy, 30, worldIn, start);
				return EnumActionResult.SUCCESS;
			}

			return EnumActionResult.FAIL;
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
