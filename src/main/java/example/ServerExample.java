package example;

import com.pingplusplus.model.Charge;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.*;

/**
 * A simple server to create charge and handle notification
 *
 * start the server:
 *
 *   mvn package && java -jar target/pingpp-demo-1.0-SNAPSHOT.jar
 *
 *
 * Created by zhengyue on 16/2/5.
 */
@RestController
public class ServerExample {

    /**
     * create a charge
     */
    @RequestMapping("/charge")
    public String charge(@RequestParam("channel") String channel, @RequestParam("amount") int amount) {
        ChargeExample ce = new ChargeExample(channel, amount);
        Charge charge = ce.charge();
        return charge.toString();
    }

    /**
     * handle callback notification
     */
    @RequestMapping("/callback")
    public String callback(@RequestHeader(value = "x-pingplusplus-signature") String signature,
                           @RequestBody String body) throws Exception {
        System.out.println(signature);
        System.out.println(body);

        //WARNING: the verification is not working yet, needs to figure out
        boolean result = WebHooksVerifyExample.verify(body.getBytes(), Base64.decodeBase64(signature));

        System.out.println("verification result: " + result);
        return "OK";
    }
}
