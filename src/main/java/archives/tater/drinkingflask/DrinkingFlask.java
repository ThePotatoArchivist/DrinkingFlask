package archives.tater.drinkingflask;

import archives.tater.drinkingflask.item.DrinkingFlaskItem;
import archives.tater.drinkingflask.item.FlaskContentsComponent;
import archives.tater.drinkingflask.item.PhantomDrinkingFlaskItem;
import archives.tater.drinkingflask.mixin.CuttingRecipeSerializerInvoker;
import archives.tater.drinkingflask.recipe.FlaskRemainderRecipe;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class DrinkingFlask implements ModInitializer {
	public static final String MOD_ID = "drinkingflask";

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static <T extends Recipe<?>> RecipeType<T> registerRecipeType(Identifier id) {
        return Registry.register(Registries.RECIPE_TYPE, id, new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });
    }

    private static <T> ComponentType<T> register(Identifier id, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id, ComponentType.<T>builder().codec(codec).packetCodec(packetCodec).build());
    }

    private static <T> ComponentType<T> register(String path, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
        return register(id(path), codec, packetCodec);
    }

    private static Item register(Identifier id, Function<Item.Settings, Item> item, Item.Settings settings) {
        return Registry.register(Registries.ITEM, id, item.apply(settings));
    }

    private static Item register(String path, Function<Item.Settings, Item> item, Item.Settings settings) {
        return register(id(path), item, settings);
    }

    private static TagKey<Item> tagOf(Identifier id) {
        return TagKey.of(RegistryKeys.ITEM, id);
    }

    private static TagKey<Item> tagOf(String path) {
        return tagOf(id(path));
    }

    public static final TagKey<Item> FLASK_MATERIAL = tagOf("flask_material");
	public static final TagKey<Item> CAN_POUR_INTO_FLASK = tagOf("can_pour_into_flask");
	public static final TagKey<Item> BOTTLE_REMAINDER = tagOf("remainder/glass_bottle");
	public static final TagKey<Item> BOWL_REMAINDER = tagOf("remainder/bowl");
	public static final TagKey<Item> BUCKET_REMAINDER = tagOf("remainder/bucket");
	public static final TagKey<Item> DOUBLE_SIZE = tagOf("double_size");

    public static final RecipeType<FlaskRemainderRecipe> REMAINDER_RECIPE_TYPE = registerRecipeType(id("remainder"));
    public static final RecipeSerializer<FlaskRemainderRecipe> REMAINDER_RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, id("remainder"), CuttingRecipeSerializerInvoker.newSerializer(FlaskRemainderRecipe::new));

    public static final ComponentType<Integer> FLASK_CAPACITY = register("flask_capacity", Codec.intRange(1, 99), PacketCodecs.INTEGER);
    public static final ComponentType<FlaskContentsComponent> FLASK_CONTENTS = register("flask_contents", FlaskContentsComponent.CODEC, FlaskContentsComponent.PACKET_CODEC);

	public static final Item DRINKING_FLASK = register("drinking_flask", DrinkingFlaskItem::new, new Item.Settings()
			.maxCount(1)
            .component(FLASK_CAPACITY, 16)
	);

	public static final Item PHANTOM_DRINKING_FLASK = register("phantom_drinking_flask", PhantomDrinkingFlaskItem::new, new Item.Settings()
			.maxCount(1)
            .component(FLASK_CAPACITY, 16)
	);

    public static int getSize(ItemStack stack) {
        return stack.isIn(DOUBLE_SIZE) ? 2 : 1;
    }

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
			entries.add(DRINKING_FLASK);
			entries.add(PHANTOM_DRINKING_FLASK);
		});
	}
}
