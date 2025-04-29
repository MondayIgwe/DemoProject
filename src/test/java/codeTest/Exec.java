package codeTest;

public class Exec implements Runnable {


    public static void main(String[] args) {
        Thread thread = new Thread(new Exec());
        thread.start();
        System.out.println(thread.getId());
    }

    @Override
    public void run() {
    }
}
