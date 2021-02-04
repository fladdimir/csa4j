package fs.csa4j.blocks;

import lombok.Builder;
import uia.cor.Yield2Way;
import uia.sim.Env;
import uia.sim.Event;

public class Sink extends Block {

    @Builder
    public Sink(Env env, String name) {
        super(env, name, 1);
    }

    @Override
    protected void mainProcess(Yield2Way<Event, Object> yield, Entity entity) {
        counterIn.incrementAndGet();
        entity.getBlockRequest().close(); // release
        notifyEntityMovementListeners(entity, null); // move into the void
    }

}
