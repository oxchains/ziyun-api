## 紫云接NMBA api平台
紫云NMBA api平台，主要提供上传数据、查询数据等接口。


### 身份认证
	1. 访问控制

### 数据类型
	1. 传感器
	2. 货运单
	3. 仓储
	4. 产品
	5. 生产信息
	6. 货物
	7. 采购信息
	8. 批发零售信息
	9. 生产销售信息
	10. 产品首营资料
	11. 企业首营资料

### 健康检查
	1. 心跳检测
	
	
-----------------------------------------------------------------
### Docker 命令
docker build -t oxchains/ziyun-api .

docker run -d --name ziyun-api -p 8080:8080 -v /etc/localtime:/etc/localtime -it oxchains/ziyun-api
