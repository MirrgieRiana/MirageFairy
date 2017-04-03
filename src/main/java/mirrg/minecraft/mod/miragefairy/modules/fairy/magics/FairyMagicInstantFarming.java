package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import mirrg.minecraft.mod.miragefairy.modules.fairy.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicBoolean;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FairyMagicInstantFarming extends FairyMagic
{

	public PropertyFairyMagicInteger propertyRange = new PropertyFairyMagicInteger("Range",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) ((In + Vi) / 50),
		"(int) ((In + Vi) / 50)");
	public PropertyFairyMagicInteger propertyHeight = new PropertyFairyMagicInteger("Height",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (Ma / 100),
		"(int) (Ma / 100)");
	public PropertyFairyMagicInteger propertyMoisture = new PropertyFairyMagicInteger("Moisture",
		(p, Co, In, Vi, Lo, Ma, Et) -> Math.min((int) (Et / 10), 7),
		"Math.min((int) (Et / 10), 7)");
	public PropertyFairyMagicBoolean propertyForce = new PropertyFairyMagicBoolean("Force",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 30,
		"Lo > 30");
	public PropertyFairyMagicInteger propertyCooltime = new PropertyFairyMagicInteger("Cooltime",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (40 / (1 + Co / 50)),
		"(int) (40 / (1 + Co / 50))");

	public FairyMagicInstantFarming()
	{
		super("instantFarming", 1);
		properties.add(propertyRange);
		properties.add(propertyHeight);
		properties.add(propertyMoisture);
		properties.add(propertyForce);
		properties.add(propertyCooltime);
	}

	@Override
	public EnumActionResult onItemUse(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX,
		float hitY, float hitZ)
	{
		if (!ItemFairyBase.tryUseMana(player, getCost(fairy))) return EnumActionResult.FAIL;

		boolean flag = false;

		int range = propertyRange.get(fairy);
		int height = propertyHeight.get(fairy);
		for (int y = -height; y <= height; y++) {
			for (int x = -range; x <= range; x++) {
				for (int z = -range; z <= range; z++) {
					BlockPos pos2 = pos.add(x, y, z);
					if (!player.canPlayerEdit(pos2.offset(facing), facing, itemStack)) continue;
					IBlockState blockState = worldIn.getBlockState(pos2);
					Block block = blockState.getBlock();

					if (propertyForce.get(fairy)) {
						if (!worldIn.getBlockState(pos2.up()).getBlock().isReplaceable(worldIn, pos2.up())) continue;
					} else {
						if (!worldIn.isAirBlock(pos2.up())) continue;
					}

					if (setBlock(worldIn, pos2, blockState, block, fairy)) {
						spawnParticle(EnumParticleTypes.SPELL, fairy, 2, worldIn, new Vec3d(pos2).addVector(0.5, 1.1, 0.5));
						worldIn.destroyBlock(pos2.up(), true);
						flag = true;
					}
				}
			}
		}

		if (flag) worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

		addCooltime(player, propertyCooltime.get(fairy));
		play(ModuleMain.soundFairyWandUse, player, worldIn);
		return EnumActionResult.SUCCESS;
	}

	protected boolean setBlock(World worldIn, BlockPos pos, IBlockState blockState, Block block, IFairy fairy)
	{
		if (block == Blocks.GRASS) {
			if (!worldIn.isRemote) {
				worldIn.setBlockState(pos, Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, propertyMoisture.get(fairy)), 11);
			}
			return true;
		} else if (block == Blocks.GRASS_PATH) {
			if (!worldIn.isRemote) {
				worldIn.setBlockState(pos, Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, propertyMoisture.get(fairy)), 11);
			}
			return true;
		} else if (block == Blocks.DIRT) {
			DirtType variant = blockState.getValue(BlockDirt.VARIANT);
			if (variant == DirtType.DIRT) {
				if (!worldIn.isRemote) {
					worldIn.setBlockState(pos, Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, propertyMoisture.get(fairy)), 11);
				}
				return true;
			} else if (variant == DirtType.COARSE_DIRT) {
				if (!worldIn.isRemote) {
					worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), 11);
				}
				return true;
			}
		}
		return false;
	}

}
