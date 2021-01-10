#####云存储 spring boot starter

------
**使用说明**

- ###功能
  > 提供通用云存储基础能力，上传、下载、删除等。
  
- ###引入
  > ```xml
  > <dependency>
  >     <groupId>com.rugoo.cloud.storage</groupId>
  >     <artifactId>cloud-storage-spring-boot-starter</artifactId>
  >     <version>1.0.0-SNAPSHOT</version>
  > </dependency>
  > ```
  
- ###配置
  > 配置前置项 **com.rugoo.cloud.storage**
  > ```yaml
  > prefer-cloud-type: aliyun       # 云服务商
  > endpoint: https://hello-aliyun  # 服务商域名节点 
  > access-key: ${random.uuid}      # access key
  > secret-key: ${random.uuid}      # access secret
  > bucket: hz-photos               # 存储桶（必须使已创建且有存储权限的桶名）
  > max-file-size: 1024             # 最大文件限制，单位：byte
  > parentpath:  /cloud/test/        # 业务基础存储路径（例如根据业务划分不同的路径）
  > storepath: /1111/               # 业务子存储路径
  > ```