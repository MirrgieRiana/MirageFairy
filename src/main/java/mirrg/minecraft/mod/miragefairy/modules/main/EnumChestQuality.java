package mirrg.minecraft.mod.miragefairy.modules.main;

public enum EnumChestQuality
{
	NONE(false, 0, 0),
	LOW(true, 1, 0.1),
	NORMAL(true, 2, 0.3),
	HIGH(true, 3, 0.8);

	public final boolean enable;
	public final int quality;
	public final double ratioPerRate;

	private EnumChestQuality(boolean enable, int quality, double ratioPerRate)
	{
		this.enable = enable;
		this.quality = quality;
		this.ratioPerRate = ratioPerRate;
	}

}
