import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time
        for (String text : texts) {
            int maxSize = 0;
            for (int i = 0; i < text.length(); i++) {
                for (int j = 0; j < text.length(); j++) {
                    if (i >= j) {
                        continue;
                    }
                    boolean bFound = false;
                    for (int k = i; k < j; k++) {
                        if (text.charAt(k) == 'b') {
                            bFound = true;
                            break;
                        }
                    }
                    if (!bFound && maxSize < j - i) {
                        maxSize = j - i;
                    }
                }
            }
            System.out.println(text.substring(0, 100) + " -> " + maxSize);
        }
        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs - startTs) + "ms");

        // Многопоточность
        long startTs2 = System.currentTimeMillis(); // start time
        List<Thread> threads = new ArrayList<>();

        var thread = new Thread(
                () -> {
                    for (String text : texts) {
                        int maxSize = 0;
                        for (int i = 0; i < text.length(); i++) {
                            for (int j = 0; j < text.length(); j++) {
                                if (i >= j) {
                                    continue;
                                }
                                boolean bFound = false;
                                for (int k = i; k < j; k++) {
                                    if (text.charAt(k) == 'b') {
                                        bFound = true;
                                        break;
                                    }
                                }
                                if (!bFound && maxSize < j - i) {
                                    maxSize = j - i;
                                }
                            }
                        }
                        System.out.println(text.substring(0, 100) + " -> " + maxSize);
                    }
                }
        );
        threads.add(thread);
        thread.start();

        for (Thread threadJoin : threads) {
            thread.join(); // зависаем, ждём когда поток объект которого лежит в thread завершится
        }
        long endTs2 = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs2 - startTs2) + "ms" + " 2");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}