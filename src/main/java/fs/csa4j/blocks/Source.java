package fs.csa4j.blocks;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uia.cor.Yield2Way;
import uia.sim.Env;
import uia.sim.Event;

@Data
@EqualsAndHashCode(callSuper = true)
public class Source extends Block {

    private int numberOfEntities;
    private int interArrivalTime;

    @Builder
    public Source(Env env, String name, int numberOfEntities, int interArrivalTime) {
        super(env, name, Integer.MAX_VALUE);
        this.numberOfEntities = numberOfEntities;
        this.interArrivalTime = interArrivalTime;
        env.process(Counter.getNextId(), this::creationLoop);
    }

    private void creationLoop(Yield2Way<Event, Object> yield) {
        for (int i = 0; i < numberOfEntities; i++) {
            Entity entity = new Entity();
            entity.setBlockRequest(resource.request(Counter.getNextId()));
            yield.call(entity.getBlockRequest());
            env.process(Counter.getNextId(), entityProcess -> receiveEntity(entityProcess, entity));
            yield.call(env.timeout(interArrivalTime));
        }
    }
}
