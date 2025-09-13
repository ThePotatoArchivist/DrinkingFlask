package archives.tater.drinkingflask.registry;

import archives.tater.drinkingflask.DrinkingFlask;
import archives.tater.drinkingflask.component.FlaskContentsComponent;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DrinkingFlaskComponents {

    private static <T> ComponentType<T> register(Identifier id, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec, boolean cache) {
        var typeBuilder = ComponentType.<T>builder().codec(codec).packetCodec(packetCodec);
        if (cache) typeBuilder.cache();
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id, typeBuilder.build());
    }

    private static <T> ComponentType<T> register(String path, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec, boolean cache) {
        return register(DrinkingFlask.id(path), codec, packetCodec, cache);
    }

    public static final ComponentType<Integer> FLASK_CAPACITY = register("flask_capacity", Codec.intRange(1, 99), PacketCodecs.INTEGER, false);
    public static final ComponentType<FlaskContentsComponent> FLASK_CONTENTS = register("flask_contents", FlaskContentsComponent.CODEC, FlaskContentsComponent.PACKET_CODEC, true);

    public static void init() {

    }
}
