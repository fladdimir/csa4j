package fs.csa4j.blocks;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uia.cor.Yield2Way;
import uia.sim.Env;
import uia.sim.Event;

@Data
@EqualsAndHashCode(callSuper = true)
public class Delay extends Block {

    private int delayTime;

    @Builder
    public Delay(Env env, String name, int resourceCapacity, int delayTime) {
        super(env, name, resourceCapacity);
        this.delayTime = delayTime;
    }

    @Override
    protected void process(Yield2Way<Event, Object> yield) {
        yield.call(env.timeout(delayTime));
    }

}
