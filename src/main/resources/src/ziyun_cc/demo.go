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
    "fmt"
    "strings"
    "strconv"
    "encoding/json"

    "github.com/hyperledger/fabric/core/chaincode/shim"
    pb "github.com/hyperledger/fabric/protos/peer"
)
//sp
var sp = "*&!"

type myChaincode struct {
}
//the struct of the product 
type ProduceInformation struct {
	Address string
	ProductionBatch string
	ProductionTime int64
	ValidDate int64
}
 
type DrugInformation struct { 
	DrugName string
	ApprovalNumber string
	Size string
	Form string
	Manufacturer string
	NDCNumber string
	NDCNumberRemark string
	MedicineInstruction string
}

type Product struct {
	GoodsType string
	DrugElectronicSupervisionCode string 
	DrugInformation DrugInformation
	ProduceInformation ProduceInformation
}


//the struct of th e sensor
type Sensor struct {
	SensorNumber string
	SensorType string
	EquipmentNumber string
	EquipmentType string
	Time int64
	Temperature []float32
	Humidity []float32
	GPSLongitude float32
	GPSLatitude float32
	Address string
}


//the struct of the transfer
type ConsignorInfo struct {
	CountrySubdivisionCode string
	PersonalIdentityDocument string
	Consignor string
}
type ConsigneeInfo struct {
	CountrySubdivisionCode string
	Consignee string
	GoodsReceiptPlace string
}

type PriceInfo struct {
	TotalMonetaryAmount float32
	Remark string
}

type GoodsInfo struct {
	DescriptionOfGoods string
	CargoTypeClassificationCode string
	GoodsItemGrossWeight float32
	Cube float32
	TotalNumberOfPackages int
}

type Driver struct {
	QualificationCertificateNumber string
	NameOfPerson string
	TelephoneNumber string
}

type VehicleInfo struct {
	RoadTransportCertificateNumber string
	PermitNumber string
	VehicleNumber string
	TrailerVehiclePlateNumber string
	VehicleClassificationCode string
	LicensePlateTypeCode string
	VehicleTonnage float32
	Owner string
	GoodsInfoList []GoodsInfo
	DriverList []Driver
}

type LogisticsTrace struct {
	TraceName string
	TraceTime int64
}

type GoodsTrace struct {
	UniqueID string
	CommodityCode string
	ProductionBatch string
}

