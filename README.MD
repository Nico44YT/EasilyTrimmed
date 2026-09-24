## Installation

## Maven
````gradle
repositories {
    maven {
        url 'https://jitpack.io'
    }
}

dependencies {
    modImplementation 'com.github.Nico44YT:EasilyTrimmed:<version_tag>'
    include 'com.github.Nico44YT:EasilyTrimmed:<version_tag>'
}
````

## Usage

View the test mod for an concrete example.

Create a new class called `ModTrimMaterials`

```java
public class ModTrimMaterials {
    public static final EasyArmorTrimMaterialRegistry REGISTRY = 
            new EasyArmorTrimMaterialRegistry();

    public static final EasyArmorTrimMaterial ECHO_SHARD = REGISTRY.register(
            TestMod.id("echo_shard"),
            Items.ECHO_SHARD,
            0x0F_0F_FF,
            0.1F
    );

    public static void init() {
        
    }
}
```

Call the ``init`` method from the main mod initializer
```java
public class TestMod implements ModInitializer {
    public static final String MOD_ID = "test_mod";

    @Override
    public void onInitialize() {
        ModTrimMaterials.init();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
```

Now setup datagen
```java
public class TestModDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(TestModItemTagProvider::new);
        pack.addProvider(TestModRegistryDataGenerator::new);
        pack.addProvider(TestModArmorTrimAtlasProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(
                RegistryKeys.TRIM_MATERIAL,
                ModTrimMaterials.REGISTRY::bootstrap
        );
    }
}
```

```java
public class TestModRegistryDataGenerator extends FabricDynamicRegistryProvider {
    public TestModRegistryDataGenerator(
            FabricDataOutput output, 
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.TRIM_MATERIAL));
    }

    @Override
    public String getName() {
        return "";
    }
}
```

Tag provider for the ``minecraft:trim_materials`` tag:
```java
public class TestModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public TestModItemTagProvider(
            FabricDataOutput output, 
            CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture
    ) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        addTag(ItemTags.TRIM_MATERIALS, Items.ECHO_SHARD);
    }

    public void addTag(TagKey<Item> tagKey, ItemConvertible... itemConvertibles) {
        getOrCreateTagBuilder(tagKey)
                .add(Arrays.stream(itemConvertibles)
                        .map(ItemConvertible::asItem)
                        .toArray(Item[]::new));
    }
}
```

```java
public class TestModArmorTrimAtlasProvider extends EasyArmorTrimAtlasProvider {
    public TestModArmorTrimAtlasProvider(
            FabricDataOutput output, 
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    public void configure() {
        registerTrimMaterial(ModTrimMaterials.ECHO_SHARD);
    }

    @Override
    public String getName() {
        return "Trim Atlas Provider";
    }
}
```