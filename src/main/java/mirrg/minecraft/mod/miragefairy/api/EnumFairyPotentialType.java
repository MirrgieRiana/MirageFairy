package mirrg.minecraft.mod.miragefairy.api;

import com.mojang.realmsclient.gui.ChatFormatting;

public enum EnumFairyPotentialType
{
	CO(ChatFormatting.DARK_GRAY),
	IN(ChatFormatting.GOLD),
	VI(ChatFormatting.DARK_GREEN),
	LO(ChatFormatting.AQUA),
	MA(ChatFormatting.LIGHT_PURPLE),
	ET(ChatFormatting.WHITE);

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
