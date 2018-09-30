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
    <version>2.0-SNAPSHOT</version>
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

---

## More

For more reusable Java components，please refer to [Apache Commons](https://commons.apache.org/) project and [Google Guava](https://github.com/google/guava) project first.