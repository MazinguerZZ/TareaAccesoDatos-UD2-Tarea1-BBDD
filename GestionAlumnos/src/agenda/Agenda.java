package agenda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Agenda implements Serializable {
	protected Map<String,Contacto> agenda = new TreeMap<String,Contacto>();
	//private static int numCamposContacto = 2;
	
	public int size() { return agenda.size(); }
	
	// borra un Contacto de la agenda.
	// Devuelve null si no existía y el objeto borrado si sí existía
	public Contacto borrarContacto(String nombre) {
		Contacto c = agenda.get(nombre);
		if (c!=null) {
			agenda.remove(nombre);
			return c;
		}
		return null;
	}
	
	public String toCSV() {
		StringBuilder stb = new StringBuilder();
		agenda.values().forEach( contact -> stb.append(contact.toCSV()).append("\n"));
		return stb.toString();
	} // toCSV
	
	public List<Contacto> getTodos() {
		return new ArrayList<Contacto>(agenda.values());
	} // getTodos()
	
	public Contacto addContacto(Contacto c) {
		return agenda.put(c.nombre, c);
	} // addContacto
	
	public Contacto getContacto(String nombre) {
		return agenda.get(nombre);
	} // getContacto
	
	public Contacto buscaUnoPorTeléfono(String teléfono) {
		Iterator<Contacto> it = agenda.values().iterator();
		while (it.hasNext()) {
			Contacto c = it.next();
			if (teléfono.equals(c.getTeléfono())) return c; 
		}
		return null;
	} // buscaUnoPorTeléfono
	
	public ArrayList<Contacto> buscaVariosPorTeléfono(String teléfono) {
		ArrayList<Contacto> res = new ArrayList<Contacto>();
		Iterator<Contacto> it = agenda.values().iterator();
		while (it.hasNext()) {
			Contacto c = it.next();
			if (c.getTeléfono().contains(teléfono)) res.add(c); 
		}
		return res;
	} // buscaVariosPorTeléfono
	
	@Override
	public String toString() {
		StringBuilder stb = new StringBuilder("Agenda:\n");
		// Itera con forEach:
		//agenda.forEach( (x,y) -> stb.append(y.toString()).append("\n")   );
		
		/* Itera sobre las claves:
		Iterator<String> it = agenda.keySet().iterator();
		while (it.hasNext()) {
			stb.append( agenda.get(it.next().toString()));
			stb.append("\n");
		}
		*/
		
		/* Itera sobre los valores: */
		
		Iterator<Contacto> it = agenda.values().iterator();
		while (it.hasNext()) stb.append(it.next()).append("\n");
		
		
		/* Iterar sobre los pares clave/valor */
		/*
		Iterator<Map.Entry<String,Contacto>> it = agenda.entrySet().iterator();
		while (it.hasNext()) 
			stb.append(  it.next().getValue() ).append("\n");
		*/
		
		return stb.toString();
	} // toString
	
} // class Agenda
