package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import mirrg.minecraft.mod.miragefairy.api.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicBoolean;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicDouble;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FairyMagicMagicArrow extends FairyMagic
{

	public PropertyFairyMagicDouble propertyDamage = new PropertyFairyMagicDouble("Damage",
		(p, Co, In, Vi, Lo, Ma, Et) -> 1.5 + (In + Ma) / 33,
		"1.5 + (In + Ma) / 33");
	public PropertyFairyMagicDouble propertyVelocity = new PropertyFairyMagicDouble("Velocity",
		(p, Co, In, Vi, Lo, Ma, Et) -> 1.5 + Co / 100,
		"1.5 + Co / 100");
	public PropertyFairyMagicInteger propertyChargeTime = new PropertyFairyMagicInteger("Charge Time",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (20 / (1 + Co / 100)),
		"(int) (20 / (1 + Co / 100))");
	public PropertyFairyMagicInteger propertyWays = new PropertyFairyMagicInteger("Ways",
		(p, Co, In, Vi, Lo, Ma, Et) -> 1 + (int) (Et / 10),
		"1 + (int) (Et / 10)");
	public PropertyFairyMagicInteger propertyKnockbackStrength = new PropertyFairyMagicInteger("Knockback Strength",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (Vi / 50),
		"(int) (Vi / 50)");
	public PropertyFairyMagicBoolean propertyCritical = new PropertyFairyMagicBoolean("Critical",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 10,
		"Lo > 10");
	public PropertyFairyMagicBoolean propertyPoison = new PropertyFairyMagicBoolean("Poison",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 30,
		"Lo > 30");
	public PropertyFairyMagicBoolean propertyFlame = new PropertyFairyMagicBoolean("Flame",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 50,
		"Lo > 50");
	public PropertyFairyMagicBoolean propertyWither = new PropertyFairyMagicBoolean("Wither",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 70,
		"Lo > 70");
	public PropertyFairyMagicInteger propertySlowness = new PropertyFairyMagicInteger("Slowness",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (Lo / 20),
		"(int) (Lo / 20)");

	public FairyMagicMagicArrow()
	{
		super("magicArrow", 1);
		properties.add(propertyDamage);
		properties.add(propertyVelocity);
		properties.add(propertyChargeTime);
		properties.add(propertyWays);
		properties.add(propertyKnockbackStrength);
		properties.add(propertyCritical);
		properties.add(propertyPoison);
		properties.add(propertyFlame);
		properties.add(propertyWither);
		properties.add(propertySlowness);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public void onPlayerStoppedUsing(IFairy fairy, ItemFairyBase itemFairy, EntityPlayer player, EnumHand hand, ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		int ways = propertyWays.get(fairy);
		float f = Math.min((float) (getMaxItemUseDuration(stack) - timeLeft) / propertyChargeTime.get(fairy), 1);

		if (f < 0.2) return;

		if (!worldIn.isRemote) {
			for (int i = 0; i < ways; i++) {
				ItemStack itemstack = new ItemStack(Items.ARROW);
				ItemArrow itemarrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.ARROW));

				EntityTippedArrow entityarrow = new EntityTippedArrow(worldIn, player);
				float angleOffset = 3;
				entityarrow.setAim(
					player,
					player.rotationPitch,
					player.rotationYaw + i * angleOffset - (ways - 1) * angleOffset / 2, 0.0F,
					(float) propertyVelocity.getAsDouble(fairy) * f,
					1.0F);
				entityarrow.setDamage(propertyDamage.get(fairy) / propertyVelocity.getAsDouble(fairy));
				entityarrow.setIsCritical(propertyCritical.get(fairy));
				if (propertyPoison.get(fairy)) entityarrow.addEffect(new PotionEffect(MobEffects.POISON, 20 * 10));
				if (propertyFlame.get(fairy)) entityarrow.setFire(20 * 20);
				if (propertyWither.get(fairy)) entityarrow.addEffect(new PotionEffect(MobEffects.WITHER, 20 * 30));
				if (propertySlowness.get(fairy) > 0) entityarrow.addEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 10, propertySlowness.get(fairy) - 1));
				entityarrow.setKnockbackStrength(propertyKnockbackStrength.get(fairy));
				entityarrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
				worldIn.spawnEntity(entityarrow);
			}
		}

		spawnParticle(fairy, 10, worldIn, new Vec3d(player.posX, player.posY + player.height / 2, player.posZ));
		play(ModuleMain.soundFairyWandUse, player, worldIn);
		worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS,
			1.0F, 1.0F / (worldIn.rand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
	}

	@Override
	public void onUsingTick(IFairy fairy, ItemFairyBase itemFairyBase, EntityPlayer player, ItemStack stack, EntityLivingBase player2, int count)
	{
		if (getMaxItemUseDuration(stack) - count == propertyChargeTime.get(fairy)) play(ModuleMain.soundFairyWandCharge, player, player.world);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if (!ItemFairyBase.tryUseMana(playerIn, getCost(fairy))) return new ActionResult<>(EnumActionResult.FAIL, itemStack);
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

}
