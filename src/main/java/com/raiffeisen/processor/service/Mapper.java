package com.raiffeisen.processor.service;

public interface Mapper<D, E> {
    D toDto(E entity);

    E toEntity(D dto);
}
