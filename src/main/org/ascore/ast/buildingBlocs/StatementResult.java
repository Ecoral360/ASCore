package org.ascore.ast.buildingBlocs;

import org.ascore.lang.objects.ASCObject;

import java.util.Objects;

public final class StatementResult<T> {
    public static final StatementResult<?> CONTINUE = new StatementResult<>(null, StatementResultType.CONTINUE);
    public static final StatementResult<?> END_PROGRAM = new StatementResult<>(null, StatementResultType.END_PROGRAM);

    private final T value;
    private final StatementResultType type;

    private StatementResult(T value, StatementResultType type) {
        this.value = value;
        this.type = type;
    }

    public static <T extends ASCObject<?>> StatementResult<T> ascValue(T value) {
        return new StatementResult<>(value, StatementResultType.VALUE);
    }

    public static <T> StatementResult<T> returnValue(T value) {
        return new StatementResult<>(value, StatementResultType.RETURN_VALUE);
    }

    public T value() {
        return value;
    }

    public StatementResultType type() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (StatementResult<?>) obj;
        return Objects.equals(this.value, that.value) &&
               Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

    private enum StatementResultType {
        CONTINUE,
        END_PROGRAM,
        VALUE,
        RETURN_VALUE
    }
}
