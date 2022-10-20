package io.github.mjcro.sda;

class PagingImpl implements Paging {
    private final int page;
    private final int limit;

    PagingImpl(int page, int limit) {
        if (page < 1) {
            throw new IllegalArgumentException("Page starts with 1, but " + page + " given");
        }
        if (limit < 1) {
            throw new IllegalArgumentException("Illegal limit " + limit);
        }

        this.page = page;
        this.limit = limit;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public int getLimit() {
        return limit;
    }
}
