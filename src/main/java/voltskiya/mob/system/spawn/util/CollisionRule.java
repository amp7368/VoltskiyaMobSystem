package voltskiya.mob.system.spawn.util;

import apple.mc.utilities.item.material.MaterialUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

public class CollisionRule {

    private static final int STEPS_TO_TRY = 2;

    @Nullable
    public static Location calculateValidSpawn(EntityType<?> type, Location location) {
        CollisionRuleTimings timings = new CollisionRuleTimings();
        float height = type.getHeight();
        float halfWidth = type.getWidth() / 2;
        int checkRadius = (int) Math.ceil(Math.max(height * 4, halfWidth * 8));
        timings.gatherWorld();
        List<VoxelShape> blocks = new ArrayList<>();
        for (int xi = -checkRadius; xi <= checkRadius; xi++) {
            for (int yi = -checkRadius; yi <= checkRadius; yi++) {
                for (int zi = -checkRadius; zi <= checkRadius; zi++) {
                    Location local = location.clone().add(xi, yi, zi);
                    Block block = local.getBlock();
                    if (MaterialUtils.isWalkThroughable(block.getType())) continue;

                    Collection<BoundingBox> boundingBlock = block.getCollisionShape().getBoundingBoxes();

                    for (BoundingBox boundingBox : boundingBlock) {
                        blocks.add(shapes(boundingBox).move(xi, yi, zi));
                    }
                }
            }
        }
        timings.gatherWorld();

        timings.optimizeWorld();
        VoxelShape world = Shapes.or(Shapes.empty(), blocks.toArray(VoxelShape[]::new));
        VoxelShape entity = Shapes.create(-halfWidth, 0, -halfWidth, halfWidth, height, halfWidth);
        timings.optimizeWorld();

        Vec3 moveTo = new Vec3(0, 0, 0);
        for (int step = 0; step < STEPS_TO_TRY; step++) {
            timings.createStepMath();
            timings.stepMath();
            VoxelShape shiftedEntity = entity.move(moveTo.x, moveTo.y, moveTo.z);
            // check collisions
            VoxelShape intersection = Shapes.join(shiftedEntity, world, BooleanOp.AND);
            if (intersection.isEmpty()) {
                // no collisions!
                timings.stepMath();
                Vec3 center = shiftedEntity.bounds().getCenter();
//                timings.report();
                return location.clone().add(center.x, center.y, center.z);
            }
            Vec3 moveDelta = checkedMoveFromFailure(shiftedEntity, intersection, checkRadius);
            if (moveDelta == null) {
                timings.stepMath();
//                timings.report();
                return null;
            }
            moveTo = moveTo.add(moveDelta);
            timings.stepMath();
        }
//        timings.report();
        return null;
    }

    @Nullable
    private static Vec3 checkedMoveFromFailure(VoxelShape shiftedEntity, VoxelShape intersection, int checkRadius) {
        Vec3 moveDelta = new Vec3(0, 0, 0);
        for (AABB intersectionBox : intersection.optimize().toAabbs()) {
            Vec3 move = moveFromFailure(shiftedEntity, intersectionBox);
            if (move == null) continue;
            // if we want to move in opposite directions, in most cases, we should stop here.
            if (isOppSignsAndNotZero(moveDelta.x, move.x) ||
                isOppSignsAndNotZero(moveDelta.y, move.y) ||
                isOppSignsAndNotZero(moveDelta.z, move.z)) {
                return null;
            }
            moveDelta = moveDelta.add(move);
        }
        if (moveDelta.length() == 0) return null;
        // if we've moved out of the environment, cancel the call
        if (Math.abs(moveDelta.x) >= checkRadius) return null;
        if (Math.abs(moveDelta.y) >= checkRadius) return null;
        if (Math.abs(moveDelta.z) >= checkRadius) return null;
        return moveDelta;
    }

    private static boolean isOppSignsAndNotZero(double n1, double n2) {
        return n1 != 0 && n2 != 0 && (n1 > 0 == n2 > 0);
    }

    private static Vec3 moveFromFailure(VoxelShape shiftedEntity, AABB intersectionBox) {
        Vec3 entityCenter = shiftedEntity.bounds().getCenter();
        Vec3 failDirection = intersectionBox.getCenter().subtract(entityCenter);
        if (Vec3.ZERO.equals(failDirection)) return null;
        double x = moveFromFailure(Axis.X, failDirection.x, shiftedEntity, intersectionBox);
        double y = moveFromFailure(Axis.Y, failDirection.y, shiftedEntity, intersectionBox);
        double z = moveFromFailure(Axis.Z, failDirection.z, shiftedEntity, intersectionBox);
        return new Vec3(x, y, z).scale(1.001); // make sure we don't barely clip into the block
    }

    private static double moveFromFailure(Axis axis, double failDirection, VoxelShape currentShape, AABB failedPortion) {
        if (failDirection > 0) {
            return currentShape.max(axis) - failedPortion.min(axis);
        } else if (failDirection < 0) {
            return currentShape.min(axis) - failedPortion.max(axis);
        }
        return 0;
    }

    private static VoxelShape shapes(BoundingBox box) {
        return Shapes.create(box.getMinX(), box.getMinY(), box.getMinZ(), box.getMaxX(), box.getMaxY(), box.getMaxZ());
    }
}
