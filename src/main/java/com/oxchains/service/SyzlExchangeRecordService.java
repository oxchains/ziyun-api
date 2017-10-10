package com.oxchains.service;

import com.oxchains.bean.dto.SyzlEnterpriseGmpDTO;
import com.oxchains.bean.dto.SyzlExchangeRecordDTO;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.time.LocalDateTime.now;

/**
 * Created by root on 17-10-10.
 */
@Service
@Slf4j
public class SyzlExchangeRecordService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;


    @Value("${file.upload.dir}")
    private String upload;

    public RespDTO<String> addSyzlExchangeRecord(SyzlExchangeRecord syzlExchangeRecord) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException{
        String token = syzlExchangeRecord.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        syzlExchangeRecord.setToken(jwt.getId());
        translateFile(syzlExchangeRecord);
        SyzlEnterpriseGmp syzlEnterpriseGmp = syzlExchangeRecord.getEnterpriseFirstInformation();
        List<SyzlProductGmp> syzlProductGmpList = syzlExchangeRecord.getProductFirstInformationList();
        translateFileByEnterpriseGmp(syzlEnterpriseGmp);//translate url to localfile
        if(syzlProductGmpList != null && syzlProductGmpList.size() > 0) {
            for(SyzlProductGmp syzlProductGmp : syzlProductGmpList) {
                translateFileByProductGmp(syzlProductGmp);//translate url to localfile
            }
        }
        String txID = chaincodeService.invoke("saveSyzlExchangeRecord", new String[] { gson.toJson(syzlExchangeRecord) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<SyzlExchangeRecord>> getSyzlExchangeRecord(String SendEnterpriseName, String Token){
        String jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\n" +
                "    \"SendEnterpriseName\": \""+SendEnterpriseName+"\"\n}}"});
        log.debug("===getSyzlExchangeRecord===" + jsonStr);
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        String txId = jsonStr.split("!#!")[1];
        jsonStr =  jsonStr.split("!#!")[0];
        SyzlExchangeRecordDTO syzlExchangeRecordDTO = simpleGson.fromJson(jsonStr,SyzlExchangeRecordDTO.class);

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<SyzlExchangeRecord> it = syzlExchangeRecordDTO.getList().iterator(); it.hasNext();) {
            SyzlExchangeRecord SyzlExchangeRecord = it.next();
            SyzlExchangeRecord.setTxId(txId);
            log.debug("===SyzlExchangeRecord.getToken()==="+SyzlExchangeRecord.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { SyzlExchangeRecord.getToken() });
            log.info("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.info("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(syzlExchangeRecordDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }
        return RespDTO.success(syzlExchangeRecordDTO.getList());
    }

    private void translateFile(SyzlExchangeRecord syzlExchangeRecord) {
        List<String> list = syzlExchangeRecord.getEntrustBookUrl();
        if (list != null && list.size() > 0) {
            List<String> tmp = new ArrayList<>();
            for (String y : list) {
                if (!StringUtils.isEmpty(y) && y.startsWith("http")) {
                    tmp.add(storeFile(y));
                } else {
                    tmp.add(y);
                }
            }
            syzlExchangeRecord.setEntrustBookUrl(tmp);
        }
    }

    private void translateFileByProductGmp(SyzlProductGmp syzlProductGmp){
        List<String> ApprovalUrl = syzlProductGmp.getApprovalUrl();
        if(ApprovalUrl!=null && ApprovalUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : ApprovalUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setApprovalUrl(tmp);
        }
        List<String> ProductPatentUrl = syzlProductGmp.getProductPatentUrl();
        if(ProductPatentUrl!=null && ProductPatentUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : ProductPatentUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setProductPatentUrl(tmp);
        }
        List<String> ProductTrademarkUrl = syzlProductGmp.getProductTrademarkUrl();
        if(ProductTrademarkUrl!=null && ProductTrademarkUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : ProductTrademarkUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setProductTrademarkUrl(tmp);
        }
        List<String> SmallestPackageUrl = syzlProductGmp.getSmallestPackageUrl();
        if(SmallestPackageUrl!=null && SmallestPackageUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : SmallestPackageUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setSmallestPackageUrl(tmp);
        }
        List<String> DrugDescriptionUrl = syzlProductGmp.getDrugDescriptionUrl();
        if(DrugDescriptionUrl!=null && DrugDescriptionUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : DrugDescriptionUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setDrugDescriptionUrl(tmp);
        }
        List<String> TaxpayerRecordUrl = syzlProductGmp.getTaxpayerRecordUrl();
        if(TaxpayerRecordUrl!=null && TaxpayerRecordUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : TaxpayerRecordUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setTaxpayerRecordUrl(tmp);
        }
        List<String> FrwtsUrl = syzlProductGmp.getFrwtsUrl();
        if(FrwtsUrl!=null && FrwtsUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : FrwtsUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setFrwtsUrl(tmp);
        }
        List<String> IdCardUrl = syzlProductGmp.getIdCardUrl();
        if(IdCardUrl!=null && IdCardUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : IdCardUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setIdCardUrl(tmp);
        }
        List<String> CpscbzUrl = syzlProductGmp.getCpscbzUrl();
        if(CpscbzUrl!=null && CpscbzUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : CpscbzUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setCpscbzUrl(tmp);
        }
        List<String> GxhtUrl = syzlProductGmp.getGxhtUrl();
        if(GxhtUrl!=null && GxhtUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : GxhtUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setGxhtUrl(tmp);
        }
        List<String> CpbzsmsfjUrl = syzlProductGmp.getCpbzsmsfjUrl();
        if(CpbzsmsfjUrl!=null && CpbzsmsfjUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : CpbzsmsfjUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setCpbzsmsfjUrl(tmp);
        }

        List<String> SjjybgUrl = syzlProductGmp.getSjjybgUrl();
        if(SjjybgUrl!=null && SjjybgUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : SjjybgUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setSjjybgUrl(tmp);
        }
        List<String> CpwjwjUrl = syzlProductGmp.getCpwjwjUrl();
        if(CpwjwjUrl!=null && CpwjwjUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : CpwjwjUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setCpwjwjUrl(tmp);
        }
        List<String> MpcpcjbgUrl = syzlProductGmp.getMpcpcjbgUrl();
        if(MpcpcjbgUrl!=null && MpcpcjbgUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : MpcpcjbgUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setMpcpcjbgUrl(tmp);
        }
        List<String> GxyzgzsUrl = syzlProductGmp.getGxyzgzsUrl();
        if(GxyzgzsUrl!=null && GxyzgzsUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : GxyzgzsUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlProductGmp.setGxyzgzsUrl(tmp);
        }
    }

    private void translateFileByEnterpriseGmp(SyzlEnterpriseGmp syzlEnterpriseGmp){
        List<String> YyzzUrl = syzlEnterpriseGmp.getYyzzUrl();
        if(YyzzUrl!=null && YyzzUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YyzzUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYyzzUrl(tmp);
        }
        List<String> DistributionAgreementUrl = syzlEnterpriseGmp.getDistributionAgreementUrl();
        if(DistributionAgreementUrl!=null && DistributionAgreementUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : DistributionAgreementUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setDistributionAgreementUrl(tmp);
        }
        List<String> InvoiceUrl = syzlEnterpriseGmp.getInvoiceUrl();
        if(InvoiceUrl!=null && InvoiceUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : InvoiceUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setInvoiceUrl(tmp);
        }
        List<String> YpjyxkzUrl = syzlEnterpriseGmp.getYpjyxkzUrl();
        if(YpjyxkzUrl!=null && YpjyxkzUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YpjyxkzUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYpjyxkzUrl(tmp);
        }
        List<String> QybghztzsUrl = syzlEnterpriseGmp.getQybghztzsUrl();
        if(QybghztzsUrl!=null && QybghztzsUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : QybghztzsUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setQybghztzsUrl(tmp);
        }
        List<String> GsnjUrl = syzlEnterpriseGmp.getGsnjUrl();
        if(GsnjUrl!=null && GsnjUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : GsnjUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setGsnjUrl(tmp);
        }
        List<String> YhkhxkzUrl = syzlEnterpriseGmp.getYhkhxkzUrl();
        if(YhkhxkzUrl!=null && YhkhxkzUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YhkhxkzUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYhkhxkzUrl(tmp);
        }
        List<String> ZzszyfpybUrl = syzlEnterpriseGmp.getZzszyfpybUrl();
        if(ZzszyfpybUrl!=null && ZzszyfpybUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : ZzszyfpybUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setZzszyfpybUrl(tmp);
        }
        List<String> KpInfoUrl = syzlEnterpriseGmp.getKpInfoUrl();
        if(KpInfoUrl!=null && KpInfoUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : KpInfoUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setKpInfoUrl(tmp);
        }
        List<String> YzymbaUrl = syzlEnterpriseGmp.getYzymbaUrl();
        if(YzymbaUrl!=null && YzymbaUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YzymbaUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYzymbaUrl(tmp);
        }
        List<String> BlankSalesContractUrl = syzlEnterpriseGmp.getBlankSalesContractUrl();
        if(BlankSalesContractUrl!=null && BlankSalesContractUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : BlankSalesContractUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setBlankSalesContractUrl(tmp);
        }

        List<String> ShtxdyzUrl = syzlEnterpriseGmp.getShtxdyzUrl();
        if(ShtxdyzUrl!=null && ShtxdyzUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : ShtxdyzUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setShtxdyzUrl(tmp);
        }
        List<String> QysyndbgUrl = syzlEnterpriseGmp.getQysyndbgUrl();
        if(QysyndbgUrl!=null && QysyndbgUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : QysyndbgUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setQysyndbgUrl(tmp);
        }
        List<String> ZltxdcbUrl = syzlEnterpriseGmp.getZltxdcbUrl();
        if(ZltxdcbUrl!=null && ZltxdcbUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : ZltxdcbUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setZltxdcbUrl(tmp);
        }
        List<String> HgghfdabUrl = syzlEnterpriseGmp.getHgghfdabUrl();
        if(HgghfdabUrl!=null && HgghfdabUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : HgghfdabUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setHgghfdabUrl(tmp);
        }
        List<String> YljxjyxkzUrl = syzlEnterpriseGmp.getYljxjyxkzUrl();
        if(YljxjyxkzUrl!=null && YljxjyxkzUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YljxjyxkzUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYljxjyxkzUrl(tmp);
        }
        List<String> YpjyzlglgfrzsUrl = syzlEnterpriseGmp.getYpjyzlglgfrzsUrl();
        if(YpjyzlglgfrzsUrl!=null && YpjyzlglgfrzsUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YpjyzlglgfrzsUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYpjyzlglgfrzsUrl(tmp);
        }
        List<String> SalesIdCardUrl = syzlEnterpriseGmp.getSalesIdCardUrl();
        if(SalesIdCardUrl!=null && SalesIdCardUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : SalesIdCardUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setSalesIdCardUrl(tmp);
        }
        List<String> FrwtsyjUrl = syzlEnterpriseGmp.getFrwtsyjUrl();
        if(FrwtsyjUrl!=null && FrwtsyjUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : FrwtsyjUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setFrwtsyjUrl(tmp);
        }
        List<String> EducationProveUrl = syzlEnterpriseGmp.getEducationProveUrl();
        if(EducationProveUrl!=null && EducationProveUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : EducationProveUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setEducationProveUrl(tmp);
        }
        List<String> YljgzyxkzUrl = syzlEnterpriseGmp.getYljgzyxkzUrl();
        if(YljgzyxkzUrl!=null && YljgzyxkzUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YljgzyxkzUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYljgzyxkzUrl(tmp);
        }
        List<String> ZyyszgzUrl = syzlEnterpriseGmp.getZyyszgzUrl();
        if(ZyyszgzUrl!=null && ZyyszgzUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : ZyyszgzUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setZyyszgzUrl(tmp);
        }

        List<String> YpsczlglgfrzsUrl = syzlEnterpriseGmp.getYpsczlglgfrzsUrl();
        if(YpsczlglgfrzsUrl!=null && YpsczlglgfrzsUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YpsczlglgfrzsUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYpsczlglgfrzsUrl(tmp);
        }
        List<String> YljxscxkbabUrl = syzlEnterpriseGmp.getYljxscxkbabUrl();
        if(YljxscxkbabUrl!=null && YljxscxkbabUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YljxscxkbabUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYljxscxkbabUrl(tmp);
        }
        List<String> YljxscxkzUrl = syzlEnterpriseGmp.getYljxscxkzUrl();
        if(YljxscxkzUrl!=null && YljxscxkzUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YljxscxkzUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYljxscxkzUrl(tmp);
        }
        List<String> YpzcpjUrl = syzlEnterpriseGmp.getYpzcpjUrl();
        if(YpzcpjUrl!=null && YpzcpjUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YpzcpjUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYpzcpjUrl(tmp);
        }
        List<String> YpzcpjNextUrl = syzlEnterpriseGmp.getYpzcpjNextUrl();
        if(YpzcpjNextUrl!=null && YpzcpjNextUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YpzcpjNextUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYpzcpjNextUrl(tmp);
        }
        List<String> YpbcpjUrl = syzlEnterpriseGmp.getYpbcpjUrl();
        if(YpbcpjUrl!=null && YpbcpjUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YpbcpjUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYpbcpjUrl(tmp);
        }
        List<String> XyzsUrl = syzlEnterpriseGmp.getXyzsUrl();
        if(XyzsUrl!=null && XyzsUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : XyzsUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setXyzsUrl(tmp);
        }
        List<String> CpzlbzUrl = syzlEnterpriseGmp.getCpzlbzUrl();
        if(CpzlbzUrl!=null && CpzlbzUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : CpzlbzUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setCpzlbzUrl(tmp);
        }
        List<String> ZlzsUrl = syzlEnterpriseGmp.getZlzsUrl();
        if(ZlzsUrl!=null && ZlzsUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : ZlzsUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setZlzsUrl(tmp);
        }
        List<String> JkypzczUrl = syzlEnterpriseGmp.getJkypzczUrl();
        if(JkypzczUrl!=null && JkypzczUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : JkypzczUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setJkypzczUrl(tmp);
        }
        List<String> PriceApprovalUrl = syzlEnterpriseGmp.getPriceApprovalUrl();
        if(PriceApprovalUrl!=null && PriceApprovalUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : PriceApprovalUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setPriceApprovalUrl(tmp);
        }

        List<String> YpjybgUrl = syzlEnterpriseGmp.getYpjybgUrl();
        if(YpjybgUrl!=null && YpjybgUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YpjybgUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYpjybgUrl(tmp);
        }
        List<String> BoxUrl = syzlEnterpriseGmp.getBoxUrl();
        if(BoxUrl!=null && BoxUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : BoxUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setBoxUrl(tmp);
        }
        List<String> LabelUrl = syzlEnterpriseGmp.getLabelUrl();
        if(LabelUrl!=null && LabelUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : LabelUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setLabelUrl(tmp);
        }
        List<String> DescriptionBookUrl = syzlEnterpriseGmp.getDescriptionBookUrl();
        if(DescriptionBookUrl!=null && DescriptionBookUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : DescriptionBookUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setDescriptionBookUrl(tmp);
        }
        List<String> YpjgjgzdbaUrl = syzlEnterpriseGmp.getYpjgjgzdbaUrl();
        if(YpjgjgzdbaUrl!=null && YpjgjgzdbaUrl.size()>0){
            List<String> tmp = new ArrayList<>();
            for(String y : YpjgjgzdbaUrl){
                if(!StringUtils.isEmpty(y) && y.startsWith("http")){
                    tmp.add(storeFile(y));
                }
                else{
                    tmp.add(y);
                }
            }
            syzlEnterpriseGmp.setYpjgjgzdbaUrl(tmp);
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
