public class ThreeSum4107056042 extends ThreeSum {

  public int T_sum(int[] A) {

    // Quick Sort
    sort(A, 0, A.length - 1);

    // Pre-Processing
    int num_threads = Runtime.getRuntime().availableProcessors();
    int batch_num = (A.length - 2) / num_threads;

    // Split Calculation
    SplitSum[] work_queue = new SplitSum[num_threads];
    for (int i = 0; i < num_threads - 1; i++) {
      work_queue[i] = new SplitSum(A, batch_num * i, batch_num * (i + 1));
      work_queue[i].start();
    }
    // Manually assign last thread to prevent boundery issues
    work_queue[num_threads - 1] =
        new SplitSum(A, batch_num * (num_threads - 1), A.length - 2);
    work_queue[num_threads - 1].start();

    // Gathering Results
    int count = 0;
    for (int i = 0; i < num_threads; i++) {
      try {
        work_queue[i].join();
      } catch (InterruptedException e) { }
      count += work_queue[i].getResult();
    }

    return count;
  }

  private static class SplitSum extends Thread {
    private int[] A;
    private int from;
    private int to;
    private int result;
    public SplitSum(int[] A, int from, int to) {
      this.A = A;
      this.from = from;
      this.to = to;
    }

    @Override
    public void run() {
      int count = 0;
      for (int i = this.from; i < this.to; i++) {
        if (A[i] > 0) {
          break;
        }
        int front = i + 1, back = A.length - 1;
        while (front < back) {
          int sum = A[i] + A[front] + A[back];
          if (sum == 0) { count++; }
          if (sum >= 0) { back--; }
          if (sum <= 0) { front++; }
        }
      }
      this.result = count;
    }

    public int getResult() { return result; }
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
    int left = from, right = to, pivot = A[(from + to) / 2];
    while (left <= right) {
      while (A[left] < pivot) {
        ++left;
      }
      while (A[right] > pivot) {
        --right;
      }
      if (left < right) {
        int tmp = A[left];
        A[left] = A[right];
        A[right] = tmp;
        ++left;
        --right;
      } else if (left == right) {
        ++left;
      }
    }
    sort(A, from, right);
    sort(A, left, to);
  }
}
