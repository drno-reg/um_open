package main

import (
	"fmt"
	"net/http"
    "io/ioutil"
)
func main() {

	var url string = "https://www.fonbet.ru/#!/football-2018/results"
	url = "http://g.cn/robots.txt"
	url = "http://localhost:8080"

	// Make a get request
	rs, err := http.Get(url)
	// Process response
	if err != nil {
		panic(err) // More idiomatic way would be to print the error and die unless it's a serious error
	}
	defer rs.Body.Close()

	bodyBytes, err := ioutil.ReadAll(rs.Body)
	if err != nil {
		panic(err)
	}

	bodyString := string(bodyBytes)

	fmt.Println(bodyString)


}
