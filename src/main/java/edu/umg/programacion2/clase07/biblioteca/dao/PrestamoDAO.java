package edu.umg.programacion2.clase07.biblioteca.dao;

import edu.umg.programacion2.clase07.biblioteca.modelo.Prestamo;
import edu.umg.programacion2.clase07.biblioteca.modelo.PrestamoDetalle;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/prog2_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "123A";

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    public int registrarPrestamo(Prestamo prestamo) throws SQLException {
        String sql = "INSERT INTO prestamos (libro_id, nombre_estudiante, fecha_prestamo, fecha_devolucion) VALUES (?, ?, ?, ?)";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, prestamo.getLibroId());
            ps.setString(2, prestamo.getNombreEstudiante());
            ps.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            ps.setNull(4, java.sql.Types.DATE);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public boolean marcarDevuelto(int prestamoId, LocalDate fechaDevolucion) throws SQLException {
        String sql = "UPDATE prestamos SET fecha_devolucion = ? WHERE id = ? AND fecha_devolucion IS NULL";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fechaDevolucion));
            ps.setInt(2, prestamoId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<PrestamoDetalle> listarPrestamosActivosConLibro() throws SQLException {
        String sql = "SELECT p.nombre_estudiante, p.fecha_prestamo, l.titulo FROM prestamos p JOIN libros l ON p.libro_id = l.id WHERE p.fecha_devolucion IS NULL ORDER BY p.fecha_prestamo";
        List<PrestamoDetalle> resultado = new ArrayList<>();
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapearFila(rs));
            }
        }
        return resultado;
    }

    private PrestamoDetalle mapearFila(ResultSet rs) throws SQLException {
        return new PrestamoDetalle(
            rs.getString("titulo"),
            rs.getString("nombre_estudiante"),
            rs.getString("fecha_prestamo")
        );
    }
}