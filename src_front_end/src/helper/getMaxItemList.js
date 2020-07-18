const max = (list, field) => {
  return Math.max.apply(Math, list.map(function (item) {
    return item[field]
  }));
}
export default max