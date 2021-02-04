package fs.csa4j.blocks;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import uia.sim.Env;

public class RoundRobinGatewayTest {

    @Test
    public void testRoundRobin() {
        var env = new Env();
        var source = new Source(env, "source", 9, 0);
        var roundRobinGateway = new RoundRobinGateway(env, "roundRobinGateway");
        var sink1 = new Sink(env, "sink1");
        var sink2 = new Sink(env, "sink2");
        source.addSuccessor(roundRobinGateway);
        roundRobinGateway.addSuccessor(sink1);
        roundRobinGateway.addSuccessor(sink2);

        env.run();

        assertThat(env.getNow()).isZero();
        assertThat(sink1.counterIn.get()).isEqualTo(5);
        assertThat(sink2.counterIn.get()).isEqualTo(4);
    }

}
