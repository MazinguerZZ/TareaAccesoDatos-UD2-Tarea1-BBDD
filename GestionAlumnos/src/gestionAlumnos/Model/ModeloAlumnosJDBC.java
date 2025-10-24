package gestionAlumnos.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ModeloAlumnosJDBC implements IModeloAlumnos {

	static Connection conn;

	private static String cadenaConexion = "jdbc:mysql://localhost:3306/adat";
	private static String user = "dam2";
	private static String pass = "asdf.1234";

	public static void main(String[] args) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(cadenaConexion, user, pass);
			System.out.println("Conectado");
			System.out.println();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());

		}

		ModeloAlumnosJDBC alumno = new ModeloAlumnosJDBC();
		Alumno al1 = new Alumno();
		alumno.getAll();
		Alumno resultado = alumno.getAlumnoPorDNI("12345678A");
		alumno.eliminarAlumno("87654321B");

		al1.setDNI("99999994Z");
		al1.setNombre("María");
		al1.setApellidos("López García");
		al1.setCP("28001");
		alumno.crear(al1);
		alumno.modificarAlumno(al1);
		
		

	}

	@Override
	public List<String> getAll() {
		try {
			String sql = "SELECT * FROM Alumnos";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.out.println("DNI: " + rs.getString("DNI"));
				System.out.println("nombre: " + rs.getString("Nombre"));
				System.out.println("apellidos: " + rs.getString("Apellidos"));
				System.out.println("CP: " + rs.getString("cp"));
				System.out.println();

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}

	@Override
	public Alumno getAlumnoPorDNI(String DNI) {
		try {
			String sql = "SELECT * FROM Alumnos WHERE DNI = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, DNI);

			ResultSet rs = ps.executeQuery();

			// Procesar el resultado y retornar el alumno encontrado
			if (rs.next()) {
				Alumno alumnoEncontrado = new Alumno();
				alumnoEncontrado.setDNI(rs.getString("DNI"));
				alumnoEncontrado.setNombre(rs.getString("nombre"));
				System.out.println("Datos: " + rs.getString("DNI") + " - " + rs.getString("nombre"));

				rs.close();
				ps.close();

				return alumnoEncontrado;
			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean modificarAlumno(Alumno alumno) {

		
		  try { 
			  String sql = "UPDATE Alumnos SET DNI = ?, nombre = ?, apellidos = ?, CP = ? WHERE DNI = ?"; 
			  PreparedStatement ps = conn.prepareStatement(sql);
			  
		        ps.setString(1, alumno.getDNI());
		        ps.setString(2, alumno.getNombre());
		        ps.setString(3, alumno.getApellidos());
		        ps.setString(4, alumno.getCP());
		        ps.setString(5, alumno.getDNI());
		  
			  ps.executeUpdate();
		  
			  System.out.println("Actualizado correctamente");

		  
		  } catch (SQLException e) { 
			  e.printStackTrace(); 
			  }
		 

		return false;
	}

	@Override
	public boolean eliminarAlumno(String DNI) {

		try {
			String sql = "DELETE FROM Alumnos WHERE DNI = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, DNI);

			ps.executeUpdate();
			System.out.println("Eliminado correctamente");

		} catch (Exception e) {

		}

		return false;
	}

	@Override
	public boolean crear(Alumno alumno) {

		try {
			String sql = "INSERT INTO Alumnos(DNI, nombre, apellidos, cp) VALUES(?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, alumno.getDNI());
			ps.setString(2, alumno.getNombre());
			ps.setString(3, alumno.getApellidos());
			ps.setString(4, alumno.getCP());

			ps.executeUpdate();

			System.out.println("Creado correctamente");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;

	}

}