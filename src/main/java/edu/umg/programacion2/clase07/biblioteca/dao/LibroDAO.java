package edu.umg.programacion2.clase07.biblioteca.dao;

import edu.umg.programacion2.clase07.biblioteca.modelo.Libro;
import java.sql.*;
import java.util.*;

public class LibroDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/prog2_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "123A";

    public int crear(Libro libro) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, isbn) VALUES (?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public List<Libro> listarTodos() throws SQLException {
        String sql = "SELECT id, titulo, autor, isbn FROM libros ORDER BY id";
        List<Libro> libros = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) libros.add(mapearFila(rs));
        }
        return libros;
    }

    public Optional<Libro> buscarPorIsbn(String isbn) throws SQLException {
        String sql = "SELECT id, titulo, autor, isbn FROM libros WHERE isbn = ?";
        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearFila(rs)) : Optional.empty();
            }
        }
    }

    private Libro mapearFila(ResultSet rs) throws SQLException {
        return new Libro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"), rs.getString("isbn"));
    }
}