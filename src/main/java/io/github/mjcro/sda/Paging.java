package io.github.mjcro.sda;

/**
 * Pagination request.
 */
public interface Paging {
    static Paging single() {
        return limit(1);
    }

    static Paging limit(int limit) {
        return ofPageAndLimit(1, limit);
    }

    static Paging ofPageAndLimit(int page, int limit) {
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
