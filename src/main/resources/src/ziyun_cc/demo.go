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
	"time"
	"bytes"
	"encoding/json"
	"fmt"
	"strconv"
	//"strings"
	"sort"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

//sp
var sp = "*&!"

type myChaincode struct {
}

type AuthInfo struct {
	Id       string
	AuthList []string
}

//the struct of the product
type ProduceInformation struct {
	Address         string
	ProductionBatch string
	ProductionTime  int64
	ValidDate       int64
}

type DrugInformation struct {
	DrugName            string
	ApprovalNumber      string
	Size                string
	Form                string
	Manufacturer        string
	NDCNumber           string
	NDCNumberRemark     string
	MedicineInstruction string
}

type Product struct {
	GoodsType                     string
	DrugElectronicSupervisionCode string
	DrugInformation               DrugInformation
	ProduceInformation            ProduceInformation
}

//生产信息 Product
type ProductOther struct {
	Id                 string
	ProductName        string
	EnterpriseId       string
	EnterpriseName     string
	ProductOriginalUrl []string
	ProductBuyUrl      string
	ProductAddress     string
	ProductTime        int64
	ProductType        string
	ProductDeadline    int
	ProductTags        string
	ProductWeight      string
	ProductVolume      string
	ProductCode        string
	Remarks            string
	Size               string
	Pack               string
	ApprovalNumber     string
	Storage            string
	Describe           string
}

//产品信息 ProduceInfo
type ProduceInfo struct {
	Id                      string
	ProductionProcessName   string
	GoodsCount              int
	LastCount               int
	ProductId               string
	EnterpriseId            string
	EnterpriseName          string
	ProductionTime          int64
	InStorageTime           int64
	OutStorageTime          int64
	EnvironmentalMonitoring string
	ProductionParameters    string
	QualitySafety           string
	BatchNumber             string
	CheckDate               int64
	CheckWay                string
	CheckResult             string
	InspectorName           string
}

//the struct of th e sensor
type Sensor struct {
	SensorNumber    string
	SensorType      string
	EquipmentNumber string
	EquipmentType   string
	Time            int64
	Temperature     []float32
	Humidity        []float32
	GPSLongitude    float32
	GPSLatitude     float32
	Address         string
	Token           string
}

//the struct of the transfer
type ConsignorInfo struct {
	CountrySubdivisionCode   string
	PersonalIdentityDocument string
	Consignor                string
}
type ConsigneeInfo struct {
	CountrySubdivisionCode string
	Consignee              string
	GoodsReceiptPlace      string
}

type PriceInfo struct {
	TotalMonetaryAmount float32
	Remark              string
}

type GoodsInfo struct {
	DescriptionOfGoods          string
	CargoTypeClassificationCode string
	GoodsItemGrossWeight        float32
	Cube                        float32
	TotalNumberOfPackages       int
}

type Driver struct {
	QualificationCertificateNumber string
	NameOfPerson                   string
	TelephoneNumber                string
}

type VehicleInfo struct {
	RoadTransportCertificateNumber string
	PermitNumber                   string
	VehicleNumber                  string
	TrailerVehiclePlateNumber      string
	VehicleClassificationCode      string
	LicensePlateTypeCode           string
	VehicleTonnage                 float32
	Owner                          string
	GoodsInfoList                  []GoodsInfo
	DriverList                     []Driver
}

type LogisticsTrace struct {
	TraceName string
	TraceTime int64
}

type GoodsTrace struct {
	UniqueID        string
	CommodityCode   string
	ProductionBatch string
}

type Transfer struct {
	PermitNumber                  string
	UnifiedSocialCreditIdentifier string
	Carrier                       string
	BusinessTypeCode              string
	OriginalDocumentNumber        string
	ShippingNoteNumber            string
	ConsignmentDateTime           int64
	DespatchActualDateTime        int64
	GoodsReceiptDateTime          int64
	FreeText                      string
	ConsignorInfo                 ConsignorInfo
	ConsigneeInfo                 ConsigneeInfo
	PriceInfo                     PriceInfo
	VehicleInfo                   VehicleInfo
	LogisticsTraceList            []LogisticsTrace
	GoodsTraceList                []GoodsTrace
}

