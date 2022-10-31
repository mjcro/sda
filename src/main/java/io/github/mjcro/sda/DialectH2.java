package io.github.mjcro.sda;

class DialectH2 implements Dialect {
    @Override
    public String getName() {
        return "H2";
    }

    @Override
    public boolean isCompatibleWith(Dialect other) {
        return other == Dialect.H2 || other == Dialect.MySQL;
    }
}
