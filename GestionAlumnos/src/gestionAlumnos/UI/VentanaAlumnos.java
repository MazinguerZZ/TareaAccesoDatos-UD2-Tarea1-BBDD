package gestionAlumnos.UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gestionAlumnos.Model.Alumno;

import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.GridLayout;
import javax.swing.JScrollPane;

public class VentanaAlumnos extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField textFieldDNI;
	public JTextField textFieldNombre;
	public JTextField textFieldApellidos;
	public JTextField textFieldCP;
	public JButton btnCargarTodos;
	public JButton btnCrear;
	public JButton btnModificar;
	public JButton btnEliminar;
	private JScrollPane scrollPane;
	public JList<String> jListaAlumnos;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaAlumnos frame = new VentanaAlumnos();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaAlumnos() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		jListaAlumnos = new JList<String>();
		jListaAlumnos.setVisibleRowCount(-1);
		jListaAlumnos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jListaAlumnos.setLayoutOrientation(JList.VERTICAL);
		scrollPane.setViewportView(jListaAlumnos);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblNewLabel = new JLabel("DNI");
		panel.add(lblNewLabel);
		
		textFieldDNI = new JTextField();
		panel.add(textFieldDNI);
		textFieldDNI.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Nombre");
		panel.add(lblNewLabel_1);
		
		textFieldNombre = new JTextField();
		panel.add(textFieldNombre);
		textFieldNombre.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Apellidos");
		panel.add(lblNewLabel_2);
		
		textFieldApellidos = new JTextField();
		panel.add(textFieldApellidos);
		textFieldApellidos.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("CP");
		panel.add(lblNewLabel_3);
		
		textFieldCP = new JTextField();
		panel.add(textFieldCP);
		textFieldCP.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		btnCargarTodos = new JButton("Cargar Todos");
		panel_1.add(btnCargarTodos);
		
		btnCrear = new JButton("Crear Nuevo");
		btnCrear.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(btnCrear);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(btnModificar);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(btnEliminar);
	}

}
