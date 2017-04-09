package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import mirrg.minecraft.mod.miragefairy.api.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ModuleFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicDouble;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;
import mirrg.minecraft.mod.miragefairy.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FairyMagicTorchGeneration extends FairyMagic
{

	public PropertyFairyMagicDouble propertyReach = new PropertyFairyMagicDouble("Reach",
		(p, Co, In, Vi, Lo, Ma, Et) -> 5 + (In + Ma) / 10,
		"5 + (In + Ma) / 10");
	public PropertyFairyMagicInteger propertyLightValue = new PropertyFairyMagicInteger("LightValue",
		(p, Co, In, Vi, Lo, Ma, Et) -> Math.min((int) (7 + (Vi + Et) / 10), 15),
		"Math.min((int) (7 + (Vi + Et) / 10), 15)");
	public PropertyFairyMagicInteger propertyCooltime = new PropertyFairyMagicInteger("Cooltime",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (10 / (1 + Co / 20)),
		"(int) (10 / (1 + Co / 20))");

	public FairyMagicTorchGeneration()
	{
		super("torchGeneration", 0.2);
		properties.add(propertyReach);
		properties.add(propertyLightValue);
		properties.add(propertyCooltime);
	}

	@SuppressWarnings("unused")
	@Override
	public ActionResult<ItemStack> onItemRightClick(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		return onItemRightClickOnBlock(propertyReach.get(fairy), propertyCooltime.get(fairy), fairy, itemStack, playerIn, worldIn, r -> {

			// マナがない場合は失敗
			if (!ItemFairyBase.tryUseMana(playerIn, getCost(fairy))) return EnumActionResult.FAIL;

			// 松明検索
			Block blockTorch;
			ItemStack itemStackTorch;
			if (false) { // TODO 松明を消費する方の魔法を作る
				blockTorch = Blocks.TORCH;
				itemStackTorch = Util.findItemPredicate(playerIn, handIn, s -> s.getItem() == Item.getItemFromBlock(blockTorch))
					.orElseGet(() -> ItemStack.EMPTY);
			} else {
				blockTorch = ModuleFairy.blockFairyTorch;
				itemStackTorch = new ItemStack(blockTorch);
			}
			if (itemStackTorch.isEmpty()) return EnumActionResult.FAIL;

			// 置換不能なら隣の座標
			BlockPos pos = r.getBlockPos();
			if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
				pos = pos.offset(r.sideHit);
			}

			// 変更不可なら失敗
			if (itemStackTorch.isEmpty()) return EnumActionResult.FAIL;
			if (!playerIn.canPlayerEdit(pos, r.sideHit, itemStackTorch)) return EnumActionResult.FAIL;
			if (!worldIn.mayPlace(blockTorch, pos, false, r.sideHit, (Entity) null)) return EnumActionResult.FAIL;

			// 設置
			if (!worldIn.isRemote) {
				IBlockState blockState = blockTorch.getStateForPlacement(worldIn, pos, r.sideHit,
					(float) (r.hitVec.xCoord - r.getBlockPos().getX()),
					(float) (r.hitVec.yCoord - r.getBlockPos().getY()),
					(float) (r.hitVec.zCoord - r.getBlockPos().getZ()),
					0, playerIn, handIn);
				if (!worldIn.setBlockState(pos, blockState, 11)) return EnumActionResult.FAIL;
				IBlockState blockState2 = worldIn.getBlockState(pos);
				if (blockState2.getBlock() == blockTorch) {
					itemStackTorch.shrink(1);
					ItemBlock.setTileEntityNBT(worldIn, playerIn, pos, itemStackTorch);
					blockTorch.onBlockPlacedBy(worldIn, pos, blockState2, playerIn, itemStackTorch);
				}

				Util.getTileEntity(TileEntityFairyTorch.class, worldIn, pos)
					.ifPresent(t -> {
						t.data.lightValue = propertyLightValue.get(fairy);
						t.data.colorBody = fairy.getColor().bright;
						t.data.colorHead = fairy.getColor().hair;
					});
			}

			// エフェクト
			SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, playerIn);
			worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			spawnParticle(EnumParticleTypes.SPELL, fairy, 10, worldIn, new Vec3d(pos).addVector(0.5, 0.5, 0.5));

			return EnumActionResult.SUCCESS;
		});
	}

}
