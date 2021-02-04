package fs.csa4j.examples.gatewaysample;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import uia.sim.Env;

public class SampleModelTest {

    @Test
    public void testSampleModel() {
        var env = new Env();
        var model = new SampleModel(env);
        model.getSource1().setInterArrivalTime(1);
        model.getSource1().setNumberOfEntities(5);
        model.getDelay1().setDelayTime(20); // parallel
        model.getDelay2().setDelayTime(15); // in a row

        env.run();

        assertThat(env.getNow()).isEqualTo(31);
        assertThat(model.getDelay1().getCounterIn().get()).isEqualTo(3);
        assertThat(model.getDelay2().getCounterIn().get()).isEqualTo(2);
        assertThat(model.getSink1().getCounterIn().get()).isEqualTo(5);
    }

    @Test
    public void testSampleModel2() {
        long startTsp = System.currentTimeMillis();
        int numberOfEntities = (int) 1e2;

        var env = new Env();
        var model = new SampleModel(env);
        model.getSource1().setNumberOfEntities(numberOfEntities);
        model.getSource1().setInterArrivalTime(0);
        model.getDelay1().setDelayTime(1000); // parallel
        model.getDelay2().setDelayTime(1); // in a row

        env.run();

        assertThat(model.getDelay1().getCounterIn().get()).isEqualTo(numberOfEntities / 2);
        assertThat(model.getDelay2().getCounterIn().get()).isEqualTo(numberOfEntities / 2);
        assertThat(model.getSink1().getCounterIn().get()).isEqualTo(numberOfEntities);
        System.out.println("time: " + (double) (System.currentTimeMillis() - startTsp) / 1000);
    }

}