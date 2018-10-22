# Message Processing Application
-------------------------------------

This is a small message processing (standalone) application for processing sales notification messages.
It is based on the assumption that sales notification comes in JSON format. The application only parses all notifications but processes only (first) 50 of them. 
However, In actual implementation, the json would probably be received as a response to an API call, but here it is read from commandline.

##Processing requirements:
# All sales must be recorded
# All messages must be processed
# After every 10th message received your application should log a report detailing the number of sales of each product and their total value.
# After 50 messages your application should log that it is pausing, & stop accepting new messages 
   and log a report of the adjustments that have been made to each sale type while the application was running.

##Sales and Messages requirements:
# A sale has a product type field and a value
# Any number of different product types can be expected. There is no fixed set.
# message notification of sale is of one of the following types
 - Message Type 1 – contains the details of 1 sale E.g apple at 10p
 - Message Type 2 – contains the details of a sale and the number of occurrences of that sale. E.g 20 sales of apples at 10p each.
 - Message Type 3 – contains the details of a sale and an adjustment operation to be applied to all stored sales of this product type. 
   Operations can be add, subtract, or multiply

##On execution:
1) Application initializes stock (read from CSV file provided as commandline argument),
2) Parses sales notifications from JSON file (provided as commandline argument), and
3) Updates sales register to reflect 50 sales notifications.

##Reference Commandline Arguments:
1) Please adjust path (in the following strings) as per your needs.
2) First argument is stock file: D:/githubWorkspace/jp_morgan_tech_test/message_processing_system/src/resources/stock.csv
3) Second and last argument is notifications file: D:/githubWorkspace/jp_morgan_tech_test/message_processing_system/src/resources/Message_TYPE_1_notifications.json

##Sample Data:
There are five sample data files:
1) stock.csv: This file has stock data listed in there. The data is listed in the order of class (StockProduct) members.
2) Message_TYPE_1_notifications.json: This file only has notification messages of Message Type 1.
3) Message_TYPE_2_detailednotifications.json: This file only has notification messages of Message Type 2.
4) Message_TYPE_3_adjustmentnotifications.json: This file only has notification messages of Message Type 3.
5) Message_TYPE_ALL_mixednotifications.json: This file only has notification messages of all Message Types.

Stock data has been considered as stock products of apple,pineapple,watermelon,orange & blueberry.

