package org.polyforms.delegation.spring.converter;

import java.util.Collections;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;

/**
 * Adapter of ModelMapper {@link ModelMapper} for Spring
 * {@link org.springframework.core.convert.converter.GenericConverter}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
public class ModelMapperConverter implements ConditionalGenericConverter {
    private final ModelMapper modelMapper;

    /**
     * Create an instance with {@link ModelMapper}.
     */
    @Autowired
    public ModelMapperConverter(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * {@inheritDoc}
     */
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
    }

    /**
     * {@inheritDoc}
     */
    public boolean matches(final TypeDescriptor sourceType, final TypeDescriptor targetType) {
        return !sourceType.isAssignableTo(targetType);
    }

    /**
     * {@inheritDoc}
     */
    public Object convert(final Object source, final TypeDescriptor sourceType, final TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        return modelMapper.map(source, targetType.getType());
    }
}
