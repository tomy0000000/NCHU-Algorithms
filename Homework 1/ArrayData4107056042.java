public class ArrayData4107056042 extends ArrayData {

    public ArrayData4107056042() {}
    public ArrayData4107056042(int[] arr) {
        this.A = arr;
    }

    public int max() {
        int tmp_max = this.A[0];
        for(int i = 1; i < this.A.length; i++) {
            tmp_max = (tmp_max < this.A[i] ? this.A[i] : tmp_max);
        }
        return tmp_max;
    }

    public int min() {
        int tmp_min = this.A[0];
        for(int i = 1; i < this.A.length; i++) {
            tmp_min = (tmp_min > this.A[i] ? this.A[i] : tmp_min);
        }
        return tmp_min;
    }
}
