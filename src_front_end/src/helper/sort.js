let merger = function(left, right, comparer) {
  const arr = [];
    while (left.length && right.length) {
        if (comparer(left[0], right[0])) {
          arr.push(left.shift());
        } else {
          arr.push(right.shift());
        }
    }
    return [ ...arr, ...left, ...right ];
}
let mergeSort = function(list, half = list.length/2, comparer) {
   if(list.length < 2){
    return list
  }
  const left = list.splice(0,half);
  return merger(mergeSort(left, left.length/2, comparer), mergeSort(list, list.length/2, comparer), comparer);
}
const sort = function(list, comparer) {
  if(!list || !Array.isArray(list) || list.length == 0 || !comparer || typeof comparer != "function") {
    return list;
  }
  return mergeSort(list, list.length/2, comparer);
}
const sortBySortNumber = function(list) {
  return sort(list, (itemA, itemB) => {
    return itemA.sortNumber < itemB.sortNumber;
  });
}
export default sortBySortNumber;
export { sortBySortNumber, sort };