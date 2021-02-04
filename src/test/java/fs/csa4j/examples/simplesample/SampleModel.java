package fs.csa4j.examples.simplesample;

import java.util.List;

import fs.csa4j.blocks.Delay;
import fs.csa4j.blocks.Sink;
import fs.csa4j.blocks.Source;
import fs.csa4j.model.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uia.sim.Env;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleModel extends Model {

    // !properties
    private final Source source1;
    private final Delay delay1;
    private final Sink sink1;

    public SampleModel(Env env) {
        super(env);

        // !assignments
        source1 = Source.builder().env(env).name("source1").numberOfEntities(1).interArrivalTime(1).build();
        delay1 = Delay.builder().env(env).name("delay1").delayTime(1).resourceCapacity(1).build();
        sink1 = Sink.builder().env(env).name("sink1").build();

        // !modelGraph
        modelGraph.put(source1, List.of(delay1));
        modelGraph.put(delay1, List.of(sink1));
        modelGraph.put(sink1, List.of());
        init();
    }
}
