///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package UM.RU.Test.webparsers.checks.rules;
//
//import UM.RU.Test.webparsers.checks.CheckResult;
//import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
//import com.gargoylesoftware.htmlunit.Page;
//import com.gargoylesoftware.htmlunit.html.DomElement;
//import com.gargoylesoftware.htmlunit.html.DomNodeList;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Predicate;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.apache.commons.lang3.StringUtils;
//
///**
// *
// * @author vassilii
// */
//public class CheckReferences extends UM.RU.Test.webparsers.checks.CheckRule{
//
//    public CheckReferences(String url, String params) {
//        super(url, params);
//    }
//
//    @Override
//    protected CheckResult doCheck(HtmlPage body, String params) {
//        List<String> faildRef = new ArrayList<>();
//
//        List<DomElement> els = new ArrayList<>(body.getElementsByTagName("a"));
//        els.removeIf(new Predicate<DomElement>(){
//
//            @Override
//            public boolean test(DomElement t) {
//                return t.getAttribute("href").startsWith("#") || t.getAttribute("href").startsWith("mailto");
//            }
//
//        });
//
//        for(DomElement a: els){
//            String href = a.getAttribute("href");
//            try {
//                Page page =  a.click();
//                if(page.getWebResponse().getStatusCode() != 200){
//                    faildRef.add(href + "(" + a.getTextContent() + ")");
//                }
//            } catch (IOException | FailingHttpStatusCodeException ex) {
//                faildRef.add(href + "(" + a.getTextContent() + ")");
//                Logger.getLogger(CheckReferences.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        return new CheckResult(this, faildRef.isEmpty(), String.format("Общее количество ссылок: %d, из них не успешно: %d: %s",
//                                     els.size(), faildRef.size(), StringUtils.join(faildRef, ", ")));
//    }
//
//}
