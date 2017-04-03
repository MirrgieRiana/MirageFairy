package mirrg.minecraft.mod.miragefairy.modules.main;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class ParticleFairy extends Particle
{

	public ParticleFairy(World worldIn, double posXIn, double posYIn, double posZIn)
	{
		super(worldIn, posXIn, posYIn, posZIn);
		particleScale /= 2;
		particleMaxAge *= 4;
	}

	public ParticleFairy(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		particleScale /= 2;
		particleMaxAge *= 4;
	}

	@Override
	public int getFXLayer()
	{
		return 1;
	}

}
