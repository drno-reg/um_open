package main

//import (
//	"fmt"
//	"net/http"
//    "io/ioutil"
//	"encoding/base64"
//)
//
//func basicAuth(username, password string) string {
//	auth := username + ":" + password
//	return base64.StdEncoding.EncodeToString([]byte(auth))
//}
//
//func redirectPolicyFunc(req *http.Request, via []*http.Request) error{
//	req.Header.Add("Authorization","Basic " + basicAuth("admin","admin"))
//	return nil
//}
//
//func main() {
//
//	//var url string = "https://www.fonbet.ru/#!/football-2018/results"
//	//url = "http://g.cn/robots.txt"
//	//url = "http://localhost:8080"
//	//url = "http://dev-app-0007:9000/haproxy_stats/"
//	//
//	//// Make a get request
//	//rs, err := http.Get(url)
//	//// Process response
//	//if err != nil {
//	//	panic(err) // More idiomatic way would be to print the error and die unless it's a serious error
//	//}
//	//defer rs.Body.Close()
//	//
//	//bodyBytes, err := ioutil.ReadAll(rs.Body)
//	//if err != nil {
//	//	panic(err)
//	//}
//	//
//	//bodyString := string(bodyBytes)
//	//
//	//fmt.Println(bodyString)
//
//	    client := &http.Client{
//		Jar: cookieJar,
//		CheckRedirect: redirectPolicyFunc,
//	}
//
//	req, err := http.NewRequest("GET", "http://localhost/", nil)
//	req.Header.Add("Authorization","Basic " + basicAuth("username1","password123"))
//
//	resp, err := client.Do(req)
//
//
//}

import (
	"net/http"
	"log"
	"io/ioutil"
	"fmt"
)

func main() {
	basicAuth()
}

func basicAuth() string {
	var username string = "admin"
	var passwd string = "admin"

	var url string ="http://dev-app-0007:9000/haproxy_stats/"

	client := &http.Client{}
	req, err := http.NewRequest("GET", url, nil)
	req.SetBasicAuth(username, passwd)
	resp, err := client.Do(req)
	if err != nil{
		log.Fatal(err)
	}
	bodyText, err := ioutil.ReadAll(resp.Body)
	s := string(bodyText)
	fmt.Println(s)
	return s


}
