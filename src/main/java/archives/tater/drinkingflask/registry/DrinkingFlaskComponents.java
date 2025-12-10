package archives.tater.drinkingflask.registry;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.component.FlaskContentsComponent;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public class DrinkingFlaskComponents {

    private static <T> DataComponentType<T> register(Identifier id, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> packetCodec, boolean cache) {
        var typeBuilder = DataComponentType.<T>builder().persistent(codec).networkSynchronized(packetCodec);
        if (cache) typeBuilder.cacheEncoding();
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, typeBuilder.build());
    }

    private static <T> DataComponentType<T> register(String path, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> packetCodec, boolean cache) {
        return register(DrinkingFlask.id(path), codec, packetCodec, cache);
    }

    public static final DataComponentType<Integer> FLASK_CAPACITY = register("flask_capacity", Codec.intRange(1, 99), ByteBufCodecs.INT, false);
    public static final DataComponentType<FlaskContentsComponent> FLASK_CONTENTS = register("flask_contents", FlaskContentsComponent.CODEC, FlaskContentsComponent.PACKET_CODEC, true);

    public static void init() {

    }
}