type PurchaseInfo struct {
	PurchaseTitle     string
	Count             int64
	EnterpriseId      string
	EnterpriseName    string
	StockDate         int64
	SupperName        string
	SupperAddress     string
	SupplyPhone       string
	SupplyName        string
	Id                string
	Type              string
	UniqueCodes       []string
	CreateTime        int64
	PreEnterpriseId   string
	PreEnterpriseName string
	Token             string
}

type FoodInformation struct {
	FoodName     string
	Manufacturer string
}

type Goods struct {
	Id                 string
	Type               string
	GoodsType          string
	GoodsName          string
	EnterpriseId       string
	EnterpriseName     string
	GoodsSize          string
	ApprovalNumber     string
	ProductAddress     string
	ProductDeadline    int64
	ProductTags        string
	Pack               string
	Storage            string
	Describe           string
	ProductionTime     int64
	ParentCode         string
	ProduceInfoId      string
	ProductId          string
	ProductCode        string
	UniqueCode         string
	CommodityCode      string
	ProductionBatch    string
	SalesId            string
	DrugInformation    []DrugInformation
	FoodInformation    []FoodInformation
	ProduceInformation []ProduceInformation
	Token              string
}

type SalesInfo struct {
	Id                          string
	No                          string
	SalesTitle                  string
	PurchaseId                  string
	ProductAddress              string
	ProductionName              string
	ProductionSpecification     string
	CreateSalesEnterpriseId     string
	TranstitSalesEnterpriseId   string
	SalesCount                  int64
	ProductTime                 int64
	ProductBatch                string
	ProductDeadline             int64
	GoodsOriginalUrl            string
	SalesDate                   int64
	BuyerName                   string
	BuyerAddress                string
	BuyerTel                    string
	ResponsibilityName          string
	InspectionCertificateNumber string
	ProductionProcessId         string
	GoodsId                     string
	SalsesId                    string
	UniqueCodes                 []string
	EnterpriseId                string
	EnterpriseName              string
	SalesInvoiceUrl             string
	BuyEnterpriseId             string
	BuyEnterpriseName           string
	HandoverStatus              int32
	Token                       string
}

type Trace struct {
	TraceName string
	TraceTime int64
}

type TransportBill struct {
	Id                            string
	PermitNumber                  string
	UnifiedSocialCreditIdentifier string
	Carrier                       string
	BusinessTypeCode              string
	OriginalDocumentNumber        string
	ShippingNoteNumber            string
	ConsignmentDateTime           int64
	DespatchActualDateTime        int64
	GoodsReceiptDateTime          int64
	FreeText                      string
	Token                         string
	ConsignorInfo                 ConsignorInfo
	ConsigneeInfo                 ConsigneeInfo
	PriceInfo                     PriceInfo
	VehicleInfo                   VehicleInfo
	LogisticsTraceList            []Trace
	GoodsTraceList                []GoodsTrace
	TransportState                int32
	UniqueCodes                   []string
	InvoiceDownLoadUrl            string
	ReportDownLoadUrl             string
}

type StorageBill struct {
	Id                string
	StorageTitle      string
	WarehouseName     string
	GiverName         string
	GiverPhone        string
	RecipientName     string
	RecipientPhone    string
	StartTime         int64
	EndTime           int64
	StorageAddress    string
	HandoverInfo      string
	Type              string
	UniqueCodes       []string
	CreateTime        int64
	EnterpriseId      string
	EnterpriseName    string
	PreEnterpriseId   string
	PreEnterpriseName string
	Token             string
}

//批发零售信息
type TransitSalesInfo struct {
	Id                     string
	TransitSalesName       string
	EnterpriseId           string
	EnterpriseName         string
	Type                   string
	TransitSalesType       string
	SalesDate              int64
	CreateTime             int64
	BuyerName              string
	BuyerAddress           string
	BuyerTel               string
	UniqueCodes            []string
	TransitSalesState      int32
	TransitSalesMoney      float64
	TransitSalesInvoiceUrl string
	PreEnterpriseId        string
	PreEnterpriseName      string
	Token                  string
}

type ProductProvincialPnspectionReport struct {
	ProductProvincialPnspectionReportKey   string
	ProductProvincialPnspectionReportValue string
}
type ProductPriceDocument struct {
	ProductPriceDocumentKey   string
	ProductPriceDocumentValue string
}
type ProductFactoryInspectionReport struct {
	ProductFactoryInspectionReportKey   string
	ProductFactoryInspectionReportValue string
}
type PurchaserCertificate struct {
	PurchaserCertificateKey   string
	PurchaserCertificateValue string
}

