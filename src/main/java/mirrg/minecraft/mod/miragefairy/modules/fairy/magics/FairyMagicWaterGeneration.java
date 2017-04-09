package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import mirrg.minecraft.mod.miragefairy.api.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicBoolean;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicDouble;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FairyMagicWaterGeneration extends FairyMagic
{

	public PropertyFairyMagicDouble propertyReach = new PropertyFairyMagicDouble("Reach",
		(p, Co, In, Vi, Lo, Ma, Et) -> 5 + Et / 10,
		"5 + Et / 10");
	public PropertyFairyMagicBoolean propertyStatic = new PropertyFairyMagicBoolean("Static",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 37,
		"Lo > 37");
	public PropertyFairyMagicInteger propertyRange = new PropertyFairyMagicInteger("Range",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (In / 50),
		"(int) (In / 50)");
	public PropertyFairyMagicInteger propertyHeight = new PropertyFairyMagicInteger("Height",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (Ma / 50),
		"(int) (Ma / 50)");
	public PropertyFairyMagicInteger propertyCooltime = new PropertyFairyMagicInteger("Cooltime",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (10 / (1 + (Co + Vi) / 20)),
		"(int) (10 / (1 + (Co + Vi) / 20))");

	public FairyMagicWaterGeneration()
	{
		super("waterGeneration", 1);
		properties.add(propertyReach);
		properties.add(propertyStatic);
		properties.add(propertyRange);
		properties.add(propertyHeight);
		properties.add(propertyCooltime);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		return onItemRightClickOnBlock(propertyReach.get(fairy), propertyCooltime.get(fairy), fairy, itemStack, playerIn, worldIn, r -> {

			// マナがない場合は失敗
			if (!ItemFairyBase.tryUseMana(playerIn, getCost(fairy))) return EnumActionResult.FAIL;

			// 置換できない場合は横の座標
			BlockPos pos = r.getBlockPos();
			if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
				pos = pos.offset(r.sideHit);
			}

			// 試行
			List<Runnable> listeners = new ArrayList<>();
			boolean flag = false;
			int range = propertyRange.get(fairy);
			int height = propertyHeight.get(fairy);
			for (int y = -height; y <= height; y++) {
				for (int x = -range; x <= range; x++) {
					for (int z = -range; z <= range; z++) {
						BlockPos pos2 = pos.add(x, y, z);

						// ブロックを変更できない場合は失敗
						if (!worldIn.isBlockModifiable(playerIn, pos2)) continue;
						if (!playerIn.canPlayerEdit(pos2, r.sideHit, itemStack)) continue;

						// 試行
						if (!tryPlaceContainedLiquid(listeners, fairy, playerIn, worldIn, pos2)) continue;

						flag = true;
					}
				}
			}

			// エフェクト
			if (listeners.size() > 10) {
				Collections.shuffle(listeners);
				listeners = listeners.subList(0, 10);
			}
			listeners.forEach(Runnable::run);

			return flag ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		});
	}

	protected boolean tryPlaceContainedLiquid(List<Runnable> listeners, IFairy fairy, @Nullable EntityPlayer player, World worldIn, BlockPos posIn)
	{
		IBlockState blockState = worldIn.getBlockState(posIn);

		// 置換不能ブロックなら失敗
		if (!worldIn.isAirBlock(posIn)
			&& blockState.getMaterial().isSolid()
			&& !blockState.getBlock().isReplaceable(worldIn, posIn)) return false;

		if (!worldIn.provider.doesWaterVaporize()) {
			// 水を置ける

			// 破壊
			if (!worldIn.isRemote) {
				if (!blockState.getMaterial().isSolid() || blockState.getBlock().isReplaceable(worldIn, posIn) && !blockState.getMaterial().isLiquid()) {
					worldIn.destroyBlock(posIn, true);
				}
			}

			// 設置
			IBlockState blockState2 = propertyStatic.get(fairy)
				? Blocks.WATER.getDefaultState()
				: Blocks.FLOWING_WATER.getDefaultState().withProperty(BlockLiquid.LEVEL, 8);
			worldIn.setBlockState(posIn, blockState2, 11);

			// エフェクト
			listeners.add(() -> {
				worldIn.playSound(player, posIn, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);

				spawnParticle(EnumParticleTypes.SPELL, fairy, 2, worldIn, new Vec3d(posIn));
			});
		} else {
			// 水を置けない

			// エフェクト
			listeners.add(() -> {
				worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS,
					0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

				for (int i = 0; i < 8; ++i) {
					worldIn.spawnParticle(
						EnumParticleTypes.SMOKE_LARGE,
						posIn.getX() + Math.random(),
						posIn.getY() + Math.random(),
						posIn.getZ() + Math.random(),
						0.0D, 0.0D, 0.0D, new int[0]);
				}
			});
		}

		return true;
	}

}
