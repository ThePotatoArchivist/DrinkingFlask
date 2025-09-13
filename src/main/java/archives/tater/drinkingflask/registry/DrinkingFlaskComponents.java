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
    public static final ComponentType<Integer> FLASK_CAPACITY = register("flask_capacity", Codec.intRange(1, 99), PacketCodecs.INTEGER);
    public static final ComponentType<FlaskContentsComponent> FLASK_CONTENTS = register("flask_contents", FlaskContentsComponent.CODEC, FlaskContentsComponent.PACKET_CODEC);

    public static void init() {

    }

    private static <T> ComponentType<T> register(Identifier id, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id, ComponentType.<T>builder().codec(codec).packetCodec(packetCodec).build());
    }

    private static <T> ComponentType<T> register(String path, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
        return register(DrinkingFlask.id(path), codec, packetCodec);
    }
}
