package archives.tater.drinkingflask.datagen;

import archives.tater.drinkingflask.registry.DrinkingFlaskSounds;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;

public class SoundsGenerator extends FabricSoundsProvider {
    public SoundsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final String FLASK_FILL_SUBTITLE = "subtitles.item.drinkingflask.drinking_flask.fill";

    private static SoundTypeBuilder.RegistrationBuilder vanillaFile(String path) {
        return SoundTypeBuilder.RegistrationBuilder.create(SoundTypeBuilder.RegistrationType.FILE, Identifier.withDefaultNamespace(path));
    }

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
        exporter.add(DrinkingFlaskSounds.FLASK_FILL, SoundTypeBuilder.of()
                .subtitle(FLASK_FILL_SUBTITLE)
                .sound(vanillaFile("item/bottle/fill1"))
                .sound(vanillaFile("item/bottle/fill2"))
                .sound(vanillaFile("item/bottle/fill3"))
                .sound(vanillaFile("item/bottle/fill4"))
        );
    }

    @Override
    public String getName() {
        return "Sounds";
    }
}
