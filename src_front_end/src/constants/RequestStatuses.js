export const RequestStatuses = Object.freeze([
  Object.freeze({
    value: "create",
    label: "作成中"
  }),
  Object.freeze({
    value: "confirm-create",
    label: "確認待ち"
  }),
  Object.freeze({
    value: "apply",
    label: "申請待ち"
  }),
  Object.freeze({
    value: "approve",
    label: "承認待ち"
  }),
  Object.freeze({
    value: "reject",
    label: "却下"
  }),
  Object.freeze({
    value: "confirm-settlement",
    label: "決済確認待ち"
  }),
  Object.freeze({
    value: "payment-done",
    label: "支払済"
  })
]);