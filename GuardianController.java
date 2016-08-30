package com.anhry.app.ansafety.manage.infos.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anhry.app.ansafety.dao.UtilDao;
import com.anhry.app.ansafety.env.Constants;
import com.anhry.app.ansafety.manage.infos.bean.InfosConstants;
import com.anhry.app.ansafety.manage.infos.bean.TbGuardian;
import com.anhry.app.ansafety.manage.infos.service.GuardianService;
import com.anhry.app.ansafety.manage.system.bean.TbRole;
import com.anhry.app.ansafety.manage.system.service.RoleTbService;
import com.anhry.app.ansafety.web.BaseController;
import com.anhry.app.util.bean.JsonView;
import com.anhry.app.util.bean.QueryResult;
import com.anhry.app.util.bean.QuiPager;
import com.anhry.app.util.log.Logs;

@Controller
@RequestMapping("/infos/guardian")
public class GuardianController extends BaseController {

	@Resource 
	private UtilDao utilDao;	
	
	@Resource 
	private GuardianService guardianService;
	@Resource
	private RoleTbService roleTbService;
	
	@RequestMapping("/list")
	public String toList(Model model){
		return "infos/guardian_list";
	}
	
	@RequestMapping("/json")
	@ResponseBody
	public Map<String, Object> lawJson(QuiPager qp){
		QueryResult<TbGuardian> guas = this.guardianService.findAll(qp);
		guas.setPager(qp.getPager());
		return this.queryResultToMap(guas, qp);
	}
	
	
	/**
	/**
	 * 
	 * 方法描述 :跳转至新增页面
	 *
	 * @param model
	 * @param tabfrmId
	 * @return
	 * 创建人 :  xiongzhenghai
	 * 创建时间: 2016年8月12日 上午11:43:48
	 *
	 */
	@RequiresPermissions("infos_guardian:add")
	@RequestMapping("/add")
	public String add(){
		return "infos/guardian_edit";
	}
	
	/**
	 * 
	 * 方法描述 :跳转至修改页面
	 *
	 * @param model
	 * @param id
	 * @param tabfrmId
	 * @return
	 * 创建人 :  yangzhuo
	 * 创建时间: 2016年8月12日 上午11:46:58
	 *
	 */
	@RequiresPermissions("infos_guardian:edit")
	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable Integer id,Model model){
		TbGuardian guardian = null;
		try{
			guardian = guardianService.get(id);
		}catch(Exception e){
			LOG.info(e.getMessage());
		}
		model.addAttribute("guardian",guardian);
		return "infos/guardian_edit";
	}
	/**
	 * 
	 * 方法描述 :跳转到监护人信息查看页面
	 *
	 * @param id
	 * @param model
	 * @return
	 * 创建人 :  yangzhuo
	 * 创建时间: 2016年8月12日 下午4:04:58
	 *
	 */
	@RequestMapping("/look/{id}")
	public String look(@PathVariable Integer id,Model model){
		model.addAttribute("guardian",guardianService.get(id));
		return "infos/guardian_look";
	}
	
	/**
	 * 
	 * 方法描述 :添加/修改学生信息并记录日志
	 *
	 * @param student
	 * @param content
	 * @return
	 * 创建人 :  xiongzhenghai
	 * 创建时间: 2016年8月12日 下午3:07:08
	 *
	 */
	@RequestMapping("/save")
	@ResponseBody  
	public JsonView save(TbGuardian guardian, String content){
		JsonView jv = new JsonView();
		if (guardian.getId() != null) {// 修改
			try {
				this.guardianService.saveGuardian(guardian);
				jv.setMsg("修改资助人信息成功！");
				jv.setSuccess(true);
				Logs.info("修改资助人信息id : " + guardian.getId());
			} catch (Exception e) {
				Logs.error("修改资助人信息失败 ----> " + e.getMessage());
				jv.setMsg(Constants.UPDATE_TIP_FAILED);
				jv.setSuccess(false);
			}
		} else {// 新增
			try {
				
				String userType = null;
				TbRole role = null;
				if(InfosConstants.GUARDIAN_IS_CHIEF.equals(guardian.getIsChief())){
					userType = InfosConstants.USER_TYPE_SITE_CHIEF;
					role = roleTbService.getByCode(InfosConstants.ROLE_CODE_SITE_CHIEF);
				}else{
					userType = InfosConstants.USER_TYPE_GUARDIAN;
					role = roleTbService.getByCode(InfosConstants.ROLE_CODE_GUARDIAN);
				}
				
				guardian.setUserType(userType);
				if(role != null){
					guardian.setRoleIds(role.getId().toString());
				}
				this.guardianService.saveGuardian(guardian);
				jv.setSuccess(true);
				jv.setMsg("添加资助人信息成功！");
				Logs.info("添加资助人信息id : " + guardian.getId());
			} catch (Exception e) {
				Logs.error("添加资助人信息失败 ----> " + e.getMessage());
				jv.setMsg("添加资助人信息失败！");
				jv.setSuccess(false);
			}
		}
		return jv;
	}
	
	

	/**
	 * 
	 * 方法描述 :删除资助人信息
	 *
	 * @param ids
	 * @return
	 * 创建人 :  xiongzhenghai
	 * 创建时间: 2016年8月12日 下午4:01:51
	 *
	 */
	@RequiresPermissions("infos_student:del")
	@RequestMapping("/delete/{ids}")
	@ResponseBody
	public JsonView delete(@PathVariable String ids){
		JsonView jv = new JsonView();
		try{
			this.guardianService.delGuardians(ids);
			jv.setMsg("删除资助人成功");
			jv.setSuccess(true);
		}catch(Exception e){
			jv.setMsg("删除资助人失败");
			jv.setSuccess(false);
			e.printStackTrace();
			LOG.error("资助人信息 - 删除失败:"+e.getMessage());
		}
		Logs.info("删除资助人信息ids : " + ids);
		return jv;
	}
	
}