//产品首营资料
type ProductGmp struct {
	Id                                string
	EnterpriseId                      string
	EnterpriseName                    string
	ApprovalUrl                       string
	ApprovalNo                        string
	ProductCode                       string
	ProductPatentCertificateUrl       string
	ProductTrademarkDocumentsUrl      string
	ProductName                       string
	ProductMiniPackageUrl             string
	DrugInstructionsUrl               string
	GeneralTaxpayerRecordsUrl         string
	LegalPowerOfAttorneyUrl           string
	IdCardUrl                         string
	ProudctProduceStandardUrl         string
	PurchaseAndSaleContractUrl        string
	ProductPackageAndManualUrl        string
	ProductProvincialPnspectionReport []map[string]string
	ProductPriceDocument              []map[string]string
	ProductFactoryInspectionReport    []map[string]string
	PurchaserCertificate              []map[string]string
	Token                             string
}
type YearTaxReport struct {
	YearTaxReportKey   string
	YearTaxReportValue string
}
type EnterpriseQualityQuestionnaire struct {
	EnterpriseQualityQuestionnaireKey   string
	EnterpriseQualityQuestionnaireValue string
}
type DeliveryUnitQualityQuestionnaire struct {
	DeliveryUnitQualityQuestionnaireKey   string
	DeliveryUnitQualityQuestionnaireValue string
}

//企业首营资料
type EnterpriseGmp struct {
	Id                               string
	EnterpriseId                     string
	EnterpriseName                   string
	EnterpriseType                   string
	EnterpriseLicenseUrl             string
	EnterpriseLicenseNo              string
	TaxRegistrationCertificateUrl    string
	TaxRegistrationCode              string
	OrganizationCodeCertificateUrl   string
	OrganizationCode                 string
	QualityAssuranceUrl              string
	DrugProductionLicenseUrl         string
	DrugProductionLicensNo           string
	GoodManufacturPracticesUrl       string
	DrugOperatingLicenseUrl          string
	DrugOperatingLicenseNo           string
	GoodSupplyingPracticesUrl        string
	OpeningPermitNo                  string
	OpeningPermitUrl                 string
	OpenBank                         string
	BankAccountNumber                string
	BillingUnit                      string
	TaxpayerIdentificationNumber     string
	EnterprisePhone                  string
	EnterpriseAdress                 string
	YearTaxReport                    []map[string]string
	EnterpriseQualityQuestionnaire   []map[string]string
	DeliveryUnitQualityQuestionnaire []map[string]string
	Token                            string
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

	case "saveEnterpriseGmp":
		return t.saveEnterpriseGmp(stub, args)

	case "saveProductGmp":
		return t.saveProductGmp(stub, args)

	case "getProductGmpByProducName":
		return t.getProductGmpByProducName(stub, args)

	case "saveTransitSalesInfo":
		return t.saveTransitSalesInfo(stub, args)

	case "saveStorageBill":
		return t.saveStorageBill(stub, args)

	case "saveTransportBill":
		return t.saveTransportBill(stub, args)

	case "saveSalesInfo":
		return t.saveSalesInfo(stub, args)

	case "savePurchaseInfo":
		return t.savePurchaseInfo(stub, args)

	case "saveGoods":
		return t.saveGoods(stub, args)

	case "saveProductInfo":
		return t.saveProductInfo(stub, args)

	case "saveSensorData":
		return t.saveSensorData(stub, args)

	case "saveTransferInfo":
		return t.saveTransferInfo(stub, args)

	case "saveProduct":
		return t.saveProduct(stub, args)

	case "saveProduceInfo":
		return t.saveProduceInfo(stub, args)

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

	case "getTransferInfoByBatch":
		return t.getTransferInfoByBatch(stub, args)

	case "searchByQuery":
		return t.searchByQuery(stub, args)

	case "searchByView":
		return t.searchByView(stub, args)

	case "add":
		return t.add(stub, args)
	case "auth":
		return t.auth(stub, args)
	case "revoke":
		return t.revoke(stub, args)
	case "query":
		return t.query(stub, args)

	default:
		return shim.Error("Unsupported operation")
	}
}

