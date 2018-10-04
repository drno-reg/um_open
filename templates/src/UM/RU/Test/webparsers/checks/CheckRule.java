///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package UM.RU.User.webparsers.checks;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.WebResponse;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import java.io.IOException;
//import java.util.Date;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import UM.RU.User.webparsers.pageloader.WebLoader;
//
///**
// *
// * @author vassilii
// */
//public abstract class CheckRule {
//    private String url;
//    private String params;
//    private WebLoader loader = new WebLoader();
//
//    public CheckRule(String url, String params){
//        this.url = url;
//        this.params = params;
//    }
//
//    protected WebClient getWebClient(){
//        return loader.getWebClient();
//    }
//
//
//    public String getUrl() {
//        return url;
//    }
//
//    protected abstract CheckResult doCheck(HtmlPage body, String params);
//
//    public CheckResult execute(){
//        long started = (new Date()).getTime();
//        try {
//             HtmlPage page = loader.load(url);
//             WebResponse response = page.getWebResponse();
//             int statusCode = response.getStatusCode();
//             long loadingTime = response.getLoadTime();
//             if(response.getStatusCode() == 200){
//               CheckResult res = doCheck(page, params);
//               res.setLoadingTime(loadingTime);
//               res.setStatusCode(statusCode);
//               res.setExecuteStarted(started);
//               res.setExecuteFinished((new Date()).getTime());
//               res.setJavascriptErrors(loader.getJavascriptErrors());
//               res.setPageSize(response.getContentLength());
//               return res;
//             }
//             CheckResult badRes = new CheckResult(this, response.getLoadTime(), response.getStatusCode(),
//                                                  false, "Произошла ошибка при зарузке страицы");
//             badRes.setExecuteStarted(started);
//             badRes.setExecuteFinished((new Date()).getTime());
//             badRes.setJavascriptErrors(loader.getJavascriptErrors());
//             return badRes;
//        } catch (IOException ex) {
//            Logger.getLogger(CheckRule.class.getName()).log(Level.SEVERE, null, ex);
//            CheckResult exceptionResult = new CheckResult(this, -1, 500, false, "Получена ошибка при подключении к адресу");
//            exceptionResult.setExecuteStarted(started);
//            exceptionResult.setExecuteFinished((new Date()).getTime());
//            exceptionResult.setJavascriptErrors(loader.getJavascriptErrors());
//            return exceptionResult;
//        }
//    }
//
//    public WebLoader getLoader() {
//        return loader;
//    }
//
//
//}
