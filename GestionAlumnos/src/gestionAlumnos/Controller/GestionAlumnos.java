package gestionAlumnos.Controller;

import gestionAlumnos.Model.IModeloAlumnos;
import gestionAlumnos.Model.ModeloAlumnosJDBC;
import gestionAlumnos.UI.VentanaAlumnos;

public class GestionAlumnos {

	public static void main(String[] args) {
		 try {
        	VentanaAlumnos view = new VentanaAlumnos();
        	IModeloAlumnos model = new ModeloAlumnosJDBC();
        	ControladorGestionAlumnos controller = new ControladorGestionAlumnos(model, view);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
