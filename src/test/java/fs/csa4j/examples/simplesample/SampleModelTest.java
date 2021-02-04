package fs.csa4j.examples.simplesample;

import org.junit.Test;

import uia.sim.Env;
import static org.assertj.core.api.Assertions.assertThat;

public class SampleModelTest {

    @Test
    public void testSampleModel() {
        var env = new Env();
        var model = new SampleModel(env);
        model.getSource1().setNumberOfEntities(5);
        model.getDelay1().setDelayTime(10);

        env.run();

        assertThat(env.getNow()).isEqualTo(50);
        assertThat(model.getSink1().getCounterIn().get()).isEqualTo(5);
    }

}