func (t *myChaincode) saveEnterpriseGmp(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("===saveEnterpriseGmp===")
	if len(args) < 1 {
		return shim.Error("saveEnterpriseGmp operation must have 1 arg")
	}
	// get the args
	bEnterpriseGmp := []byte(args[0])
	//get some info
	enterpriseGmp := &EnterpriseGmp{}
	err := json.Unmarshal(bEnterpriseGmp, &enterpriseGmp)
	if err != nil {
		fmt.Println(err)
		return shim.Error("Unmarshal failed")
	}
	//save the json info
	err = stub.PutState(enterpriseGmp.EnterpriseName, bEnterpriseGmp)
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	return shim.Success(nil)
}

func (t *myChaincode) saveProductGmp(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("===saveProductGmp===")
	if len(args) < 1 {
		return shim.Error("saveProductGmp operation must have 1 arg")
	}
	// get the args
	bProductGmp := []byte(args[0])
	//get some info
	productGmp := &ProductGmp{}
	err := json.Unmarshal(bProductGmp, &productGmp)
	if err != nil {
		fmt.Println(err)
		return shim.Error("Unmarshal failed")
	}

	//save the json info
	err = stub.PutState(productGmp.ProductName, bProductGmp)
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	return shim.Success(nil)
}

func (t *myChaincode) getProductGmpByProducName(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("getProductGmpByProducName operation must have 1 args")
	}
	//get the args
	ProducName := args[0]
	fmt.Println("===getProductGmpByProducName===" + ProducName)
	value, err := stub.GetState(ProducName)
	if err != nil {
		return shim.Error(err.Error())
	}
	if value == nil {
		return shim.Error("no such ProducName")
	}
	return shim.Success(value)
}

func (t *myChaincode) searchByView(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("searchByView operation must have 1 ars")
	}
	opt := args[0]
	fmt.Println("=== begin === ",time.Now().Unix())
	res, err := stub.QueryByView(opt)
	fmt.Println("===  end  === ",time.Now().Unix())
	if err != nil {
		return shim.Error(err.Error())
	}

	// buffer is a JSON array containing QueryRecords
	var buffer bytes.Buffer
	buffer.WriteString("{\"list\":[")

	bArrayMemberAlreadyWritten := false
	for res.HasNext() {
		queryResponse, err := res.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]}")
	fmt.Printf("searchByView queryResult:\n%s\n", buffer.String())
	return shim.Success(buffer.Bytes())

}

func (t *myChaincode) searchByQuery(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("searchByQuery operation must have 1 ars")
	}
	queryString := args[0]

	queryResults, err := getQueryResultsForQueryString(stub, queryString)
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)

}

