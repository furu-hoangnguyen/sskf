const files = require.context('.', false, /\.js$/);
const modules = {};

files.keys().forEach(fileName => {
  if (fileName ==='./index.js') {
    return;
  }
  const moduleName=fileName.replace(/(\.\/|\.js)/g,'');
  modules[moduleName] = files(fileName).default;
});
export default modules;