package org.polyforms.delegation.builder.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.polyforms.delegation.builder.Delegation;
import org.polyforms.delegation.builder.ParameterProvider;

final class SimpleDelegation implements Delegation {
    private final List<ParameterProvider<?>> parameterProviders = new ArrayList<ParameterProvider<?>>();
    private final Class<?> delegatorType;
    private final Method delegatorMethod;
    private Class<?> delegateeType;
    private String delegateeName;
    private Method delegateeMethod;

    public SimpleDelegation(final Class<?> delegatorType, final Method delegatorMethod) {
        this.delegatorType = delegatorType;
        this.delegatorMethod = delegatorMethod;
    }

    public Class<?> getDelegatorType() {
        return delegatorType;
    }

    public Method getDelegatorMethod() {
        return delegatorMethod;
    }

    public Class<?> getDelegateeType() {
        return delegateeType;
    }

    public Method getDelegateeMethod() {
        return delegateeMethod;
    }

    public String getDelegateeName() {
        return delegateeName;
    }

    public List<ParameterProvider<?>> getParameterProviders() {
        return Collections.unmodifiableList(parameterProviders);
    }

    protected void addParameterProvider(final ParameterProvider<?> parameterProvider) {
        parameterProviders.add(parameterProvider);
    }

    protected void setDelegateeType(final Class<?> delegateeType) {
        this.delegateeType = delegateeType;
    }

    protected void setDelegateeMethod(final Method delegateeMethod) {
        this.delegateeMethod = delegateeMethod;
    }

    protected void setDelegateeName(final String delegateeName) {
        this.delegateeName = delegateeName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + delegatorType.hashCode();
        result = prime * result + delegatorMethod.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof SimpleDelegation)) {
            return false;
        }

        final SimpleDelegation other = (SimpleDelegation) obj;
        return delegatorType == other.delegatorType && delegatorMethod.equals(other.delegatorMethod);
    }
}
