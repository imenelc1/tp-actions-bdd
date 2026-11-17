import pytest
import psycopg2

from students import add_student, get_student, average_grade


@pytest.fixture
def conn():
    connection = psycopg2.connect(
        host="localhost",
        database="tp",
        user="postgres",
        password="postgres"
    )

    cursor = connection.cursor()
    cursor.execute("TRUNCATE TABLE students RESTART IDENTITY")
    connection.commit()
    cursor.close()

    yield connection

    connection.close()


def test_add_and_get_student(conn):
    student_id = add_student(conn, "Imene", 16)

    student = get_student(conn, student_id)

    assert student["name"] == "Imene"
    assert student["grade"] == 16.0


def test_get_student_not_found(conn):
    student = get_student(conn, 9999)

    assert student is None


def test_moyenne(conn):
    add_student(conn, "Amel", 10)
    add_student(conn, "Karim", 14)

    assert average_grade(conn) == 12.0
