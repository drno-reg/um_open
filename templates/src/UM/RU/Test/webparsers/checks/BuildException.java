/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UM.RU.Test.webparsers.checks;

/**
 *
 * @author vassilii
 */
public class BuildException extends Exception{
    public BuildException(String message){
        super(message);
    }
    
    public BuildException(String message, Throwable th){
        super(message, th);
    }
}
