package agenda;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import agenda.AppAgenda;
import agenda.Contacto;

// https://stackoverflow.com/questions/10435103/the-type-b-cannot-be-a-superinterface-of-c-a-superinterface-must-be-an-interfa
// MouseAdapter es una clase abstracta, no un interfaz. Podríamos usar MouseListener, este otro enfoque solo nos evita tener
//  los métodos extra vacíos.
public class VistaAgenda extends MouseAdapter implements ActionListener {
	private final String txtBorrar = "Borrar";
	private final String txtAñadir = "Añadir";
	private final String txtCargarCSV = "Cargar CSV";
	private final String txtSalvarCSV = "Guardar CSV";
	private final String txtCargarSerial = "Cargar serializado";
	private final String txtSalvarSerial = "Guardar serializado";
	private final String txtEditar = "Editar";
	AppAgenda controlador;
	
	// Componentes visuales:
	JFrame ventana;
	JPanel panelPrincipal, panelInferior, panelSuperior, panelDerecha;
	JMenu menúVentana;
	JMenuBar barraMenúVentana;
	JScrollPane panelListado;
	JTable tablaListado;
	JButton botónAñadir, botónBorrar, botónCargar, botónSalvar, botónEditar;
	JLabel lblNombre, lblTeléfono;
	JTextField txtNombre, txtTeléfono;
	JDialog diálogoAñadir;
	JPanel panelAñadir;
	JButton botónAñadirEnDiálogo;
	JMenuItem itemCargarCSV;
	JMenuItem itemSalvarCSV;
	JMenuItem itemCargarSerial;
	JMenuItem itemSalvarSerial;
	static Vector<String> nombreColumnas; 
	Vector<Vector<String>> contenidoTabla;
	
	public VistaAgenda(AppAgenda controlador) {
		this.controlador = controlador;
		nombreColumnas = new Vector<String>();
		nombreColumnas.add("Nombre");
		nombreColumnas.add("Teléfono");
		construyeVentanaAgenda();
	} // constructor
	
	protected void construyeVentanaAgenda() {
		ventana = new JFrame("Agenda");
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		panelPrincipal = (JPanel) ventana.getContentPane();
		panelPrincipal.setLayout(new BorderLayout());
		panelInferior = new JPanel();
		panelPrincipal.add(panelInferior,BorderLayout.SOUTH );
		botónAñadir = new JButton(txtAñadir);
		botónAñadir.setMnemonic('A');
		botónBorrar = new JButton(txtBorrar);
		botónBorrar.setMnemonic('B');
		botónEditar = new JButton(txtEditar);
		botónEditar.setMnemonic('E');
		panelInferior.add(botónAñadir);
		panelInferior.add(botónEditar);
		panelInferior.add(botónBorrar);
		botónAñadir.addActionListener(this);
		botónBorrar.addActionListener(this);
		botónEditar.addActionListener(this);
		panelPrincipal.getRootPane().setDefaultButton(botónAñadir);
		
		barraMenúVentana = new JMenuBar();
		menúVentana = new JMenu("Archivo");
		menúVentana.setMnemonic('r');
		//  SubMenús cargar y guardar
		itemCargarCSV = new JMenuItem(txtCargarCSV);
		itemCargarCSV.setMnemonic('C');
		itemSalvarCSV = new JMenuItem(txtSalvarCSV);
		itemSalvarCSV.setMnemonic('S');
		menúVentana.add(itemCargarCSV);
		menúVentana.add(itemSalvarCSV);
		itemCargarCSV.addActionListener(this);
		itemSalvarCSV.addActionListener(this);
		
		itemCargarSerial = new JMenuItem(txtCargarSerial);
		itemCargarSerial.setMnemonic('C');
		itemSalvarSerial = new JMenuItem(txtSalvarSerial);
		itemSalvarCSV.setMnemonic('S');
		menúVentana.add(itemCargarSerial);
		menúVentana.add(itemSalvarSerial);
		itemCargarSerial.addActionListener(this);
		itemSalvarSerial.addActionListener(this);
		
		
		barraMenúVentana.add(menúVentana);
		ventana.setJMenuBar(barraMenúVentana);

		lblNombre = new JLabel("Nombre:");
		lblTeléfono = new JLabel("Teléfono:");
		txtNombre = new JTextField("",20);
		txtTeléfono = new JTextField("",20);
		ventana.setVisible(true);
		ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		diálogoAñadir = new JDialog(ventana,true);
		panelAñadir = new JPanel();
		panelAñadir.setLayout(new BoxLayout(panelAñadir,BoxLayout.Y_AXIS));
		JPanel panelNombre = new JPanel();
		JPanel panelTeléfono = new JPanel();
		panelNombre.add(lblNombre);
		panelNombre.add(txtNombre);
		panelTeléfono.add(lblTeléfono);
		panelTeléfono.add(txtTeléfono);
		panelAñadir.add(panelNombre);
		panelAñadir.add(panelTeléfono);
		botónAñadirEnDiálogo = new JButton("Añadir");
		botónAñadirEnDiálogo.setMnemonic('A');
		panelAñadir.add(botónAñadirEnDiálogo);
		diálogoAñadir.setTitle("Añadir contacto");
		diálogoAñadir.getContentPane().add(panelAñadir);
		diálogoAñadir.getRootPane().setDefaultButton(botónAñadirEnDiálogo);
		diálogoAñadir.pack();
		// Acciones para el cuadro de diálogo de añadir o editar contacto: en formato Lambda
		botónAñadirEnDiálogo.addActionListener( evento -> {
			diálogoAñadir.dispose();
			switch (evento.getActionCommand()) { 
				case txtAñadir :
					if (!txtNombre.getText().isBlank()) {
						Contacto c = controlador.añadirContacto(txtNombre.getText(),txtTeléfono.getText());
						if (c==null) añadeContactoATabla(txtNombre.getText(),txtTeléfono.getText());
						else {
							JOptionPane.showMessageDialog(ventana,"Usuario ya existente. Se actualiza" 
									,"Error",JOptionPane.WARNING_MESSAGE);
							editaContactoEnTabla(txtNombre.getText(),txtTeléfono.getText());
						}
					}
					break;
				case txtEditar: controlador.editarContacto(txtNombre.getText(),txtTeléfono.getText());
								editaContactoEnTabla(txtNombre.getText(),txtTeléfono.getText());
					break;
			} // switch
		});
	} //construyeVentanaAgenda
	
