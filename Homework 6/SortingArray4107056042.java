public class SortingArray4107056042 extends SortingArray {

  public int[] sorting(int[] array) {

    // Small arrays, use insertion sort
    if (array.length <= 7) {
      insertion_sort(array, 0, array.length - 1);
    } else {
      quick_sort(array, 0, array.length);
    }

    return array;
  }

  private void insertion_sort(int[] array, int from, int end) {
    for (int i = from + 1; i <= end; i++) {
      for (int j = i; j > from && array[j - 1] > array[j]; j--) {
        swap(array, j, j - 1);
      }
    }
  }

  private void quick_sort(int[] array, int from, int count) {

    // Determine pivot, median element.
    int mid = count / 2;
    int lo = from;
    int hi = from + count - 1;
    int pivot = mid;

    // Big arrays use pseudomedian of 9
    if (count > 40) {
      int s = count / 8;
      lo = find_median(array, lo, lo + s, lo + 2 * s);
      mid = find_median(array, mid - s, mid, mid + s);
      hi = find_median(array, hi - 2 * s, hi - s, hi);
    }
    pivot = find_median(array, lo, mid, hi);

    // Swap pivot to front
    swap(array, from, pivot);
    int left_previous = from;
    int left_current = from;
    int right_current = from + count - 1;
    int right_previous = from + count - 1;

    // Partition
    while (true) {
      while (left_current <= right_current && array[left_current] <= array[from]) {
        if (array[left_current] == array[from]) {
          swap(array, left_previous, left_current);
          left_previous++;
        }
        left_current++;
      }
      while (right_current >= left_current && array[right_current] >= array[from]) {
        if (array[right_current] == array[from]) {
          swap(array, right_current, right_previous);
          right_previous--;
        }
        right_current--;
      }
      if (left_current > right_current) {
        break;
      }
      swap(array, left_current, right_current);
      left_current++;
      right_current--;
    }

    // Swap pivot(s) back in place, the recurse on left and right sections.
    hi = from + count;
    int span = Math.min(left_previous - from, left_current - left_previous);
    int left = from;
    int right = left_current - span;
    int range = span;
    for (; range > 0; left++, right++, range--) {
      swap(array, left, right);
    }

    span = Math.min(right_previous - right_current, hi - right_previous - 1);
    left = left_current;
    right = hi - span;
    range = span;
    for (; range > 0; left++, right++, range--) {
      swap(array, left, right);
    }

    span = left_current - left_previous;
    if (span > 1) {
      quick_sort(array, from, span);
    }

    span = right_previous - right_current;
    if (span > 1) {
      quick_sort(array, hi - span, span);
    }
  }

  private int find_median(int[] array, int one, int two, int three) {
    if (array[one] < array[two] && array[two] < array[three]) {
      return two;
    } else if (array[three] < array[two] && array[two] < array[one]) {
      return two;
    } else if (array[two] < array[one] && array[one] < array[three]) {
      return one;
    } else if (array[three] < array[one] && array[one] < array[two]) {
      return one;
    } else if (array[one] < array[three] && array[three] < array[two]) {
      return three;
    } else { // array[two] < array[three] && array[three] < array[one]
      return three;
    }
  }

  private void swap(int[] array, int left, int right) {
    int temp = array[left];
    array[left] = array[right];
    array[right] = temp;
  }
}
