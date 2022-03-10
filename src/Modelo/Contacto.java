package Modelo;


import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Juan Pablo Palomares Avila y Alvaro Bulnes Ramos
 */
public class Contacto {
   
    String nombre;
    String apellido;
    String mail;
    int telofono;
    String direccion;
    Date fechanacimiento;
    String categoria;
    int edad;
    

    public Contacto( String nombre, String apellido, String mail, int telofono, String direccion, Date fechanacimiento, String categoria) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.telofono = telofono;
        this.direccion = direccion;
        this.fechanacimiento = fechanacimiento;
        this.categoria = categoria;
        
    }

   

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getMail() {
        return mail;
    }

    public int getTelofono() {
        return telofono;
    }

    public String getDireccion() {
        return direccion;
    }

    public Date getFechanacimiento() {
        return fechanacimiento;
    }

    public String getCategoria() {
        return categoria;
    }

 

    public int getEdad() {
        return edad;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

 
    
}
