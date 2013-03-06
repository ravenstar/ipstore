package utilits.controller;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import utilits.aspect.Action;
import utilits.aspect.ActionType;
import utilits.entity.Account;
import utilits.entity.Equipment;
import utilits.service.AccountsService;
import utilits.service.EquipmentService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Here will be javadoc
 *
 * @author karlovsky
 * @since 1.0
 */
@Controller
public class ReportController {

    @Resource(name = "equipmentService")
    private EquipmentService equipmentService;

    @Resource(name = "accountsService")
    private AccountsService accountsService;

    @Action(type = ActionType.EQUIPMENT_EXPORT)
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public ModelAndView loadEquipmentReport() {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        List<Equipment> equipments = equipmentService.loadEquipments();
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(equipments);
        parameterMap.put("datasource", jrDataSource);
        return new ModelAndView("equipmentReport", parameterMap);
    }

    @Action(type = ActionType.ACCOUNTS_EXPORT)
    @RequestMapping(value = "/accounts/export", method = RequestMethod.GET)
    public ModelAndView loadAccountsReport() {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        List<Account> accounts = accountsService.loadAccounts();
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(accounts);
        parameterMap.put("datasource", jrDataSource);
        return new ModelAndView("accountsReport", parameterMap);
    }
}
