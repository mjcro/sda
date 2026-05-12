package io.github.mjcro.sda;

import org.jspecify.annotations.NonNull;

/**
 * Pagination request.
 */
public interface Paging {
    /**
     * Constructs paging matching a single (first) record.
     *
     * @return Paging instance, not null.
     */
    static @NonNull Paging single() {
        return limit(1);
    }

    /**
     * Constructs paging for the first page with the given size.
     *
     * @param limit Per-page limit, must be positive.
     * @return Paging instance, not null.
     */
    static @NonNull Paging limit(int limit) {
        return ofPageAndLimit(1, limit);
    }

    /**
     * Constructs paging for the given page and per-page limit.
     *
     * @param page  Page number, starting from 1.
     * @param limit Per-page limit, must be positive.
     * @return Paging instance, not null.
     * @throws IllegalArgumentException If page or limit is not positive.
     */
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
