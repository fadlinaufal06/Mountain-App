const Cloud = require("@google-cloud/storage");
const path = require("path");
const serviceKey = path.join(
  __dirname,
  "c22-ps217-capstone-project-90d3f1b5ceaa.json"
);

const { Storage } = Cloud;
const storage = new Storage({
  keyFilename: serviceKey,
  projectId: "c22-ps217-capstone-project",
});

// storage.getBuckets().then((x) => console.log(x));

module.exports = storage;
