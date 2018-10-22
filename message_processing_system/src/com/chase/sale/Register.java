package com.chase.sale;

import com.chase.executer.OperationType;
import com.chase.message.AdjustmentMessage;
import com.chase.message.DetailedMessage;
import com.chase.message.Message;
import com.chase.product.StockProduct;

import java.util.*;

public class Register {
    Map<StockProduct, List<Transaction>> records;

    public Register() {
        this.records = new HashMap<>();
    }

    public Register(Map<StockProduct, List<Transaction>> records) {
        this.records = records;
    }

    public Map<StockProduct, List<Transaction>> getRecords() {
        return records;
    }

    /** Add a new new product
     * @param product
     * @return
     */
    public boolean addProduct(StockProduct product) {
        if(product == null || records.containsKey(product)) {
            return false;
        }
        
        ArrayList<Transaction> trnList = new ArrayList<>();
        trnList.add(new Transaction());

        records.put(product, trnList);
        return true;
    }

    /** Updates the records of products
     * @param message
     * @return
     */
    public boolean updateRecords(Message message) {
        if(message == null) {
            System.out.println("Invalid sales record. Please review incoming data.");
            return false;
        }

        StockProduct product = findProduct(message.getType());
        if(product == null) {
            System.out.println("Invalid sales record. Sold product was not in stock.");
            return false;
        }

        List<Transaction> transactions = records.get(product);

        if(message instanceof AdjustmentMessage) {
            transactions = adjustTransactions(transactions, message);
        } else if(message instanceof DetailedMessage) {
            transactions = addNewTransactions(transactions, message);
        } else {
            examineNotification(message);
            transactions.add(new Transaction(message.getSellingPrice()));
        }

        if(transactions.size() == 0) {
            System.out.println("\nThere are no product (" + message.getType() +
                    ") related sales to adjust. Doing nothing.");
            return false;
        }

        records.put(product, transactions);
        return true;
    }

    /** Finds the product as per product type to check if exists
     * @param productType
     * @return
     */
    private StockProduct findProduct(String productType) {
        Set<StockProduct> products = records.keySet();
        
        for(StockProduct product : products) {
            if(productType.equals(product.getType())) {
                return product;
            }
        }

        return null;
    }

    /** This method adjust the the transactions
     * @param transactions
     * @param message
     * @return
     */
    private List<Transaction> adjustTransactions(List<Transaction> transactions, Message message) {
        OperationType operationType = ((AdjustmentMessage) message).getOperationType();

        switch(operationType) {
            case ADD:
                for(Transaction transaction : transactions) {
                    transaction.setValue(transaction.getValue() + message.getSellingPrice());
                    transaction.setTransactionStatus(TransactionStatus.ADJUSTED);
                }
                break;
            case MULTIPLY:
                for(Transaction transaction : transactions) {
                    transaction.setValue(transaction.getValue() * message.getSellingPrice());
                    transaction.setTransactionStatus(TransactionStatus.ADJUSTED);
                }
                break;
            case SUBTRACT:
                for(Transaction transaction : transactions) {
                    if(transaction.getValue() < message.getSellingPrice()) {
                       System.out.println("Potential loss detected @ [Product type: " +
                               message.getType() + ", Existing value: " + transaction.getValue() +
                               ", Selling price: " + message.getSellingPrice() +
                               ", Adjustment operation: SUBTRACT] during processing.");
                    }

                    transaction.setValue(transaction.getValue() - message.getSellingPrice());
                    transaction.setTransactionStatus(TransactionStatus.ADJUSTED);
                }
                break;
            default:
                System.out.println("Unsupported operation encountered during processing.");
                break;
        }

        return transactions;
    }

    /** Adds new transactions
     * @param transactions
     * @param message
     * @return
     */
    private List<Transaction> addNewTransactions(List<Transaction> transactions, Message message) {
        double price = message.getSellingPrice();
        long transactionsCount = ((DetailedMessage) message).getInstanceCount();

        examineNotification(message);

        if(transactionsCount <= 0) {
            System.out.println("Null or negative sales instance(s) found. Doing nothing.");
            return transactions;
        }

        for(long i=0; i<transactionsCount; i++) {
            transactions.add(new Transaction(price));
        }

        return transactions;
    }

    /** Examines notifications
     * @param message
     */
    private void examineNotification(Message message) {
        if(message.getSellingPrice() <= 0) {
            System.out.println("\nLogging (on console) the free distribution of goods and money @ [Product type: " +
                    message.getType() + ", Selling price: " + message.getSellingPrice() +
                    "]. Incorrect sales notification(s)");
        }
    }

    /** Prints sales report
     * 
     */
    public void printSalesReport() {
        for(Map.Entry<StockProduct, List<Transaction>> record : records.entrySet()) {
            System.out.println("Product type: " + record.getKey().getType() +
                    ", Total units sold: " + record.getValue().size() +
                    ", Revenue generated: " + getRevenueForProduct(record.getValue())
            );
        }
    }

    /** Returns generated revenue from the sales of the product
     * @param transactions
     * @return
     */
    private double getRevenueForProduct(List<Transaction> transactions) {
        double revenueGenerated = 0;

        for(Transaction transaction : transactions) {
            revenueGenerated += transaction.getValue();
        }

        return revenueGenerated;
    }
}
