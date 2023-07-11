# OptiBabric (b1.7.3)

This is an OptiFabric fork for Minecraft Beta 1.7.3 and a Fabric fork named Babric.

__Note:__ This project is not related or supported by either Fabric or Optifine.

__Note:__ This project does not contain Optifine, you must download it separately!

## Installing

- Install [Babric](https://github.com/babric/prism-instance) in a launcher of your choice that supports MultiMC format instances
- Navigate to [OptiFine History](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1286605-b1-4-1-9-optifine-history)
- Expand `Minecraft Beta 1.7.3` spoiler
- Choose an edition supported by OptiBabric
- Use the `Download, Mirror` buttons. (__Do not__ use `Download 1, Download 2`, those are very likely to be dead for all editions)
- [Optional] Apply the [long distance patch](EDITIONS.md#bugs) to the downloaded zip
- Put the zip in Mods section in your Babric instance settings (or the mods folder), __NOT__ the minecraft.jar
- Download OptiBabric and put it in mods as well

During the first launch, OptiBabric will find and transform the OptiFine zip to be compatible with Fabric.

This transformed version can be found at `.minecraft/.optifabric/optifine-remapped.jar`, but in most cases you don't need to touch it.

OptiBabric will detect if OptiFine was replaced with another edition and will rerun the procedure automatically.

## Tested and supported editions

- HD G
- HD S G
- HD MT G2
- HD MT G2 + Long Distance Patch 1.1
- HD AA G5 (without StationAPI)

[More information on editions](EDITIONS.md)

## Notes on compatibility

Since this is a fork of OptiFabric, it works similarly, and as such is __not__ automatically compatible with every mod.

If you find a compatibility issue, report to [issues](https://github.com/mineLdiver/OptiBabric/issues)