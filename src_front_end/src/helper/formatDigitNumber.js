const formatDigitNumber = (num) => {
  if (num) {
    let numStr = num.toString();
    let addZero = "";

    for (var i = 0; i < 11 - numStr.length; i++) {
      addZero += "0";
    }

    return addZero + numStr;
  } else {
    return "";
  }
}
export default formatDigitNumber;