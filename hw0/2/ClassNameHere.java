public class ClassNameHere {
    /** Returns the maximum value from m. */
    public static int max(int[] m) {
        int temp = m[0];
        for (int i = 1; i < m.length; i++) {
           if(temp<m[i]) temp = m[i];
        }
        return temp;
    }
    public static void main(String[] args) {
       int[] numbers = new int[]{9, 2, 15, 2, 22, 10, 6};      
       System.out.println(max(numbers));
    }
}