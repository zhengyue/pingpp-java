### Overview

This is a demo of how to use the Ping++ server SDK. See the comments in the following files for more detail.

* `ChargeExample`: example of creating and query a transaction
* `RefundsExample`: example of doing a refund
* `TransferExample`: example of transfering money to a recipient
* `WebHooksVerifyExample`: example of verifying the callback notification from ping++
* `ServerExample`: a simple sample server to create charge and handle callback notification

### ServerExample

To start the server, do

    $ mvn package && java -jar target/pingpp-demo-1.0-SNAPSHOT.jar

There are 2 endpoints:

* `/charge` - create a charge (transaction), can be used with the iOS demo client: https://github.com/zhengyue/pingpp-ios
* `/callback` - handle callback from Ping++. It just print out the request body and return 200 OK

You need to setup the callback URL in Ping++ dashboard, in the WebHooks page.
