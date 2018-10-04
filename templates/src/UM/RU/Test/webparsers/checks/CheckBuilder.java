///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package UM.RU.User.webparsers.checks;
//
//import UM.RU.User.webparsers.checks.rules.CheckReferences;
//import UM.RU.User.webparsers.checks.rules.JavascriptCheck;
//import UM.RU.User.webparsers.checks.rules.KeywordsCheck;
//
///**
// *
// * @author vassilii
// */
//public class CheckBuilder {
//   public CheckRule buildRule(String moniker, String url, String params) throws BuildException {
//       switch(moniker){
//           case "keywords": return new KeywordsCheck(url, params);
//           case "javascript": return new JavascriptCheck(url, params);
//           case "references": return new CheckReferences(url, params);
//           default: throw new BuildException("Не найдена проверка: " + moniker);
//       }
//   }
//}
