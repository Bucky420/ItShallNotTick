package dev.wuffs.itshallnottick;

import dev.ftb.mods.ftbchunks.data.ClaimedChunk;
import dev.ftb.mods.ftbchunks.data.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Utils {

    public static boolean isInClaimedChunk(Level level, BlockPos blockPos) {
        if (!FTBChunksAPI.isManagerLoaded())
            return true;

        ClaimedChunk chunk = FTBChunksAPI.getManager().getChunk(new ChunkDimPos(level, blockPos));

        return chunk != null;
    }

    public static boolean isNearPlayer(Level level, BlockPos blockPos, int maxHeight, int maxDistanceSquare) {
        return isNearPlayerInternal(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), maxHeight, maxDistanceSquare, false);
    }

    private static boolean isNearPlayerInternal(Level world, double posx, double posy, double posz, int maxHeight, int maxDistanceSquare, boolean allowNullPlayers) {
        List<? extends Player> closest = world.players();

        for (Player player : closest) {
            if (player == null)
                return allowNullPlayers;

            if (Math.abs(player.getY() - posy) < maxHeight) {
                double x = player.getX() - posx;
                double z = player.getZ() - posz;


                boolean nearPlayer = x * x + z * z < maxDistanceSquare;
//                System.out.printf("D: %d, XZ: %d %n",maxDistanceSquare, Math.round(x * x + z * z));

                if (nearPlayer) {
//                    System.out.printf("X: %d, Z: %d,XX: %d, ZZ %d, D: %d, XZ: %d %n", Math.round(x), Math.round(z),Math.round(x * x), Math.round(z * z),maxDistanceSquare, Math.round(x * x + z * z));
                    return true;
                }
            }
        }

        return false;

    }


    public static boolean isEntityWithinDistance(Entity player, Entity entity, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(player.getY() - entity.getY()) < maxHeight) {
            double x = player.getX() - entity.getX();
            double z = player.getZ() - entity.getZ();

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }

    public static boolean isEntityWithinDistance(BlockPos player, Vec3 entity, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(player.getY() - entity.y) < maxHeight) {
            double x = player.getX() - entity.x;
            double z = player.getZ() - entity.z;

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }

    public static boolean isEntityWithinDistance(Entity player, double cameraX, double cameraY, double cameraZ, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(player.getY() - cameraY) < maxHeight) {
            double x = player.getX() - cameraX;
            double z = player.getZ() - cameraZ;

            return x * x + z * z < maxDistanceSquare;
        }

        return false;
    }
    public static boolean isIgnoredEntity(Entity entity) {
        if (Config.entityIgnoreList.get().isEmpty()) {
            return false;
        }

        var entityType = entity.getType();
        var entityRegName = entityType.getRegistryName();

        if (entityRegName == null) {
            return false;
        }

        var ignored = false;
        if (!Config.entityResources.isEmpty()) {
            ignored = Config.entityResources.contains(entityRegName);
        }

        if (!Config.entityWildcards.isEmpty()) {
            ignored = Config.entityWildcards.stream().anyMatch(e -> entityRegName.toString().startsWith(e));
        }

        if (!Config.entityTagKeys.isEmpty()) {
            ignored = Config.entityTagKeys.stream().anyMatch(entityType::is);
        }

        return ignored;
//
////        return entityType.getTags().anyMatch(e -> {
////            var values = Config.entityIgnoreList.get();
//////            if (values)
////        });
//
//        var entityTags = entityType.getTags().toList();
//
//        if (entityTags.size() > 0) {
//            for (TagKey<EntityType<?>> tag : entityTags) {
//                if (Config.entityIgnoreList.get().contains("#" + tag.location().toString())) {
//                    return true;
//                }
//            }
//        }
//
//        for (String s : Config.entityIgnoreList.get()) {
//            if (s.matches(".*:\\*")) {
//                String id = s.split(":")[0];
//                String entityId = entityRegName.toString().split(":")[0];
//
//                if (id.equals(entityId)) {
//                    return true;
//                }
//            } else {
//                if (s.equals(entityRegName.toString())) {
//                    return true;
//                }
//            }
//        }
//        return false;
    }
}