package com.oxchains;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.oxchains.bean.dto.CargoDTO;
import com.oxchains.bean.model.ziyun.Cargo;
import com.oxchains.common.RespDTO;
import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Test
 *
 * @author liuruichao
 * Created on 2017/4/6 18:37
 */
public class Test {
    public static void main(String[] args) throws Exception {
        String str = "{\n" +
                "\"GoodsType\": \"drug\", \n" +
                "\"DrugElectronicSupervisionCode\": \"0123456789\", \n" +
                "\"DrugInformation\": {\n" +
                "\"DrugName\": \"活血风湿膏\", \n" +
                "\"ApprovalNumber\": \"ZC20150005\", \n" +
                "\"Size\": \"10cmX15cm\", \n" +
                "\"Form\": \"贴膏剂\", \n" +
                "\"Manufacturer\": \"得生制药股份有限公司\", \n" +
                "\"NDCNumber\": \"86978998000169\", \n" +
                "\"NDCNumberRemark\": \"无\", \n" +
                "\"MedicineInstruction\": \"~~\" \n" +
                "},\n" +
                "\"ProduceInformation\": {\n" +
                "\"Address\": \"台南市永康区环工路42号\", \n" +
                "\"ProductionBatch\": \"abc123456\", \n" +
                "\"ProductionTime\": 1490155871000, \n" +
                "\"ValidDate\": 1553212800000 \n" +
                "}\n" +
                "}";
        System.out.println(str.replaceAll(" |\n|\t", ""));
        str = "{" +
                "\"SensorNumber\": \"cgq20170330001\",\n" +
                "\"SensorType\": \"HD-3K1\",\n" +
                "\"EquipmentNumber\": \"sb20170330000\",\n" +
                "\"EquipmentType\": \"冷藏车\",\n" +
                "\"Time\": 1490155871000, \n" +
                "\"Temperature\": [12.2, 12.3], \n" +
                "\"Humidity\": [20.3, 20.4], \n" +
                "\"GPSLongitude\": 113.653056, \n" +
                "\"GPSLatitude\": 34.860076, \n" +
                "\"Address\": \"河南省郑州市惠济区英才街\"\n" +
                "}";
        System.out.println(str.replaceAll(" |\n|\t", ""));
        str = "{\n" +
                "\t\"PermitNumber\": \"310112002939\", \n" +
                "\t\"UnifiedSocialCreditIdentifier\": \"11991000010647510A\",\n" +
                "\t\"Carrier\": \"河南紫云云计算股份有限公司\", \n" +
                "\t\"BusinessTypeCode\": \"1002996\", \n" +
                "\t\"OriginalDocumentNumber\": \"ORD170228111223255\", \n" +
                "\t\"ShippingNoteNumber\": \"RCD170228111701238\", \n" +
                "\t\"ConsignmentDateTime\": 1490155871000, \n" +
                "\t\"DespatchActualDateTime\": 1490155871000, \n" +
                "\t\"GoodsReceiptDateTime\": 1490155871000, \n" +
                "\t\"FreeText\": \"自由文本\", \n" +
                "\t\"ConsignorInfo\": { \n" +
                "\t\t\"CountrySubdivisionCode\": \"110101\", \n" +
                "\t\t\"PersonalIdentityDocument\": \"330102198402124417\",\n" +
                "\t\t\"Consignor\": \"紫云企业货主\" \n" +
                "\t},\n" +
                "\t\"ConsigneeInfo\": {\n" +
                "\t\t\"CountrySubdivisionCode\": \"310101\", \n" +
                "\t\t\"Consignee\": \"鲁\", \n" +
                "\t\t\"GoodsReceiptPlace\": \"to\" \n" +
                "\t},\n" +
                "\t\"PriceInfo\": { \n" +
                "\t\t\"TotalMonetaryAmount\": 12.0, \n" +
                "\t\t\"Remark\": \"人民币\" \n" +
                "\t},\n" +
                "\t\"VehicleInfo\": { \n" +
                "\t\t\"RoadTransportCertificateNumber\": \"330111003790\", \n" +
                "\t\t\"PermitNumber\": \"330301000307\", \n" +
                "\t\t\"VehicleNumber\": \"豫A44444\", \n" +
                "\t\t\"TrailerVehiclePlateNumber\": \"豫AR527挂\", \n" +
                "\t\t\"VehicleClassificationCode\": \"H01\", \n" +
                "\t\t\"LicensePlateTypeCode\": \"01\", \n" +
                "\t\t\"VehicleTonnage\": 20.0, \n" +
                "\t\t\"Owner\": \"河南紫云物流有限公司\", \n" +
                "\t\t\"GoodsInfoList\": [{\n" +
                "\t\t\t\t\"DescriptionOfGoods\": \"水果\", \n" +
                "\t\t\t\t\"CargoTypeClassificationCode\": \"999\", \n" +
                "\t\t\t\t\"GoodsItemGrossWeight\": 151.333, \n" +
                "\t\t\t\t\"Cube\": 11.3333, \n" +
                "\t\t\t\t\"TotalNumberOfPackages\": 12 \n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"DescriptionOfGoods\": \"水果\",\n" +
                "\t\t\t\t\"CargoTypeClassificationCode\": \"999\",\n" +
                "\t\t\t\t\"GoodsItemGrossWeight\": 151.333,\n" +
                "\t\t\t\t\"Cube\": 11.3333,\n" +
                "\t\t\t\t\"TotalNumberOfPackages\": 12\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"DriverList\": [{ \n" +
                "\t\t\t\t\"QualificationCertificateNumber\": \"431224198708273098\", \n" +
                "\t\t\t\t\"NameOfPerson\": \"张三\", \n" +
                "\t\t\t\t\"TelephoneNumber\": \"15167338765\" \n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"QualificationCertificateNumber\": \"431224198708273098\",\n" +
                "\t\t\t\t\"NameOfPerson\": \"张三\",\n" +
                "\t\t\t\t\"TelephoneNumber\": \"15167338765\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t\"LogisticsTraceList\": [{ \n" +
                "\t\t\t\"TraceName\": \"确认到车\", \n" +
                "\t\t\t\"TraceTime\": 1490155871000 \n" +
                "\t\t}, {\n" +
                "\t\t\t\"TraceName\": \"开始发货\",\n" +
                "\t\t\t\"TraceTime\": 1490155871000\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"GoodsTraceList\": [{ \n" +
                "\t\t\t\"UniqueID\": \"4\",\n" +
                "\t\t\t\"CommodityCode\": \"0123456789\", \n" +
                "\t\t\t\"ProductionBatch\": \"20170330141830\" \n" +
                "\t\t}, {\n" +
                "\t\t\t\"UniqueID\": \"#{uniqueID}\",\n" +
                "\t\t\t\"CommodityCode\": \"0123456789\", \n" +
                "\t\t\t\"ProductionBatch\": \"20170330141830\" \n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";

        for (int i = 0; i < 1000; i++) {
            str = str.replaceAll(" |\n|\t", "").replace("#{uniqueID}", i + "");

            URL uri = new URL("http://localhost:8080/waybill");
            //URL uri = new URL("http://42.236.61.79:57280/waybill");
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            // send data
            OutputStream out = conn.getOutputStream();
            out.write(str.getBytes("utf-8"));
            out.flush();
            out.close();

            if (conn.getResponseCode() == 200) {
                System.out.println(IOUtils.toString(conn.getInputStream()));
            } else {
                System.out.println("error");
            }
        }
    }
}