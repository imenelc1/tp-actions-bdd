const { Client } = require('pg');

async function addStudent(client, name, grade) {
    const result = await client.query(
        'INSERT INTO students (name, grade) VALUES ($1, $2) RETURNING id',
        [name, grade]
    );

    return result.rows[0].id;
}

async function getStudent(client, id) {
    const result = await client.query(
        'SELECT id, name, grade FROM students WHERE id = $1',
        [id]
    );

    return result.rows[0] || null;
}

async function averageGrade(client) {
    const result = await client.query(
        'SELECT AVG(grade) AS average FROM students'
    );

    return result.rows[0].average === null
        ? null
        : Number(result.rows[0].average);
}

module.exports = { addStudent, getStudent, averageGrade };
