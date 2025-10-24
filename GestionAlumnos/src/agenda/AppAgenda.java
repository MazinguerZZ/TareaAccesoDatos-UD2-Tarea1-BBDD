package agenda;

import java.io.File;
import java.io.IOException;
import agenda.VistaAgenda;
import agenda.Agenda;
import agenda.Contacto;
import agenda.ES_Agenda;

public class AppAgenda {
	public static String ficheroAgendaCSV = "agenda.csv";
	public static String ficheroAgendaSERIAL = "agenda.ser";
	protected File fichero;
	protected Agenda agenda;
	protected VistaAgenda vista;
	
	
	public AppAgenda() {
		agenda = new Agenda();
		cargarDesdeBaseDatos(); // Cargar contactos desde BD al iniciar
	}
	/*
	public AppAgenda(String fileName) {
		fichero = new File(fileName);
		try {
			agenda = ES_Agenda.leeAgendaDeCsv(fichero);
		} catch (IOException e) {
			agenda = new Agenda();
		}
	} // constructor
	*/
	
	private void cargarDesdeBaseDatos() {
		try {
			agenda = ES_Agenda.leeAgendaDeCsv(new File(ficheroAgendaCSV));
			System.out.println("Contactos cargados desde base de datos: " + agenda.size());
		} catch (IOException e) {
			System.err.println("Error cargando desde base de datos: " + e.getMessage());
		}
	}
	
	public void editarContacto(String nombre, String teléfono) {
		Contacto contacto = agenda.getContacto(nombre);
		if (contacto != null) {
			contacto.setTeléfono(teléfono);
			// Actualizar inmediatamente en la base de datos
			ES_Agenda.actualizarContacto(contacto);
			System.out.println("Contacto actualizado en BD: " + nombre);
		}
	} // editaContacto
	
	public Contacto borrarContacto(String nombre) {
		Contacto eliminado = agenda.borrarContacto(nombre);
		if (eliminado != null) {
			// Eliminar inmediatamente de la base de datos
			ES_Agenda.eliminarContacto(nombre);
			System.out.println("Contacto eliminado de BD: " + nombre);
		}
		return eliminado;
	} // borrarContacto
	
	public Contacto añadirContacto(String nombre, String teléfono) {
		Contacto nuevoContacto = new Contacto(nombre,teléfono);
		Contacto existente = agenda.addContacto(nuevoContacto);
		
		// Guardar inmediatamente en la base de datos
		if (existente == null) {
			// Es un contacto nuevo
			ES_Agenda.guardarContacto(nuevoContacto);
			System.out.println("Nuevo contacto guardado en BD: " + nombre);
		} else {
			// El contacto ya existía, se actualiza
			ES_Agenda.actualizarContacto(nuevoContacto);
			System.out.println("Contacto existente actualizado en BD: " + nombre);
		}
		
		return existente;
	} // añadirContacto
	
	
	
	public void rellenaVista() {
		vista.actualizaListado(agenda.getTodos());
	} // rellenaVista
	
	public boolean guardarFicheroCSV() {
		try {
			ES_Agenda.escribeAgendaEnCsv(new File(ficheroAgendaCSV) , agenda);
			System.out.println("Agenda guardada en BD (método CSV)");
			return true;
		} catch (IOException e) {
			return false;
		}
	} // guardaFicheroCSV
	
	public void recargaFicheroCSV() {
		try {
			agenda = ES_Agenda.leeAgendaDeCsv(new File(ficheroAgendaCSV));
			System.out.println("Agenda recargada desde BD (método CSV): " + agenda.size() + " contactos");
		} catch (IOException e) {
			agenda = new Agenda();
			System.err.println("Error recargando desde BD: " + e.getMessage());
		}
		if (vista != null) {
			vista.actualizaListado(agenda.getTodos());
		}
	} // recargaFicheroCSV
	
	public void recargaFicheroSerial() {
		try {
			agenda = ES_Agenda.leeAgendaDeSerial(new File(ficheroAgendaSERIAL));
			System.out.println("Agenda recargada desde BD (método Serial): " + agenda.size() + " contactos");
		} catch (Exception e) {
			agenda = new Agenda();
			System.err.println("Error recargando desde BD: " + e.getMessage());
		}
		if (vista != null) {
			vista.actualizaListado(agenda.getTodos());
		}
	} // recargaFicheroSerial
	
	public boolean guardarFicheroSerial() {
		try {
			ES_Agenda.escribeAgendaEnSerial(new File(ficheroAgendaSERIAL) , agenda);
			System.out.println("Agenda guardada en BD (método Serial)");
			return true;
		} catch (IOException e) {
			return false;
		}
	} // guardaFicheroSerial
	
	// Método adicional para forzar la recarga desde BD
	public void recargarDesdeBaseDatos() {
		cargarDesdeBaseDatos();
		if (vista != null) {
			vista.actualizaListado(agenda.getTodos());
		}
	}
	
	@Override
	public String toString() {
		return agenda.toCSV();
	} // toString
	
	public static void main(String[] args) {
		AppAgenda app = new AppAgenda();
		app.vista = new VistaAgenda(app);
		app.rellenaVista();
	} // main

} // class