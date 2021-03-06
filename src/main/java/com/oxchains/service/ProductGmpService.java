package com.oxchains.service;

import com.oxchains.bean.model.ziyun.*;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * Created by root on 17-8-8.
 */
@Service
@Slf4j
public class ProductGmpService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    @Value("${file.upload.dir}")
    private String upload;

    public RespDTO<String> addProductGmp(ProductGmp productGmp) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException {
        String token = productGmp.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        productGmp.setToken(jwt.getId());// store username ,not token
        translateFile(productGmp);//translate url to localfile
        String txID = chaincodeService.invoke("saveProductGmp", new String[] { gson.toJson(productGmp) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<ProductGmp>> getProductGmpByProducName(String producName,String token){
        String result = chaincodeService.getPayloadAndTxid("getProductGmpByProducName", new String[]{producName});
        log.debug("===getProductGmpByProducName===" + result);
        if (StringUtils.isEmpty(result)) {
            return RespDTO.fail("没有数据");
        }
        String jsonStr = result.split("!#!")[0];
        String txId = result.split("!#!")[1];
        ProductGmp productGmp = simpleGson.fromJson(jsonStr, ProductGmp.class);
        productGmp.setTxId(txId);
        JwtToken jwt = TokenUtils.parseToken(token);
        String username = jwt.getId();
        log.debug("===ProductGmp.getToken()==="+productGmp.getToken());
        String jsonAuth = chaincodeService.query("query", new String[] { productGmp.getToken() });
        log.info("===jsonAuth==="+jsonAuth);
        Auth auth = gson.fromJson(jsonAuth, Auth.class);
        ArrayList<String> authList = auth.getAuthList();
        log.info("===username==="+username);
        if(!authList.contains(username)){
            log.debug("===remove===");
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }

        return RespDTO.success(Arrays.asList(productGmp));
    }

    private void translateFile(ProductGmp productGmp){

        String approvalUrl = productGmp.getApprovalUrl();
        if(!StringUtils.isEmpty(approvalUrl) && approvalUrl.startsWith("http")){
            productGmp.setApprovalUrl(storeFile(approvalUrl));
        }

        String productPatentCertificateUrl = productGmp.getProductPatentCertificateUrl();
        if(!StringUtils.isEmpty(productPatentCertificateUrl) && productPatentCertificateUrl.startsWith("http")){
            productGmp.setProductPatentCertificateUrl(storeFile(productPatentCertificateUrl));
        }

        String productTrademarkDocumentsUrl = productGmp.getProductTrademarkDocumentsUrl();
        if(!StringUtils.isEmpty(productTrademarkDocumentsUrl) && productTrademarkDocumentsUrl.startsWith("http")){
            productGmp.setProductTrademarkDocumentsUrl(storeFile(productTrademarkDocumentsUrl));
        }

        String productMiniPackageUrl = productGmp.getProductMiniPackageUrl();
        if(!StringUtils.isEmpty(productMiniPackageUrl) && productMiniPackageUrl.startsWith("http")){
            productGmp.setProductMiniPackageUrl(storeFile(productMiniPackageUrl));
        }

        String drugInstructionsUrl = productGmp.getDrugInstructionsUrl();
        if(!StringUtils.isEmpty(drugInstructionsUrl) && drugInstructionsUrl.startsWith("http")){
            productGmp.setDrugInstructionsUrl(storeFile(drugInstructionsUrl));
        }

        String generalTaxpayerRecordsUrl = productGmp.getGeneralTaxpayerRecordsUrl();
        if(!StringUtils.isEmpty(generalTaxpayerRecordsUrl) && generalTaxpayerRecordsUrl.startsWith("http")){
            productGmp.setGeneralTaxpayerRecordsUrl(storeFile(generalTaxpayerRecordsUrl));
        }

        String legalPowerOfAttorneyUrl = productGmp.getLegalPowerOfAttorneyUrl();
        if(!StringUtils.isEmpty(legalPowerOfAttorneyUrl) && legalPowerOfAttorneyUrl.startsWith("http")){
            productGmp.setLegalPowerOfAttorneyUrl(storeFile(legalPowerOfAttorneyUrl));
        }

        String idCardUrl = productGmp.getIdCardUrl();
        if(!StringUtils.isEmpty(idCardUrl) && idCardUrl.startsWith("http")){
            productGmp.setIdCardUrl(storeFile(idCardUrl));
        }

        String proudctProduceStandardUrl = productGmp.getProudctProduceStandardUrl();
        if(!StringUtils.isEmpty(proudctProduceStandardUrl) && proudctProduceStandardUrl.startsWith("http")){
            productGmp.setProudctProduceStandardUrl(storeFile(proudctProduceStandardUrl));
        }

        String purchaseAndSaleContractUrl = productGmp.getPurchaseAndSaleContractUrl();
        if(!StringUtils.isEmpty(purchaseAndSaleContractUrl) && purchaseAndSaleContractUrl.startsWith("http")){
            productGmp.setPurchaseAndSaleContractUrl(storeFile(purchaseAndSaleContractUrl));
        }

        String productPackageAndManualUrl = productGmp.getProductPackageAndManualUrl();
        if(!StringUtils.isEmpty(productPackageAndManualUrl) && productPackageAndManualUrl.startsWith("http")){
            productGmp.setProductPackageAndManualUrl(storeFile(productPackageAndManualUrl));
        }

        List<Map<String,String>> productProvincialPnspectionReportList = productGmp.getProductProvincialPnspectionReport();
        if(productProvincialPnspectionReportList!=null && productProvincialPnspectionReportList.size()>0) {
            for (Map<String, String> productProvincialPnspectionReport : productProvincialPnspectionReportList) {
                Iterator it = productProvincialPnspectionReport.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    String value = productProvincialPnspectionReport.get(key);
                    if (!StringUtils.isEmpty(value) && value.startsWith("http")) {
                        productProvincialPnspectionReport.put(key, storeFile(value));
                    }
                }

            }
        }

        List<Map<String,String>> productPriceDocumentList = productGmp.getProductPriceDocument();
        if(productPriceDocumentList!=null && productPriceDocumentList.size()>0) {
            for (Map<String, String> productPriceDocument : productPriceDocumentList) {
                Iterator it = productPriceDocument.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    String value = productPriceDocument.get(key);
                    if (!StringUtils.isEmpty(value) && value.startsWith("http")) {
                        productPriceDocument.put(key, storeFile(value));
                    }
                }
            }
        }

        List<Map<String,String>> productFactoryInspectionReportList = productGmp.getProductFactoryInspectionReport();
        if(productFactoryInspectionReportList!=null && productFactoryInspectionReportList.size()>0) {
            for (Map<String, String> productFactoryInspectionReport : productFactoryInspectionReportList) {
                Iterator it = productFactoryInspectionReport.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    String value = productFactoryInspectionReport.get(key);
                    if (!StringUtils.isEmpty(value) && value.startsWith("http")) {
                        productFactoryInspectionReport.put(key, storeFile(value));
                    }
                }
            }
        }

        List<Map<String,String>> purchaserCertificates = productGmp.getPurchaserCertificate();
        if(purchaserCertificates!=null && purchaserCertificates.size()>0) {
            for (Map<String, String> purchaserCertificate : purchaserCertificates) {
                Iterator it = purchaserCertificate.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    String value = purchaserCertificate.get(key);
                    if (!StringUtils.isEmpty(value) && value.startsWith("http")) {
                        purchaserCertificate.put(key, storeFile(value));
                    }
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
        } finally {
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
