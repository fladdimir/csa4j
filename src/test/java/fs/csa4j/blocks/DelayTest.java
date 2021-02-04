package fs.csa4j.blocks;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import uia.sim.Env;

public class DelayTest {

    @Test
    public void testDelayX1entitiesX1() {
        var env = new Env();
        var source = new Source(env, "source", 1, 0);
        var delay = new Delay(env, "delay", 1, 1);
        var sink = new Sink(env, "sink");
        source.addSuccessor(delay);
        delay.addSuccessor(sink);

        env.run();

        assertThat(env.getNow()).isEqualTo(1);
        assertThat(source.counterIn.get()).isEqualTo(1);
        assertThat(delay.counterIn.get()).isEqualTo(1);
        assertThat(sink.counterIn.get()).isEqualTo(1);
    }

    @Test
    public void testDelayX1entitiesX0() {
        var env = new Env();
        var source = new Source(env, "source", 0, 0);
        var delay = new Delay(env, "delay", 1, 1);
        var sink = new Sink(env, "sink");
        source.addSuccessor(delay);
        delay.addSuccessor(sink);

        env.run();

        assertThat(env.getNow()).isZero();
        assertThat(source.counterIn.get()).isZero();
        assertThat(delay.counterIn.get()).isZero();
        assertThat(sink.counterIn.get()).isZero();
    }

    @Test
    public void testDelayX2entitiesX1() {
        var env = new Env();
        var source = new Source(env, "source", 1, 0);
        var delay1 = new Delay(env, "delay1", 1, 1);
        var delay2 = new Delay(env, "delay2", 1, 1);
        var sink = new Sink(env, "sink");
        source.addSuccessor(delay1);
        delay1.addSuccessor(delay2);
        delay2.addSuccessor(sink);

        env.run();

        assertThat(env.getNow()).isEqualTo(2);
        assertThat(source.counterIn.get()).isEqualTo(1);
        assertThat(delay1.counterIn.get()).isEqualTo(1);
        assertThat(delay2.counterIn.get()).isEqualTo(1);
        assertThat(sink.counterIn.get()).isEqualTo(1);
    }

    @Test
    public void testDelayX2entitiesX2() {
        var env = new Env();
        var source = new Source(env, "source", 2, 0);
        var delay1 = new Delay(env, "delay1", 1, 1);
        var delay2 = new Delay(env, "delay2", 1, 1);
        var sink = new Sink(env, "sink");
        source.addSuccessor(delay1);
        delay1.addSuccessor(delay2);
        delay2.addSuccessor(sink);

        env.run();

        assertThat(env.getNow()).isEqualTo(3);
        assertThat(source.counterIn.get()).isEqualTo(2);
        assertThat(delay1.counterIn.get()).isEqualTo(2);
        assertThat(delay2.counterIn.get()).isEqualTo(2);
        assertThat(sink.counterIn.get()).isEqualTo(2);
    }

    @Test
    public void testDelayX3entitiesX3() {
        var env = new Env();
        var source = new Source(env, "source", 3, 0);
        var delay1 = new Delay(env, "delay1", 1, 1);
        var delay2 = new Delay(env, "delay2", 1, 1);
        var delay3 = new Delay(env, "delay3", 1, 1);
        var sink = new Sink(env, "sink");
        source.addSuccessor(delay1);
        delay1.addSuccessor(delay2);
        delay2.addSuccessor(delay3);
        delay3.addSuccessor(sink);

        env.run();

        assertThat(env.getNow()).isEqualTo(5);
        assertThat(source.counterIn.get()).isEqualTo(3);
        assertThat(delay1.counterIn.get()).isEqualTo(3);
        assertThat(delay2.counterIn.get()).isEqualTo(3);
        assertThat(sink.counterIn.get()).isEqualTo(3);
    }

    @Test
    public void testDelayX3entitiesX3parallel() {
        var env = new Env();
        var source = new Source(env, "source", 3, 0);
        var delay1 = new Delay(env, "delay1", 3, 1);
        var delay2 = new Delay(env, "delay2", 3, 1);
        var delay3 = new Delay(env, "delay3", 3, 1);
        var sink = new Sink(env, "sink");
        source.addSuccessor(delay1);
        delay1.addSuccessor(delay2);
        delay2.addSuccessor(delay3);
        delay3.addSuccessor(sink);

        env.run();

        assertThat(env.getNow()).isEqualTo(3);
        assertThat(source.counterIn.get()).isEqualTo(3);
        assertThat(delay1.counterIn.get()).isEqualTo(3);
        assertThat(delay2.counterIn.get()).isEqualTo(3);
        assertThat(sink.counterIn.get()).isEqualTo(3);
    }

    @Test
    public void testDelayX3entitiesX3parallelWithGap() {
        var env = new Env();
        var source = new Source(env, "source", 3, 1);
        var delay1 = new Delay(env, "delay1", 3, 5);
        var delay2 = new Delay(env, "delay2", 3, 5);
        var delay3 = new Delay(env, "delay3", 3, 5);
        var sink = new Sink(env, "sink");
        source.addSuccessor(delay1);
        delay1.addSuccessor(delay2);
        delay2.addSuccessor(delay3);
        delay3.addSuccessor(sink);

        env.run();

        assertThat(env.getNow()).isEqualTo(17);
        assertThat(source.counterIn.get()).isEqualTo(3);
        assertThat(delay1.counterIn.get()).isEqualTo(3);
        assertThat(delay2.counterIn.get()).isEqualTo(3);
        assertThat(sink.counterIn.get()).isEqualTo(3);
    }

    @Test
    public void testDelayX3entitiesX1000() {
        var env = new Env();
        var source = new Source(env, "source", 1000, 0);
        var delay1 = new Delay(env, "delay1", 1, 5);
        var delay2 = new Delay(env, "delay2", 1, 5);
        var delay3 = new Delay(env, "delay3", 1, 5);
        var sink = new Sink(env, "sink");
        source.addSuccessor(delay1);
        delay1.addSuccessor(delay2);
        delay2.addSuccessor(delay3);
        delay3.addSuccessor(sink);

        env.run();

        assertThat(env.getNow()).isEqualTo(5010);
        assertThat(source.counterIn.get()).isEqualTo(1000);
        assertThat(delay1.counterIn.get()).isEqualTo(1000);
        assertThat(delay2.counterIn.get()).isEqualTo(1000);
        assertThat(sink.counterIn.get()).isEqualTo(1000);
    }

}
