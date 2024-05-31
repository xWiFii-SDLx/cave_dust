package net.lizistired.cavedust.utils.event;

import net.lizistired.cavedust.CaveDust;
import net.lizistired.cavedust.CaveDustParticleFactory;
import net.lizistired.cavedust.utils.ParticleRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "cavedust", bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventBusEvents {
    @SubscribeEvent
    public static void registerParticleTypes(final RegisterParticleProvidersEvent event) {
        MinecraftClient.getInstance().particleManager.registerFactory(ParticleRegistry.CAVE_DUST.get(), CaveDustParticleFactory.Factory::new);
    }
}
