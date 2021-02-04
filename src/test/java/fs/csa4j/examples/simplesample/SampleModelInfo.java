package fs.csa4j.examples.simplesample;

import java.util.List;

import fs.csa4j.visualization.ModelInfo;
import fs.csa4j.visualization.ModelInfo.VisualizableBlock.Point;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleModelInfo extends ModelInfo {

    public SampleModelInfo() {
        visualizerInfo.put("source1", new VisualizableBlock());
        visualizerInfo.get("source1").setPosition(new Point(0, 0));
        visualizerInfo.get("source1").getWays().put("delay1", List.of(new Point(0, 0), new Point(100, 100)));

        visualizerInfo.put("delay1", new VisualizableBlock());
        visualizerInfo.get("delay1").setPosition(new Point(100, 100));
        visualizerInfo.get("delay1").getWays().put("sink1",
                List.of(new Point(100, 100), new Point(200, 100), new Point(200, 200)));

        visualizerInfo.put("sink1", new VisualizableBlock());
        visualizerInfo.get("sink1").setPosition(new Point(200, 200));
    }

}
