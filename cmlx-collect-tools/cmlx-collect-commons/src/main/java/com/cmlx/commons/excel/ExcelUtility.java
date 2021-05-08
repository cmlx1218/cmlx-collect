package com.cmlx.commons.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 10:20
 * @Desc ->
 **/
@Slf4j
@UtilityClass
public class ExcelUtility {

    private final Sheet initSheet;

    static {
        initSheet = new Sheet(1, 0);
        initSheet.setSheetName("sheet");
        // 设置自适应宽度
        initSheet.setAutoWidth(Boolean.TRUE);
    }

    /**
     * 读取少于1000行数据，，默认读第一个Sheet【1】，从一行开始读取【0】
     *
     * @param clazz    返回类型
     * @param filePath 绝对路径
     * @param <T>      限定参数
     * @return Excel导出数据
     */
    public <T> List<T> readLessThan1000Row(Class<T> clazz, String filePath) {
        return readLessThan1000RowBySheet(clazz, filePath, null);
    }

    /**
     * 读取少于1000行数据，自定义Sheet
     * initSheet:
     * sheetNo: sheet页码，默认为1
     * headLineMun：从第几行开始读取数据，默认为0，表示从第一行开始读取
     * clazz：返回数据List<T> 中 T 的类名
     */
    public <T> List<T> readLessThan1000RowBySheet(Class<T> clazz, String filePath, Sheet sheet) {
        if (!StringUtils.hasText(filePath)) {
            return null;
        }
        // 初始化Sheet参数
        sheet = sheet != null ? sheet : initSheet;

        InputStream fileStream = null;
        try {
            T t1 = clazz.newInstance();
            fileStream = new FileInputStream(filePath);
            List<Object> read = EasyExcelFactory.read(fileStream, sheet);
            List<T> result = new ArrayList<>();
            List<String> keys = (List<String>) read.get(0);
            for (int i = 1; i < read.size(); i++) {
                Map<String, Object> json = new HashMap<>();
                if (read.get(i) instanceof List) {
                    for (int j = 0; j < ((List) read.get(i)).size(); j++) {
                        json.put(keys.get(j), ((List) read.get(i)).get(j));
                    }
                    T t = JSON.parseObject(JSON.toJSONString(json), clazz);
                    result.add(t);
                }
            }
            return result;
        } catch (Exception e) {
            log.info("找不到文件或文件路径错误，文件：{}", filePath);
        } finally {
            try {
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                log.info("excel文件读取失败，失败原因：{}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 读取大于1000行数据，默认读第一个Sheet【1】，从一行开始读取【0】
     *
     * @param clazz    返回java泛型类
     * @param filePath 读取Excel绝对路径
     * @param <T>      入参限制
     * @return
     */
    public <T> List<T> readMoreThan1000Row(Class<T> clazz, String filePath) {
        return readMoreThan1000RowBySheet(clazz, filePath, null);
    }

    /**
     * 读取大于1000行数据，自定义Sheet
     *
     * @param clazz    返回java泛型类
     * @param filePath 读取Excel绝对路径
     * @param sheet    自定义Sheet
     * @param <T>      入参限制
     * @return
     */
    public <T> List<T> readMoreThan1000RowBySheet(Class<T> clazz, String filePath, Sheet sheet) {
        if (!StringUtils.hasText(filePath)) {
            return null;
        }

        sheet = sheet != null ? sheet : initSheet;

        InputStream fileStream = null;
        try {
            fileStream = new FileInputStream(filePath);
            ExcelListener excelListener = new ExcelListener();
            EasyExcelFactory.readBySax(fileStream, sheet, excelListener);
            List<Object> datas = excelListener.getDataList();
            List<T> result = new ArrayList<>();
            List<String> keys = (List<String>) datas.get(0);
            for (int i = 1; i < datas.size(); i++) {
                Map<String, Object> json = new HashMap<>();
                if (datas.get(i) instanceof List) {
                    for (int j = 0; j < ((List) datas.get(i)).size(); j++) {
                        json.put(keys.get(j), ((List) datas.get(i)).get(j));
                    }
                    T t = JSON.parseObject(JSON.toJSONString(json), clazz);
                    result.add(t);
                }
            }
            return result;
        } catch (FileNotFoundException e) {
            log.info("找不到文件或文件路径错误，文件：{}", filePath);
        } finally {
            try {
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                log.info("excel文件读取失败，失败原因：{}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 传入java对象默认Sheet生成Excel
     *
     * @param filePath 生成表格的存放路径 -> 绝对路径
     * @param data     数据源
     */
    public <T> void writeBySimple(String filePath, List<T> data, List<String> head) {
        writeSimpleBySheet(filePath, data, head, null);
    }


    /**
     * 传入java对象自定义Sheet对象生成Excel
     *
     * @param filePath 生成表格的存放路径 -> 绝对路径，如：/opt/download/document/excel/userInfo.xlsx
     * @param data     数据源
     * @param head     自定义Excel Header名
     * @param sheet    sheet excel 页面样式
     */
    public <T> void writeSimpleBySheet(String filePath, List<T> data, List<String> head, Sheet sheet) {
        sheet = sheet != null ? sheet : initSheet;

        // 自定义Excel表 Header
        if (head != null) {
            List<List<String>> list = new ArrayList<>();
            head.forEach(h -> list.add(Collections.singletonList(h)));
            sheet.setHead(list);
        }

        if (data != null && data.size() > 0) {
            Field[] fields = data.get(0).getClass().getDeclaredFields();
            final List<List<String>> list = new ArrayList<List<String>>();
            List<String> names = new ArrayList<>();
            for (Field field : fields) {
                list.add(Collections.singletonList(field.getName()));
                names.add(field.getName());
            }
            // 未自定义Excel表Header 就采用Java对象的属性名
            if (head == null) {
                sheet.setHead(list);
            }
            OutputStream outputStream = null;
            ExcelWriter writer = null;
            try {
                outputStream = new FileOutputStream(filePath);
                writer = EasyExcelFactory.getWriter(outputStream);
                List<List<Object>> realData = new ArrayList<>();
                for (T t : data) {
                    List<Object> objects = new ArrayList<>();
                    for (String name : names) {
                        Field field = t.getClass().getDeclaredField(name);
                        // 对private的属性访问
                        field.setAccessible(true);
                        objects.add(field.get(t));
                    }
                    realData.add(objects);
                }
                writer.write1(realData, sheet);
            } catch (FileNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                log.info("找不到文件或文件路径错误，文件：{}", filePath);
            } finally {
                try {
                    if (writer != null) {
                        writer.finish();
                    }
                    if (outputStream != null) {

                        outputStream.close();
                    }
                } catch (IOException e) {
                    log.info("excel文件导出失败，失败原因：{}", e.getMessage());
                }
            }
        }
    }


    /**
     * 生成Excel，带用模型，默认Sheet
     *
     * @param filePath 生成表格的存放路径 -> 绝对路径，如：/opt/download/document/excel/userInfo.xlsx
     * @param data     数据源
     */
    public void writeWithTemplate(String filePath, List<? extends BaseRowModel> data) {
        writeWithTemplateAndSheet(filePath, data, null);
    }

    /**
     * 生成Excel，带用模型，自定义Sheet
     *
     * @param filePath 生成表格的存放路径 -> 绝对路径，如：/opt/download/document/excel/userInfo.xlsx
     * @param data     数据源【可自定义Excel表属性-> 字体、格式等】
     * @param sheet    Excel页面样式
     */
    public void writeWithTemplateAndSheet(String filePath, List<? extends BaseRowModel> data, Sheet sheet) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }

        sheet = sheet != null ? sheet : initSheet;
        sheet.setClazz(data.get(0).getClass());

        OutputStream outputStream = null;
        ExcelWriter writer = null;
        try {
            outputStream = new FileOutputStream(filePath);
            writer = EasyExcelFactory.getWriter(outputStream);
            writer.write(data, sheet);
        } catch (FileNotFoundException e) {
            log.info("找不到文件或文件路径错误，文件：{}", filePath);
        } finally {
            try {
                if (writer != null) {
                    writer.finish();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.info("excel文件导出失败，失败原因：{}", e.getMessage());
            }
        }
    }

    /**
     * 生成Excel，带用模型，多个Sheet
     * @param filePath 生成表格的存放路径 -> 绝对路径，如：/opt/download/document/excel/userInfo.xlsx
     * @param multipleSheetProperties 数据源
     */
    public void writeWithMultipleSheet(String filePath, List<MultipleSheetProperty> multipleSheetProperties) {
        if (CollectionUtils.isEmpty(multipleSheetProperties)) {
            return;
        }

        OutputStream outputStream = null;
        ExcelWriter writer = null;
        try {
            outputStream = new FileOutputStream(filePath);
            writer = EasyExcelFactory.getWriter(outputStream);
            for (MultipleSheetProperty multipleSheetProperty : multipleSheetProperties) {
                Sheet sheet = multipleSheetProperty.getSheet() != null ? multipleSheetProperty.getSheet() : initSheet;
                if (!CollectionUtils.isEmpty(multipleSheetProperty.getData())) {
                    sheet.setClazz(multipleSheetProperty.getData().get(0).getClass());
                }
                writer.write(multipleSheetProperty.getData(), sheet);
            }
        } catch (FileNotFoundException e) {
            log.info("找不到文件或文件路径错误，文件：{}", filePath);
        } finally {
            try {
                if (writer != null) {
                    writer.finish();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.info("excel文件导出失败，失败原因：{}", e.getMessage());
            }
        }

    }


    /************************************ 匿名内部类开始，可以提取出去 **********************************/
    @Data
    public class MultipleSheetProperty {
        private List<? extends BaseRowModel> data;

        private Sheet sheet;
    }

    @Getter
    @Setter
    public class ExcelListener extends AnalysisEventListener {

        private List<Object> dataList = new ArrayList<>();

        /**
         * 逐行解析
         *
         * @param object
         * @param context
         */
        @Override
        public void invoke(Object object, AnalysisContext context) {
            if (object != null) {
                dataList.add(object);
            }
        }

        /**
         * 解析完所有数据后会调用这个方法
         *
         * @param context
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 解析结束销毁不用的资源
        }
    }
    /************************匿名内部类结束，可以提取出去***************************/

    public void main(String[] args) throws Exception {
        // 测试读取少于1000行的Excel
        testReadLessThan1000Row();
        // 测试读取多于1000行的Excel
        //testReadMoreThan1000Row();
        // 测试传入java对象默认Sheet生成Excel
        //testWriteBySimple();
        // 测试生成Excel，带用模型
        //testWriteWithTemplate();
        // 测试生成Excel，多个Sheet
        //writeWithMultipleSheet();
    }
    public void testReadLessThan1000Row() {
        String filePath = "D:\\project\\practice\\java\\document\\easyExcel\\userInfo.xlsx";
        List<UserInfoDto> userInfoDtoList = readLessThan1000Row(UserInfoDto.class, filePath);
        userInfoDtoList.forEach(System.out::println);
    }


    public void testReadMoreThan1000Row() {
        String filePath = "D:\\project\\practice\\java\\document\\easyExcel\\userInfo.xlsx";
        List<UserInfoDto> userInfoDtoList = readMoreThan1000Row(UserInfoDto.class, filePath);
        userInfoDtoList.forEach(System.out::println);
    }


    public void testWriteBySimple() {
        String filePath = "D:\\project\\practice\\java\\document\\easyExcel\\inUserInfo.xlsx";
        List<UserInfoDto> list = new ArrayList<>();
        list.add(new UserInfoDto("路飞", 16, "草帽海贼团"));
        list.add(new UserInfoDto("索隆", 18, "草帽海贼团"));
        list.add(new UserInfoDto("娜美", 17, "草帽海贼团"));
        list.add(new UserInfoDto("罗宾", 15, "草帽海贼团"));
        list.add(new UserInfoDto("乔巴", 12, "草帽海贼团"));
        List<String> head = new ArrayList<>();
        head.add("姓名");
        head.add("年龄");
        head.add("学校");
        // 默认使用Java对象属性名作为Excel表的Header
        //writeBySimple(filePath, list,null);
        // 自定义Excel表Header，注意要顺序要一一对应
        writeBySimple(filePath, list, head);
    }


    public void testWriteWithTemplate() {
        String filePath = "D:\\project\\practice\\java\\document\\easyExcel\\生成Excel带用模型.xlsx";
        List<TableHeaderExcelProperty> data = new ArrayList<>();
        // 这里可以使用 EntityPropertyUtility 方法中的CopyNotNull方法直接将对象数据copy进这里
        for (int i = 0; i < 4; i++) {
            TableHeaderExcelProperty tableHeaderExcelProperty = new TableHeaderExcelProperty();
            tableHeaderExcelProperty.setName("cmlx" + i);
            tableHeaderExcelProperty.setAge(22 + i);
            tableHeaderExcelProperty.setSchool("清华大学" + i);
            data.add(tableHeaderExcelProperty);
        }
        writeWithTemplate(filePath, data);
    }


    public void writeWithMultipleSheet() {
        List<MultipleSheetProperty> multipleList = new ArrayList<>();
        for (int j = 1; j < 4; j++) {
            List<TableHeaderExcelProperty> list = new ArrayList<>();
            // 这里可以使用 EntityPropertyUtility 方法中的CopyNotNull方法直接将对象数据copy进这里
            for (int i = 0; i < 4; i++) {
                TableHeaderExcelProperty tableHeaderExcelProperty = new TableHeaderExcelProperty();
                tableHeaderExcelProperty.setName("cmlx" + i);
                tableHeaderExcelProperty.setAge(22 + i);
                tableHeaderExcelProperty.setSchool("清华大学" + i);
                list.add(tableHeaderExcelProperty);
            }
            Sheet sheet = new Sheet(j, 0);
            sheet.setSheetName("sheet" + j);
            MultipleSheetProperty multipleSheelPropety = new MultipleSheetProperty();
            multipleSheelPropety.setData(list);
            multipleSheelPropety.setSheet(sheet);

            multipleList.add(multipleSheelPropety);
        }
        String filePath = "D:\\project\\practice\\java\\document\\easyExcel\\生成Excel带用模型和多个Sheet.xlsx";
        writeWithMultipleSheet(filePath, multipleList);
    }

    /***************************** 匿名内部类，这里为了测试方便，实际开发中该对象要提取出去************************/
    @Data
    @EqualsAndHashCode(callSuper = true)
    public class TableHeaderExcelProperty extends BaseRowModel {

        @ExcelProperty(value = "姓名", index = 0)
        private String name;

        @ExcelProperty(value = "年龄", index = 1)
        private Integer age;

        @ExcelProperty(value = "学校", index = 2)
        private String school;

    }
}

