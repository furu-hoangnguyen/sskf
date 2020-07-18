const path = require("path");
module.exports = {
  devServer: {
    port: 3000,
  },
  outputDir: path.resolve(__dirname, "../src/main/resources/static")
};
