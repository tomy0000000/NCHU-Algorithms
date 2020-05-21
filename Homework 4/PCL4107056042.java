public class PCL4107056042 extends PCL {

  private static boolean terminate = false;
  private static boolean[] done;
  private static boolean[] results;
  private static boolean[] not_checked;

  public boolean checkPCL(int[][] array) {

    // Initialize
    int num_threads = Runtime.getRuntime().availableProcessors();
    Thread[] run_queue = new Thread[num_threads];
    Runnable[] runnables = new Runnable[num_threads];
    done = new boolean[num_threads];
    results = new boolean[num_threads];
    not_checked = new boolean[num_threads];
    for (int i = 0; i < num_threads; i++) {
      not_checked[i] = true;
    }

    // Fire Threads
    int batch_num = array.length / (num_threads - 1);
    runnables[0] = new CheckVerticalHorizontal(array, 0);
    run_queue[0] = new Thread(runnables[0]);
    run_queue[0].start();
    for (int i = 1; i < num_threads - 1; i++) {
      runnables[i] =
          new CheckSlant(array, batch_num * (i - 1), batch_num * i, i);
      run_queue[i] = new Thread(runnables[i]);
      run_queue[i].start();
    }
    // Manually assign last thread to prevent boundery issues
    runnables[num_threads - 1] = new CheckSlant(
        array, batch_num * (num_threads - 2), array.length, num_threads - 1);
    run_queue[num_threads - 1] = new Thread(runnables[num_threads - 1]);
    run_queue[num_threads - 1].start();

    // Round-Robin each thread
    int still_running = num_threads;
    while (still_running != 0) {
      for (int i = 0; i < num_threads; i++) {
        if (done[i] && not_checked[i]) {
          if (results[i]) {
            terminate = true;
            return true;
          } else {
            not_checked[i] = false;
            still_running--;
          }
        }
      }
    }
    return false;
  }

  //    #####
  //   #     # #    # ######  ####  #    #  ####
  //   #       #    # #      #    # #   #  #
  //   #       ###### #####  #      ####    ####
  //   #       #    # #      #      #  #        #
  //   #     # #    # #      #    # #   #  #    #
  //    #####  #    # ######  ####  #    #  ####

  private static class CheckSlant implements Runnable {
    private int[][] array;
    private int from;
    private int to;
    private int index;
    public CheckSlant(int[][] array, int from, int to, int index) {
      this.from = from;
      this.to = to;
      this.array = array;
      this.index = index;
    }

    @Override
    public void run() {

      // Run each line (paris for dot)
      double slant;
      LineSet dot_lines;
      for (int i = this.from; i < this.to; i++) {

        // Initialize Set
        dot_lines = new LineSet(this.array.length - this.from);
        for (int j = i + 1; j < this.array.length; j++) {

          // Passes
          if (i == j) { // Same Dot -> pass
            continue;
          }
          if (this.array[i][0] == this.array[j][0]) { // Same x -> pass
            continue;
          }
          if (this.array[i][0] == this.array[j][0]) { // Same y -> pass
            continue;
          }

          // Calculation
          slant = (double)(this.array[i][1] - this.array[j][1]) /
                  (double)(this.array[i][0] - this.array[j][0]);

          // End Loop if Duplicate is found
          if (dot_lines.add(slant) || terminate) {
            results[this.index] = true;
            break;
          }
        }
        if (results[this.index] || terminate) {
          break;
        }
      }
      done[this.index] = true;
    }

    // Custom-Thread-Safe HashSet to Store Line with slant
    private static class LineSet {
      private static class LineNode {
        private double slant;
        public LineNode next;
        public LineNode(double slant) { this.slant = slant; }
      }

      private LineNode[] buckets;
      private Object[] bucket_locks;
      public LineSet(int maximum_load) {
        // Ceil to power of 2 with load factor of 0.75
        int bucket_size = (int)(maximum_load / 0.75) - 1;
        bucket_size |= bucket_size >> 1;
        bucket_size |= bucket_size >> 2;
        bucket_size |= bucket_size >> 4;
        bucket_size |= bucket_size >> 8;
        bucket_size |= bucket_size >> 16;
        this.buckets = new LineNode[++bucket_size];
        this.bucket_locks = new Object[bucket_size];
        for (int i = 0; i < this.bucket_locks.length; i++) {
          this.bucket_locks[i] = new Object();
        }
      }

      // Return True if this line is already in this set
      public boolean add(double slant) {
        int bucket = Double.hashCode(slant) & (this.buckets.length - 1);
        synchronized (this.bucket_locks[bucket]) {
          if (this.buckets[bucket] == null) {
            this.buckets[bucket] = new LineNode(slant);
            return false; // New at Array Head
          } else if (this.buckets[bucket].slant == slant) {
            return true; // Old at Array Head
          }
          LineNode loc = this.buckets[bucket];
          while (loc.next != null) {
            loc = loc.next;
            if (loc.slant == slant) {
              return true; // Old inside chain
            }
          }
          loc.next = new LineNode(slant);
          return false; // New inside chain
        }
      }
    }
  }

  private static class CheckVerticalHorizontal implements Runnable {
    private int[][] array;
    private int index;
    public CheckVerticalHorizontal(int[][] array, int index) {
      this.array = array;
      this.index = index;
    }

    @Override
    public void run() {

      // Initialize
      IntSet x_coords = new IntSet(this.array.length);
      IntSet y_coords = new IntSet(this.array.length);
      boolean x_duplicate, y_duplicate;

      // Run each dot
      for (int i = 0; i < this.array.length; i++) {
        x_duplicate = x_coords.add(this.array[i][0]);
        y_duplicate = y_coords.add(this.array[i][1]);
        if (x_duplicate) {
          results[this.index] = true;
          break;
        }
        if (y_duplicate) {
          results[this.index] = true;
          break;
        }
      }
      done[this.index] = true;
    }

    // Custom HashMap which store a coordinate (either x or y)
    // and its occuence number
    private static class IntSet {
      private static class LineNode {
        private int value;
        public int occuence = 1;
        public LineNode next;
        public LineNode(int value) { this.value = value; }
      }

      private LineNode[] buckets;
      public IntSet(int maximum_load) {
        // Ceil to power of 2 with load factor of 0.75
        int bucket_size = (int)(maximum_load / 0.75) - 1;
        bucket_size |= bucket_size >> 1;
        bucket_size |= bucket_size >> 2;
        bucket_size |= bucket_size >> 4;
        bucket_size |= bucket_size >> 8;
        bucket_size |= bucket_size >> 16;
        this.buckets = new LineNode[++bucket_size];
      }

      // Return True iff this is the third time value has been add to this map
      public boolean add(int value) {
        int bucket = value & (this.buckets.length - 1);
        if (this.buckets[bucket] == null) {
          this.buckets[bucket] = new LineNode(value);
          return false; // New at Array Head
        } else if (this.buckets[bucket].value == value) {
          return (++this.buckets[bucket].occuence) == 3; // Old at Array Head
        }
        LineNode loc = this.buckets[bucket];
        while (loc.next != null) {
          loc = loc.next;
          if (loc.value == value) {
            return (++loc.occuence) == 3; // Old inside chain
          }
        }
        loc.next = new LineNode(value);
        return false; // New inside chain
      }
    }
  }
}