import MySQLdb
import pylibmc as memcache
from flask import Flask, g, request, jsonify
from sae.const import (MYSQL_HOST, MYSQL_HOST_S,
    MYSQL_PORT, MYSQL_USER, MYSQL_PASS, MYSQL_DB
)

app = Flask(__name__)
app.debug = True
mc = memcache.Client()

@app.before_request
def before_request():
  g.db = MySQLdb.connect(MYSQL_HOST, MYSQL_USER, MYSQL_PASS,
                           MYSQL_DB, port=int(MYSQL_PORT))

@app.teardown_request
def teardown_request(exception):
  if hasattr(g, 'db'): g.db.close()

@app.route('/')
def hello():
  return "Hello, world! - Flask"

@app.route('/login', methods=['POST'])
def login():
  id = request.form['id'].encode("utf-8")
  pw = request.form['pw'].encode("utf-8")
  status, msg = memLogin(id, pw)
  return jsonify({'status':status, 'msg':msg})

@app.route('/register', methods=['POST'])
def register():
  id = request.form['id'].encode("utf-8")
  pw = request.form['pw'].encode("utf-8")
  status, msg = memRegister(id, pw)
  return jsonify({'status':status, 'msg':msg})

def memLogin(id, pw):
  if not mc.get(id):
    flag = sqlLogin(id, pw) 
    if flag:
      mc.set(id, pw)
      return "success", "login success"
    return "fail", "id not exit or pw error"
  else:
    rpw = mc.get(id)
    if rpw == pw:
      return "success", "login success"
    return "fail", "id not exit or pw error"

def memRegister(id, pw):
  flag = sqlRegister(id, pw)
  if flag:
    return "success", "register success"
  return "fail", "id exit"

def sqlLogin(id, pw):
  c = g.db.cursor()
  c.execute("SELECT * FROM users WHERE id=%s AND pw=%s", (id, pw))
  u = list(c.fetchall())
  if len(u) > 0:
    return True
  return False

def sqlRegister(id, pw):
  c = g.db.cursor()
  c.execute("SELECT * FROM users WHERE id=%s", (id))
  u = list(c.fetchall())
  if len(u) > 0:
    return False
  c.execute("INSERT INTO users(id,pw) VALUES(%s,%s)", (id, pw))
  return True
