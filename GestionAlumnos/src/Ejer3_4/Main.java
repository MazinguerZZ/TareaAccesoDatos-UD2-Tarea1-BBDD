package Ejer3_4;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/adat3";
    private static final String USER = "dam2";
    private static final String PASSWORD = "asdf.1234";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            menuPrincipal();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void menuPrincipal() {
        while (true) {
            System.out.println("\n=== GESTIÓN EMPRESA ===");
            System.out.println("1. Ver empleados");
            System.out.println("2. Agregar empleado");
            System.out.println("3. Buscar empleados");
            System.out.println("4. Ver departamentos");
            System.out.println("5. Eliminar empleado");
            System.out.println("6. Información completa con JOINs");
            System.out.println("7. Salir");
            System.out.print("Elige: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1: verEmpleados(); break;
                case 2: agregarEmpleado(); break;
                case 3: buscarEmpleados(); break;
                case 4: verDepartamentos(); break;
                case 5: eliminarEmpleado(); break;
                case 6: informacionCompletaJoins(); break;
                case 7: 
                    System.out.println("¡Adiós!");
                    return;
                default: 
                    System.out.println("Opción no válida");
            }
        }
    }

    // VER TODOS LOS EMPLEADOS
    private static void verEmpleados() {
        String sql = "SELECT e.*, d.nombre_departamento FROM t_empleados e " +
                    "JOIN t_departamentos d ON e.codigo_departamento = d.codigo_departamento";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql)) {

            System.out.println("\n--- EMPLEADOS ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("codigo_empleado") + 
                                 " | Nombre: " + rs.getString("nombre_empleado") +
                                 " | Depto: " + rs.getString("nombre_departamento") +
                                 " | Salario: " + rs.getDouble("salario_base_empleado") +
                                 " | Ingreso: " + rs.getDate("fecha_ingreso_empleado"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // AGREGAR EMPLEADO
    private static void agregarEmpleado() {
        try {
            System.out.println("\n--- NUEVO EMPLEADO ---");
            System.out.print("Código: ");
            int codigo = scanner.nextInt();
            System.out.print("Departamento: ");
            int depto = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Salario: ");
            double salario = scanner.nextDouble();
            System.out.print("Fecha ingreso (YYYY-MM-DD): ");
            String fecha = scanner.next();

            String sql = "INSERT INTO t_empleados (codigo_empleado, codigo_departamento, " +
                        "extension_telefonica_empleado, fecha_nacimiento_empleado, " +
                        "fecha_ingreso_empleado, salario_base_empleado, comision_empleado, " +
                        "numero_hijos_empleado, nombre_empleado) VALUES (?, ?, 100, '1990-01-01', ?, ?, 0, 0, ?)";

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, codigo);
                pstmt.setInt(2, depto);
                pstmt.setString(3, fecha);
                pstmt.setDouble(4, salario);
                pstmt.setString(5, nombre);

                pstmt.executeUpdate();
                System.out.println("Empleado agregado!");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // BUSCAR EMPLEADOS
    private static void buscarEmpleados() {
        System.out.print("\nBuscar por nombre: ");
        String nombre = scanner.nextLine();

        String sql = "SELECT * FROM t_empleados WHERE nombre_empleado LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n--- RESULTADOS ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("codigo_empleado") + 
                                 " | Nombre: " + rs.getString("nombre_empleado") +
                                 " | Salario: " + rs.getDouble("salario_base_empleado") +
                                 " | Ingreso: " + rs.getDate("fecha_ingreso_empleado"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // VER DEPARTAMENTOS
    private static void verDepartamentos() {
        String sql = "SELECT * FROM t_departamentos";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery(sql)) {

            System.out.println("\n--- DEPARTAMENTOS ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("codigo_departamento") + 
                                 " | Nombre: " + rs.getString("nombre_departamento") +
                                 " | Presupuesto: " + rs.getDouble("presupuesto_departamento"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void informacionCompletaJoins() {
        String sql = "SELECT " +
                    "e.codigo_empleado, " +
                    "e.nombre_empleado, " +
                    "e.salario_base_empleado, " +
                    "e.fecha_ingreso_empleado, " +
                    "d.nombre_departamento, " +
                    "d.presupuesto_departamento, " +
                    "c.nombre_centro, " +
                    "c.ubicacion " +
                    "FROM t_empleados e " +
                    "JOIN t_departamentos d ON e.codigo_departamento = d.codigo_departamento " +
                    "JOIN t_centros c ON d.codigo_centro = c.codigo_centro " +
                    "ORDER BY e.codigo_empleado";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- INFORMACIÓN COMPLETA EMPLEADOS (CON JOINS) ---");
            System.out.println("==================================================================================");
            while (rs.next()) {
                System.out.println("ID Empleado: " + rs.getInt("codigo_empleado"));
                System.out.println("Nombre: " + rs.getString("nombre_empleado"));
                System.out.println("Salario: " + rs.getDouble("salario_base_empleado"));
                System.out.println("Fecha Ingreso: " + rs.getDate("fecha_ingreso_empleado"));
                System.out.println("Departamento: " + rs.getString("nombre_departamento"));
                System.out.println("Presupuesto Depto: " + rs.getDouble("presupuesto_departamento"));
                System.out.println("Centro: " + rs.getString("nombre_centro"));
                System.out.println("Ubicación: " + rs.getString("ubicacion"));
                System.out.println("----------------------------------------------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void eliminarEmpleado() {
    	
		try {
			System.out.println("Ingresa el codigo del empleado que quieras eliminar: ");
			int codigo = scanner.nextInt();
			
			String sql = "DELETE e FROM t_empleados e " +
                    "JOIN t_departamentos d ON e.codigo_departamento = d.codigo_departamento " +
                    "WHERE e.codigo_empleado = ?";
			
			
			try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement ps = conn.prepareStatement(sql)) {
				
				ps.setInt(1, codigo);
				ps.executeUpdate();
				System.out.println("Empleado eliminado correctamente");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    
    
}
