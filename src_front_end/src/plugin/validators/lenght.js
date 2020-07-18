import isEmpty from "lodash/isEmpty";

export default {
  params: ["min", "max"],
  validate(value, { min, max }) {
    if (isEmpty(value)) {
      return true;
    }
    const minValue = min || 1;
    const maxValue = max || Number.MAX_SAFE_INTEGER;
    return minValue <= value.length && value.length <= maxValue;
  }
};
