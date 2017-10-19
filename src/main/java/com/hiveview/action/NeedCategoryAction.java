package com.hiveview.action;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.hiveview.action.base.BaseController;
import com.hiveview.entity.Category;
import com.hiveview.entity.Paging;
import com.hiveview.service.ICategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import utils.IssueType;
import utils.StatusUtil;
import utils.log.LogMgr;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/needCategory")
public class NeedCategoryAction extends BaseController {

    @Autowired
    private ICategoryService categoryService;


    @RequestMapping(value="/list")
    public String list() {
        return "needCategory/category_list";
    }

    @RequestMapping(value="/page")
    public ModelAndView page(HttpServletRequest request, ModelAndView mav) {
        Paging paging = super.getPaging(request);
        Category category = new Category();
        String categoryName = request.getParameter("categoryName");
        if (StringUtil.isNotEmpty(categoryName)) {
                category.setName(categoryName);
        }
        String categoryLevel = request.getParameter("categoryLevel");
        if (StringUtil.isNotEmpty(categoryLevel)) {
                category.setLevel(Integer.parseInt(categoryLevel));
        }
        Page<Object> page = PageHelper.startPage(paging.getCurrentPage(), paging.getPageSize());
        category.setType(IssueType.NEED.getVal());
        List<Category> categorys =  categoryService.getCategory(category);
        paging.setTotalPages(page.getPages());
        mav.getModel().put("paging",paging);
        mav.getModel().put("categorys",categorys);
        mav.setViewName("needCategory/paging");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value="/operation")
    public Boolean operation(Category category) {
        Boolean flag = false;
        if (category.getId() != null) {
            try {
                if (!(category.getOldName().equals(category.getName()))
                        && categoryService.checkCategoryNameRepetition(category.getName(), category.getType())) {
                    return flag;
                }
                category.setUpdateTime(new Date());
                categoryService.updateCategoryAndAttr(category);
                flag = true;
            } catch (Exception e) {
                LogMgr.writeErrorLog(e);
            }
        }
        return flag;
    }
    @ResponseBody
    @RequestMapping(value="/delete")
    public Boolean delete(HttpServletRequest request) {
        Boolean flag = false;
        String code = request.getParameter("code");
        if (StringUtils.isNotEmpty(code)) {
            try {
                Category category = new Category();
                category.setCode(code);
                category.setStatus(StatusUtil.INVALID.getVal());
                category.setUpdateTime(new Date());
                categoryService.updateByCode(category);
//                List<Category> categorys = categoryService.getCategory(category);
//                if (CollectionUtils.isNotEmpty(categorys)) {
//                    List<Long> ids = categorys.stream().map(category1 -> category1.getId()).collect(Collectors.toList());
//                    categoryService.batchDelete(ids);
//                }
                flag = true;
            } catch (Exception e) {
                LogMgr.writeErrorLog(e);
            }
        }
        return flag;
    }

    /**
     * 去添加类目
     * @param mav
     * @return
     */
    @RequestMapping(value="/toAdd/{parentId}")
    public ModelAndView toAdd(ModelAndView mav,@PathVariable("parentId") Long parentId) {
        if (parentId > 0 ) {
            Category parentCategory = categoryService.getCategoryById(parentId);
            mav.getModel().put("parentCategory", parentCategory);
        }
        mav.setViewName("needCategory/category_add");
        return mav;
    }
    /**
     * 去添加类目
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/addCategory")
    public Boolean addCategory(Category category) {
        Boolean flag = false;
        try {
            category.setType(IssueType.NEED.getVal());
            categoryService.saveCategory(category);
            flag = true;
        } catch (Exception e) {
            LogMgr.writeErrorLog(e);
        }
        return flag;
    }
    /**
     * 去设置页面
     * @param id
     * @param mav
     * @return
     */
    @RequestMapping(value="/toSetting/{id}")
    public ModelAndView toSetting(@PathVariable("id") long id,ModelAndView mav) {
        Category category = categoryService.getCategoryAndAttr(id);
        mav.getModel().put("category", category);
        mav.setViewName("needCategory/setting");
        return mav;
    }

}
