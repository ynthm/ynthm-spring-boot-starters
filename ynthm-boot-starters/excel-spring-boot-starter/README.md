# Read Me First

```java
new ExcelExtraDataDynamic(total, "统计数据")
.setSheetWriteHandlers(Lists.newArrayList(new SimpleColumnWidthStyleStrategy(15)));



@Operation(summary = "工作日报统计 - 导出")
@PostMapping("/statistics/users/export")
public void dailyReportStatisticsExport(
        HttpServletResponse response, @Validated @RequestBody ReportStatisticsReq req)
        throws BaseException {
        try {
        
        } catch (Exception e) {
        response.reset();
        if (e instanceof BaseException) {
        throw (BaseException) e;
        } else {
        throw new OfficeException(e);
        }
        }
        }
```
