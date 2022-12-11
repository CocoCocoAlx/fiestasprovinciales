package com.polotic.fiestasprovinciales.repositorios;

import org.springframework.data.repository.CrudRepository;

import com.polotic.fiestasprovinciales.entidades.Fiesta;

public interface FiestaRepositorio extends CrudRepository<Fiesta, Long> {
    
}
