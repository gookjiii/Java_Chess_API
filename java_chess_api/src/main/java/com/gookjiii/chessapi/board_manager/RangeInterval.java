package com.gookjiii.chessapi.board_manager;

import java.util.Objects;

public class RangeInterval {
    private int a;
    private int b;

    public RangeInterval(int a, int b) {
        this.a = a;
        this.b = b;
    }

    // Implement hashCode and equals methods for use in the HashMap
    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RangeInterval other = (RangeInterval) obj;
        return a == other.a && b == other.b;
    }

    public int getA() {
        return this.a;
    }

    public int getB() {
        return this.b;
    }
}
