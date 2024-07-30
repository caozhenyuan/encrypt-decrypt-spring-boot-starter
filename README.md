# encrypt-decrypt-spring-boot-starter

## 已做：

1. RSA和AES接口加密

## 使用方式：

pom.xml

~~~xml
        <dependency>
            <groupId>com.czy</groupId>
            <artifactId>encrypt-decrypt-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
~~~

application.yml

~~~yaml
spring:
  encrypt:
    #是否开启加密
    enabled: true
    #私钥
    privateKey: your key
    #前端内容json key
    contentJsonKey: content
    #前端加密过后的aesKey key
    aesJsonKey: aesKey
~~~

测试entity

~~~java
public class TbStudent implements Serializable {

    private static final long serialVersionUID = -7251361343985943405L;
    private Long id;

    private String stuName;

    private Long classId;

    private Date createTime;

    private Integer delFlag;

    private Integer version;

    private String stuId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    @Override
    public String toString() {
        return "TbStudent{" +
                "id=" + id +
                ", stuName='" + stuName + '\'' +
                ", classId=" + classId +
                ", createTime=" + createTime +
                ", delFlag=" + delFlag +
                ", version=" + version +
                ", stuId='" + stuId + '\'' +
                '}';
    }
}
~~~

controller

**注意：如果不想使用AjaxResultVo做返回，请参考EncryptResponse类里的beforeBodyWrite的todo,自做修改。**

```java
@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/saveStudent")
    @Encrypt
    public AjaxResultVo<String> saveStudent(@RequestBody @Decrypt TbStudent student) {
        return new AjaxResultVo<>(student);
    }
}
```

前端默认传值：JSON,这是默认的传入JSON,可以在配置文件里修改键。

~~~json
{
    "content": "",
    "aesKey":""
}
~~~

## 流程：

1. 用户操作
2. 前端组装JSON
3. 生成随机AES秘钥
4. 用AES秘钥加密JSON得到密文C
5. 用RSA公钥加密AES秘钥，得到aesKey
6. 传入密文C和aesKey到后端
7. 后端用RSA私钥解密AES秘钥
8. 用AES秘钥解密密文C
9. 处理业务
10. 响应数据（可加密返回，返回结果用前端生产的AES秘钥加密返回）

测试示例：

```java
 public static void main(String[] args) throws Exception {
        String publicKey = "your key";
        //生成随机的AES秘钥
        String aesKey = AES.getAESKey(16);
        System.out.println("AES的key是(加密之前): " + aesKey);

        //模拟业务数据，并用AES的key加密
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("stuId", 1);
        dataMap.put("stuName", "张三");

        JSONObject dataJson = new JSONObject(dataMap);
        String encryptData = AES.encryptToBase64(dataJson.toJSONString(), aesKey);
        System.out.println("加密后的数据: " + encryptData);

        //把AES的key用RSA加密
        byte[] encryptAesKey = RSAUtil.encryptByPublicKey(aesKey.getBytes(StandardCharsets.UTF_8), publicKey, 245);
        System.out.println("加密之后的AES-key: " + toHexString(encryptAesKey));

        //AES解密
        //再用AES的key解密数据
        String data = AES.decryptFromBase64(encryptData, aesKey);
        System.out.println(data);

//        encryptAesTest();
    }

    public static void encryptAesTest(){
        String content="your content";
        String result = AES.decryptFromBase64(content, "your aesKey");
        System.out.println(result);
    }
```
