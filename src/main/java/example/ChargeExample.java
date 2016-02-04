package example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.exception.PingppException;
import com.pingplusplus.model.App;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;

/**
 * examples for doing the payment
 *
 */
public class ChargeExample {

	/**
	 * API Key from Ping++ backend
     *
     * this is the test secret key, so no real payment will be made
	 */
	public static String apiKey = "sk_test_f980u9mHqHK8LmP4CCCKOajL";

	/**
	 * App ID get from Ping++ backend
	 */
	public static String appId = "app_PK00mDb1Oyv9zDiD";
	
    public static void main(String[] args) {
        Pingpp.apiKey = apiKey;
        ChargeExample ce = new ChargeExample();
        System.out.println("---------creating transaction");
        Charge charge = ce.charge();

        System.out.println("---------querying transaction");
        ce.retrieve(charge.getId());

        System.out.println("---------querying all pending transactions");
        ce.all();
    }

    /**
     * create a new transaction
     * 
     * to create a new transaction, we need to pass a map object to Charge.create();
     * the descriptions of the fields is in: https://pingxx.com/document/api#api-c-new
     *
     * @return a charge object that we can return to the client-side
     *
     * example of the charge object
      {
        "id": "ch_PyzPiDev9yT8T0K0uD0yDy5G",  // this is the id created by ping++, required for other actions like query or refund
        "object": "charge",
        "created": 1454590882,
        "livemode": false,
        "paid": false,
        "refunded": false,      // true when there're any refund requests, no matter succeeded or not
        "app": "app_PK00mDb1Oyv9zDiD",  // app_id, can be expanded to a full app object if we pass "expand" to query transaction API call
        "channel": "alipay",
        "order_no": "123456789",  // this is the order number created by us
        "client_ip": "127.0.0.1",
        "amount": 100,
        "amount_settle": 100,
        "currency": "cny",
        "subject": "Your Subject",
        "body": "Your Body",
        "time_paid": null,
        "time_expire": 1454677282, // timestamp of when the transaction expires, depends on the payment method
        "time_settle": null,
        "transaction_no": null,  // this is the transaction number created by the payment provider
        "refunds": {   // list of refund objects
        "object": "list",
        "url": "/v1/charges/ch_PyzPiDev9yT8T0K0uD0yDy5G/refunds",
        "has_more": false,
        "data": []
        },
        "amount_refunded": 0,  // amount already has been refunded, because we can refund multiple times
        "failure_code": null,
        "failure_msg": null,
        "metadata": {},
        "credential": {  // information needed by client SDK, we don't need to worry about
        "object": "credential",
        "alipay": {
        "orderInfo": "_input_charset=\"utf-8\"&body=\"Your Body\"&it_b_pay=\"2016-02-05 21:01:22\"&notify_url=\"https%3A%2F%2Fapi.pingxx.com%2Fnotify%2Fcharges%2Fch_PyzPiDev9yT8T0K0uD0yDy5G\"&out_trade_no=\"123456789\"&partner=\"2008981912747609\"&payment_type=\"1\"&seller_id=\"2008981912747609\"&service=\"mobile.securitypay.pay\"&subject=\"Your Subject\"&total_fee=\"1.00\"&sign=\"OENHR0tDVEt5WGZIdkxDdW5USGlmTHk1\"&sign_type=\"RSA\""
        }
        },
        "extra": {},
        "description": null
        }
     */
    public Charge charge() {
        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();

        // amount in cents
        chargeMap.put("amount", 100);

        // now, only "cny" is supported
        chargeMap.put("currency", "cny");

        // order title
        // for UnionPay, should be less than 128 bytes, for others, should be less than 32 characters,
        chargeMap.put("subject", "Order Subject");

        // order description, should be less than 128 characters
        chargeMap.put("body", "Order Body");

        // payment method
        //   Alipay: alipay
        //   WeChat Pay: wx
        //   UnionPay: upacp
        chargeMap.put("channel", "alipay");

        // order number defined by app, same order number cannot be paid twice
        // length limitation differs by payment method:
        //   alipay: 1-64 bytes
        //   wx: 1-32 bytes
        //   upacp: 8-40 bytes
        // recommended: 8-20 bytes, alphabet and numbers only
        chargeMap.put("order_no", "123456789");

        // ip address of the client that starts the payment
        // I think this is for records only, so later you can find in ping++ backend who initiate the payment
        chargeMap.put("client_ip", "127.0.0.1");

        // app id
        Map<String, String> app = new HashMap<String, String>();
        app.put("id",appId);
        chargeMap.put("app", app);

        try {
            // call SDK method to send request to ping++ server to create a transaction
            charge = Charge.create(chargeMap);
            System.out.println(charge);
        } catch (PingppException e) {
            e.printStackTrace();
        }
        return charge;
    }

    /**
     * query transaction
     * 
     * doc：https://pingxx.com/document/api#api-c-inquiry
     * 
     * @param id the charge.id we got when creating a transaction
     */
    public void retrieve(String id) {
        try {

            // we can just query by charge.id
            //Charge charge = Charge.retrieve(id);

            // Or, if we pass "expand" parameter to this API, the object returned will contain an app object that is expanded.
            // otherwise, only an appid is returned
            // doc: https://pingxx.com/document/api#api-expanding
            Map<String, Object> param = new HashMap<String, Object>();
            List<String> expande = new ArrayList<String>();
            expande.add("app");
            param.put("expand", expande);

            //Expand app
            Charge charge = Charge.retrieve(id, param);

            if (charge.getApp() instanceof App) {
                //App app = (App) charge.getApp();
                // System.out.println("App Object ,appId = " + app.getId());
            } else {
                // System.out.println("String ,appId = " + charge.getApp());
            }

            System.out.println(charge);

        } catch (PingppException e) {
            e.printStackTrace();
        }
    }

    /**
     * query all transactions that are ever created, sorted by time created
     *
     * @return a list of charge objects, pagination is supported
     */
    public ChargeCollection all() {
        ChargeCollection chargeCollection = null;

        /**
         *
         * parameters for pagination:
         *
         * limit: number of objects returned, can be 1 to 100, default is 10
         * starting_after: index of the first object in current page
         * ending_before: index of the last object in current page
         *
         */
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("limit", 3);

// we can expand the app object here as well
//        List<String> expande = new ArrayList<String>();
//        expande.add("app");
//        chargeParams.put("expand", expande);

        try {
            chargeCollection = Charge.all(chargeParams);
            System.out.println(chargeCollection);
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
        return chargeCollection;
    }
}
