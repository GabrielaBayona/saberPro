package com.saberpro.app.services;

import com.saberpro.app.models.Estudiante;
import com.saberpro.app.models.Usuario;
import com.saberpro.app.repositories.EstudianteRepository;
import com.saberpro.app.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List; 

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // ========== CREAR USUARIO ADMIN ==========
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setCorreo("admin@uts.edu.co");
            admin.setPassword("admin123");
            admin.setRol("ADMIN");
            usuarioRepository.save(admin);

            System.out.println("✅ Usuario administrador creado:");
            System.out.println("   Correo: admin@uts.edu.co");
            System.out.println("   Contraseña: admin123");
        }
        
        // ========== CARGAR DATOS DE ESTUDIANTES ==========
        if (estudianteRepository.count() == 0) {
            cargarDatosEstudiantes();
            System.out.println("✅ " + estudianteRepository.count() + " estudiantes cargados desde datos Saber Pro 2025");
            
            // Crear usuarios para cada estudiante
            crearUsuariosEstudiantes();
        }
    }
    
    private void crearUsuariosEstudiantes() {
        System.out.println("\n📧 Creando usuarios para estudiantes...");
        
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        int creados = 0;
        
        for (Estudiante e : estudiantes) {
            if (e.getPrimerApellido() != null && !e.getPrimerApellido().isEmpty()) {
                String apellido = e.getPrimerApellido().toLowerCase();
                String correo = apellido + "@uts.edu.co";
                String password = apellido + "123";
                
                // Verificar si ya existe
                Usuario existente = usuarioRepository.findByCorreo(correo);
                if (existente == null) {
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setCorreo(correo);
                    nuevoUsuario.setPassword(password);
                    nuevoUsuario.setRol("USUARIO");
                    usuarioRepository.save(nuevoUsuario);
                    creados++;
                }
            }
        }
        
        System.out.println("✅ " + creados + " usuarios de estudiantes creados");
        System.out.println("   Formato: apellido@uts.edu.co / apellido123");
        System.out.println("   Ejemplo: barbosa@uts.edu.co / barbosa123");
    }
    
    private void cargarDatosEstudiantes() {
        
        // Estudiante 1 - BARBOSA
        crearEstudiante("CC", "BARBOSA", "EK20183007722", 200, "Nivel 4", 
            128, "Nivel 2", 182, "Nivel 3", 202, "Nivel 4", 206, "Nivel 4", 
            183, "Nivel 3", "B1", 185, "Nivel 3", 160, "Nivel 3", 197, "Nivel 4", "ACTIVO");
        
        // Estudiante 2 - QUINTERO
        crearEstudiante("CC", "QUINTERO", "EK20183140703", 165, "Nivel 3", 
            125, "Nivel 1", 151, "Nivel 2", 179, "Nivel 3", 163, "Nivel 3", 
            205, "Nivel 4", "B2", 182, "Nivel 3", 144, "Nivel 2", 136, "Nivel 2", "ACTIVO");
        
        // Estudiante 3 - PARRA
        crearEstudiante("CC", "PARRA", "EK20183040545", 164, "Nivel 3", 
            159, "Nivel 3", 172, "Nivel 3", 182, "Nivel 3", 142, "Nivel 2", 
            165, "Nivel 3", "A2", 167, "Nivel 3", 132, "Nivel 2", 148, "Nivel 2", "ACTIVO");
        
        // Estudiante 4 - ANAYA
        crearEstudiante("CC", "ANAYA", "EK20183025381", 160, "Nivel 3", 
            146, "Nivel 2", 199, "Nivel 4", 157, "Nivel 3", 149, "Nivel 2", 
            147, "Nivel 2", "A2", 174, "Nivel 3", 127, "Nivel 2", 171, "Nivel 3", "ACTIVO");
        
        // Estudiante 5 - FLOR
        crearEstudiante("CC", "FLOR", "EK20183025335", 160, "Nivel 3", 
            198, "Nivel 4", 153, "Nivel 2", 147, "Nivel 2", 157, "Nivel 3", 
            146, "Nivel 2", "A2", 168, "Nivel 3", 114, "Nivel 1", 160, "Nivel 3", "ACTIVO");
        
        // Estudiante 6 - GARCIA
        crearEstudiante("CC", "GARCIA", "EK20183122648", 157, "Nivel 3", 
            179, "Nivel 3", 172, "Nivel 3", 158, "Nivel 3", 140, "Nivel 2", 
            136, "Nivel 2", "A1", 128, "Nivel 2", 121, "Nivel 1", 142, "Nivel 2", "ACTIVO");
        
        // Estudiante 7 - MANOSALVA
        crearEstudiante("CC", "MANOSALVA", "EK20183064605", 153, "Nivel 2", 
            115, "Nivel 1", 152, "Nivel 2", 159, "Nivel 3", 172, "Nivel 3", 
            165, "Nivel 3", "A2", 142, "Nivel 2", 118, "Nivel 1", 119, "Nivel 1", "ACTIVO");
        
        // Estudiante 8 - MENDOZA
        crearEstudiante("CC", "MENDOZA", "EK20183187351", 151, "Nivel 2", 
            132, "Nivel 2", 123, "Nivel 1", 125, "Nivel 1", 169, "Nivel 3", 
            204, "Nivel 4", "B2", 173, "Nivel 3", 127, "Nivel 2", 171, "Nivel 3", "ACTIVO");
        
        // Estudiante 9 - BELTRAN
        crearEstudiante("CC", "BELTRAN", "EK20183233820", 150, "Nivel 2", 
            86, "Nivel 1", 187, "Nivel 3", 160, "Nivel 3", 171, "Nivel 3", 
            148, "Nivel 2", "A2", 162, "Nivel 3", 125, "Nivel 1", 142, "Nivel 2", "ACTIVO");
        
        // Estudiante 10 - SANTAMARIA
        crearEstudiante("CC", "SANTAMARIA", "EK20183030016", 150, "Nivel 2", 
            175, "Nivel 3", 149, "Nivel 2", 145, "Nivel 2", 158, "Nivel 3", 
            125, "Nivel 1", "A1", 162, "Nivel 3", 76, "Nivel 1", 125, "Nivel 1", "ACTIVO");
        
        // Estudiante 11 - SANCHEZ
        crearEstudiante("CC", "SANCHEZ", "EK20183047073", 149, "Nivel 2", 
            209, "Nivel 4", 143, "Nivel 2", 117, "Nivel 1", 129, "Nivel 2", 
            147, "Nivel 2", "A2", 137, "Nivel 2", 125, "Nivel 1", 136, "Nivel 2", "ACTIVO");
        
        // Estudiante 12 - ROMERO
        crearEstudiante("CC", "ROMERO", "EK20183236451", 146, "Nivel 2", 
            93, "Nivel 1", 183, "Nivel 3", 155, "Nivel 2", 164, "Nivel 3", 
            133, "Nivel 2", "A1", 174, "Nivel 3", 130, "Nivel 2", 154, "Nivel 2", "ACTIVO");
        
        // Estudiante 13 - LUNA
        crearEstudiante("CC", "LUNA", "EK20183041714", 141, "Nivel 2", 
            125, "Nivel 1", 157, "Nivel 3", 138, "Nivel 2", 135, "Nivel 2", 
            152, "Nivel 2", "A2", 176, "Nivel 3", 128, "Nivel 2", 165, "Nivel 3", "ACTIVO");
        
        // Estudiante 14 - TRIANA
        crearEstudiante("CC", "TRIANA", "EK20183187801", 141, "Nivel 2", 
            150, "Nivel 2", 136, "Nivel 2", 145, "Nivel 2", 150, "Nivel 2", 
            126, "Nivel 2", "A1", 148, "Nivel 2", 129, "Nivel 2", 131, "Nivel 2", "ACTIVO");
        
        // Estudiante 15 - SUAREZ
        crearEstudiante("CC", "SUAREZ", "EK20183176566", 140, "Nivel 2", 
            128, "Nivel 2", 146, "Nivel 2", 146, "Nivel 2", 132, "Nivel 2", 
            147, "Nivel 2", "A2", 130, "Nivel 2", 110, "Nivel 1", 125, "Nivel 1", "ACTIVO");
        
        // Estudiante 16 - GARCIA (segundo)
        crearEstudiante("CC", "GARCIA", "EK20183204427", 139, "Nivel 2", 
            129, "Nivel 2", 138, "Nivel 2", 148, "Nivel 2", 146, "Nivel 2", 
            135, "Nivel 2", "A1", 109, "Nivel 1", 107, "Nivel 1", 131, "Nivel 2", "ACTIVO");
        
        // Estudiante 17 - PINZON
        crearEstudiante("CC", "PINZON", "EK20183196280", 138, "Nivel 2", 
            153, "Nivel 2", 123, "Nivel 1", 127, "Nivel 2", 147, "Nivel 2", 
            140, "Nivel 2", "A1", 145, "Nivel 2", 143, "Nivel 2", 160, "Nivel 3", "ACTIVO");
        
        // Estudiante 18 - JAIMES
        crearEstudiante("CC", "JAIMES", "EK20183173799", 137, "Nivel 2", 
            166, "Nivel 3", 157, "Nivel 3", 124, "Nivel 1", 100, "Nivel 1", 
            140, "Nivel 2", "A1", 100, "Nivel 1", 105, "Nivel 1", 113, "Nivel 1", "ACTIVO");
        
        // Estudiante 19 - NIÑO
        crearEstudiante("CC", "NIÑO", "EK20183009565", 134, "Nivel 2", 
            165, "Nivel 3", 137, "Nivel 2", 136, "Nivel 2", 118, "Nivel 1", 
            116, "Nivel 1", "A0", 146, "Nivel 2", 122, "Nivel 1", 154, "Nivel 2", "ACTIVO");
        
        // Estudiante 20 - FABIAN
        crearEstudiante("CC", "FABIAN", "EK20183117756", 133, "Nivel 2", 
            139, "Nivel 2", 93, "Nivel 1", 168, "Nivel 3", 150, "Nivel 2", 
            114, "Nivel 1", "A0", 102, "Nivel 1", 123, "Nivel 1", 94, "Nivel 1", "ACTIVO");
        
        // Estudiante 21 - HERNANDEZ
        crearEstudiante("CC", "HERNANDEZ", "EK20183044579", 132, "Nivel 2", 
            116, "Nivel 1", 166, "Nivel 3", 136, "Nivel 2", 104, "Nivel 1", 
            140, "Nivel 2", "A1", 158, "Nivel 3", 125, "Nivel 1", 154, "Nivel 2", "ACTIVO");
        
        // Estudiante 22 - LARIOS
        crearEstudiante("CC", "LARIOS", "EK20183045760", 131, "Nivel 2", 
            149, "Nivel 2", 123, "Nivel 1", 129, "Nivel 2", 121, "Nivel 1", 
            131, "Nivel 2", "A1", 101, "Nivel 1", 102, "Nivel 1", 165, "Nivel 3", "ACTIVO");
        
        // Estudiante 23 - CALDERON
        crearEstudiante("CC", "CALDERON", "EK20183034044", 130, "Nivel 2", 
            127, "Nivel 2", 147, "Nivel 2", 134, "Nivel 2", 111, "Nivel 1", 
            131, "Nivel 2", "A1", 65, "Nivel 1", 112, "Nivel 1", 94, "Nivel 1", "ACTIVO");
        
        // Estudiante 24 - VILLARREAL
        crearEstudiante("CC", "VILLARREAL", "EK20183041521", 129, "Nivel 2", 
            96, "Nivel 1", 162, "Nivel 3", 114, "Nivel 1", 131, "Nivel 2", 
            144, "Nivel 2", "A1", 122, "Nivel 1", 112, "Nivel 1", 131, "Nivel 2", "ACTIVO");
        
        // Estudiante 25 - RESTREPO
        crearEstudiante("CC", "RESTREPO", "EK20183027436", 126, "Nivel 2", 
            81, "Nivel 1", 134, "Nivel 2", 126, "Nivel 2", 149, "Nivel 2", 
            139, "Nivel 2", "A1", 127, "Nivel 2", 136, "Nivel 2", 142, "Nivel 2", "ACTIVO");
        
        // Estudiante 26 - CACERES
        crearEstudiante("CC", "CACERES", "EK20183031592", 125, "Nivel 1", 
            124, "Nivel 1", 135, "Nivel 2", 108, "Nivel 1", 92, "Nivel 1", 
            165, "Nivel 3", "A2", 132, "Nivel 2", 104, "Nivel 1", 131, "Nivel 2", "ACTIVO");
        
        // Estudiante 27 - TABARES
        crearEstudiante("CC", "TABARES", "EK20183004153", 124, "Nivel 1", 
            131, "Nivel 2", 131, "Nivel 2", 107, "Nivel 1", 88, "Nivel 1", 
            162, "Nivel 3", "A2", 136, "Nivel 2", 112, "Nivel 1", 148, "Nivel 2", "ACTIVO");
        
        // Estudiante 28 - NARANJO
        crearEstudiante("CC", "NARANJO", "EK20183030783", 122, "Nivel 1", 
            166, "Nivel 3", 113, "Nivel 1", 113, "Nivel 1", 112, "Nivel 1", 
            106, "Nivel 1", "A0", 135, "Nivel 2", 117, "Nivel 1", 119, "Nivel 1", "ACTIVO");
        
        // Estudiante 29 - PRADA
        crearEstudiante("CC", "PRADA", "EK20183024754", 122, "Nivel 1", 
            119, "Nivel 1", 125, "Nivel 1", 137, "Nivel 2", 107, "Nivel 1", 
            123, "Nivel 1", "A1", 83, "Nivel 1", 104, "Nivel 1", 119, "Nivel 1", "ACTIVO");
        
        // Estudiante 30 - VARGAS
        crearEstudiante("CC", "VARGAS", "EK20183186200", 114, "Nivel 1", 
            95, "Nivel 1", 120, "Nivel 1", 151, "Nivel 2", 86, "Nivel 1", 
            119, "Nivel 1", "A0", 149, "Nivel 2", 103, "Nivel 1", 119, "Nivel 1", "ACTIVO");
        
        // Estudiante 31 - TORRES
        crearEstudiante("CC", "TORRES", "EK20183182410", 113, "Nivel 1", 
            109, "Nivel 1", 105, "Nivel 1", 104, "Nivel 1", 103, "Nivel 1", 
            142, "Nivel 2", "A1", 102, "Nivel 1", 135, "Nivel 2", 80, "Nivel 1", "ACTIVO");
        
        // Estudiante 32 - ORTIZ
        crearEstudiante("CC", "ORTIZ", "EK20183213735", 107, "Nivel 1", 
            128, "Nivel 2", 81, "Nivel 1", 107, "Nivel 1", 102, "Nivel 1", 
            119, "Nivel 1", "A0", 130, "Nivel 2", 111, "Nivel 1", 125, "Nivel 1", "ACTIVO");
        
        // Estudiante 33 - VILLAMIZAR
        crearEstudiante("CC", "VILLAMIZAR", "EK20183065220", 106, "Nivel 1", 
            134, "Nivel 2", 96, "Nivel 1", 92, "Nivel 1", 110, "Nivel 1", 
            97, "Nivel 1", "A0", 83, "Nivel 1", 107, "Nivel 1", 119, "Nivel 1", "ACTIVO");
        
        // Estudiante 34 - RESTREPO (segundo)
        crearEstudiante("CC", "RESTREPO", "EK20183028123", 96, "Nivel 1", 
            0, "Nivel 1", 117, "Nivel 1", 122, "Nivel 1", 105, "Nivel 1", 
            137, "Nivel 2", "A1", 157, "Nivel 3", 96, "Nivel 1", 131, "Nivel 2", "ACTIVO");
        
        // Estudiante 35 - HIGUERA (ANULADO)
        crearEstudiante("CC", "HIGUERA", "EK20183207870", 0, "", 
            0, "", 0, "", 0, "", 0, "", 
            0, "", "", 0, "", 0, "", 0, "", "ANULADO");
        
        // Estudiante 36 - MATIZ (ANULADO)
        crearEstudiante("CC", "MATIZ", "EK20183144329", 0, "", 
            0, "", 0, "", 0, "", 0, "", 
            0, "", "", 0, "", 0, "", 0, "", "ANULADO");
    }
    
    private void crearEstudiante(String tipoDoc, String apellido, String registro, 
                                  Integer puntaje, String nivelSP,
                                  Integer comEsc, String nivelComEsc,
                                  Integer razCuan, String nivelRazCuan,
                                  Integer lecCrit, String nivelLecCrit,
                                  Integer compCiu, String nivelCompCiu,
                                  Integer ing, String nivelIng, String nivelIngAbrev,
                                  Integer formProy, String nivelFormProy,
                                  Integer pensCien, String nivelPensCien,
                                  Integer disSoft, String nivelDisSoft,
                                  String estado) {
        
        Estudiante e = new Estudiante();
        e.setTipoDocumento(tipoDoc);
        e.setPrimerApellido(apellido);
        e.setNumeroRegistro(registro);
        e.setPuntajeSaberPro(puntaje);
        e.setNivelSaberPro(nivelSP);
        e.setComunicacionEscrita(comEsc);
        e.setComunicacionEscritaNivel(nivelComEsc);
        e.setRazonamientoCuantitativo(razCuan);
        e.setRazonamientoCuantitativoNivel(nivelRazCuan);
        e.setLecturaCritica(lecCrit);
        e.setLecturaCriticaNivel(nivelLecCrit);
        e.setCompetenciasCiudadanas(compCiu);
        e.setCompetenciasCiudadanasNivel(nivelCompCiu);
        e.setIngles(ing);
        e.setInglesNivel(nivelIng);
        e.setNivelIngles(nivelIngAbrev);
        e.setFormulacionProyectos(formProy);
        e.setFormulacionProyectosNivel(nivelFormProy);
        e.setPensamientoCientifico(pensCien);
        e.setPensamientoCientificoNivel(nivelPensCien);
        e.setDisenoSoftware(disSoft);
        e.setDisenoSoftwareNivel(nivelDisSoft);
        e.setEstado(estado);
        
        estudianteRepository.save(e);
    }
}