func RemoveDuplicatesAndEmpty(a []string) (ret []string) {
	a_len := len(a)
	for i := 0; i < a_len; i++ {
		if (i > 0 && a[i-1] == a[i]) || len(a[i]) == 0 {
			continue
		}
		ret = append(ret, a[i])
	}
	return
}
func RemoveOne(a []string, b string) (ret []string, flag bool) {
	a_len := len(a)
	flag = false
	for i := 0; i < a_len; i++ {
		if a[i] == b {
			flag = true
			continue
		}
		ret = append(ret, a[i])
	}
	return
}
func (t *myChaincode) add(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("the new operation must have 1 arg: new user id")
	}
	fmt.Println("===add user===")
	id := args[0]
	authInfo := &AuthInfo{id, []string{id}}
	fmt.Println(authInfo)
	bAuthInfo, err := json.Marshal(authInfo)
	if err != nil {
		return shim.Error("err while Marshal authinfo")
	}
	fmt.Println(string(bAuthInfo))
	err = stub.PutState(id, bAuthInfo)
	if err != nil {
		return shim.Error("err while putting state")
	}
	return shim.Success(nil)
}
func (t *myChaincode) auth(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 2 {
		return shim.Error("the auth operation must have 2 args: id A and id B")
	}
	owner := args[0]
	com := args[1]
	value, err := stub.GetState(owner)
	if err != nil {
		return shim.Error("err while getting the state")
	}
	if value == nil {
		return shim.Error("don't have this user")
	}
	authInfo := &AuthInfo{}
	err = json.Unmarshal(value, &authInfo)
	if err != nil {
		return shim.Error("err while Unmarshal the value")
	}
	//add the new com
	authInfo.AuthList = append(authInfo.AuthList, com)
	sort.Strings(authInfo.AuthList)
	authInfo.AuthList = RemoveDuplicatesAndEmpty(authInfo.AuthList)
	bAuthInfo, err := json.Marshal(authInfo)
	if err != nil {
		return shim.Error("err while Marshal authinfo")
	}
	err = stub.PutState(owner, bAuthInfo)
	if err != nil {
		return shim.Error("err while putting state")
	}
	return shim.Success(nil)
}
func (t *myChaincode) revoke(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 2 {
		return shim.Error("the revoke operation must have 2 args: id A and id B")
	}
	owner := args[0]
	com := args[1]
	value, err := stub.GetState(owner)
	if err != nil {
		return shim.Error("err while getting the state")
	}
	if value == nil {
		return shim.Error("don't have this user")
	}
	authInfo := &AuthInfo{}
	err = json.Unmarshal(value, &authInfo)
	if err != nil {
		return shim.Error("err while Unmarshal the value")
	}
	//remove the new com
	tmp, ok := RemoveOne(authInfo.AuthList, com)
	authInfo.AuthList = tmp
	if !ok {
		return shim.Error("don't have the user in  authlist")
	}
	bAuthInfo, err := json.Marshal(authInfo)
	if err != nil {
		return shim.Error("err while Marshal authinfo")
	}
	err = stub.PutState(owner, bAuthInfo)
	if err != nil {
		return shim.Error("err while putting state")
	}
	return shim.Success(nil)
}
func (t *myChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("the query operation must have 1 args: userid")
	}
	id := args[0]
	value, err := stub.GetState(id)
	if err != nil {
		return shim.Error("err while getting the state")
	}
	if value == nil {
		return shim.Error("don't have this user")
	}
	return shim.Success(value)
}

// =========================================================================================
// getQueryResultsForQueryString executes the passed in query string.
// Result set is built and returned as a byte array containing the JSON results.
// This function return the list of the data with josn format.
// =========================================================================================
func getQueryResultsForQueryString(stub shim.ChaincodeStubInterface, queryString string) ([]byte, error) {

	fmt.Printf("getQueryResultsForQueryString queryString:\n%s\n", queryString)
	fmt.Println("=== begin === ",time.Now().Unix())
	resultsIterator, err := stub.GetQueryResult(queryString)
	fmt.Println("===  end  === ",time.Now().Unix())
	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryRecords
	var buffer bytes.Buffer
	buffer.WriteString("{\"list\":[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]}")
	fmt.Printf("getQueryResultsForQueryString queryResult:\n%s\n", buffer.String())

	return buffer.Bytes(), nil
}

// =========================================================================================
// getQueryResultForQueryString executes the passed in query string.
// Result set is built and returned as a byte array containing the JSON results.
// This function only return ONE value with json format.
// =========================================================================================
func getQueryResultForQueryString(stub shim.ChaincodeStubInterface, queryString string) ([]byte, error) {

	fmt.Printf("getQueryResultForQueryString queryString:\n%s\n", queryString)

	resultsIterator, err := stub.GetQueryResult(queryString)
	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryRecords
	var ret []byte

	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}
		ret = queryResponse.Value
		break
	}

	fmt.Printf("getQueryResultForQueryString queryResult:\n%s\n", string(ret))

	return ret, nil
}

func (t *myChaincode) saveTransitSalesInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("===saveTransitSalesInfo===")
	if len(args) < 1 {
		return shim.Error("saveTransitSalesInfo operation must have 1 arg")
	}
	// get the args
	bTransitSalesInfo := []byte(args[0])
	//get some info
	transitSalesInfo := &TransitSalesInfo{}
	err := json.Unmarshal(bTransitSalesInfo, &transitSalesInfo)
	if err != nil {
		fmt.Println(err)
		return shim.Error("Unmarshal failed")
	}

	//save the json info
	//FIXME check key exists
	err = stub.PutState(transitSalesInfo.Id, bTransitSalesInfo)
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	return shim.Success(nil)
}

func (t *myChaincode) saveStorageBill(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("===saveStorageBill===")
	if len(args) < 1 {
		return shim.Error("saveStorageBill operation must have 1 arg")
	}
	// get the args
	bStorageBill := []byte(args[0])
	//get some info
	storageBill := &StorageBill{}
	err := json.Unmarshal(bStorageBill, &storageBill)
	if err != nil {
		fmt.Println(err)
		return shim.Error("Unmarshal failed")
	}

	//save the json info
	//FIXME check key exists
	err = stub.PutState(storageBill.Id, bStorageBill)
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	return shim.Success(nil)
}

