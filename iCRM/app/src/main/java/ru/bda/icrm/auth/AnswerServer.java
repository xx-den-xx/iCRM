package ru.bda.icrm.auth;

/**
 * Created by User on 15.08.2016.
 */
public class AnswerServer {
    public static final String REQUEST_SUCCESS = "200";
    public static final String AUTH_SUCCESS = "202";
    public static final String REQUEST_INVALID = "400";
    public static final String ENDED_TOKEN = "401";
    public static final String AUTH_ERROR = "403";

    private static volatile AnswerServer instance;

    private AnswerServer () {}

    public static AnswerServer getInstance() {
        if (instance == null) {
            synchronized (AnswerServer.class) {
                if (instance == null) {
                    instance = new AnswerServer();
                }
            }
        }
        return instance;
    }

    public Boolean isAnswerServer(String state) {
        boolean isRequest;
        switch (state) {
            case REQUEST_SUCCESS:
                isRequest = true;
                break;
            case AUTH_SUCCESS:
                isRequest = true;
                break;
            case REQUEST_INVALID:
                isRequest = false;
                break;
            case ENDED_TOKEN:
                isRequest = false;
                break;
            case AUTH_ERROR:
                isRequest = false;
                break;
            default:
                isRequest = false;
                break;
        }
        return isRequest;
    }
}
