package com.illumio.analyzers.logs.core;

import com.illumio.analyzers.logs.exception.ErrorCodes;
import com.illumio.analyzers.logs.exception.FlowLogException;
import com.illumio.analyzers.logs.util.FlowLogUtil;
import com.illumio.analyzers.logs.config.LookupConfig;

import java.io.*;
import java.util.*;

public class FlowLogParser {

    Map<String, String> lookupTable = null;
    Map<String, String> protocolLookup = null;

    public FlowLogParser () throws Exception {
        lookupTable = FlowLogUtil.getLookupData();
        protocolLookup = FlowLogUtil.getProtocolLookup();
    }

    public void parseAndWriteResults () throws FlowLogException {
        Map<String, Integer> tagCounts = new HashMap<>();;
        Map<String, Integer> portProtocolCounts = new HashMap<>();;

        parseFlowLog(tagCounts, portProtocolCounts);
        FlowLogUtil.writeOutput(tagCounts, portProtocolCounts);
    }

    private void parseFlowLog(Map<String, Integer> tagCounts, Map<String, Integer> portProtocolCounts) throws FlowLogException {
        try (BufferedReader br = new BufferedReader(new FileReader(LookupConfig.FLOW_LOG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming port and protocol are at indices 1 and 2 in the line
                String[] fields = line.split("\\s+");

                if (fields.length < 7) {
                    System.out.println("Unexpected logline length " + fields.length + ", skipping row " + line);
                    continue;
                }

                // As per https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html#flow-logs-fields
                // dstport - The destination port of the traffic.
                String dstport = fields[6].toLowerCase();
                // protocol - The IANA protocol number of the traffic. For more information, see Assigned Internet Protocol Numbers.
                String protocolDecimal = fields[7].toLowerCase();
                String protocol = protocolLookup.getOrDefault(protocolDecimal, "UnknownProto");

                String key = dstport + ":" + protocol;
                String tag = lookupTable.getOrDefault(key, "Untagged");
                // Finding tag count
                int tagCount = tagCounts.getOrDefault(tag, 0) + 1;
                tagCounts.put(tag, tagCount);
                // Finding port:protocol count
                portProtocolCounts.put(key, portProtocolCounts.getOrDefault(key, 0) + 1);
            }
        } catch (IOException e) {
            throw new FlowLogException(ErrorCodes.FLOW_LOG_PARSE_IO, e);
        }
    }


}