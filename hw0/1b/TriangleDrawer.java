public class TriangleDrawer {
    public static void drawTriangle(int N) {
        for (int i = 1; i <= N; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print("*");
            }
            if (i!=N) {
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
       drawTriangle(10);
    }
}