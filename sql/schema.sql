CREATE DATABASE IF NOT EXISTS prog2_db;
USE prog2_db;

CREATE TABLE IF NOT EXISTS libros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS prestamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libro_id INT NOT NULL,
    nombre_estudiante VARCHAR(100) NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE,
    FOREIGN KEY (libro_id) REFERENCES libros(id)
);

INSERT IGNORE INTO libros VALUES 
    (1, 'Don Quijote de la Mancha', 'Miguel de Cervantes', '978-8424115159'),
    (2, 'Rayuela', 'Julio Cortazar', '978-8437604572'),
    (3, 'Fahrenheit 451', 'Ray Bradbury', '978-8445071618'),
    (4, 'El Hobbit', 'J.R.R. Tolkien', '978-8445073483'),
    (5, 'Crimen y castigo', 'Fiodor Dostoyevski', '978-8491050513');

INSERT IGNORE INTO prestamos VALUES 
    (1, 1, 'Lucia Fernandez', '2026-08-05', '2026-08-12'),
    (2, 3, 'Mateo Gomez', '2026-08-18', NULL),
    (3, 4, 'Sofia Morales', '2026-08-22', NULL);', '2026-08-22', NULL);ria Gonzalez', '2026-08-20', NULL);