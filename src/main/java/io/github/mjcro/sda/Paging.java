package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;

/**
 * Pagination request.
 */
public interface Paging {
    static @NonNull Paging single() {
        return limit(1);
    }

    static @NonNull Paging limit(int limit) {
        return ofPageAndLimit(1, limit);
    }

    static @NonNull Paging ofPageAndLimit(int page, int limit) {
        return new PagingImpl(page, limit);
    }

    /**
     * @return Page index, starting with 1.
     */
    int getPage();

    /**
     * @return Per-page limit.
     */
    int getLimit();

    /**
     * @return Offset from start.
     */
    default int getOffset() {
        return (getPage() - 1) * getLimit();
    }
}
