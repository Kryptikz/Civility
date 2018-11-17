import java.util.*;
public class AIThread implements Runnable {
    ArrayList<AI> bots;
    public AIThread() {
        bots = new ArrayList<AI>();
    }
    public void run() {
        while(true) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (AI a : bots) {
                a.update();
            }
        }
    }
    public ArrayList<AI> getBots() {
        return bots;
    }
    public void addBot(AI a) {
        bots.add(a);
    }
}