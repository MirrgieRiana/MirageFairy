package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import java.util.ArrayList;
import java.util.stream.Stream;

import mirrg.minecraft.mod.miragefairy.api.EnumFairyPotentialType;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicBoolean;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;

public class FairyMagicPotionSimple extends FairyMagicPotion
{

	public FairyMagicPotionSimple(
		String unlocalizedName,
		double costRate,
		String potionName,
		double potentialPerAmplifier,
		double durationPerPotential,
		EnumFairyPotentialType typeMagic)
	{
		super(unlocalizedName, costRate);

		ArrayList<EnumFairyPotentialType> types = new ArrayList<>();
		Stream.of(EnumFairyPotentialType.values()).forEach(types::add);
		types.remove(EnumFairyPotentialType.LO);
		types.remove(typeMagic);

		PropertyFairyMagicInteger pStrength = new PropertyFairyMagicInteger("Strength",
			(p, Co, In, Vi, Lo, Ma, Et) -> (int) (1 + typeMagic.choose(p) / potentialPerAmplifier),
			"(int) (1 + " + typeMagic.name() + " / " + potentialPerAmplifier + ")");
		PropertyFairyMagicInteger pDuration = new PropertyFairyMagicInteger("Duration",
			(p, Co, In, Vi, Lo, Ma, Et) -> (int) ((1000 + types.stream()
				.mapToDouble(t -> t.choose(p))
				.sum()) * durationPerPotential),
			"(int) ((1000 + " + String.join(" + ", types.stream()
				.map(t -> t.name())
				.toArray(String[]::new)) + ") * " + durationPerPotential + ")");
		PropertyFairyMagicBoolean pDoNotOverwrite = new PropertyFairyMagicBoolean("Do Not Overwrite",
			(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 10,
			"Lo > 10");
		properties.add(pStrength);
		properties.add(pDuration);
		properties.add(pDoNotOverwrite);
		potionEntries.add(new PotionEntry(potionName, pStrength, pDuration, pDoNotOverwrite));
	}

}
