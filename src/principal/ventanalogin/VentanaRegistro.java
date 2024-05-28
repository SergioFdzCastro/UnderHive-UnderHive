package principal.ventanalogin;

import javax.swing.*;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRegistro extends JFrame {
    private JTextField campoUsuario;
    private JTextField campoCorreo;
    private JPasswordField campoPassword;
    private JButton botonRegistrar;

    private BaseDatos baseDatos; // Necesitas una instancia de BaseDatos aquí

    public VentanaRegistro(JFrame ventanaLogin, BaseDatos baseDatos) {
        setTitle("Registro");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        this.baseDatos = baseDatos; // Asigna la instancia de BaseDatos

        JPanel panel = new JPanel(new GridBagLayout());
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

        campoUsuario = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(campoUsuario, gbc);

        JLabel etiquetaCorreo = new JLabel("Correo:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(etiquetaCorreo, gbc);

        campoCorreo = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(campoCorreo, gbc);

        JLabel etiquetaPassword = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(etiquetaPassword, gbc);

        campoPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(campoPassword, gbc);

        botonRegistrar = new JButton("Registrar");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span dos columnas
        panel.add(botonRegistrar, gbc);

        botonRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String nombre = campoUsuario.getText();
        String email = campoCorreo.getText();
        String password = new String(campoPassword.getPassword());

        if (!nombre.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            baseDatos.registrarUsuario(nombre, email, password);
            JOptionPane.showMessageDialog(this, "Registro exitoso");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos");
        }
    }
}
