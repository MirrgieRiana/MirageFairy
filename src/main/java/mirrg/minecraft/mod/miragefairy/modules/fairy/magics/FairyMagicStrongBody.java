package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicBoolean;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;

public class FairyMagicStrongBody extends FairyMagicPotion
{

	public PropertyFairyMagicInteger pStrength = new PropertyFairyMagicInteger("Strength",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (1 + Ma / 50),
		"(int) (1 + Ma / 50)");
	public PropertyFairyMagicInteger pResistance = new PropertyFairyMagicInteger("Resistance",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (1 + Et / 50),
		"(int) (1 + Et / 50)");
	public PropertyFairyMagicInteger pDuration = new PropertyFairyMagicInteger("Duration",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) ((1000 + In + Vi + Co) * 5),
		"(int) ((1000 + In + Vi + Co) * 5)");
	public PropertyFairyMagicBoolean pDoNotOverwrite = new PropertyFairyMagicBoolean("Do Not Overwrite",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 10,
		"Lo > 10");

	public FairyMagicStrongBody()
	{
		super("strongBody", 1);
		properties.add(pStrength);
		properties.add(pResistance);
		properties.add(pDuration);
		properties.add(pDoNotOverwrite);
		potionEntries.add(new PotionEntry("strength", pStrength, pDuration, pDoNotOverwrite));
		potionEntries.add(new PotionEntry("resistance", pResistance, pDuration, pDoNotOverwrite));
	}

}
