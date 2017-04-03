package mirrg.minecraft.mod.miragefairy;

import java.util.ArrayList;

import mirrg.minecraft.mod.miragefairy.core.ModuleBase;
import mirrg.minecraft.mod.miragefairy.modules.fairy.ModuleFairy;
import mirrg.minecraft.mod.miragefairy.modules.main.ModuleMain;
import mirrg.minecraft.mod.miragefairy.modules.ore.ModuleOre;
import mirrg.minecraft.mod.miragefairy.modules.test.ModuleTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MirageFairyMod.MODID, version = MirageFairyMod.VERSION)
public class MirageFairyMod
{

	public static final String MODID = "miragefairy";
	public static final String VERSION = "0.0.1";

	private ArrayList<ModuleBase> modules = new ArrayList<>();

	public MirageFairyMod()
	{
		modules.add(new ModuleTest());
		modules.add(new ModuleMain());
		modules.add(new ModuleOre());
		modules.add(new ModuleFairy());
	}

	@EventHandler
	public void init(FMLPreInitializationEvent event)
	{
		modules.forEach(m -> m.preInit(event));
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		modules.forEach(m -> m.init(event));
	}

}
