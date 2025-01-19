package com.illumio.analyzers.logs;

import com.illumio.analyzers.logs.core.FlowLogParser;
import com.illumio.analyzers.logs.exception.FlowLogException;

public class FlowLogAnalyzer {

    public static void main(String[] args) throws Exception {
        FlowLogAnalyzer analyzer = new FlowLogAnalyzer();
        analyzer.analyzeFlowLogs();
    }

    private void analyzeFlowLogs() {
        try {
            FlowLogParser parser = new FlowLogParser();
            parser.parseAndWriteResults();
        } catch (FlowLogException flowLogException) {
            System.out.println("Error while executing flowLogParsing " + flowLogException);
        } catch (Exception generalException) {
            System.out.println("General exception while executing flowLogParsing " + generalException);
        }
    }
}
