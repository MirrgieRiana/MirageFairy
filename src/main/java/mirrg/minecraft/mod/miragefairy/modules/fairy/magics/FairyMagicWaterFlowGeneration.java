package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import javax.annotation.Nullable;

import mirrg.minecraft.mod.miragefairy.modules.fairy.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class FairyMagicWaterFlowGeneration extends FairyMagic
{

	public FairyMagicWaterFlowGeneration()
	{
		super("waterFlowGeneration", 1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{

		// ブロックをタゲれない時は失敗
		RayTraceResult rayTraceResult = rayTrace(fairy, itemStack, playerIn, worldIn, 5, false);
		if (rayTraceResult == null) return new ActionResult<>(EnumActionResult.FAIL, itemStack);
		if (rayTraceResult.typeOfHit != Type.BLOCK) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

		// マナがない場合は失敗
		if (!ItemFairyBase.tryUseMana(playerIn, getCost(fairy))) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

		// ブロックを変更できない場合は失敗
		BlockPos blockPos = rayTraceResult.getBlockPos();
		if (!worldIn.isBlockModifiable(playerIn, blockPos)) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

		// 置換できない場合は横の座標
		boolean isReplaceable = worldIn.getBlockState(blockPos).getBlock().isReplaceable(worldIn, blockPos);
		BlockPos blockPos2 = isReplaceable && rayTraceResult.sideHit == EnumFacing.UP ? blockPos : blockPos.offset(rayTraceResult.sideHit);

		// 書き換え不能座標なら失敗
		if (!playerIn.canPlayerEdit(blockPos2, rayTraceResult.sideHit, itemStack)) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

		// 試行
		if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockPos2)) {
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
		} else {
			return new ActionResult<>(EnumActionResult.FAIL, itemStack);
		}

	}

	private boolean tryPlaceContainedLiquid(@Nullable EntityPlayer player, World worldIn, BlockPos posIn)
	{
		IBlockState blockState = worldIn.getBlockState(posIn);
		Material material = blockState.getMaterial();
		boolean isNotSolid = !material.isSolid();
		boolean isReplaceable = blockState.getBlock().isReplaceable(worldIn, posIn);

		// 置換不能ブロックなら失敗
		if (!worldIn.isAirBlock(posIn) && !isNotSolid && !isReplaceable) return false;

		if (!worldIn.provider.doesWaterVaporize()) {
			// 水を置ける

			// 破壊
			if (!worldIn.isRemote && (isNotSolid || isReplaceable) && !material.isLiquid()) {
				worldIn.destroyBlock(posIn, true);
			}

			// エフェクト
			worldIn.playSound(player, posIn, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			play(ModuleMain.soundFairyWandUse, player, worldIn);

			// 設置
			worldIn.setBlockState(posIn, Blocks.FLOWING_WATER.getDefaultState().withProperty(BlockLiquid.LEVEL, 8), 11);

		} else {
			// 水を置けない

			// エフェクト
			int x = posIn.getX();
			int y = posIn.getY();
			int z = posIn.getZ();
			worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
			play(ModuleMain.soundFairyWandUse, player, worldIn);
			for (int i = 0; i < 8; ++i) {
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + Math.random(), y + Math.random(), z + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
			}

		}

		return true;
	}

}
