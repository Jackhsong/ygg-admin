package com.ygg.admin.controller.qqbs;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ygg.admin.dao.AccountDao;
import com.ygg.admin.entity.qqbs.QRCodeEntity;
import com.ygg.admin.service.qqbs.IQRCodeService;

/**
 * @author wuhy
 * @date 创建时间：2016年5月16日 下午5:25:17
 */
@Controller
@RequestMapping("/qRcode")
public class QqbsQRCodeController
{
    @Resource
    private IQRCodeService qrCodeService;
    
    @Resource
    private AccountDao accountDao;
    
    @RequestMapping(value = "/index")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("qqbs/qrCode/qrCodeIndex");
        return mv;
    }
    
    @RequestMapping(value = "/query", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<QRCodeEntity> query(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "rows", defaultValue = "50") int rows, @RequestParam(value = "accountId", required = false, defaultValue = "0") String accountId_str)
        throws Exception
    {
        int accountId = StringUtils.isEmpty(accountId_str) ? 0 : Integer.valueOf(accountId_str);
        return qrCodeService.findQRCodesByQueryCriteria(buildQueryCriteria(accountId, page, rows));
    }
    
    private QRCodeEntity buildQueryCriteria(int accountId, int page, int rows)
    {
        QRCodeEntity qqbsUserQueryCriteria = new QRCodeEntity();
        qqbsUserQueryCriteria.setAccountId(accountId);
        if (page == 0)
        {
            page = 1;
        }
        qqbsUserQueryCriteria.setStart(rows * (page - 1));
        qqbsUserQueryCriteria.setMax(rows);
        return qqbsUserQueryCriteria;
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> create(HttpServletRequest request, @RequestParam(value = "accountId", required = true) int accountId)
    {
        return qrCodeService.createQRCodeByAccountId(accountId);
    }
    
}
