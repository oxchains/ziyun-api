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

    public RespDTO<List<ProductGmp>> getProductGmpByProducName(String ProducName,String Token){
        String result = chaincodeService.getPayloadAndTxid("getProductGmpByProducName", new String[]{ProducName});

        log.debug("===getProductGmpByProducName===" + result);
        if (StringUtils.isEmpty(result)) {
            return RespDTO.fail("没有数据");
        }
        String jsonStr = result.split("!#!")[0];
        String txId = result.split("!#!")[1];
        ProductGmp productGmp = simpleGson.fromJson(jsonStr, ProductGmp.class);
        productGmp.setTxId(txId);
        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        log.debug("===ProductGmp.getToken()==="+productGmp.getToken());
        String jsonAuth = chaincodeService.query("query", new String[] { productGmp.getToken() });
        log.debug("===jsonAuth==="+jsonAuth);
        Auth auth = gson.fromJson(jsonAuth, Auth.class);
        ArrayList<String> authList = auth.getAuthList();
        log.debug("===username==="+username);
        if(!authList.contains(username)){
            log.debug("===remove===");
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }

        return RespDTO.success(Arrays.asList(productGmp));
    }

    private void translateFile(ProductGmp productGmp){

        String ApprovalUrl = productGmp.getApprovalUrl();
        if(!StringUtils.isEmpty(ApprovalUrl) && ApprovalUrl.startsWith("http")){
            productGmp.setApprovalUrl(storeFile(ApprovalUrl));
        }

        String ProductPatentCertificateUrl = productGmp.getProductPatentCertificateUrl();
        if(!StringUtils.isEmpty(ProductPatentCertificateUrl) && ProductPatentCertificateUrl.startsWith("http")){
            productGmp.setProductPatentCertificateUrl(storeFile(ProductPatentCertificateUrl));
        }

        String ProductTrademarkDocumentsUrl = productGmp.getProductTrademarkDocumentsUrl();
        if(!StringUtils.isEmpty(ProductTrademarkDocumentsUrl) && ProductTrademarkDocumentsUrl.startsWith("http")){
            productGmp.setProductTrademarkDocumentsUrl(storeFile(ProductTrademarkDocumentsUrl));
        }

        String ProductMiniPackageUrl = productGmp.getProductMiniPackageUrl();
        if(!StringUtils.isEmpty(ProductMiniPackageUrl) && ProductMiniPackageUrl.startsWith("http")){
            productGmp.setProductMiniPackageUrl(storeFile(ProductMiniPackageUrl));
        }

        String DrugInstructionsUrl = productGmp.getDrugInstructionsUrl();
        if(!StringUtils.isEmpty(DrugInstructionsUrl) && DrugInstructionsUrl.startsWith("http")){
            productGmp.setDrugInstructionsUrl(storeFile(DrugInstructionsUrl));
        }

        String GeneralTaxpayerRecordsUrl = productGmp.getGeneralTaxpayerRecordsUrl();
        if(!StringUtils.isEmpty(GeneralTaxpayerRecordsUrl) && GeneralTaxpayerRecordsUrl.startsWith("http")){
            productGmp.setGeneralTaxpayerRecordsUrl(storeFile(GeneralTaxpayerRecordsUrl));
        }

        String LegalPowerOfAttorneyUrl = productGmp.getLegalPowerOfAttorneyUrl();
        if(!StringUtils.isEmpty(LegalPowerOfAttorneyUrl) && LegalPowerOfAttorneyUrl.startsWith("http")){
            productGmp.setLegalPowerOfAttorneyUrl(storeFile(LegalPowerOfAttorneyUrl));
        }

        String IdCardUrl = productGmp.getIdCardUrl();
        if(!StringUtils.isEmpty(IdCardUrl) && IdCardUrl.startsWith("http")){
            productGmp.setIdCardUrl(storeFile(IdCardUrl));
        }

        String ProudctProduceStandardUrl = productGmp.getProudctProduceStandardUrl();
        if(!StringUtils.isEmpty(ProudctProduceStandardUrl) && ProudctProduceStandardUrl.startsWith("http")){
            productGmp.setProudctProduceStandardUrl(storeFile(ProudctProduceStandardUrl));
        }

        String PurchaseAndSaleContractUrl = productGmp.getPurchaseAndSaleContractUrl();
        if(!StringUtils.isEmpty(PurchaseAndSaleContractUrl) && PurchaseAndSaleContractUrl.startsWith("http")){
            productGmp.setPurchaseAndSaleContractUrl(storeFile(PurchaseAndSaleContractUrl));
        }

        String ProductPackageAndManualUrl = productGmp.getProductPackageAndManualUrl();
        if(!StringUtils.isEmpty(ProductPackageAndManualUrl) && ProductPackageAndManualUrl.startsWith("http")){
            productGmp.setProductPackageAndManualUrl(storeFile(ProductPackageAndManualUrl));
        }

        List<ProductProvincialPnspectionReport> productProvincialPnspectionReportList = productGmp.getProductProvincialPnspectionReport();
        if(productProvincialPnspectionReportList!=null && productProvincialPnspectionReportList.size()>0)
        for(ProductProvincialPnspectionReport productProvincialPnspectionReport : productProvincialPnspectionReportList){
            String p = productProvincialPnspectionReport.getProductProvincialPnspectionReportValue();
            if(!StringUtils.isEmpty(p) && p.startsWith("http")){
                productProvincialPnspectionReport.setProductProvincialPnspectionReportValue(storeFile(p));
            }
        }

        List<ProductPriceDocument> productPriceDocumentList = productGmp.getProductPriceDocument();
        if(productPriceDocumentList!=null && productPriceDocumentList.size()>0)
        for(ProductPriceDocument productPriceDocument : productPriceDocumentList){
            String p = productPriceDocument.getProductPriceDocumentValue();
            if(!StringUtils.isEmpty(p) && p.startsWith("http")){
                productPriceDocument.setProductPriceDocumentValue(storeFile(p));
            }
        }

        List<ProductFactoryInspectionReport> productFactoryInspectionReportList = productGmp.getProductFactoryInspectionReport();
        if(productFactoryInspectionReportList!=null && productFactoryInspectionReportList.size()>0)
        for(ProductFactoryInspectionReport productFactoryInspectionReport : productFactoryInspectionReportList){
            String p = productFactoryInspectionReport.getProductFactoryInspectionReportValue();
            if(!StringUtils.isEmpty(p) && p.startsWith("http")){
                productFactoryInspectionReport.setProductFactoryInspectionReportValue(storeFile(p));
            }
        }

        List<PurchaserCertificate> purchaserCertificates = productGmp.getPurchaserCertificate();
        if(purchaserCertificates!=null && purchaserCertificates.size()>0)
        for(PurchaserCertificate purchaserCertificate : purchaserCertificates){
            String p = purchaserCertificate.getPurchaserCertificateValue();
            if(!StringUtils.isEmpty(p) && p.startsWith("http")){
                purchaserCertificate.setPurchaserCertificateValue(storeFile(p));
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
            System.out.println("===head==="+head);
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
            log.error(e.toString());
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }
}
