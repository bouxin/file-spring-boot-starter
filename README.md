<h2>云存储 spring boot starter</h2>
------
集成阿里云oss、华为云obs、腾讯云cos

------
**使用说明**

- 功能
  > 提供通用云存储基础能力，上传、下载、删除等。
  
- 引入：打包推到私服，在项目中引入依赖
  > ```xml
  > <dependency>
  >     <groupId>com.rugoo.cloud.storage</groupId>
  >     <artifactId>cloud-storage-spring-boot-starter</artifactId>
  >     <version>1.0.0-SNAPSHOT</version>
  > </dependency>
  > ```
  
- 配置
  > 配置前置项 **com.rugoo.cloud.storage**
  > ```yaml
  > prefer-cloud-type: huawei                 # 云服务商
  > endpoint: obs-east.huawei.com             # 服务商上传地址
  > domain: https://hz-photos.obs.huawei.com  # 访问域名 
  > access-key: ${random.uuid}                # access key
  > secret-key: ${random.uuid}                # access secret
  > bucket: hz-photos                         # 存储桶（必须使已创建且有存储权限的桶名）
  > max-file-size: 10MB                       # 最大文件限制
  > storepath: /1111                          # 业务子存储路径
  > ```