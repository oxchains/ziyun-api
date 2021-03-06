package com.oxchains.service;

import com.oxchains.bean.dto.EnterpriseGmpDTO;
import com.oxchains.bean.model.ziyun.*;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.time.LocalDateTime.now;

/**
 * Created by root on 17-8-8.
 */
@Service
@Slf4j
public class EnterpriseGmpService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;


    @Value("${file.upload.dir}")
    private String upload;

    public RespDTO<String> addEnterpriseGmp(EnterpriseGmp enterpriseGmp) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException {
        String token = enterpriseGmp.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        enterpriseGmp.setToken(jwt.getId());
        translateFile(enterpriseGmp);//translate url to localfile
        String txID = chaincodeService.invoke("saveEnterpriseGmp", new String[] { gson.toJson(enterpriseGmp) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<EnterpriseGmp>> getEnterpriseGmpByEnterpriseNameAndType(String EnterpriseName, String EnterpriseType, String Token){
        String jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\n" +
                "    \"EnterpriseName\": \""+EnterpriseName+"\"\n" + " ,   \"EnterpriseType\": \""+EnterpriseType+ "\"}}"});
        log.debug("===getEnterpriseGmpByEnterpriseNameAndType===" + jsonStr);
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        String txId = jsonStr.split("!#!")[1];
        jsonStr =  jsonStr.split("!#!")[0];
        EnterpriseGmpDTO enterpriseGmpDTO = simpleGson.fromJson(jsonStr, EnterpriseGmpDTO.class);

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<EnterpriseGmp> it = enterpriseGmpDTO.getList().iterator(); it.hasNext();) {
            EnterpriseGmp EnterpriseGmp = it.next();
            EnterpriseGmp.setTxId(txId);
            log.debug("===EnterpriseGmp.getToken()==="+EnterpriseGmp.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { EnterpriseGmp.getToken() });
            log.info("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.info("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(enterpriseGmpDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }
        return RespDTO.success(enterpriseGmpDTO.getList());
    }

    private void translateFile(EnterpriseGmp enterpriseGmp){

        String EnterpriseLicenseUrl = enterpriseGmp.getEnterpriseLicenseUrl();
        if(!StringUtils.isEmpty(EnterpriseLicenseUrl) && EnterpriseLicenseUrl.startsWith("http")){
            enterpriseGmp.setEnterpriseLicenseUrl(storeFile(EnterpriseLicenseUrl));
        }

        String TaxRegistrationCertificateUrl = enterpriseGmp.getTaxRegistrationCertificateUrl();
        if(!StringUtils.isEmpty(TaxRegistrationCertificateUrl) && TaxRegistrationCertificateUrl.startsWith("http")){
            enterpriseGmp.setTaxRegistrationCertificateUrl(storeFile(TaxRegistrationCertificateUrl));
        }

        String OrganizationCodeCertificateUrl = enterpriseGmp.getOrganizationCodeCertificateUrl();
        if(!StringUtils.isEmpty(OrganizationCodeCertificateUrl) && OrganizationCodeCertificateUrl.startsWith("http")){
            enterpriseGmp.setOrganizationCodeCertificateUrl(storeFile(OrganizationCodeCertificateUrl));
        }

        String QualityAssuranceUrl = enterpriseGmp.getQualityAssuranceUrl();
        if(!StringUtils.isEmpty(QualityAssuranceUrl) && QualityAssuranceUrl.startsWith("http")){
            enterpriseGmp.setQualityAssuranceUrl(storeFile(QualityAssuranceUrl));
        }

        String DrugProductionLicenseUrl = enterpriseGmp.getDrugProductionLicenseUrl();
        if(!StringUtils.isEmpty(DrugProductionLicenseUrl) && DrugProductionLicenseUrl.startsWith("http")){
            enterpriseGmp.setDrugProductionLicenseUrl(storeFile(DrugProductionLicenseUrl));
        }

        String GoodManufacturPracticesUrl = enterpriseGmp.getGoodManufacturPracticesUrl();
        if(!StringUtils.isEmpty(GoodManufacturPracticesUrl) && GoodManufacturPracticesUrl.startsWith("http")){
            enterpriseGmp.setGoodManufacturPracticesUrl(storeFile(GoodManufacturPracticesUrl));
        }

        String DrugOperatingLicenseUrl = enterpriseGmp.getDrugOperatingLicenseUrl();
        if(!StringUtils.isEmpty(DrugOperatingLicenseUrl) && DrugOperatingLicenseUrl.startsWith("http")){
            enterpriseGmp.setDrugOperatingLicenseUrl(storeFile(DrugOperatingLicenseUrl));
        }

        String GoodSupplyingPracticesUrl = enterpriseGmp.getGoodSupplyingPracticesUrl();
        if(!StringUtils.isEmpty(GoodSupplyingPracticesUrl) && GoodSupplyingPracticesUrl.startsWith("http")){
            enterpriseGmp.setGoodSupplyingPracticesUrl(storeFile(GoodSupplyingPracticesUrl));
        }

        String OpeningPermitUrl = enterpriseGmp.getOpeningPermitUrl();
        if(!StringUtils.isEmpty(OpeningPermitUrl) && OpeningPermitUrl.startsWith("http")){
            enterpriseGmp.setOpeningPermitUrl(storeFile(OpeningPermitUrl));
        }

        List<Map<String,String>> yearTaxReportList = enterpriseGmp.getYearTaxReport();
        if(yearTaxReportList!=null && yearTaxReportList.size()>0)
        for(Map<String,String> yearTaxReport : yearTaxReportList){
            Iterator it = yearTaxReport.keySet().iterator();
            while(it.hasNext()){
                String key = it.next().toString();
                String value = yearTaxReport.get(key);
                if(!StringUtils.isEmpty(value) && value.startsWith("http")){
                    yearTaxReport.put(key,storeFile(value));
                }
            }
        }

        List<Map<String,String>> enterpriseQualityQuestionnaireList = enterpriseGmp.getEnterpriseQualityQuestionnaire();
        if(enterpriseQualityQuestionnaireList!=null && enterpriseQualityQuestionnaireList.size()>0)
        for(Map<String,String> enterpriseQualityQuestionnaire : enterpriseQualityQuestionnaireList){
            Iterator it = enterpriseQualityQuestionnaire.keySet().iterator();
            while(it.hasNext()){
                String key = it.next().toString();
                String value = enterpriseQualityQuestionnaire.get(key);
                if(!StringUtils.isEmpty(value) && value.startsWith("http")){
                    enterpriseQualityQuestionnaire.put(key,storeFile(value));
                }
            }
        }

        List<Map<String,String>> deliveryUnitQualityQuestionnaireList = enterpriseGmp.getDeliveryUnitQualityQuestionnaire();
        if(deliveryUnitQualityQuestionnaireList!=null && deliveryUnitQualityQuestionnaireList.size()>0)
        for(Map<String,String> deliveryUnitQualityQuestionnaire : deliveryUnitQualityQuestionnaireList){
            Iterator it = deliveryUnitQualityQuestionnaire.keySet().iterator();
            while(it.hasNext()){
                String key = it.next().toString();
                String value = deliveryUnitQualityQuestionnaire.get(key);
                if(!StringUtils.isEmpty(value) && value.startsWith("http")){
                    deliveryUnitQualityQuestionnaire.put(key,storeFile(value));
                }
            }
        }
    }

    private String storeFile(String fileUrl){
        String fileName = "";
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(3000);
            urlCon.setReadTimeout(3000);
            int code = urlCon.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                log.error("===getfile error===" + fileUrl);
            }
            String head = urlCon.getHeaderField("Content-Disposition");
            String filename = head.split("filename=")[1].replace("\"","");
            log.debug("===head==="+head);
            String fileType = filename.substring(filename.lastIndexOf("."));

            fileName = now().toLocalDate() +"-" + UUID.randomUUID().toString() + fileType;
            String cacheFilename = String.format("%s/%s", upload, fileName);
            //读文件流
            in = new DataInputStream(urlCon.getInputStream());
            out = new DataOutputStream(new FileOutputStream(cacheFilename));
            byte[] buffer = new byte[2048];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
        } catch (Exception e) {
            log.error("storeFile error: ",e);
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("storeFile error: ",e);
                }
            }
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("storeFile error: ",e);
                }
            }
        }
        return fileName;
    }
}
