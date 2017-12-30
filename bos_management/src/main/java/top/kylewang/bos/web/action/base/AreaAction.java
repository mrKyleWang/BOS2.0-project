package top.kylewang.bos.web.action.base;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import top.kylewang.bos.domain.base.Area;
import top.kylewang.bos.service.base.AreaService;
import top.kylewang.bos.utils.PinYin4jUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Kyle.Wang
 * 2017/12/30 0030 16:53
 */
@Controller
@Scope("prototype")
@Actions
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends ActionSupport implements ModelDriven<Area> {

    @Autowired
    private AreaService areaService;

    private Area area;

    @Override
    public Area getModel() {
        return area;
    }

    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    @Action(value = "area_batchImport")
    public String batchImport() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = workbook.getSheetAt(0);
        List<Area> list = new ArrayList<>();
        for (Row cells : sheet) {
            if (cells.getRowNum() == 0) {
                continue;
            }
            if (cells.getCell(0) == null || StringUtils.isBlank(cells.getCell(0).getStringCellValue())) {
                continue;
            }
            Area area = new Area();
            area.setId(cells.getCell(0).getStringCellValue());
            area.setProvince(cells.getCell(1).getStringCellValue());
            area.setCity(cells.getCell(2).getStringCellValue());
            area.setDistrict(cells.getCell(3).getStringCellValue());
            area.setPostcode(cells.getCell(4).getStringCellValue());

            //去掉"省","市","区" 后缀
            String province = area.getProvince().substring(0, area.getProvince().length() - 1);
            String city = area.getCity().substring(0, area.getCity().length() - 1);
            String district = area.getDistrict().substring(0, area.getDistrict().length() - 1);

            //简码
            StringBuffer stringBuffer = new StringBuffer();
            String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
            for (String head : headArray) {
                stringBuffer.append(head);
            }
            String shortCode = stringBuffer.toString();
            area.setShortcode(shortCode);

            //城市编码
            String cityCode = PinYin4jUtils.hanziToPinyin(city, "");
            area.setCitycode(cityCode);
            list.add(area);
        }
        areaService.saveBatch(list);
        return null;
    }

}
