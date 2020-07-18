var modelApi = {
  modelGet: function (keyword, sortField, limitNumber, pageNumber){
    this.keyword = keyword;
    this.sortField = sortField;
    this.limitNumber = limitNumber;
    this.pageNumber = pageNumber;
    this.queryString = function () {
      return "keyword=" + this.keyword + "&sortField=" + this.sortField + "&limitNumber=" + this.limitNumber
          + "&pageNumber=" + this.pageNumber
    }
  }
}
export default modelApi