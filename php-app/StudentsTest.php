<?php

use PHPUnit\Framework\TestCase;

require_once __DIR__ . '/Students.php';

class StudentsTest extends TestCase
{
    private PDO $conn;
    private Students $students;

    protected function setUp(): void
    {
        $this->conn = new PDO(
            'pgsql:host=localhost;dbname=tp',
            'postgres',
            'postgres'
        );

        $this->conn->exec(
            'TRUNCATE TABLE students RESTART IDENTITY'
        );

        $this->students = new Students($this->conn);
    }

    public function testAddAndGetStudent(): void
    {
        $id = $this->students->addStudent('Imene', 16);

        $student = $this->students->getStudent($id);

        $this->assertEquals('Imene', $student['name']);
        $this->assertEquals(16.0, (float) $student['grade']);
    }

    public function testGetStudentNotFound(): void
    {
        $student = $this->students->getStudent(9999);

        $this->assertNull($student);
    }

    public function testAverageGrade(): void
    {
        $this->students->addStudent('Amel', 10);
        $this->students->addStudent('Karim', 14);

        $this->assertEquals(12.0, $this->students->averageGrade());
    }
}
