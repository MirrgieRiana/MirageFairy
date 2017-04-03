package mirrg.minecraft.mod.miragefairy.colormaker;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import mirrg.minecraft.mod.miragefairy.MirageFairyMod;

public class ImageLoader
{

	public static ArrayList<ArrayList<SimpleEntry<BufferedImage, SimpleEntry<Boolean, String>>>> load() throws IOException
	{
		ArrayList<ArrayList<SimpleEntry<BufferedImage, SimpleEntry<Boolean, String>>>> array1 = new ArrayList<>();
		{
			ArrayList<SimpleEntry<BufferedImage, SimpleEntry<Boolean, String>>> array2 = new ArrayList<>();
			{
				array2.add(new SimpleEntry<>(load("fairy_0"), new SimpleEntry<>(false, "skin")));
				array2.add(new SimpleEntry<>(load("fairy_1"), new SimpleEntry<>(true, "#00BE00")));
				array2.add(new SimpleEntry<>(load("fairy_2"), new SimpleEntry<>(false, "darker")));
				array2.add(new SimpleEntry<>(load("fairy_3"), new SimpleEntry<>(false, "brighter")));
				array2.add(new SimpleEntry<>(load("fairy_4"), new SimpleEntry<>(false, "hair")));
			}
			array1.add(array2);
		}
		{
			ArrayList<SimpleEntry<BufferedImage, SimpleEntry<Boolean, String>>> array2 = new ArrayList<>();
			{
				array2.add(new SimpleEntry<>(load("fairy_spirit_0"), new SimpleEntry<>(false, "darker")));
				array2.add(new SimpleEntry<>(load("fairy_spirit_1"), new SimpleEntry<>(false, "skin")));
				array2.add(new SimpleEntry<>(load("fairy_spirit_2"), new SimpleEntry<>(false, "brighter")));
				array2.add(new SimpleEntry<>(load("fairy_spirit_3"), new SimpleEntry<>(false, "hair")));
			}
			array1.add(array2);
		}
		{
			ArrayList<SimpleEntry<BufferedImage, SimpleEntry<Boolean, String>>> array2 = new ArrayList<>();
			{
				array2.add(new SimpleEntry<>(load("magic_sphere_0"), new SimpleEntry<>(false, "darker")));
				array2.add(new SimpleEntry<>(load("magic_sphere_1"), new SimpleEntry<>(false, "hair")));
				array2.add(new SimpleEntry<>(load("magic_sphere_2"), new SimpleEntry<>(false, "skin")));
				array2.add(new SimpleEntry<>(load("magic_sphere_3"), new SimpleEntry<>(false, "brighter")));
			}
			array1.add(array2);
		}
		return array1;
	}

	public static BufferedImage load(String name) throws IOException
	{
		URL url = ImageLoader.class.getClassLoader().getResource("assets/" + MirageFairyMod.MODID + "/textures/items/" + name + ".png");
		return ImageIO.read(url);
	}

}
