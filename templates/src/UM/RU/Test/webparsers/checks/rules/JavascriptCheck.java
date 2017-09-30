/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UM.RU.Test.webparsers.checks.rules;

import UM.RU.Test.webparsers.checks.CheckResult;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vassilii
 */
public class JavascriptCheck extends UM.RU.Test.webparsers.checks.CheckRule{

    public JavascriptCheck(String url, String params) {
        super(url, params);
    }

    @Override
    protected CheckResult doCheck(HtmlPage body, String params) {
       
       //getWebClient().setJavaScriptErrorListener();
        
      for(HtmlElement el: body.getBody().getElementsByAttribute("input", "type", "text")){
          if(el.isDisplayed()){
            el.setNodeValue("hi me");
          }
      }
      
      CheckResult res = null;
      
      try {
            for(HtmlElement el: body.getBody().getElementsByAttribute("input", "type", "submit")){
                if(el.isDisplayed()){
                  el.click();
                }
            }
            if(getLoader().getJavascriptErrors() > 0){
               res = new CheckResult(this, false, String.format("Были обнаружеы ошибки javascript на странице в количестве %d:\n%s", getLoader().getJavascriptErrors(), getLoader().getErrorsList())); 
            }else{ 
              return new CheckResult(this, true, "Успешно");    
            }
            return res;
        } catch (IOException ex) {
            Logger.getLogger(JavascriptCheck.class.getName()).log(Level.SEVERE, null, ex);
            return new CheckResult(this, false, "Ошибка при клике на кнопку");
        }
    }
    
}
