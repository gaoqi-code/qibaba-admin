/**
 * 代号:ab
 * 文件名：CompanyAction.java
 * 创建人：李文辉
 * 日期：2017年5月22日
 * 修改人：
 * 描述：
 */
package com.hiveview.action;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hiveview.action.base.BaseController;
import com.hiveview.entity.Area;
import com.hiveview.entity.Company;
import com.hiveview.entity.Paging;
import com.hiveview.service.IAreaService;
import com.hiveview.service.ICompanyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import utils.log.LogMgr;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 用途：公司信息管理
 */
@RequestMapping("/company")
@Controller
public class CompanyAction extends BaseController{
	 	@Autowired
	    private ICompanyService companyService;
	 	@Autowired
	 	private IAreaService areaService;
	 	
	 
	 /**
	     *  @功能:跳转到公司列表页面
	     *  @作者:李文辉 
	     *  @代号:ab
	     *  @时间:2017年5月22日
	     *  @return  
	     */
	    @RequestMapping("/sendCompanyPage")
	    public String sendCompanyPage(){
	    	return "company/company_list";
	    }
	    
	    /**
	     *  @功能:查询全部公司列表并分页
	     *  @作者:李文辉 
	     *  @代号:ab
	     *  @时间:2017年5月22日
	     *  @param request
	     *  @param mav
	     *  @return  
	     */
	    @RequestMapping("/companyListData")
	    public ModelAndView queryCompanyListData(HttpServletRequest request,ModelAndView mav){
	    	Paging paging=super.getPaging(request);
	    	Page<Object> page = PageHelper.startPage(paging.getCurrentPage(), paging.getPageSize());
	    	List<Company> companys = companyService.queryCompanyList();
	    	paging.setTotalPages(page.getPages());
	    	mav.getModel().put("paging", paging);
	    	mav.getModel().put("companys", companys);
	    	mav.setViewName("company/company_paging");
	    	return mav;
	    }
	    
	    /**
	     *  @功能:通过公司名称查询公司信息,模糊查询并分页
	     *  @作者:李文辉 
	     *  @代号:ab
	     *  @时间:2017年5月23日
	     *  @param request
	     *  @param mav
	     *  @param companyName
	     *  @return  
	     */
	    @RequestMapping("/queryCompanyByName")
	    public ModelAndView queryCompanyByName(HttpServletRequest request,ModelAndView mav,String companyName){
	    	Paging paging = super.getPaging(request);
	    	Page<Object> page = PageHelper.startPage(paging.getCurrentPage(), paging.getPageSize());
	    	List<Company> companys = companyService.queryCompanyListByName(companyName);
	    	paging.setTotalPages(page.getPages());
	    	mav.getModel().put("paging", paging);
	    	mav.getModel().put("companys", companys);
	    	mav.setViewName("company/company_paging");
	    	return mav;
	    }
	    
	    /**
	     * 去设置页面
	     * @param id
	     * @param mav
	     * @return
	     */
	    @RequestMapping(value="/toSetting/{id}")
	    public ModelAndView toSetting(@PathVariable("id") long id,ModelAndView mav) {
	    	Company company = companyService.selectByPrimaryKey(id);
//	    	List<Area> area = areaService.queryAllArea();

			List<Area> oneLevelAreas = areaService.getAllOneLevel();
			String areaCode = company.getAreaCode();
			if (StringUtils.isNotEmpty(areaCode)) {
				Area selectArea = new Area();
				List<Area> twoLevelAreas;
				List<Area> threeLevelAreas;
				String[] areas = areaCode.split("-");
				for (int i = 0; i < areas.length; i++) {
					Long areaId = Long.parseLong(areas[i]);
					if (i == 0) {
						twoLevelAreas = areaService.getSonAreas(areaId);
						mav.getModel().put("twoLevelAreas", twoLevelAreas);
						selectArea.setOneLevel(areaId);
					}
					if (i == 1) {
						threeLevelAreas = areaService.getSonAreas(areaId);
						mav.getModel().put("threeLevelAreas", threeLevelAreas);
						selectArea.setTwoLevel(areaId);
					}
					if (i == 2) {
						selectArea.setThreeLevel(areaId);
					}
				}
				mav.getModel().put("selectArea", selectArea);
			}

			mav.getModel().put("oneLevelAreas", oneLevelAreas);
	        mav.getModel().put("company", company);
//	        mav.getModel().put("areas", area);
	        mav.setViewName("company/setting");
	        return mav;
	    }
	
	    @ResponseBody
	    @RequestMapping(value="/operation")
	    public Boolean operation(Company company) {
	        Boolean flag = false;
	        if (company.getId() != null) {
	            try {
	            	company.setUpdateTime(new Date());
	                companyService.updateByPrimaryKeySelective(company);
	                flag = true;
	            } catch (Exception e) {
	                LogMgr.writeErrorLog(e);
	            }
	        }
	        return flag;
	    }

	    
}
