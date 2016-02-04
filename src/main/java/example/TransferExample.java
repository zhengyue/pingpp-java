package example;



import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.model.App;
import com.pingplusplus.model.Transfer;
import com.pingplusplus.model.TransferCollection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Transfer example
 *
 * currently, only WeChat transfer is supported
 *
 * openId is the receiver's identifier in WeChat, (not WeChat ID), we can get openId within WeChat:
 * http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html
 *
 */
public class TransferExample {

	public static String apiKey = "sk_test_f980u9mHqHK8LmP4CCCKOajL";
	public static String appId = "app_PK00mDb1Oyv9zDiD";
	
	/**
	 * recipient's openId
     *
     * TODO: We need to figure out a way to get recipient's openid
	 */
	public static String openId ="OPEN_ID";

    public static void main(String args[]) {

        Pingpp.apiKey = apiKey;
        TransferExample transferExample = new TransferExample();

        System.out.println("---------creating Transfer");
        Transfer transfer = transferExample.transfer();

        System.out.println("---------querying Transfer");
        transferExample.retrieve(transfer.getId());

        System.out.println("---------querying all Transfers");
        transferExample.all();
    }

    /**
     * create a transfer
     * 
     * doc: https://pingxx.com/document/api#api-t-new
     * 
     * @return a transfer object
     */
    public Transfer transfer() {
        Transfer transfer = null;

        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        Map<String, Object> transferMap = new HashMap<String, Object>();
        transferMap.put("channel", "wx_pub");

        // order number created by us
        transferMap.put("order_no", orderNo);

        transferMap.put("amount", "10");

        // only b2c is supported at this moment
        transferMap.put("type", "b2c");

        // only cny is supportec
        transferMap.put("currency", "cny");

        // recipient's openId in WeChat
        transferMap.put("recipient", openId);

        transferMap.put("description", "your description");

        Map<String, String> app = new HashMap<String, String>();
        app.put("id", appId);
        transferMap.put("app", app);

        try {
            transfer = Transfer.create(transferMap);
            System.out.println(transfer);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return transfer;
    }

    /**
     * query transfer
     * 
     * doc: https://pingxx.com/document/api#api-t-inquiry
     * @param id
     */
    public void retrieve(String id) {
        Map<String, Object> param = new HashMap<String, Object>();
        List<String> expande = new ArrayList<String>();
        expande.add("app");
        param.put("expand", expande);
        try {
            Transfer transfer = Transfer.retrieve(id, param);
            //Transfer transfer = Transfer.retrieve(id);
            //if you expand app properties  transfer.getApp() will return a App Object.
            if (transfer.getApp() instanceof App) {
                App app = (App) transfer.getApp();
                System.out.println("App Object ,appId = " + app.getId());
            } else {
                System.out.println("String ,appId = " + transfer.getApp());
            }
            System.out.println(transfer);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    /**
     * query all transfers
     */
    public void all() {
        Map<String, Object> parm = new HashMap<String, Object>();
        parm.put("limit", 3);
//        List<String> expande = new ArrayList<String>();
//        expande.add("app");
//        parm.put("expand", expande);

        try {
            TransferCollection transferCollection = Transfer.all(parm);
            System.out.println(transferCollection);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
