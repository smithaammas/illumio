package com.illumio.analyzers.logs.util;

import com.illumio.analyzers.logs.config.LookupConfig;
import com.illumio.analyzers.logs.exception.ErrorCodes;
import com.illumio.analyzers.logs.exception.FlowLogException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FlowLogUtil {

    public static Map<String, String> lookupData = null;
    public static Map<String, String> protocolLookupData = null;

    public static Map<String, String> getLookupData() throws FlowLogException {
        if (lookupData == null)
            lookupData = loadLookupData();
        return lookupData;
    }

    public static Map<String, String> getProtocolLookup() throws FlowLogException{
        if (protocolLookupData == null)
            protocolLookupData = loadProtocolLookup();
        return protocolLookupData;
    }

    private static Map<String, String> loadLookupData() throws FlowLogException {
        Map<String, String> lookupData = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LookupConfig.LOOKUP_FILE))) {
            String line;
            // Skip header row
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length!=3) {
                    throw new FlowLogException(ErrorCodes.LOOKUP_DATA_PARSE_BAD_FORMAT, " Corrupt line is " + line);
                }
                String port = parts[0].toLowerCase();
                String protocol = parts[1].toLowerCase();
                String tag = parts[2];
                lookupData.put(port + ":" + protocol, tag);
            }
        } catch (IOException e) {
            throw new FlowLogException(ErrorCodes.LOOKUP_DATA_PARSE_IO, e);
        }
        if (lookupData.isEmpty()) {
            throw new FlowLogException(ErrorCodes.LOOKUP_DATA_EMPTY);
        }
        return lookupData;
    }

    private static Map<String, String> loadProtocolLookup() throws FlowLogException {
        Map<String, String> protocolLookup = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LookupConfig.PROTOCOL_LOOKUP_FILE))) {
            String line;
            // Skip header row
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s");
                if (parts.length < 2) {
                    throw new FlowLogException (
                            ErrorCodes.PROTO_MAP_DATA_PARSE_BAD_FORMAT, "Corrupt line is " + line);
                }
                String decimal = parts[0].toLowerCase();
                String protocol = parts[1].toLowerCase();
                protocolLookup.put(decimal, protocol);
            }
        } catch (IOException e) {
            throw new FlowLogException(ErrorCodes.PROTO_MAP_DATA_PARSE_IO, e);
        }
        return protocolLookup;
    }

    //Static string values for outfile creation
    public static String OUTPUT_FILE_TAG_MAIN_HEADER = "Tag Counts:\n";
    public static String OUTPUT_FILE_TAG_SUB_HEADER = "Tag\t\t\tCount\n";
    public static String OUTPUT_FILE_PROTOCOL_MAIN_HEADER = "\nPort/Protocol Combination Counts:\n";
    public static String OUTPUT_FILE_PROTOCOL_SUB_HEADER = "Port\t\tProtocol\tCount\n";
    public static final String NEW_LINE = "\n";
    public static final String DOUBLE_TAB = "\t\t";
    public static final String TRIPLE_TAB = "\t\t\t";


    public static void writeOutput(Map<String, Integer> tagCounts,
                             Map<String, Integer> portProtocolCounts) throws FlowLogException {

        try (Writer bw = new BufferedWriter(new FileWriter(LookupConfig.OUTPUT_FILE))) {
            bw.write(OUTPUT_FILE_TAG_MAIN_HEADER);
            bw.write(OUTPUT_FILE_TAG_SUB_HEADER);
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                bw.write(entry.getKey() + TRIPLE_TAB + entry.getValue() + NEW_LINE);
            }

            bw.write(OUTPUT_FILE_PROTOCOL_MAIN_HEADER);
            bw.write(OUTPUT_FILE_PROTOCOL_SUB_HEADER);

            for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
                String[] parts = entry.getKey().split(":");
                bw.write(parts[0] + DOUBLE_TAB + parts[1] + DOUBLE_TAB + entry.getValue() + NEW_LINE);
            }
        } catch (IOException e) {
            throw new FlowLogException(ErrorCodes.FLOW_LOG_RESULT_WRITER_ERROR, e);
        }
    }
}
