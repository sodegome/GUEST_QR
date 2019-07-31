package com.example.guest_qr.Clases;

import java.util.Date;

public class Invitados {
    private int id;
    private String name;
    private String apellido;
    private String cell;
    private String identificacion;
    private String tipoId;
    private String estado;
    private String email;
    private int user_id;
    private Date fechaCreacion;
    private Date fechaActualizacion;


    public Invitados(int id, String name, String apellido, String cell, String identificacion, String tipoId, String estado, String email, int user_id, Date fechaCreacion, Date fechaActualizacion) {
        this.id = id;
        this.name = name;
        this.apellido = apellido;
        this.cell = cell;
        this.identificacion = identificacion;
        this.tipoId = tipoId;
        this.estado = estado;
        this.email = email;
        this.user_id = user_id;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoId() {
        return tipoId;
    }

    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public String toString() {
        return "Invitados{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", apellido='" + apellido + '\'' +
                ", cell='" + cell + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", tipoId='" + tipoId + '\'' +
                ", estado='" + estado + '\'' +
                ", email='" + email + '\'' +
                ", user_id=" + user_id +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}
