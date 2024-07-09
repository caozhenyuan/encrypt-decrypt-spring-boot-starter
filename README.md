# encrypt-decrypt-spring-boot-starter

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
    enabled: true
    #私钥
    privateKey: your key
~~~

entity

~~~
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

前端默认传值：

json

~~~json
{
    "content": "",
    "aesKey":""
}
~~~

流程：

1. 用户出发操作
2. 前端组装JSON
3. 生成随机AES秘钥
4. 用AES秘钥加密JSON得到密文C
5. 用RSA公钥加密AES秘钥，得到aesKey
6. 传入密文C和aesKey到后端
7. 后端用RSA私钥解密AES秘钥
8. 用AES秘钥解密密文C
9. 处理业务
10. 响应数据（可加密返回，返回结果用前端生产的AES秘钥加密返回）
