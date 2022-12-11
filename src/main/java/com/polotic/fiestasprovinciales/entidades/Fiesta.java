package com.polotic.fiestasprovinciales.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name="fiestas")

public class Fiesta {
    
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 250, message= "Nombre demasiado largo")
    private String nombre;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 250, message= "Nombre demasiado largo")
    private String descripcion;

    private Date fecha;
    
    private String enlace;

    private String foto;

    // private Predio predio;

}
