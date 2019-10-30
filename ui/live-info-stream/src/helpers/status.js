const removedStatuses = ['REMOVED', 'REMOVED_VACANT', 'HIDDEN'];

export function isStatusRemoved(status) {
  return removedStatuses.includes(status);
}
