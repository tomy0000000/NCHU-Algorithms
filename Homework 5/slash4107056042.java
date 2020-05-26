public class slash4107056042 extends slash {

  public int slash_min(int[] A) {
    return A.length - factorize_search(A, 0, A.length - 1, 8) - 2;
  }

  private int factorize_search(int[] A, int from, int to, int factor) {

    // Fallback to Linear Search
    if (to - from <= factor + 1) {
      for (int i = from; i < to; i++) {
        if (A[i] > A[i + 1]) {
          return i;
        }
      }
      return -1;
    }

    // Fast Scan Blocks
    int previous;
    int index = from;
    for (int i = 0; i < factor; i++) {
      previous = index;
      index = from + (to - from) * (i + 1) / (factor + 1);
      if (A[previous] > A[index]) { // slash before here
        if (previous + 1 == index) {
          return previous;
        }
        return factorize_search(A, previous, index, factor);
      }
    }

    // Slashed at Last Block
    return factorize_search(A, index, to, factor);
  }
}
