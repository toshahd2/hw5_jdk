package philosophers;

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
        Thread phil1 = new Thread(new Main.Phil("Философ №1"));
        Thread phil2 = new Thread(new Main.Phil("Философ №2"));
        Thread phil3 = new Thread(new Main.Phil("Философ №3"));
        Thread phil4 = new Thread(new Main.Phil("Философ №4"));
        Thread phil5 = new Thread(new Main.Phil("Философ №5"));

        phil1.start();
        phil2.start();
        phil3.start();
        phil4.start();
        phil5.start();
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
                eating(firstMeal, " обедает первый раз");
                skipLastEater();
                eating(secondMeal, " обедает второй раз");
                skipLastEater();
                eating(thirdMeal, " обедает третий раз");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void eating(CountDownLatch count, String meal) throws InterruptedException {
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