package archives.tater.drinkingflask.registry;

import archives.tater.drinkingflask.DrinkingFlask;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class DrinkingFlaskSounds {
    private static SoundEvent register(Identifier id) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    private static SoundEvent register(String path) {
        return register(DrinkingFlask.id(path));
    }

    public static final SoundEvent FLASK_FILL = register("item.drinking_flask.fill");

    public static void init() {}
}
