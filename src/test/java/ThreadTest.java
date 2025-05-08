import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.yxchen.xinpicturebackend.model.entity.User;

import java.util.Date;

/**
 * 功能：
 * 作者：chen
 * 日期： 2025/5/8 10:19
 **/
public class ThreadTest {
    private static final Object lock = new Object();
    private static boolean isA = true; // 控制权标志，true表示A线程可以打印

    public static void main(String[] args) {
        /*Thread threadA = new Thread(new PrintTask("A", 8));
        Thread threadB = new Thread(new PrintTask("B", 8));

        threadA.start();
        threadB.start();*/
        User user1 = new User();
        user1.setId(1L);
        user1.setUserAccount("123");
        user1.setUserName("123");
        user1.setUserAvatar("123");
        user1.setUserProfile("123");
        user1.setUserRole("123");
        user1.setCreateTime(new Date());
        user1.setUpdateTime(new Date());
        user1.setIsDelete(0);
        User user2 = new User();

        System.out.println(user1);
        System.out.println(user2);



    }

    static class PrintTask implements Runnable {
        private final String printContent;
        private final int count;

        public PrintTask(String printContent, int count) {
            this.printContent = printContent;
            this.count = count;
        }

        @Override
        public void run() {
            for (int i = 0; i < count; i++) {
                synchronized (lock) {
                    // 判断当前是否轮到自己打印
                    while ((printContent.equals("A") && !isA) || (printContent.equals("B") && isA)) {
                        try {
                            lock.wait(); // 不是自己的回合就等待
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.print(printContent);

                    isA = !isA; // 切换控制权
                    lock.notify(); // 唤醒另一个线程
                }
            }
        }
    }

}
