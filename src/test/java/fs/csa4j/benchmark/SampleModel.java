package fs.csa4j.benchmark;

import java.util.List;

import fs.csa4j.blocks.Delay;
import fs.csa4j.blocks.RoundRobinGateway;
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
    private final Source source;
    private final RoundRobinGateway gateway1;
    private final Delay parallelProcessing;
    private final Delay buffer;
    private final Delay sequentialProcessing;
    private final RoundRobinGateway gateway2;
    private final Sink sink;

    public SampleModel(Env env) {
        super(env);

        // !assignments
        source = Source.builder().env(env).name("source1").numberOfEntities(1).interArrivalTime(0).build();
        gateway1 = RoundRobinGateway.builder().env(env).name("gateway1").build();
        parallelProcessing = Delay.builder().env(env).name("delay1").delayTime(1).resourceCapacity(Integer.MAX_VALUE)
                .build();
        buffer = Delay.builder().env(env).name("buffer").delayTime(0).resourceCapacity(Integer.MAX_VALUE).build();
        sequentialProcessing = Delay.builder().env(env).name("delay2").delayTime(1).resourceCapacity(1).build();
        gateway2 = RoundRobinGateway.builder().env(env).name("gateway2").build();
        sink = Sink.builder().env(env).name("sink1").build();

        // !modelGraph
        modelGraph.put(source, List.of(gateway1));
        modelGraph.put(gateway1, List.of(parallelProcessing, buffer));
        modelGraph.put(parallelProcessing, List.of(gateway2));
        modelGraph.put(buffer, List.of(sequentialProcessing));
        modelGraph.put(sequentialProcessing, List.of(gateway2));
        modelGraph.put(gateway2, List.of(sink));
        modelGraph.put(sink, List.of());
        init();
    }
}
