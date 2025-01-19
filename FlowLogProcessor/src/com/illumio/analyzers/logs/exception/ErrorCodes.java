package com.illumio.analyzers.logs.exception;

public enum ErrorCodes {

    //Flow log errors
    FLOW_LOG_PARSE_IO("FLOWLOG-PRSE-IO", "Unexpected error while parsing flowlog."),

    //Lookup map errors
    LOOKUP_DATA_PARSE_IO("LOOKUP-PRSE-IO", "Unexpected IOException while parsing lookup data."),
    LOOKUP_DATA_EMPTY("LOOKUP-PRSE-EMPTY", "Provided lookup data mapping in lookup_table.csv is empty"),
    LOOKUP_DATA_PARSE_BAD_FORMAT("LOOKUP-FORMAT-BAD", "Unexpected IOException while parsing lookup data"),

    //Proto Mapper Errors
    PROTO_MAP_DATA_PARSE_IO("PROTO-PRSE-IO", "Unexpected IOException while parsing protocol lookup data."),
    PROTO_MAP_DATA_PARSE_BAD_FORMAT("PROTO-FORMAT-BAD", "Unexpected token length in protocol lookup file, aborting process"),

    //Writer Errors
    FLOW_LOG_RESULT_WRITER_ERROR("WRTR-001", "Unexpected IOException while writing results.");


    private final String code;
    private final String message;

    ErrorCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}