	// Convierte los datos conocidos de cada Contacto de la Agenda a
	// un Vector de vectores de String adecuado para JTable
	public Vector<Vector<String>> listaContactosAVector(List<Contacto> lista) {
		return new Vector<Vector<String>>(lista.stream().map( c -> new Vector<String>(Arrays.asList(c.getNombre(),c.getTeléfono())))
				.collect(Collectors.toList()));
		/*  // Sin streams:
		Iterator<Contacto> it = lista.iterator();
		while (it.hasNext()) {
			Contacto c = it.next();
			Vector<String> vc = new Vector<String>();
			vc.add(c.getNombre());
			vc.add(c.getTeléfono());
			res.add(vc);
		}
		return res;
		*/
	} // agendaToVector
	
	public void actualizaListado(List<Contacto> listaOrig) {
		Vector<Vector<String>> lista = listaContactosAVector(listaOrig);
		
		if (contenidoTabla == null) contenidoTabla = lista;
		else {
			contenidoTabla.removeAllElements();
			contenidoTabla.addAll(lista);
		}
		if (tablaListado == null) {
			tablaListado = new JTable(lista,nombreColumnas);
			tablaListado.addMouseListener(this);
			tablaListado.addKeyListener( new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent arg0) {
					if (arg0.getKeyChar()!=KeyEvent.VK_ENTER) return;
					muestraDiálogoInfoContactoSeleccionadoEnTabla();
				}
			});
		}
		else tablaListado.updateUI();
		// Ponemos la tabla como no editable
		tablaListado.setDefaultEditor(Object.class,null);
		if (panelListado==null ) {
			panelListado = new JScrollPane(tablaListado);
			panelPrincipal.add(panelListado, BorderLayout.CENTER);
		}
		// else panelListado.setViewportView(tablaListado);
		ventana.pack();
		
	} // actualizaListado
	
	private void editaContactoEnTabla(String nombre, String teléfono) {
		int pos = tablaListado.getSelectedRow();
		if (pos<0 || !contenidoTabla.get(pos).get(0).equals(nombre)) {
		// Hay que buscarlo, se añade uno que ya existía y no estaba seleccionado
			for (pos=0; pos<contenidoTabla.size();pos++) {
				if (contenidoTabla.get(pos).get(0).equals(nombre)) break;
			}
		}
		if (pos<contenidoTabla.size()) { 
			contenidoTabla.get(pos).set(1, teléfono);
			tablaListado.updateUI();
			tablaListado.clearSelection();
		}
	} // editaContactoEnTabla
	
	private void añadeContactoATabla(String nombre, String teléfono) {
		Vector<String> vc = new Vector<String>();
		vc.add(nombre);
		vc.add(teléfono);
		// Búsqueda binaria para insertar en posición
		// Para conjuntos pequeños bastaría una búsqueda líneal
		//int pos = buscaPuntoDeInserción(nombre,0,contenidoTabla.size()-1);
		//int pos = -Arrays.binarySearch(contenidoTabla.stream().map( a -> a.get(0).toLowerCase() ).toArray(),nombre.toLowerCase())-1;
		int pos = -Collections.binarySearch(contenidoTabla.stream().map( a -> a.get(0).toLowerCase()).collect(Collectors.toList()),nombre.toLowerCase())-1;
		contenidoTabla.add(pos,vc);
		tablaListado.updateUI();
		tablaListado.clearSelection();
	} // añadeContactoATabla
	
	/*  // sin streams:
	// búsqueda binaria en el Vector de Vector<String> contenidoTabla
	private int buscaPuntoDeInserción(String n, int posIni, int posFin) {
		if (posFin<posIni) return posIni;
		if (posFin==posIni) {
			if (contenidoTabla.get(posIni).get(0).toLowerCase().compareTo(n.toLowerCase()) < 0) return posIni+1;
			else return posIni;
		}
		int mitad = (posIni+posFin)/2;
		int comp =contenidoTabla.get(mitad).get(0).toLowerCase().compareTo(n.toLowerCase()); 
		if (comp==0) return mitad;
		if (comp<0) return buscaPuntoDeInserción(n,mitad+1,posFin);
		return buscaPuntoDeInserción(n,posIni,mitad-1);
	} // buscaPuntoDeInserción
	*/
	
	private void borraContactoDeTabla(Contacto c) {
		Vector<String> vc = new Vector<String>();
		vc.add(c.getNombre());
		vc.add(c.getTeléfono());
		int pos = contenidoTabla.indexOf(vc);
		if (pos>=0) contenidoTabla.remove(pos);
		tablaListado.updateUI();
		tablaListado.clearSelection();
	} // borraContactoDeTabla
	
	// Acciones para los botones de la ventana principal: Añadir, Editar y Borrar, y el menú de Archivo (Cargar y Salvar)
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int pos;
		switch(arg0.getActionCommand()) {
			case txtBorrar :
				pos = tablaListado.getSelectedRow();
				if (pos>=0) {
					Contacto c = controlador.borrarContacto(tablaListado.getValueAt(pos,0).toString());
					if (c!=null) borraContactoDeTabla(c);
				}
				break;
			case txtAñadir :
					diálogoAñadir.setTitle(txtAñadir);
					botónAñadirEnDiálogo.setText(txtAñadir);
					txtNombre.setText("");
					txtNombre.setEditable(true);
					txtTeléfono.setText("");
					diálogoAñadir.setLocationRelativeTo(ventana);
					// Para que funcione ENTER para cerrar el diálogo:
					diálogoAñadir.getRootPane().setDefaultButton(botónAñadirEnDiálogo);
					diálogoAñadir.setVisible(true);
				break;
			case txtEditar:
				pos = tablaListado.getSelectedRow();
				if (pos>=0) {
					txtNombre.setText(tablaListado.getValueAt(pos,0).toString());
					txtNombre.setEditable(false);
					txtTeléfono.setText(tablaListado.getValueAt(pos,1).toString());
					diálogoAñadir.setTitle(txtEditar);
					botónAñadirEnDiálogo.setText(txtEditar);
					diálogoAñadir.setLocationRelativeTo(ventana);
					// Para que funcione ENTER para cerrar el diálogo:
					diálogoAñadir.getRootPane().setDefaultButton(botónAñadirEnDiálogo);
					diálogoAñadir.setVisible(true);
				}
				break;
			case txtSalvarCSV :
				controlador.guardarFicheroCSV();
				break;
			case txtCargarCSV : 
				controlador.recargaFicheroCSV();
				break;
			case txtSalvarSerial :
				controlador.guardarFicheroSerial();
				break;
			case txtCargarSerial : 
				controlador.recargaFicheroSerial();
				break;
		}
	} // actionPerformed
	    
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getClickCount() == 2 && arg0.getButton() == MouseEvent.BUTTON1) { 
			muestraDiálogoInfoContactoSeleccionadoEnTabla();
		}
	} //mouseClicked

	protected void muestraDiálogoInfoContactoSeleccionadoEnTabla() {
		int pos = tablaListado.getSelectedRow();
		if (pos>=0)
			JOptionPane.showMessageDialog(null,
					tablaListado.getValueAt(pos,0) + ":" + tablaListado.getValueAt(pos,1) 
					,"Contacto",JOptionPane.INFORMATION_MESSAGE);
	} // muestraDiálogoInfoContactoSeleccionadoEnTabla
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	} //mouseEntered

} // class VistaAgenda
