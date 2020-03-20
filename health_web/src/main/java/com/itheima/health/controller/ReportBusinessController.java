package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.ReportBusinessService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reportBusiness")
public class ReportBusinessController {
    @Reference
    private ReportBusinessService reportBusinessService;
    //从数据库获取报表需要的参数
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object>map=reportBusinessService.getBusinessReportData();

        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);

        }


    }
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String,Object>map=reportBusinessService.getBusinessReportData();
            //一、读取报表中的内容
            //1.当前日期
            String reportDate = (String) map.get("reportDate");
            //2.今天新增会员数
            Integer todayNewMember =(Integer) map.get("todayNewMember");
            //3.总会员数
            Integer totalMember =(Integer) map.get("totalMember");
            //4.本周新增会员数(>=本周的周一的日期)
            Integer thisWeekNewMember =(Integer) map.get("thisWeekNewMember");
            //5.本月新增会员数(>=本月的第一天的日期)
            Integer thisMonthNewMember =(Integer)map.get("thisMonthNewMember");
            //6.今日预约数
            Integer todayOrderNumber =(Integer)map.get("todayOrderNumber");
            //7.今日到诊数
            Integer todayVisitsNumber =(Integer)map.get("todayVisitsNumber");

            //8.本周预约数(>=本周的周一的日期 <=本周的周日的日期)
            Integer thisWeekOrderNumber =(Integer) map.get("thisWeekOrderNumber");
            //9.本周到诊数
            Integer thisWeekVisitsNumber =(Integer) map.get("thisWeekVisitsNumber");
            //10.本月预约数(>=每月的第一天的日期 <=每月的最后一天的日期)
            Integer thisMonthOrderNumber =(Integer) map.get("thisMonthOrderNumber");

            //11.本月到诊数
            Integer thisMonthVisitsNumber =(Integer) map.get("thisMonthVisitsNumber");

            //12.热门套餐
            List<Map> hotSetmeal= (List<Map>) map.get("hotSetmeal");

           //二、将数据导入到指定文件中
            //1.获取文件模板
        String templatePath=request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));

           //读取第一个文件薄
            XSSFSheet sheet=workbook.getSheetAt(0);
            //2.读取第3行，第6列
            XSSFRow row = sheet.getRow(2);
                  row.getCell(5).setCellValue(reportDate);

            //3.读取第5行，第6列，以及第8列
                   row=sheet.getRow(4);
                   row.getCell(5).setCellValue(todayNewMember);
                   row.getCell(7).setCellValue(totalMember);

            //4.读取第6行，第6列，及第8列
                   row=sheet.getRow(5);
                   row.getCell(5).setCellValue(thisWeekNewMember);

                   row.getCell(7).setCellValue(thisMonthNewMember);


           //5.读取第8行，第6列及第8列
            row=sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);
            row.getCell(7).setCellValue(todayVisitsNumber);

            //6.读取第9行，第6列及第8列
            row=sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            row.getCell(7).setCellValue(thisWeekVisitsNumber);

            //7.读取第10行，第6列及第8列
            row=sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            row.getCell(7).setCellValue(thisMonthVisitsNumber);

            //8.读取第13行到16行，的第5，6,7列循环遍历 List<Map> hotSetmeal集合
            //先读取第12行
            int rowNumber =12;

            for (Map mapSetmeal : hotSetmeal) {
                String name = (String) mapSetmeal.get("name");
                Long setmeal_count= (Long) mapSetmeal.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) mapSetmeal.get("proportion");
               row= sheet.getRow(rowNumber++);
               row.getCell(4).setCellValue(name);//套餐名
               row.getCell(5).setCellValue(setmeal_count);//预约数
               row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }
            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

            return null ;


        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);

        }


    }


}
