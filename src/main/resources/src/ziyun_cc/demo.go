/*
Copyright IBM Corp 2016 All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package main

import (
	//"errors"
	//"time"
	//"bytes"
	"crypto/sha512"
	//"encoding/json"
	"encoding/hex"
	"fmt"
	"io"
	//"strconv"
	//"strings"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

//sp
var sp = "*&!"

type myChaincode struct {
}

// ============================================================================================================================
// Main
// ============================================================================================================================
func main() {
	err := shim.Start(new(myChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}

// Init resets all the things
func (t *myChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

// Invoke is our entry point to invoke a chaincode function
func (t *myChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	function, args := stub.GetFunctionAndParameters()
	switch function {

	case "sign":
		return t.sign(stub, args)

	case "getSignature":
		return t.getSignature(stub, args)

	case "verify":
		return t.verify(stub, args)

	default:
		return shim.Error("Unsupported operation")
	}
}

func (t *myChaincode) sign(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 3 {
		return shim.Error("saveProductInfo operation must have 3 args, rhash of the file , id of user and random number")
	}

	//get the args
	dataHash := args[0]
	id := args[1]
	rdm := args[2]

	//show the info of the args
	fmt.Println("the ars is :\n", dataHash, id, rdm)

	//combine the key
	key := id + dataHash + rdm

	hs := sha512.New()
	io.WriteString(hs, key)
	signature := hs.Sum(nil)

	//show the signature
	sgStr := hex.EncodeToString(signature)
	fmt.Println("the signature is: \n", sgStr, string(sgStr))

	err := stub.PutState(key, []byte(sgStr))
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	err = stub.PutState(sgStr, []byte(dataHash))
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	return shim.Success([]byte(sgStr))
}

func (t *myChaincode) getSignature(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 3 {
		return shim.Error("saveProductInfo operation must have 3 args, hash of the file , id of user and random number")
	}

	//get the args
	dataHash := args[0]
	id := args[1]
	rdm := args[2]

	fmt.Println("the ars is :\n", dataHash, id, rdm)

	//combine the key
	key := id + dataHash + rdm

	value, err := stub.GetState(key)
	if err != nil {
		return shim.Error("getting state err: " + err.Error())
	}
	if value == nil {
		return shim.Error("don't sign this file")
	}
	return shim.Success(value)
}

func (t *myChaincode) verify(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 2 {
		return shim.Error("saveProductInfo operation must have 4 args, hash of the file, the signature")
	}

	//get the args
	dataHash := args[0]
	signature := args[1]

	fmt.Println(dataHash, signature)

	//combine the key
	key := signature

	value, err := stub.GetState(key)
	if err != nil {
		return shim.Error("getting state err: " + err.Error())
	}
	if value == nil {
		return shim.Error("don't sign this file")
	}
	fmt.Println(string(value))
	ret := "233"
	if dataHash == string(value) {
		ret = "1"
	} else {
		ret = "0"
	}
	return shim.Success([]byte(ret))
}
