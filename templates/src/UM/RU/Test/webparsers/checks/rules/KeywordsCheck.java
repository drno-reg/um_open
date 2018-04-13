///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package UM.RU.Test.webparsers.checks.rules;
//
//import UM.RU.Test.webparsers.checks.CheckResult;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.google.gson.Gson;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//
///**
// *
// * @author vassilii
// */
//public class KeywordsCheck extends UM.RU.Test.webparsers.checks.CheckRule{
//
//    public KeywordsCheck(String url, String params) {
//        super(url, params);
//    }
//
//    @Override
//    protected CheckResult doCheck(HtmlPage body, String params) {
//            Gson gson = new Gson();
//            String keywords[] = gson.fromJson(params, String[].class);
//            List<String> failedKeywords = new ArrayList<>();
//            for(Object k: keywords){
//                String keyword = k.toString();
//
//                if(!body.getWebResponse().getContentAsString().contains(keyword)){
//                    failedKeywords.add(keyword);
//                }
//            }
//
//            String message = failedKeywords.isEmpty() ? "Проверка прошла успешно" :
//                    ( "Не прошли проверку ключевые слова: " + StringUtils.join(failedKeywords, ", "));
//            return new CheckResult(this, failedKeywords.isEmpty(), message);
//    }
//
//}
