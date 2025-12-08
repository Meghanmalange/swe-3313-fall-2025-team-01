const express = require('express');

const sqlite3 = require('sqlite3').verbose();

const path = require('path');
 
const DB_FILE = path.join(__dirname, 'africanroyals.db');

const app = express();

const PORT = process.env.PORT || 3000;
 
app.use(express.json());
 
// open (or create) the database

const db = new sqlite3.Database(DB_FILE, (err) => {

  if (err) return console.error('DB open error:', err);

  console.log('Connected to SQLite DB:', DB_FILE);

});
 
// Initialize table if not exists (safe to run every start)

db.serialize(() => {

  db.run(`

    CREATE TABLE IF NOT EXISTS users (

      id INTEGER PRIMARY KEY AUTOINCREMENT,

      name TEXT NOT NULL,

      email TEXT UNIQUE NOT NULL

    )

  `);
 
  // optional: insert sample rows if table empty

  db.get("SELECT COUNT(*) AS c FROM users", (err, row) => {

    if (err) return console.error(err);

    if (row.c === 0) {

      const stmt = db.prepare("INSERT INTO users (name, email) VALUES (?, ?)");

      stmt.run('Alice', 'alice@example.com');

      stmt.run('Bob', 'bob@example.com');

      stmt.run('Carol', 'carol@example.com');

      stmt.finalize();

      console.log('Inserted sample users');

    }

  });

});
 
// --- Endpoints ---
 
// GET all users

app.get('/users', (req, res) => {

  db.all("SELECT * FROM users", (err, rows) => {

    if (err) return res.status(500).json({ error: err.message });

    res.json(rows);

  });

});
 
// GET user by id

app.get('/users/:id', (req, res) => {

  const id = +req.params.id;

  db.get("SELECT * FROM users WHERE id = ?", id, (err, row) => {

    if (err) return res.status(500).json({ error: err.message });

    if (!row) return res.status(404).json({ error: 'User not found' });

    res.json(row);

  });

});
 
// POST create a new user

app.post('/users', (req, res) => {

  const { name, email } = req.body;

  if (!name || !email) return res.status(400).json({ error: 'name and email required' });
 
  const sql = "INSERT INTO users (name, email) VALUES (?, ?)";

  db.run(sql, [name, email], function (err) {

    if (err) return res.status(500).json({ error: err.message });

    res.status(201).json({ id: this.lastID, name, email });

  });

});
 
// PUT update user

app.put('/users/:id', (req, res) => {

  const id = +req.params.id;

  const { name, email } = req.body;

  const sql = "UPDATE users SET name = COALESCE(?, name), email = COALESCE(?, email) WHERE id = ?";

  db.run(sql, [name, email, id], function (err) {

    if (err) return res.status(500).json({ error: err.message });

    if (this.changes === 0) return res.status(404).json({ error: 'User not found' });

    res.json({ id, name, email });

  });

});
 
// DELETE user

app.delete('/users/:id', (req, res) => {

  const id = +req.params.id;

  db.run("DELETE FROM users WHERE id = ?", id, function (err) {

    if (err) return res.status(500).json({ error: err.message });

    if (this.changes === 0) return res.status(404).json({ error: 'User not found' });

    res.status(204).send();

  });

});
 
// health

app.get('/', (req, res) => res.send('SQLite Express API is running'));
 
app.listen(PORT, () => {

  console.log(`Server listening on http://localhost:${PORT}`);

});
 
// graceful close on exit

process.on('SIGINT', () => {

  console.log('Closing DB...');

  db.close();

  process.exit();

});

 