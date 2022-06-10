require("dotenv").config();

var express = require("express");
var app = express();
var bodyParser = require("body-parser");
var connection = require("./database");

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.get("/", (req, res) =>
  res.send(
    "Try: /status, /feeds, /mountain_detail, /mountain_review, /users, /users_detail"
  )
);

app.get("/status", (req, res) => res.send("Success."));

// FEEDS
app.get("/feeds", (req, res) => {
  connection.query(
    "SELECT * FROM `mountain`.`feeds`",
    (error, results, fields) => {
      if (error) throw error;
      res.json(results);
    }
  );
});

app.route("/feeds/:id").get((req, res, next) => {
  connection.query(
    "SELECT * FROM `mountain`.`feeds` WHERE id = ?",
    req.params.id,
    (error, results, fields) => {
      if (error) throw error;
      res.json(results);
    }
  );
});

app.post("/feeds", function (req, res) {
  let data = {
    username: req.body.username,
    upload_date: req.body.upload_date,
    caption: req.body.caption,
    photo: req.body.photo,
  };
  let sql = "INSERT INTO feeds SET ?";
  let query = connection.query(sql, data, (err, results) => {
    if (err) {
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log("success");
      res.redirect("/feeds");
    }
  });
});

app.delete("/feeds/:id", (req, res) => {
  let sql = "DELETE FROM feeds WHERE id= ?";
  let query = connection.query(sql, req.params.id, (err, results) => {
    if (err) {
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log("success");
      res.redirect("/feeds");
    }
  });
});

// Mountain Detail
app.get("/mountain_detail", (req, res) => {
  connection.query(
    "SELECT * FROM `mountain`.`mountain_detail`",
    (error, results, fields) => {
      if (error) throw error;
      res.json(results);
    }
  );
});

app.route("/mountain_detail/:id").get((req, res, next) => {
  connection.query(
    "SELECT * FROM `mountain_detail`.`feeds` WHERE id = ?",
    req.params.id,
    (error, results, fields) => {
      if (error) throw error;
      res.json(results);
    }
  );
});

app.post("/mountain_detail", function (req, res) {
  let data = {
    mountain_name: req.body.mountain_name,
    location: req.body.location,
    history: req.body.history,
    iconic_site: req.body.iconic_site,
  };
  let sql = "INSERT INTO mountain_detail SET ?";
  let query = connection.query(sql, data, (err, results) => {
    if (err) {
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log("success");
      res.redirect("/mountain_detail");
    }
  });
});

app.post("/mountain_detail", function (req, res) {
  let data = {
    mountain_name: req.body.mountain_name,
    location: req.body.location,
    history: req.body.history,
    iconic_site: req.body.iconic_site,
  };
  let sql = "INSERT INTO mountain_detail SET ?";
  let query = connection.query(sql, data, (err, results) => {
    if (err) {
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log("success");
      res.redirect("/mountain_detail");
    }
  });
});

//Mountain Review
app.get("/mountain_review", (req, res) => {
  connection.query(
    "SELECT * FROM `mountain`.`mountain_review`",
    (error, results, fields) => {
      if (error) throw error;
      res.json(results);
    }
  );
});

app.route("/mountain_review/:id").get((req, res, next) => {
  connection.query(
    "SELECT * FROM `mountain`.`mountain_review` WHERE id = ?",
    req.params.id,
    (error, results, fields) => {
      if (error) throw error;
      res.json(results);
    }
  );
});

app.post("/mountain_review", function (req, res) {
  let data = {
    mountain_name: req.body.mountain_name,
    username: req.body.username,
    rating: req.body.rating,
    comment: req.body.comment,
  };
  let sql = "INSERT INTO mountain_review SET ?";
  let query = connection.query(sql, data, (err, results) => {
    if (err) {
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log("success");
      res.redirect("/mountain_review");
    }
  });
});

app.delete("/mountain_review/:id", (req, res) => {
  let sql = "DELETE FROM mountain_review WHERE id= ?";
  let query = connection.query(sql, req.params.id, (err, results) => {
    if (err) {
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log("success");
      res.redirect("/mountain_review");
    }
  });
});

//Users
app.get("/users", (req, res) => {
  connection.query(
    "SELECT * FROM `mountain`.`users`",
    (error, results, fields) => {
      if (error) throw error;
      res.json(results);
    }
  );
});

app.route("/users/:id").get((req, res, next) => {
  connection.query(
    "SELECT * FROM `mountain`.`users` WHERE id = ?",
    req.params.id,
    (error, results, fields) => {
      if (error) throw error;
      res.json(results);
    }
  );
});

app.post("/users", function (req, res) {
  let data = {
    username: req.body.username,
    email: req.body.email,
    password: req.body.password,
  };
  let sql = "INSERT INTO users SET ?";
  let query = connection.query(sql, data, (err, results) => {
    if (err) {
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log("success");
      res.redirect("/users");
    }
  });
});

app.delete("/users/:id", (req, res) => {
  let sql = "DELETE FROM users WHERE id= ?";
  let query = connection.query(sql, req.params.id, (err, results) => {
    if (err) {
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log("success");
      res.redirect("/users");
    }
  });
});

//Users Detail
app.get("/users_detail", (req, res) => {
  connection.query(
    "SELECT * FROM `mountain`.`users_detail`",
    (error, results, fields) => {
      if (error) throw error;
      res.json(results);
    }
  );
});

app.route("/users_detail/:id").get((req, res, next) => {
  connection.query(
    "SELECT * FROM `mountain`.`users_detail` WHERE id = ?",
    req.params.id,
    (error, results, fields) => {
      if (error) throw error;
      res.json(results);
    }
  );
});

app.post("/users_detail", function (req, res) {
  let data = {
    username: req.body.username,
    name: req.body.name,
    photo: req.body.photo,
    favourite: req.body.favourite,
  };
  let sql = "INSERT INTO users_detail SET ?";
  let query = connection.query(sql, data, (err, results) => {
    if (err) {
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log("success");
      res.redirect("/users_detail");
    }
  });
});

app.delete("/users_detail/:id", (req, res) => {
  let sql = "DELETE FROM users_detail WHERE id= ?";
  let query = connection.query(sql, req.params.id, (err, results) => {
    if (err) {
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log("success");
      res.redirect("/users_detail");
    }
  });
});

app.put("/users_detail/:id", (req, res) => {
  let data = {
    username: req.body.username,
    name: req.body.name,
    photo: req.body.photo,
    favourite: req.body.favourite,
  };
  let sql = "UPDATE users_detail SET ? WHERE id= ?";
  let query = connection.query(sql, req.params.id, (err, results) => {
    if (err) {
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log("success");
      res.redirect("/users_detail");
    }
  });
});

// Use port 8080 by default, unless configured differently in Google Cloud
const port = process.env.PORT || 8000;
app.listen(port, () => {
  console.log(`App is running at: http://localhost:${port}`);
});
