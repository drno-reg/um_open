package main

import (
	"net/http"
	//"io/ioutil"
	"fmt"
)
func handler(w http.ResponseWriter, r *http.Request) {
	// For this case, we will always pipe "Hello World" into the response writer
	fmt.Fprintf(w, "Hello World!")
}
func main() {

	//resp, err := http.Get("http://localhost:8080/")
	//if err != nil {
	//	// handle error
	//}
	//defer resp.Body.Close()
	//body, err := ioutil.ReadAll(resp.Body)

	var i interface{} = 23
	fmt.Printf("%v\n", i)

	//http.HandleFunc("/", handler)
	//http.ListenAndServe(":8080", nil)

}
