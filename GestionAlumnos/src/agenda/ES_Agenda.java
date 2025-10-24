package agenda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class ES_Agenda {
    private static final String URL = "jdbc:mysql://localhost:3306/adat1";
    private static String user = "dam2";
    private static String pass = "asdf.1234";
    
    static {
        inicializarBaseDatos();
    }
    
    private static void inicializarBaseDatos() {
        String sql = "CREATE TABLE IF NOT EXISTS contactos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL UNIQUE, " +
                    "telefono VARCHAR(20) NOT NULL)";
        
        try (Connection conn = DriverManager.getConnection(URL, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.execute();
            
        } catch (SQLException e) {
            System.err.println("Error inicializando base de datos: " + e.getMessage());
        }
    }
    
    // Métodos originales para CSV (ahora usan BD)
    public static Agenda leeAgendaDeCsv(File fichero) throws IOException {
        return leeAgendaDeBaseDatos();
    }
    
    public static void escribeAgendaEnCsv(File fichero, Agenda agenda) throws IOException {
        guardarAgendaEnBaseDatos(agenda);
    }
    
    // Métodos originales para Serialización (ahora usan BD)
    public static Agenda leeAgendaDeSerial(File fichero) throws Exception {
        return leeAgendaDeBaseDatos();
    }
    
    public static void escribeAgendaEnSerial(File fichero, Agenda agenda) throws IOException {
        guardarAgendaEnBaseDatos(agenda);
    }
    
    // Nuevos métodos para base de datos con PreparedStatement
    private static Agenda leeAgendaDeBaseDatos() {
        Agenda agenda = new Agenda();
        
        String sql = "SELECT nombre, telefono FROM contactos ORDER BY nombre";
        
        try (Connection conn = DriverManager.getConnection(URL, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Contacto contacto = new Contacto(
                    rs.getString("nombre"),
                    rs.getString("telefono")
                );
                agenda.addContacto(contacto);
            }
            
        } catch (SQLException e) {
            System.err.println("Error leyendo de base de datos: " + e.getMessage());
        }
        
        return agenda;
    }
    
    private static void guardarAgendaEnBaseDatos(Agenda agenda) {
        String deleteSQL = "DELETE FROM contactos";
        String insertSQL = "INSERT INTO contactos (nombre, telefono) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(URL, user, pass);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL);
             PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
            
            // Eliminar todos los contactos existentes
            deleteStmt.executeUpdate();
            
            // Insertar todos los contactos nuevos
            for (Contacto contacto : agenda.getTodos()) {
                insertStmt.setString(1, contacto.getNombre());
                insertStmt.setString(2, contacto.getTeléfono());
                insertStmt.executeUpdate();
            }
            
        } catch (SQLException e) {
            System.err.println("Error guardando en base de datos: " + e.getMessage());
        }
    }
    
    // Métodos para operaciones individuales en BD
    public static void guardarContacto(Contacto contacto) {
        String sql = "INSERT INTO contactos (nombre, telefono) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE telefono = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, contacto.getNombre());
            pstmt.setString(2, contacto.getTeléfono());
            pstmt.setString(3, contacto.getTeléfono());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error guardando contacto: " + e.getMessage());
        }
    }
    
    public static void eliminarContacto(String nombre) {
        String sql = "DELETE FROM contactos WHERE nombre = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error eliminando contacto: " + e.getMessage());
        }
    }
    
    public static void actualizarContacto(Contacto contacto) {
        String sql = "UPDATE contactos SET telefono = ? WHERE nombre = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, contacto.getTeléfono());
            pstmt.setString(2, contacto.getNombre());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error actualizando contacto: " + e.getMessage());
        }
    }
    

    public static Agenda leeAgendaDeCsvOriginal(File fichero) throws IOException {
        Agenda agenda = new Agenda();
        FileReader fr = new FileReader(fichero,StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(fr);
        String línea;
        while (  (línea=br.readLine()) != null) {
            String campos[] = línea.split(",");
            if (campos.length>1) {
                Contacto c = new Contacto(campos[0],campos[1]);
                agenda.addContacto(c);
            }
            else System.err.println("Línea de entrada inválida: " + línea);
        }
        br.close();
        return agenda;
    }
    
    public static void escribeAgendaEnCsvOriginal(File fichero, Agenda agenda) throws IOException {
        FileWriter salida = new FileWriter(fichero);
        salida.write(agenda.toCSV());
        salida.close();
    }
    
    public static Agenda leeAgendaDeSerialOriginal(File fichero) throws Exception {
        Agenda agenda;
    
        try ( FileInputStream file = new FileInputStream(fichero);
                ObjectInputStream input = new ObjectInputStream(file);)
        
        {
            agenda = (Agenda)input.readObject();
        } catch (ClassNotFoundException | IOException e) {
            agenda = new Agenda();
            System.err.println("Error leyendo agenda de fichero: " + fichero.getAbsolutePath());
            System.err.println(e.getMessage());
            throw e;
        }
        return agenda;
    }
    
    public static void escribeAgendaEnSerialOriginal(File fichero, Agenda agenda) throws IOException {
        try (
                FileOutputStream file = new FileOutputStream(fichero);
                ObjectOutputStream output = new ObjectOutputStream(file);
        )
        {
            output.writeObject(agenda);
        } catch (IOException e) {
            System.err.println("Error guardando agenda a fichero: " + fichero.getAbsolutePath());
            System.err.println(e.getMessage());
            throw e;
        }
    }
    
}