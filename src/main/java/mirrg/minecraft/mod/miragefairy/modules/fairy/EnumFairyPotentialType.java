package mirrg.minecraft.mod.miragefairy.modules.fairy;

import com.mojang.realmsclient.gui.ChatFormatting;

public enum EnumFairyPotentialType
{
	Co(ChatFormatting.DARK_GRAY),
	In(ChatFormatting.GOLD),
	Vi(ChatFormatting.DARK_GREEN),
	Lo(ChatFormatting.AQUA),
	Ma(ChatFormatting.LIGHT_PURPLE),
	Et(ChatFormatting.WHITE);

	public final ChatFormatting color;

	private EnumFairyPotentialType(ChatFormatting color)
	{
		this.color = color;
	}

	public double choose(double[] p)
	{
		return p[ordinal()];
	}

}
