package Controlador;

import Modelo.*;
import Vista.VistaAñadirPersona;
import Vista.VistaEditarPersona;
import Vista.VistaLog;
import Vista.VistaPrincipal;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import Modelo.Contacto;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Juan Pablo Palomares Avila y Alvaro Bulnes Ramos
 */
public class Controlador implements ActionListener {

    Modelo modelo;
    VistaPrincipal vp;
    VistaLog vl;
    VistaAñadirPersona vAñadir;
    VistaEditarPersona vEditar;

    public Controlador() {
        modelo = new Modelo();
        vp = new VistaPrincipal();
        addActionPerformedToVistaPrincipal();
    }

    public void addActionPerformedToVistaPrincipal() {
        vp.getjButtonConectar().addActionListener(this);
        vp.getjButtonAgregar().addActionListener(this);
        vp.getjButtonEliminar().addActionListener(this);
        //Mouse listener para clickar en el jtable pasandole la clase tabla de abajo
        vp.getjTableContactos().addMouseListener(new ListerTabla());
        vp.getjComboBox1().addActionListener(this);

    }

    public void addActionPerformedToVistaLog() {
        vl.getjButtonCerrar().addActionListener(this);
        vl.getjButtonConectar().addActionListener(this);

    }

    public void addActionPerformedToVistaEditar() {
        vEditar.getjButtonCerrar().addActionListener(this);
        vEditar.getjButtonBorrar().addActionListener(this);
        vEditar.getjButtonEditar().addActionListener(this);

    }

    public void addActionPerformedToVistaAnyadir() {
        vAñadir.getjButtonCerrar().addActionListener(this);
        vAñadir.getjButtonAñadir().addActionListener(this);

    }

    @Override
    //Manera de distinguir de donde viene el evento con el actionComand de cada elemento en cada ventana
    public void actionPerformed(ActionEvent e) {
        String vista = e.getActionCommand();
        switch (vista) {
            case "VistaPrincipal":
                actionPerformedToVistaPrincipal(e);
                break;
            case "VistaLog":
                actionPerformedToVistaLog(e);
                break;
            case "VistaEditar":
                actionPerfomedToVistaEditar(e);
                break;
            case "VistaAnyadir":
                actionPerfomedToVistaAnyadir(e);
                break;

        }
    }

    //ACCIONES SOBRE LA VISTA PRINCIPAL (BOTON CONECTAR, BORRAR, AÑADIR)
    public void actionPerformedToVistaPrincipal(ActionEvent e) {
        if (e.getSource() == vp.getjButtonAgregar()) {
            vp.setEnabled(false);
            vAñadir = new VistaAñadirPersona();
            addActionPerformedToVistaAnyadir();
        } else if (e.getSource() == vp.getjButtonConectar()) {
            vp.setEnabled(false);
            vl = new VistaLog();
            addActionPerformedToVistaLog();
        } else if (e.getSource() == vp.getjButtonEliminar()) {
            boolean confirmar = confirmar("¿Esta seguro que quiere borrar la agenda?");

            if (confirmar) {
                modelo.borrarAgenda();
                vp.limpiarTabla();

            }
            //Filtro de la jtable por categoria
        } else if (e.getSource() == vp.getjComboBox1()) {
            if (!vp.getjComboBox1().getSelectedItem().toString().equals("TODOS")) {
                vp.limpiarTabla();
                vp.llenarTabla(modelo.filtrarPorCategoria(vp.getjComboBox1().getSelectedItem().toString()));

            } else {
                vp.limpiarTabla();
                vp.llenarTabla(modelo.selectBBDDInfo());
            }

        }
    }

