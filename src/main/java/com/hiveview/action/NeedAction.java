package com.hiveview.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.hiveview.action.base.BaseController;
import com.hiveview.entity.ApprovalRecord;
import com.hiveview.entity.Need;
import com.hiveview.entity.Paging;
import com.hiveview.entity.bo.Data;
import com.hiveview.service.IApprovalRecordService;
import com.hiveview.service.INeedService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import utils.IssueType;
import utils.log.LogMgr;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/need")
public class NeedAction extends BaseController {

    @Autowired
    private INeedService needService;
    @Autowired
    private IApprovalRecordService approvalRecordService;


    @RequestMapping(value="/list")
    public String list() {
        return "need/need_list";
    }

    /**
     * 去审批
     * @param id
     * @param mav
     * @return
     */
    @RequestMapping(value="/toApproval/{id}")
    public ModelAndView toApproval(@PathVariable("id") long id,ModelAndView mav) {
        Need need = needService.getNeedById(id);
        mav.getModel().put("need", need);
        mav.setViewName("need/approval");
        return mav;
    }
    @RequestMapping(value="/page")
    public ModelAndView page(HttpServletRequest request, ModelAndView mav) {
        Paging paging = super.getPaging(request);
        Need need = new Need();
        String status = request.getParameter("status");
        if (StringUtil.isNotEmpty(status)) {
                need.setStatus(Integer.parseInt(status));
        }
        Page<Object> page = PageHelper.startPage(paging.getCurrentPage(), paging.getPageSize());
        List<Need> needs =  needService.getNeedPage(need);
        paging.setTotalPages(page.getPages());
        mav.getModel().put("paging",paging);
        mav.getModel().put("needs",needs);
        mav.setViewName("need/paging");
        return mav;
    }


    /**
     * 会员留言管理入口页面地址
     * @return
     */
    @RequestMapping(value="/toUserNeedList")
    public String toUserNeedList() {
        return "userNeed/userneed_list";
    }

    /**
     * 会员留言管理入口（删除操作）
     * @return
     */
    @RequestMapping(value="/deleteUserNeedByIds")
    public @ResponseBody  Data deleteUserNeedByIds(HttpServletRequest req,String ids) {
        List<Integer> idList = new ArrayList<Integer>();
        String []idArray = ids.split(",");
        for (String id : idArray){
            idList.add(Integer.parseInt(id));
        }
        needService.deleteUserNeedByIds(idList);
        return new Data(200,"success");
    }

    
    /**
     *  @功能:删除用户需求信息
     *  @作者:李文辉 
     *  @代号:ab
     *  @时间:2017年5月22日
     *  @param req
     *  @param id
     *  @return  
     */
    @RequestMapping(value="/deleteUserNeedById")
    @ResponseBody 
    public String deleteUserNeedById(HttpServletRequest req,Integer id) {
    	Data data=new Data(500, "error");
    	int deleteUserNeedByPrimaryKey = needService.deleteUserNeedByPrimaryKey(id);
    	if(deleteUserNeedByPrimaryKey>0){
    		data.setCode(200);
    		data.setMsg("success");
    	}
    	ObjectMapper mapper=new ObjectMapper();
    	String writeValueAsString=null;
    	try {
			 writeValueAsString = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return writeValueAsString;
    }
    

    /**
     * 会员留言列表
     * @param request
     * @param mav
     * @return
     */
    @RequestMapping(value="/userNeedPage")
    public ModelAndView userNeedPage(HttpServletRequest request, ModelAndView mav) {
        Paging paging = super.getPaging(request);
        Page<Object> page = PageHelper.startPage(paging.getCurrentPage(), paging.getPageSize());
        Map<String,Object> map = new HashMap<String,Object>();
        List<Map<String,Object>> needs =  needService.getLiuYanList(map);
        paging.setTotalPages(page.getPages());
        mav.getModel().put("paging",paging);
        mav.getModel().put("needs",needs);
        mav.setViewName("userNeed/userneed_paging");
        return mav;
    }

    /**
     * 审批记录列表
     * @param request
     * @param mav
     * @return
     */
    @RequestMapping(value="/approvalPage")
    public ModelAndView approvalPage(HttpServletRequest request, ModelAndView mav) {
        Paging paging = super.getPaging(request);
        String relateId = request.getParameter("relateId");
        if (StringUtils.isNotEmpty(relateId)) {
            ApprovalRecord approvalRecord = new ApprovalRecord();
            approvalRecord.setType(IssueType.NEED.getVal());
            approvalRecord.setRelateId(Long.parseLong(relateId));
            Page<Object> page = PageHelper.startPage(paging.getCurrentPage(), paging.getPageSize());
            List<ApprovalRecord> approvalRecords =  approvalRecordService.getApprovalList(approvalRecord);
            paging.setTotalPages(page.getPages());
            mav.getModel().put("paging",paging);
            mav.getModel().put("approvalRecords",approvalRecords);
        }
        mav.setViewName("need/approvalPage");
        return mav;
    }

    /**
     * 添加审批记录
     * @param request
     * @param approvalRecord
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/approval")
    public Boolean approval(HttpServletRequest request,ApprovalRecord approvalRecord) {
        Boolean flag = false;
        Integer status = approvalRecord.getStatus();
        if (approvalRecord.getRelateId() != null  && status != null && status >0) {
            try {
                approvalRecord.setAddTime(new Date());
                approvalRecord.setType(IssueType.NEED.getVal());
                approvalRecord.setOperationId(super.getSysUserId(request));
                approvalRecordService.saveApproval(approvalRecord);
                Need need = new Need();
                need.setId(approvalRecord.getRelateId());
                need.setStatus(status);
                needService.updateNeed(need);
                flag = true;
            } catch (Exception e) {
                LogMgr.writeErrorLog(e);
            }
        }
        return flag;
    }
    @ResponseBody
    @RequestMapping(value="/operation")
    public Boolean operation(Need need) {
        Boolean flag = false;
        if (need.getId() != null) {
            try {
                need.setUpdateTime(new Date());
                needService.updateNeed(need);
                flag = true;
            } catch (Exception e) {
                LogMgr.writeErrorLog(e);
            }
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
        Need need = needService.getNeedById(id);
        mav.getModel().put("need", need);
        mav.setViewName("need/setting");
        return mav;
    }


}
