package fs.csa4j.examples.simplesample;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import fs.csa4j.visualization.JFrameCanvas;
import fs.csa4j.visualization.Visualizer;
import uia.sim.Env;

public class JFrameTest {

    @Test
    public void testJFrame() throws InterruptedException {
        var canvas = new JFrameCanvas(getClass().getClassLoader().getResource("astronomy.jpg"));
        var env = new Env();
        var model = new SampleModel(env);
        var modelInfo = new SampleModelInfo();
        var visualizer = new Visualizer(model, modelInfo, canvas);
        visualizer.setFlowSpeedPxPerSec(1e20);

        env.run();

        assertThat(model.getSink1().getCounterIn().get()).isEqualTo(1);
    }
}
