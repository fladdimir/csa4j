package fs.csa4j.visualization;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import fs.csa4j.blocks.Block;
import fs.csa4j.blocks.Entity;
import fs.csa4j.model.Model;
import fs.csa4j.visualization.ModelInfo.VisualizableBlock.Point;
import lombok.Data;
import lombok.extern.java.Log;

@Log
@Data
public class Visualizer {

    private final Model model;
    private final ModelInfo modelInfo;
    private final Canvas canvas;

    private double flowSpeedPxPerSec = 100;

    public Visualizer(Model model, ModelInfo modelInfo, Canvas canvas) {
        this.model = model;
        this.modelInfo = modelInfo;
        this.canvas = canvas;

        registerListener();
    }

    private void registerListener() {
        for (Block block : model.getModelGraph().keySet())
            block.addEntityMovementListener(this::animateEntityMovement);
    }

    private void animateEntityMovement(Entity entity, Block from, Block to) {
        if (Objects.isNull(to)) {
            removeEntity(entity);
            return;
        }

        var visualizableBlock = requireNonNull(modelInfo.getVisualizerInfo().get(from.getName()));
        var wayPoints = requireNonNull(visualizableBlock.getWays().get(to.getName()));

        // animation-loop
        Point currentPosition = wayPoints.get(0);
        for (Point target : wayPoints.subList(1, wayPoints.size())) {
            Point start = currentPosition;
            Point direction = new Point(target.getX() - start.getX(), target.getY() - start.getY());
            double distance = Math.sqrt(Math.pow(direction.getX(), 2) + Math.pow(direction.getY(), 2));
            double time = distance / flowSpeedPxPerSec;
            double breakTime = 1f / 40;
            for (double t = 0; t <= time; t += breakTime) {
                long startTsp = System.currentTimeMillis();
                double progress = t / time;
                currentPosition = new Point((int) (progress * direction.getX() + start.getX()),
                        (int) (progress * direction.getY() + start.getY()));
                drawAtPosition(entity, currentPosition);

                sleep(breakTime, startTsp);
            }
            currentPosition = target;
            drawAtPosition(entity, currentPosition);
        }
    }

    private void sleep(double breakTime, long startTsp) {
        long timeSpent = System.currentTimeMillis() - startTsp;
        log.info("time spent: " + timeSpent);
        try {
            TimeUnit.MILLISECONDS.sleep(Math.max((long) ((breakTime - timeSpent) * 1000), 0));
        } catch (InterruptedException e) {
            log.info(e.toString());
            Thread.currentThread().interrupt();
        }
    }

    private void drawAtPosition(Entity entity, Point currentPosition) {
        canvas.drawEntityAtPosition(entity, currentPosition);
    }

    private void removeEntity(Entity entity) {
        canvas.removeEntity(entity);
    }

}
