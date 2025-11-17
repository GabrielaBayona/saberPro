package com.saberpro.app.models;

import jakarta.persistence.*;

@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos personales
    private String tipoDocumento;
    private String numeroDocumento;
    private String primerApellido;
    private String segundoApellido;
    private String primerNombre;
    private String segundoNombre;
    private String correoElectronico;
    private String numeroTelefonico;
    private String numeroRegistro;

    // Puntajes generales
    private Integer puntajeSaberPro;
    private String nivelSaberPro;

    // Comunicación Escrita
    private Integer comunicacionEscrita;
    private String comunicacionEscritaNivel;

    // Razonamiento Cuantitativo
    private Integer razonamientoCuantitativo;
    private String razonamientoCuantitativoNivel;

    // Lectura Crítica
    private Integer lecturaCritica;
    private String lecturaCriticaNivel;

    // Competencias Ciudadanas
    private Integer competenciasCiudadanas;
    private String competenciasCiudadanasNivel;

    // Inglés
    private Integer ingles;
    private String inglesNivel;
    private String nivelIngles; // B1, B2, A2, etc.

    // Formulación de Proyectos
    private Integer formulacionProyectos;
    private String formulacionProyectosNivel;

    // Pensamiento Científico
    private Integer pensamientoCientifico;
    private String pensamientoCientificoNivel;

    // Diseño de Software
    private Integer disenoSoftware;
    private String disenoSoftwareNivel;

    // Estado
    private String estado; // ACTIVO, ANULADO

    // Constructores
    public Estudiante() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNumeroTelefonico() {
        return numeroTelefonico;
    }

    public void setNumeroTelefonico(String numeroTelefonico) {
        this.numeroTelefonico = numeroTelefonico;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public Integer getPuntajeSaberPro() {
        return puntajeSaberPro;
    }

    public void setPuntajeSaberPro(Integer puntajeSaberPro) {
        this.puntajeSaberPro = puntajeSaberPro;
    }

    public String getNivelSaberPro() {
        return nivelSaberPro;
    }

    public void setNivelSaberPro(String nivelSaberPro) {
        this.nivelSaberPro = nivelSaberPro;
    }

    public Integer getComunicacionEscrita() {
        return comunicacionEscrita;
    }

    public void setComunicacionEscrita(Integer comunicacionEscrita) {
        this.comunicacionEscrita = comunicacionEscrita;
    }

    public String getComunicacionEscritaNivel() {
        return comunicacionEscritaNivel;
    }

    public void setComunicacionEscritaNivel(String comunicacionEscritaNivel) {
        this.comunicacionEscritaNivel = comunicacionEscritaNivel;
    }

    public Integer getRazonamientoCuantitativo() {
        return razonamientoCuantitativo;
    }

    public void setRazonamientoCuantitativo(Integer razonamientoCuantitativo) {
        this.razonamientoCuantitativo = razonamientoCuantitativo;
    }

    public String getRazonamientoCuantitativoNivel() {
        return razonamientoCuantitativoNivel;
    }

    public void setRazonamientoCuantitativoNivel(String razonamientoCuantitativoNivel) {
        this.razonamientoCuantitativoNivel = razonamientoCuantitativoNivel;
    }

    public Integer getLecturaCritica() {
        return lecturaCritica;
    }

    public void setLecturaCritica(Integer lecturaCritica) {
        this.lecturaCritica = lecturaCritica;
    }

    public String getLecturaCriticaNivel() {
        return lecturaCriticaNivel;
    }

    public void setLecturaCriticaNivel(String lecturaCriticaNivel) {
        this.lecturaCriticaNivel = lecturaCriticaNivel;
    }

    public Integer getCompetenciasCiudadanas() {
        return competenciasCiudadanas;
    }

    public void setCompetenciasCiudadanas(Integer competenciasCiudadanas) {
        this.competenciasCiudadanas = competenciasCiudadanas;
    }

    public String getCompetenciasCiudadanasNivel() {
        return competenciasCiudadanasNivel;
    }

    public void setCompetenciasCiudadanasNivel(String competenciasCiudadanasNivel) {
        this.competenciasCiudadanasNivel = competenciasCiudadanasNivel;
    }

    public Integer getIngles() {
        return ingles;
    }

    public void setIngles(Integer ingles) {
        this.ingles = ingles;
    }

    public String getInglesNivel() {
        return inglesNivel;
    }

    public void setInglesNivel(String inglesNivel) {
        this.inglesNivel = inglesNivel;
    }

    public String getNivelIngles() {
        return nivelIngles;
    }

    public void setNivelIngles(String nivelIngles) {
        this.nivelIngles = nivelIngles;
    }

    public Integer getFormulacionProyectos() {
        return formulacionProyectos;
    }

    public void setFormulacionProyectos(Integer formulacionProyectos) {
        this.formulacionProyectos = formulacionProyectos;
    }

    public String getFormulacionProyectosNivel() {
        return formulacionProyectosNivel;
    }

    public void setFormulacionProyectosNivel(String formulacionProyectosNivel) {
        this.formulacionProyectosNivel = formulacionProyectosNivel;
    }

    public Integer getPensamientoCientifico() {
        return pensamientoCientifico;
    }

    public void setPensamientoCientifico(Integer pensamientoCientifico) {
        this.pensamientoCientifico = pensamientoCientifico;
    }

    public String getPensamientoCientificoNivel() {
        return pensamientoCientificoNivel;
    }

    public void setPensamientoCientificoNivel(String pensamientoCientificoNivel) {
        this.pensamientoCientificoNivel = pensamientoCientificoNivel;
    }

    public Integer getDisenoSoftware() {
        return disenoSoftware;
    }

    public void setDisenoSoftware(Integer disenoSoftware) {
        this.disenoSoftware = disenoSoftware;
    }

    public String getDisenoSoftwareNivel() {
        return disenoSoftwareNivel;
    }

    public void setDisenoSoftwareNivel(String disenoSoftwareNivel) {
        this.disenoSoftwareNivel = disenoSoftwareNivel;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método helper para obtener nombre completo
    public String getNombreCompleto() {
        StringBuilder nombre = new StringBuilder();
        if (primerNombre != null) nombre.append(primerNombre).append(" ");
        if (segundoNombre != null) nombre.append(segundoNombre).append(" ");
        if (primerApellido != null) nombre.append(primerApellido).append(" ");
        if (segundoApellido != null) nombre.append(segundoApellido);
        return nombre.toString().trim();
    }
}