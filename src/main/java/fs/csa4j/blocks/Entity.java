package fs.csa4j.blocks;

import fs.csa4j.blocks.Block.Counter;
import lombok.Data;
import uia.sim.resources.Resource.Request;

@Data
public class Entity {

    private final String id = Counter.getNextId();

    private Request blockRequest;

}
