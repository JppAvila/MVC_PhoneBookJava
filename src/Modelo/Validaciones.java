package Modelo;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Juan Pablo Palomares Avila y Alvaro Bulnes Ramos
 */
public class Validaciones {

    public static boolean estaVacio(String cadena) {
        boolean estaVacio = true;
        if (!cadena.isEmpty()) {
            estaVacio = false;
        }
        return estaVacio;
    }

    public static boolean mailCorrecto(String correo) {
        boolean mailCorrecto;
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(correo);

        mailCorrecto = mather.find() == true;
        return mailCorrecto;
    }

    public static boolean conexionCorrecta(String driver, String url, String user, String pass) {
        boolean correcto=true;

        if (!driver.equals("com.mysql.jdbc.Driver")) {
            correcto = false;
        }
        if(!url.equals("jdbc:mysql://localhost:3306/agenda")){
         correcto=false;
        }
        if(!user.equals("root")){
         correcto=false;
        }
        if(pass == null || !pass.equals("")){
         correcto=false;
        }
        return correcto;
        
        
    }
    
    public static String validacionesLogin(String driver,String url,String user,String pass){
       String mensaje="Por favor corrija los siguientes campos:\n";
       
       if (!driver.equals("com.mysql.jdbc.Driver")){
           mensaje+="Driver erroneo--->com.mysql.jdbc.Driver\n";
       }
       if(!url.equals("jdbc:mysql://localhost:3306/agenda")){
           mensaje+="URL erronea-->jdbc:mysql://localhost:3306/agenda\n";
           
    }
        
       if(!user.equals("root")){
        mensaje+="Usuario erroreno -->root\n";
       }
       
       if(!pass.equals("")||!pass.isEmpty()){
           mensaje+="Contraseña erronea-->Dejala en blanco";
       }
       return mensaje;
    }
    
    public static String validacionesAñadir(String nombre,String apellido,String mail,String telefono,String direccion,Date fecha){
     String fallos="";
     
     if (nombre.isEmpty()){
     fallos+="El campo nombre esta vacio\n";
     }
    
     if (apellido.isEmpty()){
     fallos+="El campo apellido esta vacio\n";
     }
    
      if (mail.isEmpty()){
     fallos+="El campo mail esta vacio\n";
     }else  if(!mailCorrecto(mail)){
      fallos+="El campo mail, no representa un mail valido\n";
      } 
      if (telefono.isEmpty()){
     fallos+="El campo telefono esta vacio\n";
     }else if(!esNumero(telefono)){
     fallos+="Introduzca solo numeros en el telefono\n";
     }
      
      if (direccion.isEmpty()){
      fallos+="El campo direccion esta vacio\n";
      }
      
     
      if (fecha==null){
      fallos+="El campo fecha de nacimiento esta vacio";
      }
      
      return fallos;
    
    }
    
    public static boolean esNumero(String numero){
       boolean esNumero=true;
       try{
       Integer.valueOf(numero);
       esNumero=true;
       }catch (NumberFormatException aeo){
       esNumero=false;
       }
       
       return esNumero;
    }
}

