package kz.atu.lab11.service;

import kz.atu.lab11.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private StudentService service;

    @BeforeEach
    void setUp() {
        service = new StudentService();
    }

    // --- тесты из методических указаний ---

    @Test
    void shouldAddStudent() {
        service.addStudent("Алия", "ИС-301", 3.7);
        assertEquals(1, service.getStudents().size());
    }

    @Test
    void shouldRejectInvalidGpa() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addStudent("Алия", "ИС-301", 5.0));
    }

    // --- собственные тесты ---

    @Test
    void shouldRejectEmptyName() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.addStudent("   ", "ИС-301", 3.0));
        assertEquals("Введите ФИО", ex.getMessage());
    }

    @Test
    void shouldRejectEmptyGroup() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addStudent("Алия", "", 3.0));
    }

    @Test
    void shouldRejectNegativeAndNanGpa() {
        assertThrows(IllegalArgumentException.class, () -> service.addStudent("Алия", "ИС-301", -0.1));
        assertThrows(IllegalArgumentException.class, () -> service.addStudent("Алия", "ИС-301", Double.NaN));
        assertTrue(service.getStudents().isEmpty());
    }

    @Test
    void shouldAcceptBoundaryGpa() {
        service.addStudent("Первый", "ИС-301", 0);
        service.addStudent("Второй", "ИС-301", 4);
        assertEquals(2, service.getStudents().size());
    }

    @Test
    void shouldTrimInput() {
        service.addStudent("  Алия  ", " ИС-301 ", 3.7);
        Student s = service.getStudents().get(0);
        assertEquals("Алия", s.getName());
        assertEquals("ИС-301", s.getGroup());
    }

    @Test
    void shouldRejectDuplicateNameAndGroup() {
        service.addStudent("Алия", "ИС-301", 3.7);
        assertThrows(IllegalArgumentException.class,
                () -> service.addStudent("алия ", "ис-301", 3.0));
        assertEquals(1, service.getStudents().size());
        // то же ФИО в другой группе допустимо
        service.addStudent("Алия", "ИС-302", 3.0);
        assertEquals(2, service.getStudents().size());
    }

    @Test
    void shouldUpdateStudent() {
        service.addStudent("Алия", "ИС-301", 3.7);
        Student s = service.getStudents().get(0);
        service.updateStudent(s, "Алия Н.", "ИС-302", 3.9);
        assertEquals("Алия Н.", s.getName());
        assertEquals("ИС-302", s.getGroup());
        assertEquals(3.9, s.getGpa(), 1e-9);
    }

    @Test
    void shouldAllowUpdateWithoutChangingNameAndGroup() {
        service.addStudent("Алия", "ИС-301", 3.7);
        Student s = service.getStudents().get(0);
        assertDoesNotThrow(() -> service.updateStudent(s, "Алия", "ИС-301", 3.8));
    }

    @Test
    void shouldRejectUpdateWithoutSelection() {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateStudent(null, "Алия", "ИС-301", 3.0));
    }

    @Test
    void shouldDeleteStudent() {
        service.addStudent("Алия", "ИС-301", 3.7);
        service.deleteStudent(service.getStudents().get(0));
        assertTrue(service.getStudents().isEmpty());
    }

    @Test
    void shouldRejectDeleteWithoutSelection() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.deleteStudent(null));
        assertEquals("Студент не выбран", ex.getMessage());
    }

    @Test
    void shouldCalculateAverageGpa() {
        assertEquals(0.0, service.getAverageGpa(), 1e-9);   // пустой список
        service.addStudent("А", "ИС-301", 3.0);
        service.addStudent("Б", "ИС-301", 4.0);
        assertEquals(3.5, service.getAverageGpa(), 1e-9);
    }

    @Test
    void shouldReturnSortedUniqueGroups() {
        service.addStudent("А", "ИС-302", 3.0);
        service.addStudent("Б", "ИС-301", 3.0);
        service.addStudent("В", "ИС-302", 3.0);
        assertEquals(List.of("ИС-301", "ИС-302"), service.getGroups());
    }
}
