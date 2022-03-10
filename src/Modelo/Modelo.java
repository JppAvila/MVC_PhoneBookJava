package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Juan Pablo Palomares Avila y Alvaro Bulnes Ramos
 */
public class Modelo {

    static Connection conexion;

    public void MySQLConnection(String driver,String url, String user, String pass) throws SQLException {
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(url, user,
                    pass);
        } catch (ClassNotFoundException ex) {
            System.out.println("Clase no encontrada");
        } 
    }

    public int contactosExistentes() {
        int contador = 0;
        try {
            Statement sta = conexion.createStatement();
            String consulta = "SELECT * FROM CONTACTOS ";
            ResultSet rs = sta.executeQuery(consulta);
            while (rs.next()) {
                contador++;
            }
            rs.close();
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error en contactos existentes");
        }

        return contador;

    }
   //Hay que hacer una base de datos con autoincremento y poner el 4 a null
    public void insertarContacto(Contacto contacto) {
     java.sql.Date fecnac= utilDateToSqlDate(contacto.getFechanacimiento());
     String query="INSERT INTO CONTACTOS VALUES (4,'" + contacto.getNombre() + "','" + contacto.getApellido() + "','" + contacto.getMail() + "'," + contacto.getTelofono() + ",'" + contacto.getDireccion() 
             + "','" + fecnac + "','" + contacto.getCategoria() + "',0)";
 
        try {

            Statement sta = conexion.createStatement();
            sta.executeUpdate(query);
           
            sta.close();
            
        } catch (SQLException ex) {
            System.out.println("slq exception en insertar");
        }

    }

    public LinkedList selectBBDDInfo() {
        LinkedList infoJtable = new LinkedList();
        try {
            Statement sta = conexion.createStatement();
            String consulta = "SELECT * FROM CONTACTOS ";
            ResultSet rs = sta.executeQuery(consulta);
            while (rs.next()) {
                infoJtable.add(rs.getInt("ID"));
                infoJtable.add(rs.getString("NOMBRE"));
                infoJtable.add(rs.getString("APELLIDO"));
                infoJtable.add(rs.getString("MAIL"));
            }
            rs.close();
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error  en el select");
        }
        return infoJtable;
    }

    public Contacto selectContacto(int id) {
        String name = null;
        String ape = null;
        String mail = null;
        String direccion = null;
        int telefono = 0;
        Date fecnac = null;
        String categoria = null;

        try {
            Statement sta = conexion.createStatement();
            String consulta = "SELECT * FROM CONTACTOS WHERE ID = '" + id + "'";
            ResultSet rs = sta.executeQuery(consulta);
            while (rs.next()) {
                name = rs.getString("NOMBRE");
                ape = rs.getString("APELLIDO");
                mail = rs.getString("MAIL");
                direccion = rs.getString("DIRECCION");
                telefono = rs.getInt("TELEFONO");
                fecnac = rs.getDate("FECNAC");
                categoria = rs.getString("CATEGORIA");
            }
            rs.close();
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error  en el select que devuelve un contacto");
        }
      
        return new Contacto(name, ape, mail, telefono, direccion, fecnac, categoria);
    }

    public void updatePorId(int id, Contacto co) {
        String campos = "nombre = ?, apellido = ?, mail = ?, telefono = ?";
        campos += ", direccion = ?, fecnac = ?, categoria = ?";
        String query = "UPDATE contactos SET " + campos + " WHERE id = " + id;
        try (PreparedStatement ps = this.conexion.prepareStatement(query);) {
            cargarDatosDeContactoEnSentencia(co, ps);
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo actualizar contacto\n" + co, ex);
        }
    }

    public void borrarContacto(String nombre) {
        try {
            Statement sta = conexion.createStatement();
            sta.executeUpdate("DELETE FROM CONTACTOS WHERE NOMBRE='" + nombre + "'");
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error sql en el update");
        }

    }

    public void borrarAgenda() {
        try {
            Statement sta = conexion.createStatement();
            sta.executeUpdate("DELETE FROM CONTACTOS ");
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error sql en el update");
        }

    }

    public void borrarContacto(int id) {
        try {
            Statement sta = conexion.createStatement();
            sta.executeUpdate("DELETE FROM CONTACTOS WHERE ID = " + id);
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error sql en el update");
        }

    }

    public void cerrarConexion() {
        try {
            this.conexion.close();
        } catch (SQLException ex) {
            System.out.println("Error en la conexion");
        }
    }

    public LinkedList filtrarPorCategoria(String categoria) {
        
        LinkedList infoJtable = new LinkedList();
        try {
            Statement sta = conexion.createStatement();
            String consulta = "SELECT * FROM CONTACTOS WHERE CATEGORIA='"+categoria+"'";
            ResultSet rs = sta.executeQuery(consulta);
            while (rs.next()) {
                infoJtable.add(rs.getInt("ID"));
                infoJtable.add(rs.getString("NOMBRE"));
                infoJtable.add(rs.getString("APELLIDO"));
                infoJtable.add(rs.getString("MAIL"));
            }
            rs.close();
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error  en el select");
        }
        return infoJtable;
    }
    
    public void agregarContacto(Contacto co) {
        String query = "INSERT INTO contactos VALUES (null,?,?,?,?,?,?,?,0)";
      
        try (PreparedStatement ps = this.conexion.prepareStatement(query);) {
            cargarDatosDeContactoEnSentencia(co, ps);
            ps.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Excepcion en agregarContacto " + ex.getMessage());
        }
    }

    private void cargarDatosDeContactoEnSentencia(Contacto co, PreparedStatement ps) {
        
        
        try {
            ps.setString(1, co.getNombre());
            ps.setString(2, co.getApellido());
            ps.setString(3, co.getMail());
            ps.setInt(4, co.getTelofono());
            ps.setString(5, co.getDireccion());
            ps.setDate(6, utilDateToSqlDate(co.getFechanacimiento()));
            
            ps.setString(7, co.getCategoria());
        
        } catch (SQLException ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public java.sql.Date utilDateToSqlDate(Date date) {
    
        

        long timeInMilliSeconds = date.getTime();
        java.sql.Date date1 = new java.sql.Date(timeInMilliSeconds);

        return date1;
     
    }
}
