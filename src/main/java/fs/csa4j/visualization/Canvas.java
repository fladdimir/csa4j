package fs.csa4j.visualization;

import fs.csa4j.blocks.Entity;
import fs.csa4j.visualization.ModelInfo.VisualizableBlock.Point;

public interface Canvas {

    void drawEntityAtPosition(Entity entity, Point currentPosition);

    void removeEntity(Entity entity);

}
