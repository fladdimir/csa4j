package fs.csa4j.benchmark;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import uia.sim.Env;

public class BenchmarkTest {

    private static final String RESULTS_PATH = Paths.get("").toAbsolutePath() + "/benchmark/results.csv";
    private static final String runtime = "Casymda@UiaSim(OpenJDK14)";
    private static final List<Integer> n_entities = List.of(10, 100, 1_000, 2_000, 3_000, 5_000, 10_000, 50_000);
    // 100_000,
    // 200_000);
    private static final List<Integer> inter_arrival_times = List.of(10); // ,0
    private static int c;

    @Test
    @Ignore("takes time")
    public void RunBenchmark() throws FileNotFoundException {
        c = 0;
        List<Map<String, String>> results = new ArrayList<>();
        for (var n_entity : n_entities) {
            for (var iat : inter_arrival_times) {
                var parallel_proc_time = 10;
                var sequential_proc_time = 10;
                var overall_seq_time = iat + n_entity / 2 * sequential_proc_time;
                var last_time = (n_entity - 1) * iat + sequential_proc_time;
                var expected_end = Math.max(last_time, overall_seq_time);
                var t = Run(n_entity, iat, parallel_proc_time, sequential_proc_time, expected_end);
                Map<String, String> result = Map.of("runtime", runtime.toString(), "n_entities", n_entity.toString(),
                        "inter_arrival_time", iat.toString(), "time", Long.toString(t));
                results.add(result);
            }
        }
        String content = String.join(",", results.get(0).keySet()) + System.lineSeparator();
        content += String.join(System.lineSeparator(),
                results.stream().map(mp -> String.join(",", mp.values())).collect(Collectors.toList()));
        try (PrintWriter out = new PrintWriter(RESULTS_PATH)) {
            out.println(content);
        }
    }

    private long Run(int max_entities, int inter_arrival_time, int parallel_proc_time, int sequential_proc_time,
            int expected_end) {
        c++;
        var env = new Env();
        var model = new SampleModel(env);

        model.getSource().setNumberOfEntities(max_entities);
        model.getSource().setInterArrivalTime(inter_arrival_time);

        model.getParallelProcessing().setDelayTime(parallel_proc_time);
        model.getSequentialProcessing().setDelayTime(sequential_proc_time);

        var t0 = System.currentTimeMillis();
        env.run();
        long t = (System.currentTimeMillis() - t0) / 1000;

        assertThat(model.getSink().getCounterIn().get()).isEqualTo(model.getSource().getCounterIn().get());
        assertThat(model.getParallelProcessing().getCounterIn().get())
                .isEqualTo(model.getSource().getNumberOfEntities() / 2);
        assertThat(model.getSequentialProcessing().getCounterIn().get())
                .isEqualTo(model.getSource().getNumberOfEntities() / 2);
        assertThat(env.getNow()).isEqualTo(expected_end);

        System.out.println("Finished run: " + c);
        return t;
    }

    @Test
    public void testSampleModel() {
        Run(10, 0, 10, 10, 50);
    }
}
