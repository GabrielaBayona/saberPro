package com.saberpro.app.controllers;

import com.saberpro.app.models.Estudiante;
import com.saberpro.app.models.Usuario;
import com.saberpro.app.repositories.EstudianteRepository;
import com.saberpro.app.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin/estudiantes")
public class AdminEstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @GetMapping
    public String listar(HttpSession session, Model model,
                        @RequestParam(required = false) String busqueda) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        List<Estudiante> estudiantes;
        List<Estudiante> anulados = new ArrayList<>();
        
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            List<Estudiante> todosResultados = estudianteRepository.findByPrimerApellidoContainingIgnoreCaseOrNumeroRegistroContaining(
                busqueda, busqueda);
            
            estudiantes = new ArrayList<>();
            for (Estudiante e : todosResultados) {
                if ("ANULADO".equals(e.getEstado())) {
                    anulados.add(e);
                } else {
                    estudiantes.add(e);
                }
            }
            
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("mostrandoResultados", true);
        } else {
            estudiantes = estudianteRepository.findByEstadoOrderByPuntajeSaberProDesc("ACTIVO");
            anulados = estudianteRepository.findByEstado("ANULADO");
            model.addAttribute("mostrandoResultados", false);
        }

        // Calcular estadísticas
        double promedio = 0;
        int maximo = 0;
        int minimo = 0;
        long nivel4 = 0;
        long nivel3 = 0;
        long nivel2 = 0;
        long nivel1 = 0;

        if (!estudiantes.isEmpty()) {
            int suma = 0;
            minimo = Integer.MAX_VALUE;
            
            for (Estudiante e : estudiantes) {
                int puntaje = e.getPuntajeSaberPro() != null ? e.getPuntajeSaberPro() : 0;
                suma += puntaje;
                if (puntaje > maximo) maximo = puntaje;
                if (puntaje < minimo) minimo = puntaje;
                
                if ("Nivel 4".equals(e.getNivelSaberPro())) nivel4++;
                else if ("Nivel 3".equals(e.getNivelSaberPro())) nivel3++;
                else if ("Nivel 2".equals(e.getNivelSaberPro())) nivel2++;
                else if ("Nivel 1".equals(e.getNivelSaberPro())) nivel1++;
            }
            
            promedio = (double) suma / estudiantes.size();
        }

        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("anulados", anulados);
        model.addAttribute("totalEstudiantes", estudiantes.size());
        model.addAttribute("totalAnulados", anulados.size());
        model.addAttribute("promedio", String.format("%.1f", promedio));
        model.addAttribute("maximo", maximo);
        model.addAttribute("minimo", minimo);
        model.addAttribute("nivel4", nivel4);
        model.addAttribute("nivel3", nivel3);
        model.addAttribute("nivel2", nivel2);
        model.addAttribute("nivel1", nivel1);
        model.addAttribute("usuario", usuario);
        
        return "admin/estudiantes";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, HttpSession session, Model model) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
        
        if (estudiante == null) {
            return "redirect:/admin/estudiantes";
        }

        model.addAttribute("estudiante", estudiante);
        model.addAttribute("usuario", usuario);
        
        return "admin/estudiante-detalle";
    }

    // ============================================
    // NUEVOS MÉTODOS PARA CREAR/EDITAR ESTUDIANTE
    // ============================================

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        model.addAttribute("estudiante", new Estudiante());
        model.addAttribute("usuario", usuario);
        model.addAttribute("modoEdicion", false);
        
        return "admin/estudiante-form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
        
        if (estudiante == null) {
            return "redirect:/admin/estudiantes";
        }

        model.addAttribute("estudiante", estudiante);
        model.addAttribute("usuario", usuario);
        model.addAttribute("modoEdicion", true);
        
        return "admin/estudiante-form";
    }

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Estudiante estudiante, 
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            // Calcular automáticamente los niveles basados en los puntajes
            calcularNiveles(estudiante);
            
            // Calcular puntaje promedio general
            calcularPuntajeGeneral(estudiante);
            
            // Guardar el estudiante
            estudianteRepository.save(estudiante);
            
            // ✅ CREAR USUARIO AUTOMÁTICAMENTE (solo si NO está editando)
            if (estudiante.getId() == null && estudiante.getPrimerApellido() != null) {
                String apellido = estudiante.getPrimerApellido().toLowerCase().trim();
                String correo = apellido + "@uts.edu.co";
                
                // Verificar si ya existe
                Usuario usuarioExistente = usuarioRepository.findByCorreo(correo);
                
                if (usuarioExistente == null) {
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setCorreo(correo);
                    nuevoUsuario.setPassword(apellido + "123");
                    nuevoUsuario.setRol("USUARIO");
                    
                    usuarioRepository.save(nuevoUsuario);
                    
                    redirectAttributes.addFlashAttribute("success", 
                        "✅ Estudiante y usuario creados exitosamente. Credenciales: " + correo + " / " + apellido + "123");
                } else {
                    redirectAttributes.addFlashAttribute("success", 
                        "✅ Estudiante guardado (el usuario " + correo + " ya existía)");
                }
            } else if (estudiante.getId() != null) {
                // Solo está editando
                redirectAttributes.addFlashAttribute("success", 
                    "✅ Estudiante actualizado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("success", 
                    "✅ Estudiante guardado");
            }
            
            return "redirect:/admin/estudiantes";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "❌ Error al guardar: " + e.getMessage());
            return "redirect:/admin/estudiantes/nuevo";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, 
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            estudianteRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", 
                "🗑️ Estudiante eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "❌ Error al eliminar el estudiante");
        }
        
        return "redirect:/admin/estudiantes";
    }

    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================

    private void calcularNiveles(Estudiante est) {
        // Comunicación Escrita
        if (est.getComunicacionEscrita() != null) {
            est.setComunicacionEscritaNivel(obtenerNivel(est.getComunicacionEscrita()));
        }
        
        // Razonamiento Cuantitativo
        if (est.getRazonamientoCuantitativo() != null) {
            est.setRazonamientoCuantitativoNivel(obtenerNivel(est.getRazonamientoCuantitativo()));
        }
        
        // Lectura Crítica
        if (est.getLecturaCritica() != null) {
            est.setLecturaCriticaNivel(obtenerNivel(est.getLecturaCritica()));
        }
        
        // Competencias Ciudadanas
        if (est.getCompetenciasCiudadanas() != null) {
            est.setCompetenciasCiudadanasNivel(obtenerNivel(est.getCompetenciasCiudadanas()));
        }
        
        // Inglés
        if (est.getIngles() != null) {
            est.setInglesNivel(obtenerNivel(est.getIngles()));
            est.setNivelIngles(obtenerNivelIngles(est.getIngles()));
        }
        
        // Formulación de Proyectos
        if (est.getFormulacionProyectos() != null) {
            est.setFormulacionProyectosNivel(obtenerNivel(est.getFormulacionProyectos()));
        }
        
        // Pensamiento Científico
        if (est.getPensamientoCientifico() != null) {
            est.setPensamientoCientificoNivel(obtenerNivel(est.getPensamientoCientifico()));
        }
        
        // Diseño de Software
        if (est.getDisenoSoftware() != null) {
            est.setDisenoSoftwareNivel(obtenerNivel(est.getDisenoSoftware()));
        }
    }

    private String obtenerNivel(int puntaje) {
        if (puntaje >= 180) return "Nivel 4";
        if (puntaje >= 150) return "Nivel 3";
        if (puntaje >= 120) return "Nivel 2";
        return "Nivel 1";
    }

    private String obtenerNivelIngles(int puntaje) {
        if (puntaje >= 200) return "B2";
        if (puntaje >= 160) return "B1";
        if (puntaje >= 130) return "A2";
        if (puntaje >= 100) return "A1";
        return "A0";
    }

    private void calcularPuntajeGeneral(Estudiante est) {
        int suma = 0;
        int cantidad = 0;

        if (est.getComunicacionEscrita() != null) {
            suma += est.getComunicacionEscrita();
            cantidad++;
        }
        if (est.getRazonamientoCuantitativo() != null) {
            suma += est.getRazonamientoCuantitativo();
            cantidad++;
        }
        if (est.getLecturaCritica() != null) {
            suma += est.getLecturaCritica();
            cantidad++;
        }
        if (est.getCompetenciasCiudadanas() != null) {
            suma += est.getCompetenciasCiudadanas();
            cantidad++;
        }
        if (est.getIngles() != null) {
            suma += est.getIngles();
            cantidad++;
        }
        if (est.getFormulacionProyectos() != null) {
            suma += est.getFormulacionProyectos();
            cantidad++;
        }
        if (est.getPensamientoCientifico() != null) {
            suma += est.getPensamientoCientifico();
            cantidad++;
        }
        if (est.getDisenoSoftware() != null) {
            suma += est.getDisenoSoftware();
            cantidad++;
        }

        if (cantidad > 0) {
            int promedio = suma / cantidad;
            est.setPuntajeSaberPro(promedio);
            est.setNivelSaberPro(obtenerNivel(promedio));
        }
    }
}