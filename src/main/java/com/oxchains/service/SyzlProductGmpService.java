package com.oxchains.service;

import com.oxchains.bean.dto.SyzlProductGmpDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.SyzlProductGmp;
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
 * Created by root on 17-8-8.
 */
@Service
@Slf4j
public class SyzlProductGmpService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    @Value("${file.upload.dir}")
    private String upload;

    public RespDTO<String> addSyzlProductGmp(SyzlProductGmp syzlProductGmp) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException {
        String token = syzlProductGmp.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        syzlProductGmp.setToken(jwt.getId());// store username ,not token
        translateFile(syzlProductGmp);//translate url to localfile
        String txID = chaincodeService.invoke("saveSyzlProductGmp", new String[] { gson.toJson(syzlProductGmp) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<SyzlProductGmp>> getSyzlProductGmpByProductName(String ProductName,String Token){
        String result = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\n" +
                "    \"ProductName\": \""+ProductName+"\"}}"});
        log.debug("===getSyzlProductGmpByProductName===" + result);
        if (StringUtils.isEmpty(result)) {
            return RespDTO.fail("没有数据");
        }
        String jsonStr = result.split("!#!")[0];
        String txId = result.split("!#!")[1];
        SyzlProductGmpDTO syzlProductGmpDTO = simpleGson.fromJson(jsonStr, SyzlProductGmpDTO.class);

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();

        for(Iterator<SyzlProductGmp> it = syzlProductGmpDTO.getList().iterator(); it.hasNext();){
            SyzlProductGmp syzlProductGmp = it.next();
            syzlProductGmp.setTxId(txId);
            log.debug("===SyzlEnterpriseGmp.getToken()==="+syzlProductGmp.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { syzlProductGmp.getToken() });
            log.info("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.info("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(syzlProductGmpDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }
        return RespDTO.success(syzlProductGmpDTO.getList());
    }

    private void translateFile(SyzlProductGmp syzlProductGmp){
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
