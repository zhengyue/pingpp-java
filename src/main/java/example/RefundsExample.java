package example;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeRefundCollection;
import com.pingplusplus.model.Refund;

import java.util.HashMap;
import java.util.Map;

/**
 * Refund Examples
 *
 */
public class RefundsExample {
	
	/**
	 * API Key
	 */
	public static String apiKey = "sk_test_f980u9mHqHK8LmP4CCCKOajL";
	/**
	 * the charge.id we want to refund
     * this is the id we got when creating a new transaction
	 */
	public static String chargeId = "ch_PyzPiDev9yT8T0K0uD0yDy5G";

    public static void main(String args[]) {
        Pingpp.apiKey = apiKey;
        RefundsExample refundsExample = new RefundsExample();

        Charge ch = null;
        try {
            ch = Charge.retrieve(chargeId);
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

        //退款的时候注意，已经退完款无法继续推
        System.out.println("---------creating refund");
        Refund refund = refundsExample.refund(ch);

        System.out.println("---------querying refund");
        refundsExample.retrieve(refund.getId(), ch);

        System.out.println("---------query all refund requests");
        refundsExample.all(ch);
    }

    /**
     * Refund
     *
     * to refund, first we need the charge object, then call charge.getRefunds().create()
     * doc：https://pingxx.com/document/api#api-r-new
     * 
     * can do full refund or partial refund
     * 
     * @param charge the charge object
     * @return a refund object
     */
    public Refund refund(Charge charge) {
        Refund refund = null;
        Map<String, Object> params = new HashMap<String, Object>();

        // maximum length is 255 characters
        params.put("description", "reason of the refund");

        // the amount must be less or euqal to the remaining amount
        // if no amount is set, then it's a full refund
        params.put("amount", "1");

        try {
            refund = charge.getRefunds().create(params);
            System.out.println(refund);
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
        return refund;
    }

    /**
     * query refund
     * 
     * doc：https://pingxx.com/document/api#api-r-inquiry
     * 
     * @param id the refund id
     * @param charge the charge object
     */
    public void retrieve(String id, Charge charge) {

        try {
            Refund refund = charge.getRefunds().retrieve(id);
            System.out.println(refund);
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
     * 分页查询退款
     * 
     * 批量查询退款。默认一次 10 条，用户可以通过 limit 自定义查询数目，但是最多不超过 100 条。
     * 参考文档：https://pingxx.com/document/api#api-r-list
     * 
     * @param charge
     */
    public void all(Charge charge) {
        Map<String, Object> refundParams = new HashMap<String, Object>();
        refundParams.put("limit", 3);
        try {
            ChargeRefundCollection refunds = charge.getRefunds().all(refundParams);
            System.out.println(refunds);
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
