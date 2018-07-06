package main

import(
	"fmt"
	"net/http"
	"io/ioutil"
	"strings"
)


func keepLines(s string, n int) string {
	result := strings.Join(strings.Split(s, "\n")[:n], "\n")
	return strings.Replace(result, "\r", "", -1)
}

func main() {




var i interface{}=212

var url string="http://dev-app-0007:9000"

	url = "http://rnd-dwh-mn-001.msk.mts.ru:9090/targets"


fmt.Printf("%v\n", i)


	resp, err := http.Get(url)
	if err != nil {
		// handle error
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)


	fmt.Printf("%v\n", body)


}