func (t *myChaincode) saveTransportBill(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("===saveTransportBill===")
	if len(args) < 1 {
		return shim.Error("saveTransportBill operation must have 1 arg")
	}
	// get the args
	bTransportBill := []byte(args[0])
	//get some info
	transportBill := &TransportBill{}
	err := json.Unmarshal(bTransportBill, &transportBill)
	if err != nil {
		fmt.Println(err)
		return shim.Error("Unmarshal failed")
	}

	//save the json info
	//FIXME check key exists
	err = stub.PutState(transportBill.Id, bTransportBill)
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	return shim.Success(nil)
}

func (t *myChaincode) saveSalesInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("===saveSalesInfo===")
	if len(args) < 1 {
		return shim.Error("saveGoods operation must have 1 arg")
	}
	// get the args
	bSalesInfo := []byte(args[0])
	//get some info
	salesInfo := &SalesInfo{}
	err := json.Unmarshal(bSalesInfo, &salesInfo)
	if err != nil {
		fmt.Println(err)
		return shim.Error("Unmarshal failed")
	}

	//save the json info
	//FIXME check key exists
	err = stub.PutState(salesInfo.Id, bSalesInfo)
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	return shim.Success(nil)
}

func (t *myChaincode) saveGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("===saveGoods===")
	if len(args) < 1 {
		return shim.Error("saveGoods operation must have 1 arg")
	}
	// get the args
	bGoods := []byte(args[0])
	//get some info
	goods := &Goods{}
	err := json.Unmarshal(bGoods, &goods)
	if err != nil {
		fmt.Println(err)
		return shim.Error("Unmarshal failed")
	}

	//save the json info
	//FIXME check key exists
	err = stub.PutState(goods.UniqueCode, bGoods)
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	return shim.Success(nil)
}

func (t *myChaincode) savePurchaseInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("===savePurchaseInfo===")
	if len(args) < 1 {
		return shim.Error("addPurchaseInfo operation must have 1 arg")
	}
	// get the args
	bPurchaseInfo := []byte(args[0])
	//get some info
	purchaseInfo := &PurchaseInfo{}
	err := json.Unmarshal(bPurchaseInfo, &purchaseInfo)
	if err != nil {
		fmt.Println(err)
		return shim.Error("Unmarshal failed")
	}

	//save the json info
	//FIXME check key exists
	err = stub.PutState(purchaseInfo.Id, bPurchaseInfo)
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	return shim.Success(nil)
}

func (t *myChaincode) saveProductInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
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
	if product.DrugElectronicSupervisionCode == "" {
		return shim.Error("bad format of the DrugElectronicSupervisionCode")
	}
	if product.DrugInformation.NDCNumber == "" {
		return shim.Error("bad format of the NDCNumber")
	}
	if product.ProduceInformation.ProductionBatch == "" {
		return shim.Error("bad format ot the ProductionBatch")
	}

	//save the json info
	//bProduct = json.Marshal(product)
	err = stub.PutState("product"+sp+product.DrugElectronicSupervisionCode, bProduct)
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	return shim.Success(nil)
}

//保存product 产品信息的数据
func (t *myChaincode) saveProduct(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("saveProduct operation must have 1 arg")
	}
	// get the args
	bApprovalNumber := []byte(args[0])
	//get some info
	product := &ProductOther{}
	err := json.Unmarshal(bApprovalNumber, &product)
	if err != nil {
		return shim.Error(err.Error())
	}
	//save the json info
	//bProduct = json.Marshal(product)
	err = stub.PutState(product.ProductCode, bApprovalNumber)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(nil)
}

