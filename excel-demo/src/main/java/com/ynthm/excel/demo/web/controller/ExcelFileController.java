package com.ynthm.excel.demo.web.controller;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynthm.common.Result;
import com.ynthm.common.enums.ResultCode;
import com.ynthm.common.web.util.ServletUtil;
import com.ynthm.excel.demo.excel.util.ExcelUtil;
import com.ynthm.excel.demo.web.client.DownloadData;
import com.ynthm.excel.demo.web.client.UploadData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/** @author Ethan Wang */
@Slf4j
@RestController
public class ExcelFileController {

  @Autowired private ObjectMapper objectMapper;
  /**
   * 文件下载（失败了会返回一个有部分数据的Excel）
   *
   * <p>1. 创建excel对应的实体对象 参照{@link DownloadData}
   *
   * <p>2. 设置返回的 参数
   *
   * <p>3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
   */
  @GetMapping("download")
  public void download(HttpServletResponse response) throws IOException {
    // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
    response.setContentType("application/vnd.ms-excel");
    response.setCharacterEncoding("utf-8");
    // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
    String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
    response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("模板").doWrite(data());
  }

  /**
   * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
   *
   * @since 2.1.1
   */
  @GetMapping("downloadFailedUsingJson")
  public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
    // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
    ExcelUtil<DownloadData> excelUtil = new ExcelUtil<>(DownloadData.class);
    ServletUtil.responseForDownload(response, "测试.xlsx");
    try (ServletOutputStream outputStream = response.getOutputStream()) {
      // 这里需要设置不关闭流
      excelUtil.write("模板", outputStream, data());
    } catch (IOException e) {
      ServletUtil.exception4Download(
          response,
          Result.error(ResultCode.FAILED).setMessage(e.getMessage()).toJson(objectMapper));
    }
  }

  /**
   * 文件上传
   *
   * <p>1. 创建excel对应的实体对象 参照{@link UploadData}
   *
   * <p>2. 直接读即可
   */
  @PostMapping("upload")
  public String upload(@RequestParam(required = false) MultipartFile excelFile) throws Exception {

    Consumer<List<UploadData>> consumer = list -> log.info("消费条数： {}", list.size());

    ExcelUtil<UploadData> excelUtil = new ExcelUtil<>(UploadData.class);
    excelUtil.read(excelFile.getInputStream(), consumer);
    return "success";
  }

  private List<DownloadData> data() {
    List<DownloadData> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      DownloadData data = new DownloadData();
      data.setString("字符串" + 0);
      data.setDate(new Date());
      data.setDoubleData(0.56);
      list.add(data);
    }
    return list;
  }
}
