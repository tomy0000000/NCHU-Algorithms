public class MPI4107056042 extends MPI {
    public int min(int[] array) {
        int left = 0;
        int right = array.length;
        while (left < right) {
            if (array[left] == left + 1) {
                left++;
            } else if (array[left] <= left || right < array[left]) {
                array[left] = array[--right];
            } else if (array[array[left] - 1] == array[left]) {
                array[left] = array[--right];
            } else {
                swap(array, left, array[left] - 1);
            }
        }
        return left + 1;
    }

    private void swap(int[] array, int left, int right) {
        int tmp = array[left];
        array[left] = array[right];
        array[right] = tmp;
    }
}
