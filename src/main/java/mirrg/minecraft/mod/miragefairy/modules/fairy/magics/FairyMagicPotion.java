package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import java.util.ArrayList;

import mirrg.minecraft.mod.miragefairy.modules.fairy.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ItemFairyBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.FairyMagic;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FairyMagicPotion extends FairyMagic
{

	public ArrayList<PotionEntry> potionEntries = new ArrayList<>();

	public FairyMagicPotion(String unlocalizedName, double costRate)
	{
		super(unlocalizedName, costRate);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(IFairy fairy, ItemStack itemStack, ItemFairyBase itemFairy, World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ArrayList<Runnable> listeners = new ArrayList<>();

		// 実行可能か評価
		boolean flag = false;
		for (PotionEntry potionEntry : potionEntries) {
			if (potionEntry.test(listeners, fairy, playerIn)) {
				flag = true;
			}
		}
		if (!flag) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

		// マナの消費
		if (!ItemFairyBase.tryUseMana(playerIn, getCost(fairy))) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

		// 適用
		listeners.forEach(Runnable::run);

		// エフェクト
		spawnParticle(fairy, 10, worldIn, new Vec3d(playerIn.posX, playerIn.posY + playerIn.height / 2, playerIn.posZ));
		play(ModuleMain.soundFairyWandUse, playerIn, worldIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

}
