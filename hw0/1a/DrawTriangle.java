public class DrawTriangle{
    public static void main(String[] args){
        for (int i = 1; i <= 5; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print("*");
            }
            if (i!=5) {
                System.out.println();
            }
        }
    }
}

