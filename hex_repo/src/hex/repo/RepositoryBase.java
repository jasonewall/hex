package hex.repo;

import hex.repo.metadata.Metadata;

/**
 * Created by jason on 14-10-25.
 */
public class RepositoryBase<T> extends AbstractRepository<T> {
    private final Metadata<T> metadata;

    public RepositoryBase(Metadata<T> metadata) {
        this.metadata = metadata;
    }

    public RepositoryBase(Class<T> sourceType) {
        this(Metadata.fromClass(sourceType));
    }

    @Override
    public Metadata<T> get_metadata() {
        return this.metadata;
    }

    @Override
    public String getTableName() {
        return metadata.getTableName();
    }
}