type Transfer struct {
	PermitNumber string
	UnifiedSocialCreditIdentifier string
	Carrier string
	BusinessTypeCode string
	OriginalDocumentNumber string
	ShippingNoteNumber string
	ConsignmentDateTime int64
	DespatchActualDateTime int64
	GoodsReceiptDateTime int64
	FreeText string
	ConsignorInfo ConsignorInfo
	ConsigneeInfo ConsigneeInfo
	PriceInfo PriceInfo
	VehicleInfo VehicleInfo
	LogisticsTraceList []LogisticsTrace
	GoodsTraceList []GoodsTrace
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
func (t *myChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response{
    return shim.Success(nil)
}

// Invoke is our entry point to invoke a chaincode function
func (t *myChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
    function, args := stub.GetFunctionAndParameters()
    switch function {

    case "saveProductInfo":
        return t.saveProductInfo(stub, args)

    case "saveSensorData":
        return t.saveSensorData(stub, args)

    case "saveTransferInfo":
        return t.saveTransferInfo(stub, args)

    case "getProductInfo":
        return t.getProductInfo(stub, args)

    case "getBatchProductInfo":
        return t.getBatchProductInfo(stub, args)

    case "getSensorDataBySensorNum":
        return t.getSensorDataBySensorNum(stub, args)

    case "getSensorDataByEquipmentNum":
        return t.getSensorDataByEquipmentNum(stub, args)

    case "getTransferInfoByDocNum":
        return t.getTransferInfoByDocNum(stub, args)

    case "getTransferInfoByNoteNum":
        return t.getTransferInfoByNoteNum(stub, args)

    case "getTransferInfoByUniqueID":
        return t.getTransferInfoByUniqueID(stub, args)

    default:
        return shim.Error("Unsupported operation " + function)
    }
}

func (t *myChaincode) saveProductInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    if len(args) < 1{
        return shim.Error("saveProductInfo operation must have 1 arg")
    }
    // get the args
    bProduct := []byte(args[0])
    //get some info
    product := &Product{}
    err := json.Unmarshal(bProduct, &product)
    if err != nil {
    	return shim.Error("Unmarshal failed")
    }
    if (product.DrugElectronicSupervisionCode == ""){
    	return shim.Error("bad format of the DrugElectronicSupervisionCode")
    }
    if (product.DrugInformation.NDCNumber == ""){
    	return shim.Error("bad format of the NDCNumber")
    }
    if (product.ProduceInformation.ProductionBatch == ""){
    	return shim.Error("bad format ot the ProductionBatch")
    }

    //save the json info
    err = stub.PutState(product.DrugElectronicSupervisionCode, bProduct)
    if err != nil {
    	return shim.Error("putting state err: " +  err.Error())
    }
    //save the other info
    key := product.DrugInformation.NDCNumber + sp + product.ProduceInformation.ProductionBatch
    value, err := stub.GetState(key)
    if err != nil {
    	return shim.Error("getting state err: " +  err.Error())
    }
    newValue := ""
    if value == nil {
    	newValue = product.DrugElectronicSupervisionCode
    } else {
    	newValue = string(value) + sp + product.DrugElectronicSupervisionCode
    }
    err = stub.PutState(key, []byte(newValue))
    if err != nil {
    	return shim.Error("updating state err: " + err.Error())
    }
    return shim.Success(nil)
}

func (t *myChaincode) saveSensorData(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("saveSensorData operation must have 1 arg")
	}

	// get hte args
	bSensor := []byte(args[0])
	//get the info 
	sensor := &Sensor{}
	err := json.Unmarshal(bSensor, &sensor)
	if err != nil {
		return shim.Error("Unmarshal failed")
	}
	tm := sensor.Time
	sTm := strconv.FormatInt(tm, 10)
	eqmtNum := sensor.EquipmentNumber
	if sTm == "" {
		return shim.Error("bad format of the time")
	}
	if eqmtNum == ""{
		return shim.Error("bad format of the eqmtNum")
	}
	//save the json data
	key := sensor.SensorNumber + sp + sTm
	err = stub.PutState(key, bSensor)
	if err != nil {
		return shim.Error("saveSensorData operation failed. Error while putting the SensorData : " + err.Error())
	}
	//save the other info
	err = stub.PutState(sensor.SensorNumber, []byte(eqmtNum))
	if err != nil {
		return shim.Error("saveSensorData operation failed. Error while putting the other info : " + err.Error())
	}
	return shim.Success(nil)
}

func (t *myChaincode) saveTransferInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("saveTransferInfo operation must have 1 arg")
	}
	bTransfer := []byte(args[0])
	//get the info
	transfer := &Transfer{}
	err := json.Unmarshal(bTransfer, &transfer)
	if err != nil {
		return shim.Error(err.Error())
	}
	docNum := transfer.OriginalDocumentNumber
	noteNum := transfer.ShippingNoteNumber

	//save the json data
	err = stub.PutState(docNum, bTransfer)
	if err != nil {
		return shim.Error(err.Error())
	}
	//save the other info
	err = stub.PutState(docNum, []byte(noteNum))
	if err != nil {
		return shim.Error(err.Error())
	}
	//save the uniqueid with the docnum
	for _, trace := range transfer.GoodsTraceList {
		uniqueId := trace.UniqueID
		//find the uniquedId's state 
		value, err := stub.GetState(uniqueId)
		if err != nil {
			return shim.Error(err.Error())
		}
		newValue := ""
		if value == nil {
			newValue = uniqueId
		} else {
			newValue = string(value) + sp + uniqueId
		}
		err = stub.PutState(uniqueId, []byte(newValue))
		if err != nil {
			return shim.Error(err.Error())
		}
	}

	return shim.Success(nil)
}

func (t *myChaincode) getProductInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1{
        return shim.Error("getProductInfo operation must have 1 arg")
    }
    key := string(args[0])

    value, err := stub.GetState(key)
    if err != nil{
    	return shim.Error("getProductInfo operation failed while getting the state : " + err.Error())
    }

	return shim.Success(value)
}

