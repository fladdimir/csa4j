package fs.csa4j.blocks;

import lombok.Builder;
import uia.sim.Env;

public class RoundRobinGateway extends Block {

    @Builder
    public RoundRobinGateway(Env env, String name) {
        super(env, name, 1);
    }

    @Override
    protected Block findSuccessor() {
        int nextIndex = (int) ((counterIn.get() - 1) % successors.size());
        return successors.get(nextIndex);
    }

}
