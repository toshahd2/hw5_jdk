import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Main {
    static CountDownLatch firstMeal = new CountDownLatch(5);
    static CountDownLatch secondMeal = new CountDownLatch(5);
    static CountDownLatch thirdMeal = new CountDownLatch(5);
    static String lastEater;
    static final Object fork = new Object();
    static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {

        Thread ph1 = new Thread(new Main.Phil("Философ №1"));
        Thread ph2 = new Thread(new Main.Phil("Философ №2"));
        Thread ph3 = new Thread(new Main.Phil("Философ №3"));
        Thread ph4 = new Thread(new Main.Phil("Философ №4"));
        Thread ph5 = new Thread(new Main.Phil("Философ №5"));


        ph1.start();
        ph2.start();
        ph3.start();
        ph4.start();
        ph5.start();

    }

    public static class Phil implements Runnable {
        String name;

        public Phil(String name) {

            this.name = name;

        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(10));
                eat(firstMeal, " обедает первый раз");
                skipLastEater();
                eat(secondMeal, " обедает второй раз");
                skipLastEater();
                eat(thirdMeal, " обедает третий раз");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void eat(CountDownLatch count, String meal) throws InterruptedException {
            synchronized (fork) {
                lastEater = name;
                System.out.println(name + meal);
                Thread.sleep(500);
                System.out.println(name + " размышляет");
            }
            count.countDown();
            count.await();
        }

        public void skipLastEater() throws InterruptedException {

            while (Objects.equals(lastEater, name)) {
                Thread.sleep(1);
            }
        }
    }
}