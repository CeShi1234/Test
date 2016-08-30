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
	 * 鏂规硶鎻忚堪 :璺宠浆鑷虫柊澧為〉闈�
	 *
	 * @param model
	 * @param tabfrmId
	 * @return
	 * 鍒涘缓浜� :  xiongzhenghai
	 * 鍒涘缓鏃堕棿: 2016骞�8鏈�12鏃� 涓婂崍11:43:48
	 *
	 */
	@RequiresPermissions("infos_guardian:add")
	@RequestMapping("/add")
	public String add(){
		return "infos/guardian_edit";
	}
	
	/**
	 * 
	 * 鏂规硶鎻忚堪 :璺宠浆鑷充慨鏀归〉闈�
	 *
	 * @param model
	 * @param id
	 * @param tabfrmId
	 * @return
	 * 鍒涘缓浜� :  yangzhuo
	 * 鍒涘缓鏃堕棿: 2016骞�8鏈�12鏃� 涓婂崍11:46:58
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
	 * 鏂规硶鎻忚堪 :璺宠浆鍒扮洃鎶や汉淇℃伅鏌ョ湅椤甸潰
	 *
	 * @param id
	 * @param model
	 * @return
	 * 鍒涘缓浜� :  yangzhuo
	 * 鍒涘缓鏃堕棿: 2016骞�8鏈�12鏃� 涓嬪崍4:04:58
	 *
	 */
	@RequestMapping("/look/{id}")
	public String look(@PathVariable Integer id,Model model){
		model.addAttribute("guardian",guardianService.get(id));
		return "infos/guardian_look";
	}
	
	/**
	 * 
	 * 鏂规硶鎻忚堪 :娣诲姞/淇敼瀛︾敓淇℃伅骞惰褰曟棩蹇�
	 *
	 * @param student
	 * @param content
	 * @return
	 * 鍒涘缓浜� :  xiongzhenghai
	 * 鍒涘缓鏃堕棿: 2016骞�8鏈�12鏃� 涓嬪崍3:07:08
	 *
	 */
	@RequestMapping("/save")
	@ResponseBody  
	public JsonView save(TbGuardian guardian, String content){
		JsonView jv = new JsonView();
		if (guardian.getId() != null) {// 淇敼
			try {
				this.guardianService.saveGuardian(guardian);
				jv.setMsg("淇敼璧勫姪浜轰俊鎭垚鍔燂紒");
				jv.setSuccess(true);
				Logs.info("淇敼璧勫姪浜轰俊鎭痠d : " + guardian.getId());
			} catch (Exception e) {
				Logs.error("淇敼璧勫姪浜轰俊鎭け璐� ----> " + e.getMessage());
				jv.setMsg(Constants.UPDATE_TIP_FAILED);
				jv.setSuccess(false);
			}
		} else {// 鏂板
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
				jv.setMsg("娣诲姞璧勫姪浜轰俊鎭垚鍔燂紒");
				Logs.info("娣诲姞璧勫姪浜轰俊鎭痠d : " + guardian.getId());
			} catch (Exception e) {
				Logs.error("娣诲姞璧勫姪浜轰俊鎭け璐� ----> " + e.getMessage());
				jv.setMsg("娣诲姞璧勫姪浜轰俊鎭け璐ワ紒");
				jv.setSuccess(false);
			}
		}
		return jv;
	}
	
	

	/**
	 * 
	 * 鏂规硶鎻忚堪 :鍒犻櫎璧勫姪浜轰俊鎭�
	 *
	 * @param ids
	 * @return
	 * 鍒涘缓浜� :  xiongzhenghai
	 * 鍒涘缓鏃堕棿: 2016骞�8鏈�12鏃� 涓嬪崍4:01:51
	 *
	 */
	@RequiresPermissions("infos_student:del")
	@RequestMapping("/delete/{ids}")
	@ResponseBody
	public JsonView delete(@PathVariable String ids){
		JsonView jv = new JsonView();
		try{
			this.guardianService.delGuardians(ids);
			jv.setMsg("鍒犻櫎璧勫姪浜烘垚鍔�");
			jv.setSuccess(true);
		}catch(Exception e){
			jv.setMsg("鍒犻櫎璧勫姪浜哄け璐�");
			jv.setSuccess(false);
			e.printStackTrace();
			LOG.error("璧勫姪浜轰俊鎭� - 鍒犻櫎澶辫触:"+e.getMessage());
		}
		Logs.info("鍒犻櫎璧勫姪浜轰俊鎭痠ds : " + ids);
		return jv;
	}
	
}
