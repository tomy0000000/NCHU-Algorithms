public class IslandCounting4107056042 extends IslandCounting {

  // int count() variables
  private static int[] node_map;
  private static IslandTree island_tree;
  private static int island_num, island_merge, left, right;
  // class IslandTree variables
  private static int[] island_ids, island_sizes;
  private static int l_root, r_root;

  public int count(String[] A, String[] B) {

    // Initialize
    island_num = island_merge = 0;
    node_map = new int[A.length * 2];
    for (int i = 0; i < node_map.length; i++) {
      node_map[i] = -1;
    }
    island_tree = new IslandTree(A.length);

    // Go through each pairs
    for (int i = 0; i < A.length; i++) {
      left = Integer.parseInt(A[i]);
      right = Integer.parseInt(B[i]);
      if (node_map[left] == -1 && node_map[right] == -1) {
        // Case 1   : New + New -> New Island (+1)
        node_map[left] = island_num;
        node_map[right] = island_num;
        island_num++;
      } else if (node_map[left] == -1 && node_map[right] != -1) {
        // Case 2   : New + Old -> Island Expansion (0)
        node_map[left] = node_map[right];
      } else if (node_map[left] != -1 && node_map[right] == -1) {
        // Case 2   : Old + New -> Island Expansion (0)
        node_map[right] = node_map[left];
      } else if (node_map[left] != -1 && node_map[right] != -1) {
        // Case 3.1 : Old + Old -> Island Combination (-1)
        // Case 3.2 : Old + Old -> Internal Connection (0)
        island_merge += island_tree.union(left, right) ? 1 : 0;
      }
    }

    // Calculate Result
    island_num -= island_merge;
    return island_num;
  }

  // A Modified version of Weighted Union Find
  private static class IslandTree {
    public IslandTree(int size) {
      island_ids = new int[size];
      island_sizes = new int[size];
      for (int i = 0; i < size; i++) {
        island_ids[i] = i;
        island_sizes[i] = 1;
      }
    }
    private int find(int island_index) {
      while (island_index != island_ids[island_index]) {
        island_ids[island_index] = island_ids[island_ids[island_index]]; // OptImp
        island_index = island_ids[island_index];
      }
      return island_index;
    }
    public boolean union(int left, int right) {
      l_root = find(node_map[left]);
      r_root = find(node_map[right]);
      if (l_root == r_root) {
        node_map[left] = l_root;  // OptImp
        node_map[right] = r_root; // OptImp
        return false;             // Indicate already unioned
      }
      if (island_sizes[l_root] < island_sizes[r_root]) {
        island_ids[l_root] = r_root;
        island_sizes[r_root] += island_sizes[l_root];
        node_map[left] = node_map[right] = r_root; // OptImp
      } else {
        island_ids[r_root] = l_root;
        island_sizes[l_root] += island_sizes[r_root];
        node_map[left] = node_map[right] = l_root; // OptImp
      }
      return true; // Indicate union succeed
    }
  }
}
