package principal.ventanalogin;


import principal.control.GestorControles;
import principal.Constantes;
import principal.GestorPrincipal;

import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaLogin extends JFrame {
    private JTextField campoNombre;
    private JPasswordField campoPassword;
    private JButton botonLogin;
    private JButton botonRegistro;

    private BaseDatos baseDatos;

    public VentanaLogin() {
        setTitle("Login");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        baseDatos = new BaseDatos();

        JPanel panel = new JPanel(new GridBagLayout()); // Usar GridBagLayout
        add(panel);
        colocarComponentes(panel);
    }

    private void colocarComponentes(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espacio entre componentes

        JLabel etiquetaNombre = new JLabel("Nombre:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(etiquetaNombre, gbc);

        campoNombre = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(campoNombre, gbc);

        JLabel etiquetaPassword = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(etiquetaPassword, gbc);

        campoPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(campoPassword, gbc);

        botonLogin = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span dos columnas
        panel.add(botonLogin, gbc);

        botonRegistro = new JButton("Registro");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span dos columnas
        panel.add(botonRegistro, gbc);

        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        botonRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaRegistro();
            }
        });
    }

    private void login() {
        String nombre = campoNombre.getText(); 
        String contraseña = new String(campoPassword.getPassword());

        if (!nombre.isEmpty() && !contraseña.isEmpty()) {
            if (baseDatos.validarCredenciales(nombre, contraseña)) { // Validar credenciales con el nombre y contraseña
                JOptionPane.showMessageDialog(this, "Login exitoso");
                dispose();
                iniciarJuego();
            } else {
                JOptionPane.showMessageDialog(this, "Nombre o contraseña incorrectos");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos");
        }
    }

    private void abrirVentanaRegistro() {
        VentanaRegistro ventanaRegistro = new VentanaRegistro(this, baseDatos);
        ventanaRegistro.setVisible(true);
    }

    private void iniciarJuego() {
        GestorPrincipal gp = new GestorPrincipal("Underhive", Constantes.ANCHO_PANTALLA_COMPLETA,
                Constantes.ALTO_PANTALLA_COMPLETA);
        gp.iniciarJuego();
    }

    public static void main(String[] args) {
        VentanaLogin ventanaLogin = new VentanaLogin();
        ventanaLogin.setVisible(true);
    }

}
