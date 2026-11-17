const { Client } = require('pg');
const {
    addStudent,
    getStudent,
    averageGrade
} = require('./students');

let client;

beforeEach(async () => {
    client = new Client({
        host: 'localhost',
        database: 'tp',
        user: 'postgres',
        password: 'postgres'
    });

    await client.connect();
    await client.query('TRUNCATE TABLE students RESTART IDENTITY');
});

afterEach(async () => {
    await client.end();
});

test('Ajouter et lire un étudiant', async () => {
    const id = await addStudent(client, 'Imene', 16);
    const student = await getStudent(client, id);

    expect(student.name).toBe('Imene');
    expect(Number(student.grade)).toBe(16);
});

test('Lire un étudiant inexistant', async () => {
    const student = await getStudent(client, 9999);

    expect(student).toBeNull();
});

test('Calculer la moyenne', async () => {
    await addStudent(client, 'Amel', 10);
    await addStudent(client, 'Karim', 14);

    expect(await averageGrade(client)).toBe(12);
});
