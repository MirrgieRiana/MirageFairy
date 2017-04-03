package mirrg.minecraft.mod.miragefairy.modules.fairy.magic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.mojang.realmsclient.gui.ChatFormatting;

import mirrg.minecraft.mod.miragefairy.modules.fairy.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public abstract class FairyMagic
{

	public final String unlocalizedName;
	public final double costRate;

	public FairyMagic(String unlocalizedName, double costRate)
	{
		this.unlocalizedName = unlocalizedName;
		this.costRate = costRate;
	}

	public String getLocalizedName()
	{
		return I18n.translateToLocal("fairyMagic." + unlocalizedName + ".name").trim();
	}

	public String getInformation(IFairy fairyOperator)
	{
		return "" + getCost(fairyOperator);
	}

	public long getCost(IFairy fairy)
	{
		return (long) (costRate * fairy.getCost());
	}

	//

	public ArrayList<PropertyFairyMagic<?>> properties = new ArrayList<>();

	public void addInformation(Optional<IFairy> oFairy, List<String> tooltip, boolean advanced)
	{
		properties.forEach(p -> {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("  ");
			stringBuilder.append(p.name);
			stringBuilder.append(": ");
			stringBuilder.append(p.getColor(oFairy.orElse(IFairy.DUMMY)));
			stringBuilder.append(p.toString(oFairy.orElse(IFairy.DUMMY)));
			if (advanced) {
				stringBuilder.append(ChatFormatting.GRAY);
				stringBuilder.append(" (");
				stringBuilder.append(p.hint);
				stringBuilder.append(")");
			}
			tooltip.add(stringBuilder.toString());
		});
	}

	//

	public ActionResult<ItemStack> onItemRightClick(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		return new ActionResult<>(EnumActionResult.PASS, itemStack);
	}

	protected RayTraceResult rayTrace(IFairy fairy, ItemStack itemStack, EntityPlayer playerIn, World worldIn, double distance, boolean useLiquids)
	{
		float rotationPitch = playerIn.rotationPitch;
		float rotationYaw = playerIn.rotationYaw;
		double posX = playerIn.posX;
		double posY = playerIn.posY + playerIn.getEyeHeight();
		double posZ = playerIn.posZ;
		Vec3d vec3d = new Vec3d(posX, posY, posZ);
		float f2 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-rotationPitch * 0.017453292F);
		float f5 = MathHelper.sin(-rotationPitch * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		Vec3d vec3d1 = vec3d.addVector(f6 * distance, f5 * distance, f7 * distance);
		return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}

	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.NONE;
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 0;
	}

	public void onUsingTick(IFairy fairy, ItemFairyBase itemFairyBase, EntityPlayer player, ItemStack stack, EntityLivingBase player2, int count)
	{

	}

	public void onPlayerStoppedUsing(IFairy fairy, ItemFairyBase itemFairy, EntityPlayer player, EnumHand hand, ItemStack stack, World worldIn, EntityLivingBase entityLiving,
		int timeLeft)
	{

	}

	public EnumActionResult onItemUse(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX,
		float hitY, float hitZ)
	{
		return EnumActionResult.PASS;
	}

	//

	protected void play(SoundEvent sound, EntityPlayer player, World worldIn)
	{
		worldIn.playSound(player, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
	}

	protected void addCooltime(EntityPlayer playerIn, int time)
	{
		for (ResourceLocation key : Item.REGISTRY.getKeys()) {
			Item item = Item.REGISTRY.getObject(key);
			if (item instanceof ItemFairyBase) {
				ItemFairyBase itemFairy = (ItemFairyBase) item;
				if (itemFairy.canUseMagic()) {
					playerIn.getCooldownTracker().setCooldown(item, time);
				}
			}
		}
	}

	protected boolean isSolid(BlockPos pos, World world)
	{
		IBlockState blockState = world.getBlockState(pos);
		return blockState.getMaterial().blocksMovement() &&
			!blockState.getBlock().isLeaves(blockState, world, pos) &&
			!blockState.getBlock().isFoliage(world, pos);
	}

	protected void spawnParticle(IFairy fairy, int count, World worldIn, Vec3d vec3d)
	{
		spawnParticle(EnumParticleTypes.SPELL_MOB, fairy, count, worldIn, vec3d);
	}

	protected void spawnParticle(EnumParticleTypes particleType, IFairy fairy, int count, World worldIn, Vec3d vec3d)
	{
		int color = fairy.getColorHair();
		for (int j = 0; j < count; j++) {
			// TODO particleTypeがSPELLかSPELL_MOB以外だとSPELL_INSTANTでも色指定が行われない
			// playEventならできるがBlockPosになる
			worldIn.spawnParticle(particleType, vec3d.xCoord, vec3d.yCoord, vec3d.zCoord,
				((color >> 16) & 0xff) / 255.0, ((color >> 8) & 0xff) / 255.0, ((color >> 0) & 0xff) / 255.0,
				new int[0]);
		}
	}

	protected ActionResult<ItemStack> onItemRightClickOnBlock(
		double reach, int cooltime,
		IFairy fairy, ItemStack itemStack, EntityPlayer player, World world,
		Function<RayTraceResult, EnumActionResult> success)
	{

		// ブロックをタゲれない時は失敗
		RayTraceResult rayTraceResult = rayTrace(fairy, itemStack, player, world, reach, false);
		if (rayTraceResult == null || rayTraceResult.typeOfHit != Type.BLOCK) {

			// 失敗エフェクト
			float f2 = MathHelper.cos(-player.rotationYaw * 0.017453292F - (float) Math.PI);
			float f3 = MathHelper.sin(-player.rotationYaw * 0.017453292F - (float) Math.PI);
			float f4 = -MathHelper.cos(-player.rotationPitch * 0.017453292F);
			float f5 = MathHelper.sin(-player.rotationPitch * 0.017453292F);
			float f6 = f3 * f4;
			float f7 = f2 * f4;
			spawnParticle(EnumParticleTypes.SPELL_INSTANT, fairy, 10, world, new Vec3d(
				player.posX + f6 * reach,
				player.posY + player.getEyeHeight() + f5 * reach,
				player.posZ + f7 * reach));

			return new ActionResult<>(EnumActionResult.FAIL, itemStack);
		}

		// 試行
		EnumActionResult actionResult = success.apply(rayTraceResult);

		// 成功時
		if (actionResult == EnumActionResult.SUCCESS) {
			addCooltime(player, cooltime);
			play(ModuleMain.soundFairyWandUse, player, world);
		}

		return new ActionResult<>(actionResult, itemStack);
	}

}
