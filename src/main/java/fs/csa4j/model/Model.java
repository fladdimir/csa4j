package fs.csa4j.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fs.csa4j.blocks.Block;
import lombok.Data;
import uia.sim.Env;

@Data
public class Model {
    private final Env env;
    protected final Map<Block, List<Block>> modelGraph = new HashMap<>();

    protected void init() {
        for (Entry<Block, List<Block>> entry : modelGraph.entrySet()) {
            for (Block successor : entry.getValue()) {
                entry.getKey().addSuccessor(successor);
            }
        }
    }
}
