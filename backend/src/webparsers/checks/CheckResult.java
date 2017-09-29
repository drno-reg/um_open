/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webparsers.checks;

/**
 * Класс - результат проверки.
 * Счетчики
 * 1. Время ответа сервера
 * 2. Код ответа сервера
 */
public class CheckResult {
    private CheckRule check;
    private boolean isSuccess;
    private long loadingTime;
    private int statusCode;
    private final StringBuilder message = new StringBuilder();
    private long executeStarted;
    private long executeFinished;
    private int javascriptErrors = 0;
    private long pageSize = 0;
    
    public CheckResult(CheckRule check, long loadingTime, int statusCode, boolean isSuccess, String message){
        this.check = check;
        this.isSuccess = isSuccess;
        this.message.append(message);
        this.loadingTime = loadingTime;
        this.statusCode = statusCode;
    }

    public long getExecuteStarted() {
        return executeStarted;
    }

    public void setExecuteStarted(long executeStarted) {
        this.executeStarted = executeStarted;
    }

    public long getExecuteFinished() {
        return executeFinished;
    }

    public void setExecuteFinished(long executeFinished) {
        this.executeFinished = executeFinished;
    }
    
    
    
    public String getUrl(){
        return check.getUrl();
    }
    
    public CheckResult(CheckRule check, boolean isSuccess, String message){
        this(check, -1, -1, isSuccess, message);
    }
    
    
    public CheckResult(CheckRule check, long loadingTime, int statusCode){
        this(check, loadingTime, statusCode, false, "");
    }   
    
    
    public void appendMessage(String message){
        this.message.append(message);
    }

    public void setLoadingTime(long loadingTime) {
        this.loadingTime = loadingTime;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public CheckRule getCheck() {
        return check;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message.toString();
    }

    public long getLoadingTime() {
        return loadingTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getJavascriptErrors() {
        return javascriptErrors;
    }

    public void setJavascriptErrors(int javascriptErrors) {
        this.javascriptErrors = javascriptErrors;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

}
