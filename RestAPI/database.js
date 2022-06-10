const mysql = require("mysql");

var config = {
  user: process.env.DB_USER,
  database: process.env.DB_NAME,
  password: process.env.DB_PASS,
};

if (process.env.NODE_ENV === "production") {
  console.log("Running from cloud. Connecting to DB through GCP socket.");
  config.socketPath = `/cloudsql/${process.env.INSTANCE_CONNECTION_NAME}`;
} else {
  console.log("Running from localhost. Connecting to DB directly.");
  config.host = process.env.DB_HOST;
}

let connection = mysql.createConnection(config);

connection.connect(function (err) {
  if (err) {
    console.error("Error connecting: " + err.stack);
    return;
  }
  console.log("Connected as thread id: " + connection.threadId);
});

module.exports = connection;
