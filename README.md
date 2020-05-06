# EGC-Commons

Reusable Java components for EGC
> Formerly ***commons*** module in project cybersolim

**开发完之后，必须deploy到maven仓库，同时在项目中刷新之后才能使用**

## Maven dependency

> in private nexus repository  http://192.168.6.56:8081/repository/egc/

```xml
<dependency>
    <groupId>org.egc</groupId>
    <artifactId>commons</artifactId>
    <version>2.1-SNAPSHOT</version>
</dependency>
```

## Packages

- Command
  - Class **CommonsExec**：Run Cmd/Shell commands use Apache Commons-Exec
- Exception
  - Class **BusinessException**
- Files
- Raster
  - GeoTiffUtil / RasterFile2PostGIS
- Security
  - Shiro
  - JWT
- Util
  - Date / String / Properties et al.
- Web
  - **BaseController**、**JsonResult** et al.
- XML
- Test

## GDAL 版本
 gdal 2.4.4 
 > [gisinternals](http://www.gisinternals.com/) 上的 `gdal-204-1900-x64-core`
 
 运行 `RasterTest` 下的单元测试，测试 `gdal` 是否安装配置正确。
 能够输出版本信息说明 `gdal` 及 `gdal.jar` 安装配置成功。
 能够读取元数据说明安装的`gdal`版本与使用的`gdal.jar`的版本一致 

---

## More

For more reusable Java components，please refer to [Apache Commons](https://commons.apache.org/) project and [Google Guava](https://github.com/google/guava) project first.