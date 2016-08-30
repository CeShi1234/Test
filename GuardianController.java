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
	 * 閺傝纭堕幓蹇氬牚 :鐠哄疇娴嗛懛铏煀婢х偤銆夐棃锟�
	 *
	 * @param model
	 * @param tabfrmId
	 * @return
	 * 閸掓稑缂撴禍锟� :  xiongzhenghai
	 * 閸掓稑缂撻弮鍫曟？: 2016楠烇拷8閺堬拷12閺冿拷 娑撳﹤宕�11:43:48
	 *
	 */
	@RequiresPermissions("infos_guardian:add")
	@RequestMapping("/add")
	public String add(){
		return "infos/guardian_edit";
	}
	
	/**
	 * 
	 * 閺傝纭堕幓蹇氬牚 :鐠哄疇娴嗛懛鍏呮叏閺�褰掋�夐棃锟�
	 *
	 * @param model
	 * @param id
	 * @param tabfrmId
	 * @return
	 * 閸掓稑缂撴禍锟� :  yangzhuo
	 * 閸掓稑缂撻弮鍫曟？: 2016楠烇拷8閺堬拷12閺冿拷 娑撳﹤宕�11:46:58
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
	 * 閺傝纭堕幓蹇氬牚 :鐠哄疇娴嗛崚鎵磧閹躲倓姹夋穱鈩冧紖閺屻儳婀呮い鐢告桨
	 *
	 * @param id
	 * @param model
	 * @return
	 * 閸掓稑缂撴禍锟� :  yangzhuo
	 * 閸掓稑缂撻弮鍫曟？: 2016楠烇拷8閺堬拷12閺冿拷 娑撳宕�4:04:58
	 *
	 */
	@RequestMapping("/look/{id}")
	public String look(@PathVariable Integer id,Model model){
		model.addAttribute("guardian",guardianService.get(id));
		return "infos/guardian_look";
	}
	
	/**
	 * 
	 * 閺傝纭堕幓蹇氬牚 :濞ｈ濮�/娣囶喗鏁肩�涳妇鏁撴穱鈩冧紖楠炴儼顔囪ぐ鏇熸）韫囷拷
	 *
	 * @param student
	 * @param content
	 * @return
	 * 閸掓稑缂撴禍锟� :  xiongzhenghai
	 * 閸掓稑缂撻弮鍫曟？: 2016楠烇拷8閺堬拷12閺冿拷 娑撳宕�3:07:08
	 *
	 */
	@RequestMapping("/save")
	@ResponseBody  
	public JsonView save(TbGuardian guardian, String content){
		JsonView jv = new JsonView();
		if (guardian.getId() != null) {// 娣囶喗鏁�
			try {
				this.guardianService.saveGuardian(guardian);
				jv.setMsg("娣囶喗鏁肩挧鍕И娴滆桨淇婇幁顖涘灇閸旂噦绱�");
				jv.setSuccess(true);
				Logs.info("娣囶喗鏁肩挧鍕И娴滆桨淇婇幁鐥燿 : " + guardian.getId());
			} catch (Exception e) {
				Logs.error("娣囶喗鏁肩挧鍕И娴滆桨淇婇幁顖氥亼鐠愶拷 ----> " + e.getMessage());
				jv.setMsg(Constants.UPDATE_TIP_FAILED);
				jv.setSuccess(false);
			}
		} else {// 閺傛澘顤�
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
				jv.setMsg("濞ｈ濮炵挧鍕И娴滆桨淇婇幁顖涘灇閸旂噦绱�");
				Logs.info("濞ｈ濮炵挧鍕И娴滆桨淇婇幁鐥燿 : " + guardian.getId());
			} catch (Exception e) {
				Logs.error("濞ｈ濮炵挧鍕И娴滆桨淇婇幁顖氥亼鐠愶拷 ----> " + e.getMessage());
				jv.setMsg("濞ｈ濮炵挧鍕И娴滆桨淇婇幁顖氥亼鐠愩儻绱�");
				jv.setSuccess(false);
			}
		}
		return jv;
	}
	
	

	/**
	 * 
	 * 閺傝纭堕幓蹇氬牚 :閸掔娀娅庣挧鍕И娴滆桨淇婇幁锟�
	 *
	 * @param ids
	 * @return
	 * 閸掓稑缂撴禍锟� :  xiongzhenghai
	 * 閸掓稑缂撻弮鍫曟？: 2016楠烇拷8閺堬拷12閺冿拷 娑撳宕�4:01:51
	 *
	 */
	@RequiresPermissions("infos_student:del")
	@RequestMapping("/delete/{ids}")
	@ResponseBody
	public JsonView delete(@PathVariable String ids){
		JsonView jv = new JsonView();
		try{
			this.guardianService.delGuardians(ids);
			jv.setMsg("閸掔娀娅庣挧鍕И娴滅儤鍨氶崝锟�");
			jv.setSuccess(true);
		}catch(Exception e){
			jv.setMsg("閸掔娀娅庣挧鍕И娴滃搫銇戠拹锟�");
			jv.setSuccess(false);
			e.printStackTrace();
			LOG.error("鐠у嫬濮禍杞颁繆閹拷 - 閸掔娀娅庢径杈Е:"+e.getMessage());
		}
		Logs.info("閸掔娀娅庣挧鍕И娴滆桨淇婇幁鐥燿s : " + ids);
		return jv;
	}
	
}
