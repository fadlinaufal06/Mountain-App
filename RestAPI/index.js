require("dotenv").config();

var express = require("express");
var app = express();
var bodyParser = require("body-parser");
var connection = require("./database");
const posting = require("multer")();
const multer = require("multer");
const uploadImage = require("./helpers/helpers");

const multerMid = multer({
  storage: multer.memoryStorage(),
  limits: {
    fileSize: 5 * 1024 * 1024,
  },
});

app.disable("x-powered-by");
app.use(multerMid.single("file"));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// app.post("/upload-feeds", async (req, res, next) => {
//   try {
//     const myFile = req.file;
//     const imageUrl = await uploadImage(myFile);
//     let data = {
//       username: req.body.username,
//       upload_date: req.body.upload_date,
//       caption: req.body.caption,
//       photo: imageUrl,
//     };
//     res.status(200).json({
//       message: "Upload was successful",
//       data: data,
//     });
//   } catch (error) {
//     next(error);
//   }
// });

app.use((err, req, res, next) => {
  res.status(500).json({
    error: err,
    message: "Internal server error!",
  });
  next();
});

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

// app.post("/feeds", posting.any(), function (req, res) {
//   let data = {
//     username: req.body.username,
//     upload_date: req.body.upload_date,
//     caption: req.body.caption,
//     photo: req.body.photo,
//   };
//   let sql = "INSERT INTO feeds SET ?";
//   let query = connection.query(sql, data, (err, results) => {
//     if (err) {
//       console.log("error");
//       res.send({ result: "error" });
//     } else {
//       console.log("success");
//       res.send({ result: "success" });
//       res.redirect("/feeds");
//     }
//   });
// });

app.post("/feeds", async (req, res, next) => {
  try {
    const myFile = req.file;
    const imageUrl = await uploadImage(myFile);
    let data = {
      username: req.body.username,
      upload_date: req.body.upload_date,
      caption: req.body.caption,
      photo: imageUrl,
    };
    let sql = "INSERT INTO feeds SET ?";
    let query = connection.query(sql, data, (err, results) => {
      if (err) {
        console.log("error");
        console.log(err);
        res.send({ result: "error" });
      } else {
        console.log("success");
        res.status(200).json({
          result: "success",
          message: "Upload was successful",
          data: data,
        });
        // res.redirect("/feeds");
      }
    });
  } catch (error) {
    next(error);
  }
});

// res.status(200).json({
//   message: "Upload was successful",
//   data: imageUrl,
// });

app.put("/feeds/:id", posting.any(), function (req, res) {
  var user_id = req.params.id;
  var feeds = {
    username: req.body.username,
    upload_date: req.body.upload_date,
    caption: req.body.caption,
    photo: req.body.photo,
  };

  connection.query(
    "UPDATE feeds SET ? WHERE id = ?",
    [feeds, user_id],
    function (err, results) {
      if (err) {
        console.log("error");
        res.send({ result: "error" });
      } else {
        console.log("success");
        res.redirect("/feeds");
      }
    }
  );
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
    "SELECT * FROM `mountain`.`mountain_detail` WHERE id = ?",
    req.params.id,
    (error, results, fields) => {
      if (error) throw error;
      res.json(results);
    }
  );
});

app.post("/mountain_detail", posting.any(), function (req, res) {
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
      res.send({ result: "success" });
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
      res.send({ result: "success" });
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
      console.log(results[0]);
      res.json(results);
    }
  );
});

app.post("/mountain_review", posting.any(), function (req, res) {
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
      res.send({ result: "success" });
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

app.put("/mountain_review/:id", posting.any(), function (req, res) {
  var user_id = req.params.id;
  var mountain = {
    mountain_name: req.body.mountain_name,
    username: req.body.username,
    rating: req.body.rating,
    comment: req.body.comment,
  };

  connection.query(
    "UPDATE mountain_review SET ? WHERE id = ?",
    [mountain, user_id],
    function (err, results) {
      if (err) {
        console.log("error");
        res.send({ result: "error" });
      } else {
        console.log("success");
        res.redirect("/mountain_review");
      }
    }
  );
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

app.post("/users", posting.any(), function (req, res) {
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
      res.send({
        result: "success",
        loginResult: data,
      });
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

app.put("/users/:id", posting.any(), function (req, res) {
  var user_id = req.params.id;
  var user = {
    username: req.body.username,
    email: req.body.email,
    password: req.body.password,
  };

  connection.query(
    "UPDATE users SET ? WHERE id = ?",
    [user, user_id],
    function (err, results) {
      if (err) {
        console.log("error");
        res.send({ result: "error" });
      } else {
        console.log("success");
        res.redirect("/users");
      }
    }
  );
});

//Login
app.post("/login", function (req, res) {
  let data = {
    // username: req.body.username,
    email: req.body.email,
    password: req.body.password,
  };
  let sql = `SELECT * FROM users WHERE email="${data.email}" AND password="${data.password}" LIMIT 1`;
  let query = connection.query(sql, (err, results) => {
    if (err) {
      console.log(sql);
      console.log("error");
      res.send({ result: "error" });
    } else {
      console.log(sql);
      // console.log(query._results[0]);
      if (results.length) {
        console.log("success");
        console.log(results[0].username);
        data.username = results[0].username;
        res.send({
          result: "success",
          loginResult: data,
        });
      } else {
        console.log("error");
        res.send({
          result: "error",
          message: "User credentials is wrong!",
        });
      }
      // res.redirect("/users");
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

app.post("/users_detail", posting.any(), function (req, res) {
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
      res.send({ result: "success" });
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

app.put("/users_detail/:id", posting.any(), function (req, res) {
  var user_id = req.params.id;
  var users_detail = {
    username: req.body.username,
    name: req.body.name,
    photo: req.body.photo,
    favourite: req.body.favourite,
  };

  connection.query(
    "UPDATE users_detail SET ? WHERE id = ?",
    [users_detail, user_id],
    function (err, results) {
      if (err) {
        console.log("error");
        res.send({ result: "error" });
      } else {
        console.log("success");
        res.redirect("/users_detail");
      }
    }
  );
});

// Use port 8080 by default, unless configured differently in Google Cloud
const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log(`App is running at: http://localhost:${port}`);
});
