package mirrg.minecraft.mod.miragefairy.modules.fairy.magics;

import mirrg.minecraft.mod.miragefairy.MirageFairyMod;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicBoolean;
import mirrg.minecraft.mod.miragefairy.modules.fairy.magic.PropertyFairyMagicInteger;

public class FairyMagicHaste extends FairyMagicPotion
{

	public PropertyFairyMagicInteger pSpeed = new PropertyFairyMagicInteger("Speed",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (1 + Ma / 50),
		"(int) (1 + Ma / 50)");
	public PropertyFairyMagicInteger pStepHeight = new PropertyFairyMagicInteger("Step Height",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) (Et / 50),
		"(int) (Et / 50)");
	public PropertyFairyMagicInteger pDuration = new PropertyFairyMagicInteger("Duration",
		(p, Co, In, Vi, Lo, Ma, Et) -> (int) ((1000 + In + Vi + Co) * 5),
		"(int) ((1000 + In + Vi + Co) * 5)");
	public PropertyFairyMagicBoolean pDoNotOverwrite = new PropertyFairyMagicBoolean("Do Not Overwrite",
		(p, Co, In, Vi, Lo, Ma, Et) -> Lo > 10,
		"Lo > 10");

	public FairyMagicHaste()
	{
		super("haste", 1);
		properties.add(pSpeed);
		properties.add(pStepHeight);
		properties.add(pDuration);
		properties.add(pDoNotOverwrite);
		potionEntries.add(new PotionEntry("speed", pSpeed, pDuration, pDoNotOverwrite));
		potionEntries.add(new PotionEntry(MirageFairyMod.MODID + ":stepHeight", pStepHeight, pDuration, pDoNotOverwrite));
	}

}
