package fs.csa4j.blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import uia.cor.Yield2Way;
import uia.sim.Env;
import uia.sim.Event;
import uia.sim.resources.Resource;
import uia.sim.resources.Resource.Request;

@Data
public class Block {

    private final String id = Counter.getNextId();

    protected final Env env;
    protected final String name;
    protected final Resource resource;

    protected final List<Block> successors = new ArrayList<>();
    protected final List<Entity> entities = new ArrayList<>();

    private final Collection<BlockStatusChangeListener> blockStatusChangeListeners = new ArrayList<>();
    private final Collection<EntityMovementListener> entityMovementListeners = new ArrayList<>();

    @FunctionalInterface
    public static interface BlockStatusChangeListener extends Consumer<Block> {
    }

    @FunctionalInterface
    public static interface EntityMovementListener {
        void accept(Entity entity, Block from, Block to);
    }

    public static class Counter {
        private static final AtomicLong NEXT_ID = new AtomicLong(1);

        public static String getNextId() {
            return Long.toString(NEXT_ID.getAndIncrement());
        }

        private Counter() {
        }
    }

    @Setter(AccessLevel.NONE)
    protected final AtomicLong counterIn = new AtomicLong(0);

    public void addSuccessor(Block block) {
        successors.add(block);
    }

    public void addBlockStatusChangeListener(BlockStatusChangeListener listener) {
        blockStatusChangeListeners.add(listener);
    }

    protected void notifyBlockStatusChangeListeners() {
        blockStatusChangeListeners.forEach(listener -> listener.accept(this));
    }

    public void addEntityMovementListener(EntityMovementListener listener) {
        entityMovementListeners.add(listener);
    }

    protected void notifyEntityMovementListeners(Entity entity, Block to) {
        entityMovementListeners.forEach(listener -> listener.accept(entity, this, to));
    }

    /**
     * Constructor.
     */
    public Block(Env env, String name, int resourceCapacity) {
        this.env = env;
        this.name = name;
        this.resource = new Resource(env, resourceCapacity);
    }

    /**
     * Entry-point for entities coming from predecessors.
     */
    public final void receiveEntity(Yield2Way<Event, Object> yield, Entity entity) {
        this.mainProcess(yield, entity);
    }

    protected void mainProcess(Yield2Way<Event, Object> yield, Entity entity) {
        // accept
        counterIn.incrementAndGet();
        entities.add(entity);
        notifyBlockStatusChangeListeners();

        // process
        process(yield);

        // wait for successor
        Block successor = findSuccessor();
        Request ownResourceRequest = entity.getBlockRequest();
        entity.setBlockRequest(successor.getResource().request(getId()));
        yield.call(entity.getBlockRequest());
        ownResourceRequest.close(); // release

        // remove
        entities.remove(entity);
        notifyBlockStatusChangeListeners();
        // forward
        notifyEntityMovementListeners(entity, successor);
        successor.receiveEntity(yield, entity);
    }

    /**
     * To be overridden by sub-classes for custom behavior.
     */
    protected void process(Yield2Way<Event, Object> yield) {
        yield.call(env.timeout(0));
    }

    /**
     * To be overridden by sub-classes for custom behavior.
     */
    protected Block findSuccessor() {
        return successors.get(0);
    }

}
