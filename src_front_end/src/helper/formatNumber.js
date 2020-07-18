const formatNumber = (number) => {
  return new Intl.NumberFormat({ maximumSignificantDigits: 3 }).format(number);
}
export default formatNumber