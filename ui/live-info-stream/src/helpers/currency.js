export function formatDecimal(decimal) {
  return decimal.toLocaleString('en-GB', { minimumFractionDigits: 2 });
}

export function formatCcy(amount) {
  return `£${formatDecimal(amount)}`;
}
