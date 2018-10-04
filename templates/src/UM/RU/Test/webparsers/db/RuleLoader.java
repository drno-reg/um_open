///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package UM.RU.User.webparsers.db;
//
//import UM.RU.User.webparsers.checks.BuildException;
//import UM.RU.User.webparsers.checks.CheckBuilder;
//import UM.RU.User.webparsers.checks.CheckResult;
//import UM.RU.User.webparsers.checks.CheckRule;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author vassilii
// */
//public class RuleLoader {
//    private int cabinet;
//    private final CheckBuilder builder = new CheckBuilder();
//    private final String SAVE_RESULT = "INSERT INTO check_result"
//            + "(loading_time, status_code, is_success, message, exute_started, excute_end, check_id, javascript_errors, page_size) "
//            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    public RuleLoader(int cabinet){
//        this.cabinet = cabinet;
//    }
//
//    public void execute(){
//        Connection conn = DbConnection.getConnection();
//        if(conn != null){
//            try(PreparedStatement stmt = conn.prepareStatement("SELECT url, params, moniker, id FROM check_rules WHERE cabinet_id = ?")) {
//                stmt.setInt(1, cabinet);
//                try(ResultSet resSet = stmt.executeQuery()){
//                     prepareRows(resSet, conn);
//                }
//            } catch (SQLException | BuildException ex) {
//                Logger.getLogger(RuleLoader.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//    }
//
//    private void prepareRows(ResultSet res, Connection conn) throws SQLException, BuildException{
//        while(res.next()){
//            Logger.getLogger(RuleLoader.class.getName()).log(Level.INFO, "Start loading for: {0}({1})", new Object[]{res.getString("moniker"), res.getString("url")});
//            CheckRule rule = builder.buildRule(res.getString("moniker"), res.getString("url"), res.getString("params"));
//            CheckResult checkRes = rule.execute();
//            saveResult(checkRes, conn, res.getInt("id"));
//            Logger.getLogger(RuleLoader.class.getName()).log(Level.INFO, "Results saved for: {0}({1})", new Object[]{res.getString("moniker"), res.getString("url")});
//        }
//    }
//
//    private void saveResult(CheckResult res, Connection conn, int id) throws SQLException{
//          try(PreparedStatement stmt = conn.prepareStatement(SAVE_RESULT)){
//              stmt.setLong(1, res.getLoadingTime());
//              stmt.setInt(2, res.getStatusCode());
//              stmt.setBoolean(3, res.isIsSuccess());
//              stmt.setString(4, res.getMessage());
//              stmt.setLong(5, res.getExecuteStarted());
//              stmt.setLong(6, res.getExecuteFinished());
//              stmt.setInt(7, id);
//              stmt.setInt(8, res.getJavascriptErrors());
//              stmt.setLong(9, res.getPageSize());
//              stmt.execute();
//          }
//    }
//}
