public class ThreeSum4107056042 extends ThreeSum {

  public int T_sum(int[] A) {
    sort(A, 0, A.length - 1);
    int count = 0;
    for (int i = 0; i < A.length - 2; i++) {
      if (A[i] > 0) {
        break;
      }
      int front = i + 1, back = A.length - 1;
      while (front < back) {
        int sum = A[i] + A[front] + A[back];
        if (sum > 0) {
          back--;
        } else if (sum < 0) {
          front++;
        } else {
          // out.println(A[i] + " + " + A[front] + " + " + A[back]);
          count++;
          back--;
          front++;
        }
      }
    }
    return count;
  }

  //    #####
  //   #     #  ####  #####  #####
  //   #       #    # #    #   #
  //    #####  #    # #    #   #
  //         # #    # #####    #
  //   #     # #    # #   #    #
  //    #####   ####  #    #   #

  public static void sort(int[] A, int from, int to) {
    if (from >= to || A.length <= 1) {
      return;
    }
    int left = from;
    int right = to;
    int pivot = A[(from + to) / 2];
    while (left <= right) {
      while (A[left] < pivot) {
        left++;
      }
      while (A[right] > pivot) {
        right--;
      }
      if (left < right) {
        int tmp = A[left];
        A[left] = A[right];
        A[right] = tmp;
        left++;
        right--;
      } else if (left == right) {
        left++;
      }
    }
    sort(A, from, right);
    sort(A, left, to);
  }
}
