package net.lizistired.cavedust.utils;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.lizistired.cavedust.CaveDust.MOD_ID;
import static net.minecraftforge.registries.ForgeRegistries.Keys.PARTICLE_TYPES;

public class ParticleRegistry {
    private final static DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MOD_ID);
    public final static RegistryObject<DefaultParticleType> CAVE_DUST = PARTICLES.register("cave_dust", () -> new DefaultParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }
}
