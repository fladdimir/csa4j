package fs.csa4j.visualization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ModelInfo {

    protected final Map<String, VisualizableBlock> visualizerInfo = new HashMap<>();

    @Data
    public static class VisualizableBlock {

        private Point position;
        private final Map<String, List<Point>> ways = new HashMap<>();

        @Data
        public static class Point {
            private final int x;
            private final int y;
        }

    }

}