    //ACCIONES SOBRE LA VISTA QUE SE ABRE AL CONECTAR
    public void actionPerformedToVistaLog(ActionEvent e) {

        if (e.getSource() == vl.getjButtonConectar()) {
            String usuario = vl.getjTextFieldUser();
            String pass = vl.getjTextFieldPass();
            String url = vl.getjTextFieldUrl().getText();
            String driver = vl.getjTextFieldDriver().getText();
            //Ver si la base de datos esta activa para conectar sino mensaje
            if (Validaciones.conexionCorrecta(driver, url, usuario, pass)) {
                boolean conectado = true;
                try {
                    vl.getjLabelError().setText("");
                    modelo.MySQLConnection(driver, url, usuario, pass);
                    conectado = true;
                } catch (NullPointerException eoe) {
                    conectado = false;

                } catch (SQLException ex) {
                   conectado=false;
                }
                if (conectado) {
                    vl.dispose();
                    vp.setEnabled(true);
                    vp.setVisible(true);
                    vp.getjLabelEstado().setText("CONECTADO");
                    vp.getjLabelEstado().setForeground(Color.GREEN);
                    vp.getjButtonAgregar().setEnabled(true);
                    vp.getjButtonEliminar().setEnabled(true);
                    vp.getjButtonConectar().setEnabled(false);
                    vp.llenarTabla(modelo.selectBBDDInfo());
                    vp.getjLabeldobleclick().setVisible(true);
                } else {
                    vp.mensajeAdvertencia("Imposible conectar con el servidor.\n\nCompruebe si esta iniciado.");
                }
            } else {
                vp.mensajeAdvertencia(Validaciones.validacionesLogin(driver, url, url, pass));

            }
        } else if (e.getSource() == vl.getjButtonCerrar()) {
            vp.setEnabled(true);
            vl.dispose();
        }

    }

    //ACCIONES EN VISTA AÑADIR PERSONA.JAVA
    public void actionPerfomedToVistaAnyadir(ActionEvent e) {
        if (e.getSource() == vAñadir.getjButtonAñadir()) {
            //Si cada valor que cojo de la ventana añadir esta correcto y pasa el filtro en el modelo, se puede añadir el contacto, si no se crea un string con los campos que faltan
            String fallos = Validaciones.validacionesAñadir(vAñadir.getjTextFieldNombre().getText(), vAñadir.getjTextFieldApellido().getText(), vAñadir.getjTextFieldMail().getText(), vAñadir.getjTextFieldTelefono().getText(), vAñadir.getjTextFieldDireccion().getText(), vAñadir.getjDateChooser1().getDate());
            if (fallos.equals("")) {
                boolean confirmar = confirmar("¿Esta seguro que desea añadir este contacto?");
                if (confirmar) {
                    Contacto contacto = aniadirContacto();
                    modelo.agregarContacto(contacto);
                    vp.limpiarTabla();
                    vp.llenarTabla(modelo.selectBBDDInfo());
                    vp.mensajeAdvertencia("Contacto añadido");
                    vAñadir.dispose();
                    vp.setEnabled(true);
                    vp.setVisible(true);

                } else {
                    vAñadir.dispose();
                    vp.setEnabled(true);
                    vp.setVisible(true);
                }
            } else {
                vp.mensajeAdvertencia(fallos);
            }

        } else if (e.getSource() == vAñadir.getjButtonCerrar()) {
            vp.setEnabled(true);
            vp.setVisible(true);
            vAñadir.dispose();
        }
    }

    //ACCIONES SOBRE LA VISTAEDITAR.JAVA
    private void actionPerfomedToVistaEditar(ActionEvent e) {
        if (e.getSource() == vEditar.getjButtonBorrar()) {
            boolean confirmar = confirmar("¿Esta seguro que desea borrar este contacto?");
            if (confirmar) {
                modelo.borrarContacto(vp.devolverId());
                vp.limpiarTabla();
                vp.llenarTabla(modelo.selectBBDDInfo());
                vp.setVisible(true);
                vp.setEnabled(true);
                vEditar.dispose();
            }
        } else if (e.getSource() == vEditar.getjButtonCerrar()) {

            vEditar.dispose();
            vp.setVisible(true);
            vp.setEnabled(true);
        } else if (e.getSource() == vEditar.getjButtonEditar()) {

            String fallos = Validaciones.validacionesAñadir(vEditar.getjTextFieldNombre().getText(), vEditar.getjTextFieldApellido().getText(), vEditar.getjTextFieldMail().getText(), vEditar.getjTextFieldTelefono().getText(), vEditar.getjTextFieldDireccion().getText(), vEditar.getjDateChooser1().getDate());
            if (fallos.equals("")) {
                boolean confirmar = confirmar("¿Esta seguro que desea editar este contacto?");
                if (confirmar) {
                    Contacto contacto = editarContacto();
                    modelo.updatePorId(vp.devolverId(), contacto);
                    vp.limpiarTabla();
                    vp.llenarTabla(modelo.selectBBDDInfo());
                    vp.mensajeAdvertencia("Contacto Editado");
                    vEditar.dispose();
                    vp.setEnabled(true);
                    vp.setVisible(true);

                } else {
                    vEditar.dispose();
                    vp.setEnabled(true);
                    vp.setVisible(true);
                }
            } else {
                vp.mensajeAdvertencia(fallos);
                Contacto contacto = modelo.selectContacto(vp.devolverId());
                vEditar.setjTextFieldNombre(contacto.getNombre());
                vEditar.setjTextFieldApellido(contacto.getApellido());
                vEditar.setjTextFieldMail(contacto.getMail());
                vEditar.setjTextFieldDireccion(contacto.getDireccion());
                vEditar.setjTextFieldTelefono(contacto.getTelofono());
                vEditar.setjDateChooser1(contacto.getFechanacimiento());
                vEditar.getjComboBox1().setSelectedIndex(devolverCategoria(contacto.getCategoria()));
            }

        }
    }
//Clase para las acciones al clickar sobre un contacto sobre la tabla

