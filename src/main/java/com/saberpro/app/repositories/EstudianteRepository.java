package com.saberpro.app.repositories;

import com.saberpro.app.models.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    
    Estudiante findByNumeroRegistro(String numeroRegistro);
    
    List<Estudiante> findByEstado(String estado);
    
    List<Estudiante> findByEstadoOrderByPuntajeSaberProDesc(String estado);
    
    List<Estudiante> findByPrimerApellidoContainingIgnoreCaseOrNumeroRegistroContaining(
        String apellido, String registro);
    
    // ✅ MÉTODO CORREGIDO: Solo buscar por apellido
    @Query("SELECT e FROM Estudiante e WHERE UPPER(e.primerApellido) = UPPER(:apellido)")
    Estudiante findByPrimerApellidoExacto(@Param("apellido") String apellido);
    
    Estudiante findByPrimerApellidoIgnoreCase(String apellido); 
    
    Estudiante findByPrimerApellidoIgnoreCaseAndEstado(String apellido, String estado);
    
    @Query("SELECT COUNT(e) FROM Estudiante e WHERE e.estado = 'ACTIVO'")
    Long countEstudiantesActivos();
    
    @Query("SELECT AVG(e.puntajeSaberPro) FROM Estudiante e WHERE e.estado = 'ACTIVO'")
    Double promedioSaberPro();
    
    @Query("SELECT e FROM Estudiante e WHERE e.estado = 'ACTIVO' ORDER BY e.puntajeSaberPro DESC")
    List<Estudiante> findTop10ByOrderByPuntajeSaberProDesc();
}