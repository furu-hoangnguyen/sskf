export default {
  params: ["minNumber", "maxNumber"],
  validate(value, { minNumber, maxNumber }) {
    const minValue = minNumber || 1;
    const maxValue = maxNumber || Number.MAX_SAFE_INTEGER;
    return minValue <= value && value <= maxValue;
  }
};
