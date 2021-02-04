package fs.csa4j.blocks;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import uia.sim.Env;

public class BlockTest {

    @Test
    public void testBlockStatusChange() {
        var env = new Env();
        var source = new Source(env, "source", 100, 0);
        var block = new Block(env, "block", 1);
        var sink = new Sink(env, "sink");
        source.addSuccessor(block);
        block.addSuccessor(sink);

        AtomicLong statusChanges = new AtomicLong(0);
        block.addBlockStatusChangeListener((Block b) -> statusChanges.incrementAndGet());
        AtomicLong entityMovements = new AtomicLong(0);
        block.addEntityMovementListener((Entity entity, Block from, Block to) -> entityMovements.incrementAndGet());

        env.run();

        assertThat(env.getNow()).isZero();
        assertThat(statusChanges.get()).isEqualTo(200);
        assertThat(entityMovements.get()).isEqualTo(100);
    }

}
