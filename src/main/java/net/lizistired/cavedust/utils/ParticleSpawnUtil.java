package net.lizistired.cavedust.utils;

import net.lizistired.cavedust.CaveDustConfig;
import net.lizistired.cavedust.ConfigForge;
import net.lizistired.cavedust.mixin.ClientWorldAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static net.minecraft.world.biome.BiomeKeys.LUSH_CAVES;

public class ParticleSpawnUtil {
    private static float timer;
    public static boolean shouldParticlesSpawn;

    /**
     * Returns true if particles should spawn (uses particle position instead of player).
     * @param client MinecraftClient
     * @param pos BlockPos
     * @return boolean
     */
    public static boolean shouldParticlesSpawn(MinecraftClient client, BlockPos pos) {

        //checks if the config is enabled, if the game isn't paused, if the world is valid, if the particle is valid and if the player isn't in a lush caves biome
        if (!ConfigForge.CAVE_DUST_ENABLED.get()
                || client.isPaused()
                || client.world == null
                || !client.world.getDimension().bedWorks()
                || (client.world.getBottomY() > pos.getY())
                //|| client.world.getBiome(Objects.requireNonNull(pos)).matchesKey(LUSH_CAVES))
                || client.world.getBiome(Objects.requireNonNull(pos)).matchesKey(LUSH_CAVES))

        {
            timer = 0;
            shouldParticlesSpawn = false;
            return false;
        }
        if(!ConfigForge.SUPERFLAT_STATUS.get()) {
            if (((ClientWorldAccessor) client.world.getLevelProperties()).getFlatWorld()) {
                return false;
            }
        }

        World world = client.world;
        int seaLevel = world.getSeaLevel();

        if (!client.player.clientWorld.isSkyVisible(pos)) {
            if (pos.getY() + 2 < seaLevel){
                timer = timer + 1;
                if (timer > 10){
                    timer = 10;
                    shouldParticlesSpawn = true;
                    return true;
                }
            }
        }
        shouldParticlesSpawn = false;
        return false;
    }
}
