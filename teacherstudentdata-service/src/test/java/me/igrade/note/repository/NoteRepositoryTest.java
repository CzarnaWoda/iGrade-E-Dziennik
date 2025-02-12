package me.igrade.note.repository;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class NoteRepositoryTest {
	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void getTotalNotesAmountByStudentId() {
		int studentId = 123;
		int expected = 123;
		int actual = noteRepository.getTotalNotesAmountByStudentId(studentId);

		assertEquals(expected, actual);
	}
}
