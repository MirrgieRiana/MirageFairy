package mirrg.minecraft.mod.miragefairy.modules.main;

public enum EnumChestQuality
{
	NONE(false, 0),
	LOW(true, 0.1),
	NORMAL(true, 0.3),
	HIGH(true, 0.8);

	public final boolean enable;
	public final double ratioPerRate;

	private EnumChestQuality(boolean enable, double ratioPerRate)
	{
		this.enable = enable;
		this.ratioPerRate = ratioPerRate;
	}

}
