///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package UM.RU.Test.webparsers.pageloader;
//
//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.CookieManager;
//import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
//import com.gargoylesoftware.htmlunit.ScriptException;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import org.apache.commons.lang3.StringUtils;
//
///**
// *
// * @author vassilii
// */
//public class WebLoader {
//    private final WebClient client = new WebClient(BrowserVersion.CHROME);
//    private final List<String> javascriptErrors = new ArrayList<>();
//    private static final String TEMPLATE = "Файл %s, строка: %d, столбец: %d. Сообщение: %s";
//    public WebLoader(){
//
//           client.getOptions().setCssEnabled(false);
//           CookieManager man = new CookieManager();
//           man.setCookiesEnabled(true);
//           client.setCookieManager(man);
//
//           client.waitForBackgroundJavaScript(50000);
//
//           client.getOptions().setThrowExceptionOnScriptError(false);
//           client.getOptions().setJavaScriptEnabled(true);
//           client.setAjaxController(new NicelyResynchronizingAjaxController());
//           client.getOptions().setTimeout(10000);
//           client.setJavaScriptErrorListener(new JavaScriptErrorListener() {
//
//               @Override
//               public void scriptException(HtmlPage hp, ScriptException se) {
//                  javascriptErrors.add(String.format(TEMPLATE, hp.getBaseURL(), se.getFailingLineNumber(), se.getFailingColumnNumber(), se.getLocalizedMessage()));
//               }
//
//               @Override
//               public void timeoutError(HtmlPage hp, long l, long l1) {
//               }
//
//               @Override
//               public void malformedScriptURL(HtmlPage hp, String string, MalformedURLException murle) {
//               }
//
//               @Override
//               public void loadScriptError(HtmlPage hp, URL url, Exception excptn) {
//
//               }
//           });
//
//    }
//
//    public HtmlPage load(String url) throws IOException{
//        return client.getPage(url);
//    }
//
//    public WebClient getWebClient(){
//        return client;
//    }
//
//    public int getJavascriptErrors() {
//        return javascriptErrors.size();
//    }
//
//    public String getErrorsList(){
//        return StringUtils.join(javascriptErrors, "\n");
//    }
//
//}
