package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import java.util.ArrayList;

import mirrg.minecraft.mod.miragefairy.api.IFairy;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicBoolean;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PotionEntry
{

	private String name;
	private PropertyFairyMagicInteger propertyStrength;
	private PropertyFairyMagicInteger propertyDuration;
	private PropertyFairyMagicBoolean propertyDoNotOverwrite;

	public PotionEntry(
		String name,
		PropertyFairyMagicInteger propertyStrength,
		PropertyFairyMagicInteger propertyDuration,
		PropertyFairyMagicBoolean propertyDoNotOverwrite)
	{
		this.name = name;
		this.propertyStrength = propertyStrength;
		this.propertyDuration = propertyDuration;
		this.propertyDoNotOverwrite = propertyDoNotOverwrite;
	}

	public boolean test(ArrayList<Runnable> listeners, IFairy fairy, EntityLivingBase entityLiving)
	{
		Potion potion = Potion.REGISTRY.getObject(new ResourceLocation(name));
		if (potion == null) return false;

		int amplifier = propertyStrength.get(fairy) - 1;
		if (amplifier < 0) return false;
		int duration = propertyDuration.get(fairy);
		if (duration <= 0) return false;

		// 既に上位版が発動していたら除く
		if (propertyDoNotOverwrite.get(fairy)) {
			PotionEffect potionEffect = entityLiving.getActivePotionEffect(potion);
			if (potionEffect != null) {
				if (potionEffect.getAmplifier() > amplifier) return false;
				if (potionEffect.getAmplifier() == amplifier && potionEffect.getDuration() >= duration) return false;
			}
		}

		// 効果の生成
		listeners.add(() -> entityLiving.addPotionEffect(new PotionEffect(potion, duration, amplifier)));

		return true;
	}

}
