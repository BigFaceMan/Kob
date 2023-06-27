package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import com.kob.botrunningsystem.utils.Condition;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class Consumer extends Thread {
    private Bot bot;
    private static RestTemplate restTemplate;
    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();

        try {
            this.join(timeout);  // 最多等待timeout秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();  // 终端当前线程
        }
    }

    private String addUid(String code, String uid) {  // 在code中的Bot类名后添加uid
        String Main_code = "package com.kob.botrunningsystem.utils;\n" +
                "\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "\n" +
                "public class Bot implements com.kob.botrunningsystem.utils.BotInterface {\n" +
                "    static class Cell {\n" +
                "        public int x, y;\n" +
                "        public Cell(int x, int y) {\n" +
                "            this.x = x;\n" +
                "            this.y = y;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n";
        int k = Main_code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return Main_code.substring(0, k) + uid + Main_code.substring(k) + code + "}";
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        // 加个Uid保证每次都可以编译成功
        String uid = uuid.toString().substring(0, 8);
        // Reflect.complie 编译代码返回class对象  然后.creat().get() 得到一个实际的对象
        // 第一个参数是类名，第二个参数是要编译的java代码
//        System.out.println(bot.getBotCode());
        BotInterface botInterface = Reflect.compile(
                "com.kob.botrunningsystem.utils.Bot" + uid,
                addUid(bot.getBotCode(), uid)
        ).create().get();

        Integer direction = botInterface.nextMove(Condition.getCondition(bot.getInput()), Condition.getx(bot.getInput()), Condition.gety(bot.getInput()));
        System.out.println("move-direction: " + bot.getUserId() + " " + direction);

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());

        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }
}