    private class ListerTabla implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            //Evento para cuando clickas en la tabla
            if (e.getClickCount() == 2) {
                vp.setEnabled(false);
                vEditar = new VistaEditarPersona();

                addActionPerformedToVistaEditar();
                //Te rellena la ventana que se abre con los datos del contacto en la BB buscando por el id que te lo da la fila cero de la tabla
                Contacto contacto = modelo.selectContacto(vp.devolverId());
                vEditar.setjTextFieldNombre(contacto.getNombre());
                vEditar.setjTextFieldApellido(contacto.getApellido());
                vEditar.setjTextFieldMail(contacto.getMail());
                vEditar.setjTextFieldDireccion(contacto.getDireccion());
                vEditar.setjTextFieldTelefono(contacto.getTelofono());
                vEditar.setjDateChooser1(contacto.getFechanacimiento());
                vEditar.getjComboBox1().setSelectedIndex(devolverCategoria(contacto.getCategoria()));
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    public int devolverCategoria(String categoria) {
        int i = 0;
        switch (categoria) {
            case "AMIGOS":
                i = 0;
                break;
            case "OTROS":
                i = 1;
                break;

            case "FAMILIARES":
                i = 2;
                break;
            case "COMPAÑEROS DE TRABAJO":
                i = 3;
                break;
            case "COMPAÑEROS DE FACULTAD":
                i = 4;
                break;

        }
        return i;

    }
// Saca un mensaje de confirmacion para pulsar aceptar o cancelar sobre una accion

    public boolean confirmar(String mensaje) {
        int res = JOptionPane.showConfirmDialog(null, mensaje, "Confirmar...", JOptionPane.YES_NO_OPTION);
        return res == 0;
    }
//Coge los valores de la vista de Añadir contacto

    public Contacto aniadirContacto() {
        Contacto contacto;
        String nombre;
        String apellido;
        String mail;
        String telefono;
        String direccion;
        Date fechanacimiento = null;
        String categoria;

        nombre = vAñadir.getjTextFieldNombre().getText();
        apellido = vAñadir.getjTextFieldApellido().getText();
        mail = vAñadir.getjTextFieldMail().getText();
        telefono = vAñadir.getjTextFieldTelefono().getText();
        direccion = vAñadir.getjTextFieldDireccion().getText();

        fechanacimiento = vAñadir.getjDateChooser1().getDate();
        categoria = vAñadir.getjComboBox1().getSelectedItem().toString();

        contacto = new Contacto(nombre, apellido, mail, Integer.parseInt(telefono), direccion, fechanacimiento, categoria);

        return contacto;
    }
//Coge los valores de la vista de editar contacto

    public Contacto editarContacto() {
        Contacto contacto;
        String nombre;
        String apellido;
        String mail;
        String telefono;
        String direccion;
        Date fechanacimiento = null;
        String categoria;

        nombre = vEditar.getjTextFieldNombre().getText();
        apellido = vEditar.getjTextFieldApellido().getText();
        mail = vEditar.getjTextFieldMail().getText();
        telefono = vEditar.getjTextFieldTelefono().getText();
        direccion = vEditar.getjTextFieldDireccion().getText();
        System.out.println("Hasta antes de fecha");
        fechanacimiento = vEditar.getjDateChooser1().getDate();
        categoria = vEditar.getjComboBox1().getSelectedItem().toString();

        contacto = new Contacto(nombre, apellido, mail, Integer.parseInt(telefono), direccion, fechanacimiento, categoria);
        System.out.println("Contacto creado");
        return contacto;
    }

}
