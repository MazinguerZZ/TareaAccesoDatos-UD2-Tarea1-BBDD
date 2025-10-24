package agenda;

import java.io.Serializable;

public class Contacto implements Serializable, Comparable<Contacto> {
	protected String nombre;
	protected String teléfono;

	@Override
	public int compareTo(Contacto arg0) {
		return nombre.toLowerCase().compareTo(arg0.nombre.toLowerCase());
	} // compareTo
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Contacto)) return false;
		return nombre.toLowerCase().equals(((Contacto)obj).nombre.toLowerCase());
	} // hashCode
	
	@Override
	public int hashCode() {
		return nombre.toLowerCase().hashCode();
	} // hashCode
	
	@Override
	public String toString() {
		return nombre + ":" + teléfono;
	} // toString
	
	public String toCSV() {
		return nombre + "," + teléfono;
	} // toString
	
	public String[] toArray() {
		String[] res = { nombre, teléfono };
		return res;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTeléfono() {
		return teléfono;
	}
	public void setTeléfono(String teléfono) {
		this.teléfono = teléfono;
	}
	
	public Contacto(String nombre, String teléfono) {
		super();
		this.nombre = nombre;
		this.teléfono = teléfono;
	}
	
} // class Contacto
