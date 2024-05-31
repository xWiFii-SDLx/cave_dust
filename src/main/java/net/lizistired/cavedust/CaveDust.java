package net.lizistired.cavedust;

//minecraft imports
import dev.architectury.event.events.client.ClientTickEvent;
import net.lizistired.cavedust.utils.ParticleRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
//other imports
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//java imports
import java.nio.file.Path;
//static imports
import static net.lizistired.cavedust.CaveDust.MOD_ID;
import static net.lizistired.cavedust.utils.MathHelper.*;
import static net.lizistired.cavedust.utils.MathHelper.generateRandomDouble;
import static net.lizistired.cavedust.utils.ParticleRegistry.CAVE_DUST;
import static net.lizistired.cavedust.utils.ParticleSpawnUtil.shouldParticlesSpawn;

@Mod(MOD_ID)
public class CaveDust {
	//logger
	public static final String MOD_ID = "cavedust";
	public static final Logger LOGGER = LoggerFactory.getLogger("cavedust");
	//make class static
	//config assignment


	public static int PARTICLE_AMOUNT = 0;

	public static int WHITE_ASH_ID;

	public CaveDust() {

		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ParticleRegistry.register(eventBus);

		eventBus.addListener(this::clientSetup);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigForge.SPEC, "cavedust.toml");
		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new CaveDustConfigScreen(Text.of("Cave Dust Config"), screen)));
		//register end client tick to create cave dust function, using end client tick for async


		System.out.println(FMLPaths.CONFIGDIR.get().toAbsolutePath().normalize().toString());
		ClientTickEvent.CLIENT_POST.register(CaveDust::createCaveDust);

		MinecraftForge.EVENT_BUS.register(this);
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		WHITE_ASH_ID = Registries.PARTICLE_TYPE.getRawId(CAVE_DUST.get());
	}

	private static void createCaveDust(MinecraftClient client) {

		//ensure world is not null
		if (client.world == null) return;
		World world = client.world;
		//LOGGER.info(String.valueOf(((ClientWorldAccessor) client.world.getLevelProperties()).getFlatWorld()));
		// )
		double probabilityNormalized = normalize(ConfigForge.LOWER_LIMIT.get(), ConfigForge.UPPER_LIMIT.get(), client.player.getBlockY());
		PARTICLE_AMOUNT = (int) (probabilityNormalized * ConfigForge.PARTICLE_MULTIPLIER.get() * ConfigForge.PARTICLE_MULTIPLIER_MULTIPLIER.get());

		for (int i = 0; i < PARTICLE_AMOUNT; i++) {
			try {
				int x = (int) (client.player.getPos().getX() + (int) generateRandomDouble(ConfigForge.DIMENSION_WIDTH.get() *-1, ConfigForge.DIMENSION_WIDTH.get()));
				int y = (int) (client.player.getEyePos().getY() + (int) generateRandomDouble(ConfigForge.DIMENSION_HEIGHT.get() *-1, ConfigForge.DIMENSION_HEIGHT.get()));
				int z = (int) (client.player.getPos().getZ() + (int) generateRandomDouble(ConfigForge.DIMENSION_WIDTH.get() *-1, ConfigForge.DIMENSION_WIDTH.get()));
				double miniX = (x + Math.random());
				double miniY = (y + Math.random());
				double miniZ = (z + Math.random());
				BlockPos particlePos = new BlockPos(x, y, z);

				if (shouldParticlesSpawn(client, particlePos)) {
					if (client.world.getBlockState(particlePos).isAir()) {
						world.addParticle(CaveDustConfigScreen.getParticle(), miniX, miniY, miniZ, 0, 0, 0);
					}
				}
			}
			catch (NullPointerException e) {
				LOGGER.error(String.valueOf(e));
				//getConfig().setParticleID(WHITE_ASH_ID);
			}
		}
	}
}
