const convertOptionsVueSelect ={
  convertOptions : (list, fieldText, fieldValue) => {
    let _list = list.filter((elem, index, self) => self.findIndex((t) => {
      return (t[fieldText] === elem[fieldText])
    }) === index);
    return _list.reduce((options, item) => {
      let op = [{text: item[fieldText], value: item[fieldValue]}];
      return [...options, ...op]
    }, [])
  },
  convertOptionsArray : (list) => {
    return list.reduce((options, item) => {
      return [...options, ...[{text: item, value: item}]]
    }, [])
  }
}
export default convertOptionsVueSelect