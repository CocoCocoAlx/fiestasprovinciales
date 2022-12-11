package com.polotic.fiestasprovinciales.controladores;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polotic.fiestasprovinciales.entidades.Fiesta;
import com.polotic.fiestasprovinciales.servicios.FiestaServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("fiestas")
public class FiestaControlador implements WebMvcConfigurer {

    @Autowired
    FiestaServicio fiestaServicio;

    // @Autowired
    // PredioServicio predioServicio;

    @GetMapping
    private ModelAndView inicio()
    {
        ModelAndView maw = new ModelAndView();
        maw.setViewName("fragments/base");
        maw.addObject("titulo", "Listado de festividades");
        maw.addObject("vista", "fiestas/inicio");
        maw.addObject("fiestas", fiestaServicio.mostrarTodos());
        return maw;

    }

    @GetMapping("/agregar")
    private ModelAndView agregar(Fiesta fiesta)
    {
        ModelAndView maw = new ModelAndView();
        maw.setViewName("fragments/base");
        maw.addObject("titulo", "Agregar festividad");
        maw.addObject("vista", "fiestas/agregar");
        // maw.addObject("predio", fiestaServicio.mostrarTodos());
        return maw;

    }

    @PostMapping("/agregar")
    public ModelAndView guardar(@RequestParam("archivo") MultipartFile archivo,
    @Valid Fiesta fiesta, BindingResult br, RedirectAttributes ra)
    {
        if (archivo.isEmpty())
            br.reject("archivo", "Por favor, cargar un archivo válido");

        if (br.hasErrors()) {
            return this.agregar(fiesta);
        }

        fiestaServicio.guardar(fiesta);

        String tipo = archivo.getContentType();
        String extension = "." + tipo.substring(tipo.indexOf('/') + 1, tipo.length());
        String foto = fiesta.getId() + extension;
        String ruta = Paths.get("/scr/main/resources/static/images/fiestas", foto).toAbsolutePath().toString();

        ModelAndView maw = this.inicio();

        try {
            archivo.transferTo(new File(ruta));
        } catch (Exception error) {
            maw.addObject("error", "No se pudo guardar el archivo.");
            return maw;
        }

        fiesta.setFoto(foto);
        fiestaServicio.guardar(fiesta);
        maw.addObject("correcto", "El archivo fue cargado exitosamente");
        return maw;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id, Fiesta fiesta) {
        return this.editar(id, fiesta, true);
    }

    @GetMapping("/editar/{id}")
    private ModelAndView editar(@PathVariable("id") Long id, Fiesta fiesta, boolean estaGuardado) {
        ModelAndView maw = new ModelAndView();
        maw.setViewName("fragments/base");
        maw.addObject("titulo", "Editar festividad");
        maw.addObject("vista", "fiestas/editar");
        // maw.addObject("predio", fiestaServicio.mostrarTodos());

        if (estaGuardado)
            maw.addObject("fiesta", fiestaServicio.seleccionarPorId(id));
        else
            fiesta.setFoto(fiestaServicio.seleccionarPorId(id).getFoto());

        return maw;
    }

    @PutMapping("editar/{id}")
    private ModelAndView actualizar(@PathVariable("id") Long id,
    @RequestParam(value = "archivo", required = false) MultipartFile archivo,
    @Valid Fiesta fiesta, BindingResult br, RedirectAttributes ra)
    {
        if (br.hasErrors())
        {
        return this.editar(id, fiesta, false);
        }

        Fiesta registro = fiestaServicio.seleccionarPorId(id);
        registro.setNombre(fiesta.getNombre());
        registro.setDescripcion(fiesta.getDescripcion());
        registro.setFecha(fiesta.getFecha());
        registro.setEnlace(fiesta.getEnlace());
        ModelAndView maw = this.inicio();

        if (! archivo.isEmpty())
        {
        String tipo = archivo.getContentType();
        String extension = "." + tipo.substring(tipo.indexOf('/') + 1, tipo.length());
        String foto = fiesta.getId() + extension;
        String ruta = Paths.get("/scr/main/resources/static/images/fiestas", foto).toAbsolutePath().toString();

        try {
            archivo.transferTo(new File(ruta));
        } catch (Exception error) {
            maw.addObject("error", "No se pudo guardar el archivo.");
            return maw;
        }

        registro.setFoto(foto);
        }

        fiestaServicio.guardar(fiesta);
        maw.addObject("correcto","Festividad editada correctamente.");
        return maw;
    }

    @DeleteMapping("/id")
    private ModelAndView borrar(@PathVariable("id") Long id)
    {
        fiestaServicio.borrar(id);
        ModelAndView maw = this.inicio();
        maw.addObject("correcto", "Festividad eliminada correctamente.");
        return maw;
    }
}