//保存ProduceInfo 生成信息的数据
func (t *myChaincode) saveProduceInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("saveProductInfo operation must have 1 arg")
	}
	// get the args
	bProduceInfo := []byte(args[0])
	//get some info
	produceInfo := &ProduceInfo{}
	err := json.Unmarshal(bProduceInfo, &produceInfo)
	if err != nil {
		return shim.Error(err.Error())
	}

	if produceInfo.Id == "" {
		return shim.Error("produceInfo.Id is null")
	}

	//save the json info
	//bProduceInfo = json.Marshal(produceInfo)
	err = stub.PutState("produceInfo"+sp+produceInfo.Id, bProduceInfo)
	if err != nil {
		return shim.Error(err.Error())
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
	if eqmtNum == "" {
		return shim.Error("bad format of the eqmtNum")
	}
	//save the json data
	key := "sensor" + sp + sensor.SensorNumber + sp + sTm
	err = stub.PutState(key, bSensor)
	if err != nil {
		return shim.Error("saveSensorData operation failed. Error while putting the SensorData : " + err.Error())
	}
	//save the other info
	err = stub.PutState("eq"+sp+eqmtNum, []byte(sensor.SensorNumber))
	if err != nil {
		return shim.Error("saveSensorData operation failed. Error while putting the other info : " + err.Error())
	}
	//fmt.Println("the equNUm is " + eqmtNum + "and the sensorNum is " + sensor.SensorNumber)

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
	err = stub.PutState("transfer"+sp+docNum, bTransfer)
	if err != nil {
		return shim.Error(err.Error())
	}
	//save the other info
	err = stub.PutState("tran"+sp+noteNum, []byte(docNum))
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(nil)
}

func (t *myChaincode) getProductInfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("getProductInfo operation must have 1 arg")
	}
	key := "product" + sp + string(args[0])

	value, err := stub.GetState(key)
	if err != nil {
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
	queryString := fmt.Sprintf("{\"selector\":{\"DrugInformation.NDCNumber\" : \"%s\", \"ProduceInformation.ProductionBatch\" : \"%s\"}}", NDCNumber, ProductionBatch)

	queryResults, err := getQueryResultsForQueryString(stub, queryString)
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (t *myChaincode) getSensorDataBySensorNum(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 3 {
		return shim.Error("getSensorDataBySensorNum operation must have 3 args: sensorNumber, startTime and endTime")
	}
	//get the args
	sensorNum := args[0]
	startTime := args[1]
	endTime := args[2]
	//fix? do some check for the time????

	startKey := "sensor" + sp + sensorNum + sp + startTime
	endKey := "sensor" + sp + sensorNum + sp + endTime

	resultsIterator, err := stub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	var ret string
	ret = "{\"list\":["
	flag := true
	for resultsIterator.HasNext() {
		Kvalue, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		if flag {
			ret = ret + string(Kvalue.Value)
			flag = false
		} else {
			ret = ret + "," + string(Kvalue.Value)
		}
		//ret = append(ret, string(res))
	}
	ret = ret + "]}"
	return shim.Success([]byte(ret))
}

func (t *myChaincode) getSensorDataByEquipmentNum(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 3 {
		return shim.Error("getSensorDataByEquipmentNum operation must have 3 args: EquipmentNumber, startTime and endTime")
	}
	fmt.Println("the equNum is :" + args[0])
	//get the SensorNum of the EquipmentNumber
	value, err := stub.GetState("eq" + sp + args[0])
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
	value, err := stub.GetState("transfer" + sp + docNum)
	if err != nil {
		return shim.Error(err.Error())
	}
	if value == nil {
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
	value, err := stub.GetState("tran" + sp + noteNum)
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
	if len(args) < 1 {
		return shim.Error("getTransferInfoByUniqueID operation must have 1 args")
	}
	//get the args[0]
	uniqueId := args[0]

	queryString := fmt.Sprintf("{\"selector\":{\"GoodsTraceList\" : {\"$elemMatch\": {\"UniqueID\" : \"%s\"}}}}", uniqueId)

	queryResults, err := getQueryResultsForQueryString(stub, queryString)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(queryResults)
}

func (t *myChaincode) getTransferInfoByBatch(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("getTransferInfoByBatch operation must have 2 args: commodityCode and productionBatch")
	}
	//get the args[0]
	commodityCode := args[0]
	productionBatch := args[1]
	//get the list of the uniquedid

	queryString := fmt.Sprintf("{\"selector\":{\"GoodsTraceList\" : {\"$elemMatch\": {\"CommodityCode\" : \"%s\", \"ProductionBatch\" : \"%s\"}}}}", commodityCode, productionBatch)

	queryResults, err := getQueryResultsForQueryString(stub, queryString)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(queryResults)
}
