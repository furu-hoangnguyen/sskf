export default {
  filters: {
    $_mixins_formatNumber(value, format){
      if(format && value) {
        switch (format) {
          case "decimal":
            return (Math.round(value * 100) / 100).toFixed(2);
          case "percent":
            return (Math.round(value * 100) / 100).toFixed(2);
          default:
            return value;
        }
      }
      return value;
    }
  }
}