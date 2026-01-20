
import java.util.Scanner;
class java1 {
    public static int multiply(int a,int b){
        int c=a*b;
        return c;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in) {
        int a =sc.nextInt();
        int b =sc.nextInt();
        int x = multiply(a,b);
        System.out.println(x);
        }
    }