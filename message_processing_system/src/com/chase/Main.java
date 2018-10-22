package com.chase;

import com.chase.executer.SalesExecuter;
import com.chase.message.Message;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	
    public static void main(String[] args) {
    	messageProcessor(args);	
     }
    
    /** Message processing implementation. Triggers this process from main method.
     * @param args
     */
    private static void messageProcessor(String[] args){
    	if(args.length != 2) {
            System.out.println("Numbers of commandline arguments are not OK. Only two arguments are allowed at a time please.");
            System.exit(1);
        }

        if(isInvalidArgumentFilePath(args[0]) || isInvalidArgumentFilePath(args[1])) {
            System.out.println("Given file path(s) is (/are) either incorrect or inaccessible. Please confirm and re-run.");
            System.exit(1);
        }

        String stockDataFile = args[0];
        String notificationInputFile = args[1];

        SalesExecuter salesEngine = SalesExecuter.getSalesExecuter();

        boolean initializationStatus = salesEngine.initialize(stockDataFile);
        if(!initializationStatus) {
            System.out.println("Stock initialization failed.");
            System.exit(1);
        }

        List<Message> messagesList = salesEngine.parse(notificationInputFile);
        if(messagesList == null) {
            System.out.println("Sale notifications' parsing failed.");
            System.exit(1);
        }

        boolean processingStatus = salesEngine.process(messagesList);
        if(!processingStatus) {
            System.out.println("Sale notifications' processing failed.");
            System.exit(1);
        }
    }

    /** This method checks if the file path of the passed argument file is valid or invalid
     * @param argfilePath
     * @return
     */
    private static boolean isInvalidArgumentFilePath(String argfilePath) {
        try {
            Path path = Paths.get(argfilePath);

            if(!Files.exists(path) || Files.notExists(path)) {
            	System.out.println("Argument filepath either does not exist or unknown");
            	return true;
            }

            if(!Files.isRegularFile(path)) {
                return true;
            }

            if(!Files.isReadable(path)) {
            	System.out.println("Argument filepath not allowed to read");
            	return true;
            }
        } catch (InvalidPathException | NullPointerException exception) {
        	System.out.println("Argument filepath exception");
            return true;
        }

        return false;
    }
}
