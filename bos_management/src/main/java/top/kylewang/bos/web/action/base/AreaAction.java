package top.kylewang.bos.web.action.base;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import top.kylewang.bos.domain.base.Area;
import top.kylewang.bos.service.base.AreaService;
import top.kylewang.bos.utils.PinYin4jUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private Area area = new Area();

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


    private Integer page;
    private Integer rows;

    public void setPage(Integer page) {
        this.page = page;
    }
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    /**
     * 条件分页查询
     * @return
     */
    @Action(value = "area_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        Pageable pageable = new PageRequest(page - 1, rows);
        Specification<Area> specification = new Specification<Area>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (StringUtils.isNotBlank(area.getProvince())) {
                    Predicate predicate1 = criteriaBuilder.like(root.get("province").as(String.class), "%" + area.getProvince() + "%");
                    list.add(predicate1);
                }
                if (StringUtils.isNotBlank(area.getCity())) {
                    Predicate predicate2 = criteriaBuilder.like(root.get("city").as(String.class), "%" + area.getCity() + "%");
                    list.add(predicate2);
                }
                if (StringUtils.isNotBlank(area.getDistrict())) {
                    Predicate predicate3 = criteriaBuilder.like(root.get("district").as(String.class), "%" + area.getDistrict() + "%");
                    list.add(predicate3);
                }
                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        };
        Page<Area> pageData = areaService.findPageData(specification,pageable);
        Map<String,Object> result = new HashMap<>(4);
        result.put("total",pageData.getNumberOfElements());
        result.put("rows",pageData.getContent());
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;


    }

}
