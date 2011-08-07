package org.polyforms.delegation;

import java.util.ArrayList;
import java.util.List;

import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationRegister;
import org.polyforms.delegation.builder.ParameterProvider;
import org.polyforms.delegation.util.DefaultValue;
import org.springframework.core.GenericTypeResolver;

@SuppressWarnings("unchecked")
public abstract class DelegatorRegister<S> implements DelegationRegister {
    private DelegationBuilder builder;
    private S source;

    public final void register(final DelegationBuilder builder) {
        this.builder = builder;
        source = builder.from(getDelegatorType());
        register(source);
        builder.registerDelegations();
    }

    private Class<S> getDelegatorType() {
        return (Class<S>) GenericTypeResolver.resolveTypeArgument(this.getClass(), DelegatorRegister.class);
    }

    protected void register(final S source) {
    }

    protected final <T> T delegate() {
        return builder.<T> delegate();
    }

    protected final <T> T delegate(final Object delegator) {
        return delegate();
    }

    protected final <T> void with(final DelegateeRegister<T> delegateeRegister) {
        delegateeRegister.register(source);
        builder.registerDelegations();
    }

    protected final <P> P at(final Class<P> targetType, final int position) {
        return provideBy(targetType, new At<P>(position));
    }

    protected final <P> P typeOf(final Class<P> targetType, final Class<?> sourceType) {
        return provideBy(targetType, new TypeOf<P>(sourceType));
    }

    protected final <P> P constant(final P value) {
        return provideBy((Class<P>) (value == null ? null : value.getClass()), new Constant<P>(value));
    }

    protected final <P> P provideBy(final Class<P> type, final ParameterProvider<P> parameterProvider) {
        builder.parameter(parameterProvider);
        return DefaultValue.get(type);
    }

    protected abstract class DelegateeRegister<T> {
        private final T target;

        protected DelegateeRegister(final String name) {
            this();
            builder.withName(name);
        }

        protected DelegateeRegister() {
            target = builder.to(getDelegateeType());
        }

        private Class<T> getDelegateeType() {
            return (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), DelegateeRegister.class);
        }

        protected void register(final S source) {
        }

        protected final T delegate() {
            builder.delegate();
            return target;
        }

        protected final T delegate(final Object delegator) {
            return delegate();
        }
    }
}

final class At<P> implements ParameterProvider<P> {
    private final int position;

    public At(final int position) {
        if (position < 0) {
            throw new IllegalArgumentException("Parameter position(int) must start from 0.");
        }
        this.position = position;
    }

    @SuppressWarnings("unchecked")
    public P get(final Object[] arguments) {
        return (P) arguments[position];
    }

    public void validate(final Class<?>[] parameterType) {
        if (position >= parameterType.length) {
            throw new IllegalArgumentException("Parameter position " + position
                    + " must not less than parameter count " + parameterType.length + " of delegator method.");
        }
    }
}

final class Constant<P> implements ParameterProvider<P> {
    private final P value;

    public Constant(final P value) {
        this.value = value;
    }

    public P get(final Object[] arguments) {
        return value;
    }

    public void validate(final Class<?>[] parameterType) {
    }
}

final class TypeOf<P> implements ParameterProvider<P> {
    private final Class<?> type;

    public TypeOf(final Class<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("Parameter type (Class<P) must not be null.");
        }
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public P get(final Object[] arguments) {
        for (final Object argument : arguments) {
            if (argument != null && type.isInstance(argument)) {
                return (P) argument;
            }
        }

        return null;
    }

    public void validate(final Class<?>[] parameterTypes) {
        final List<Class<?>> matchedParameterTypes = new ArrayList<Class<?>>();
        for (final Class<?> parameterType : parameterTypes) {
            if (type == parameterType) {
                matchedParameterTypes.add(parameterType);
            }
        }

        if (matchedParameterTypes.size() != 1) {
            throw new IllegalArgumentException("There is one and only one parameter of type " + type
                    + " allowed in delegator method.");
        }
    }
}