func (t *myChaincode) getBatchProductInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 2 {
		return shim.Error("getBatchProductInfo operation must have 2 args: NDCNumber and ProductionBatch")
	}
	//get the list
	NDCNumber := args[0]
	ProductionBatch := args[1]
	key := NDCNumber + sp + ProductionBatch
	value, err := stub.GetState(key)
	if err != nil {
		return shim.Error("getBatchProductInfo operation failed while getting the list of the batch : " + err.Error())
	}
	if value == nil {
		return shim.Error("don't have data")
	}
	listValue := strings.Split(string(value), sp)
	//get the value of the list
	var ret []string 
	for _, ky := range listValue {
		if ky == "" {
			continue
		}
		value, err = stub.GetState(ky)
		if err != nil {
			return shim.Error("Error while getting the data of the key: " + key + "and the err is : " + err.Error())
		}
		ret = append(ret, string(value))
	}
	jsonRet, err := json.Marshal(ret)
	if err != nil {
		return shim.Error("query operation failed. Error marshaling JSON: " + err.Error())
	}
	return shim.Success(jsonRet)
}

func (t *myChaincode) getSensorDataBySensorNum(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 3{
		return shim.Error("getSensorDataBySensorNum operation must have 3 args: sensorNumber, startTime and endTime")
	}
	//get the args
	sensorNum := args[0]
	startTime := args[1]
	endTime := args[2]
	//fix? do some check for the time????

	startKey := sensorNum + sp + startTime
	endKey := sensorNum + sp + endTime

	resultsIterator, err := stub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	var ret []string
	for resultsIterator.HasNext() {
		_, res, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		ret = append(ret, string(res))

	}
	jsonRet, err := json.Marshal(ret)
	if err != nil {
		return shim.Error("query operation failed. Error marshaling JSON: " + err.Error())
	}
	return shim.Success(jsonRet)
}

func (t *myChaincode) getSensorDataByEquipmentNum(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 3{
		return shim.Error("getSensorDataBySensorNum operation must have 3 args: EquipmentNumber, startTime and endTime")
	}
	//get the SensorNum of the EquipmentNumber
	value, err := stub.GetState(args[0])
	if err != nil {
		return shim.Error("getSensorDataByEquipmentNum operation failed while get the SensorNumber : " + err.Error())
	}
	if value == nil {
		return shim.Error("no such sensor")
	}
	args[0] = string(value)
	return t.getSensorDataBySensorNum(stub, args)
}

func (t *myChaincode) getTransferInfoByDocNum(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("getTransferInfoByDocNum operation must have 1 args")
	}
	//get the args
	docNum := args[0]
	value, err := stub.GetState(docNum)
	if err != nil {
		return shim.Error(err.Error())
	}
	if value == nil{
		return shim.Error("no such DocNum")
	}
	//fix? maybe give some top if there is no such docNum
	return shim.Success(value)
}

func (t *myChaincode) getTransferInfoByNoteNum(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("getTransferInfoByNoteNum operation must have 1 args")
	}
	//get the args
	noteNum := args[0]
	//get the docNum of this noteNum
	value, err := stub.GetState(noteNum)
	if err != nil {
		return shim.Error(err.Error())
	}
	if value == nil {
		return shim.Error("no such docNum")
	}
	args[0] = string(value)
	return t.getTransferInfoByDocNum(stub, args)
}

func (t *myChaincode) getTransferInfoByUniqueID(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1{
		return shim.Error("getTransferInfoByUniqueID operation must have 1 args")
	}
	//get the args[0]
	uniqueId := args[0]
	//get the list of the uniquedid
	value, err := stub.GetState(uniqueId)
	listValue := strings.Split(string(value), sp)

	var ret []string
	for _, ky := range listValue {
		value, err = stub.GetState(ky)
		if err != nil {
			return shim.Error("getTransferInfoByUniqueID operation failed while get the -" + ky + "-, and the err is :" + err.Error())
		}
		//check the nil of the value?
		ret = append(ret, string(value))
	}
	jsonRet, err := json.Marshal(ret)
	if err != nil {
		return shim.Error("query operation failed. Error marshaling JSON: " + err.Error())
	}
	return shim.Success(jsonRet)
}



