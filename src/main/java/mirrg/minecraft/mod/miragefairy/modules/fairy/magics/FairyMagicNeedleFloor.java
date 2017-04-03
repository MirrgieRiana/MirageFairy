package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import mirrg.minecraft.mod.miragefairy.modules.fairy.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ModuleFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.TileEntityNeedleFloor;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicBoolean;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicDouble;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FairyMagicNeedleFloor extends FairyMagic
{

	public PropertyFairyMagicDouble propertyDamage = new PropertyFairyMagicDouble("Damage",
		(p, Co, In, Vi, Lo, Ma, Et) -> 2 + Vi / 33,
		"2 + Vi / 33");
	public PropertyFairyMagicInteger propertyDuration = new PropertyFairyMagicInteger("Duration",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (100 + Co),
		"(int) (100 + Co / 2)");
	public PropertyFairyMagicInteger propertyRange = new PropertyFairyMagicInteger("Range",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (1 + Math.sqrt(Ma / 100) * 100 / 50),
		"(int) (1 + Math.sqrt(Ma / 100) * 100 / 50)");
	public PropertyFairyMagicInteger propertyHeight = new PropertyFairyMagicInteger("Height",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (In / 50),
		"(int) (In / 50)");
	public PropertyFairyMagicDouble propertyReach = new PropertyFairyMagicDouble("Reach",
		(p, Co, In, Vi, Lo, Ma, Et) -> 8 + Et / 20,
		"8 + Et / 20");
	public PropertyFairyMagicBoolean propertySavePlayer = new PropertyFairyMagicBoolean("Save Player",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 10,
		"Lo > 10");
	public PropertyFairyMagicBoolean propertyPoison = new PropertyFairyMagicBoolean("Poison",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 30,
		"Lo > 30");
	public PropertyFairyMagicInteger propertyCooltime = new PropertyFairyMagicInteger("Cooltime",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (40 / (1 + Co / 100)),
		"(int) (40 / (1 + Co / 100))");

	public FairyMagicNeedleFloor()
	{
		super("needleFloor", 1);
		properties.add(propertyDamage);
		properties.add(propertyDuration);
		properties.add(propertyRange);
		properties.add(propertyHeight);
		properties.add(propertyReach);
		properties.add(propertyPoison);
		properties.add(propertySavePlayer);
		properties.add(propertyCooltime);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		return onItemRightClickOnBlock(propertyReach.get(fairy), propertyCooltime.get(fairy), fairy, itemStack, playerIn, worldIn, r -> {

			// マナがない場合は失敗
			if (!ItemFairyBase.tryUseMana(playerIn, getCost(fairy))) return EnumActionResult.FAIL;

			BlockPos pos = r.getBlockPos();
			if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
				pos = pos.offset(r.sideHit);
			}

			int range = propertyRange.get(fairy);
			int height = propertyHeight.get(fairy);
			for (int x = -range; x <= range; x++) {
				for (int y = -height; y <= height; y++) {
					for (int z = -range; z <= range; z++) {
						BlockPos pos2 = pos.add(x, y, z);

						// 設置可能でないなら除外
						if (!worldIn.isBlockModifiable(playerIn, pos2)) continue;
						if (!playerIn.canPlayerEdit(pos2, EnumFacing.UP, itemStack)) continue;
						if (!ModuleFairy.blockNeedleFloor.canPlaceBlockAt(worldIn, pos2)) continue;

						// 設置
						if (!worldIn.isRemote) {
							worldIn.setBlockState(pos2, ModuleFairy.blockNeedleFloor.getDefaultState());

							TileEntity tileEntity = worldIn.getTileEntity(pos2);
							if (tileEntity instanceof TileEntityNeedleFloor) {
								TileEntityNeedleFloor tileEntityNeedleFloor = (TileEntityNeedleFloor) tileEntity;
								tileEntityNeedleFloor.data.damage = propertyDamage.get(fairy);
								tileEntityNeedleFloor.data.duration = propertyDuration.get(fairy);
								tileEntityNeedleFloor.data.poison = propertyPoison.get(fairy);
								tileEntityNeedleFloor.data.enemyOnly = propertySavePlayer.get(fairy);
							}
						}

						// エフェクト
						spawnParticle(EnumParticleTypes.SPELL, fairy, 1, worldIn, new Vec3d(pos2).addVector(0.5, 0.25, 0.5));

					}
				}
			}

			return EnumActionResult.SUCCESS;
		});
	}

}
