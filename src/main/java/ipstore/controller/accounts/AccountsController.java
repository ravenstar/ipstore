package ipstore.controller.accounts;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ipstore.aspect.Action;
import ipstore.breadcrumb.Breadcrumb;
import ipstore.controller.ImportResultType;
import ipstore.entity.Account;
import ipstore.message.Message;
import ipstore.message.MessageStatus;
import ipstore.message.MessageType;
import ipstore.service.AccountsService;
import ipstore.service.SearchService;
import ipstore.spring.BreadcrumbInterceptor;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

import static ipstore.aspect.ActionType.*;
import static ipstore.aspect.change.ChangeType.ACCOUNTS;
import static ipstore.aspect.change.ChangeMode.IMPORT;
import static ipstore.aspect.change.ChangeMode.UPDATE;

/**
 * Here will be javadoc
 *
 * @author karlovsky
 * @since 1.0
 */
@Controller
public class AccountsController {

    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

    private
    @Value("${random_password.default_length}")
    String generatedPasswordDefaultLength;

    @Resource(name = "accountsService")
    private AccountsService accountsService;

    @Resource(name = "searchService")
    private SearchService searchService;

    @Breadcrumb(label = "Import accounts")
    @Action(value = ACCOUNTS_IMPORT_PAGE)
    @RequestMapping(value = "/accounts/import", method = RequestMethod.GET)
    public String importAccounts() {
        logger.debug("Loading accounts import page...");
        return "c-import-accounts";
    }

    @Action(value = ACCOUNTS_IMPORT, changeType = ACCOUNTS, changeMode = IMPORT)
    @RequestMapping(value = "/accounts/import", method = RequestMethod.POST)
    public String importAccounts(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        logger.info("Accounts importing, file " + file.getOriginalFilename());
        if (!file.isEmpty()) {
            logger.info("File size: " + file.getSize());
            try {
                ImportResultType<Account> result = accountsService.importFile(file.getInputStream());
                model.addAttribute("result", result);
                logger.info("Accounts importing success, added=" + result.getAddedCount() +
                        " accounts, exists=" + result.getExistsCount());
            } catch (Exception e) {
                model.addAttribute("error", true);
                logger.error("Accounts importing error...", e);
            }
        }
        return "c-import-accounts";
    }

    @Breadcrumb(label = "Accounts")
    @Action(value = ACCOUNTS_LIST)
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public String viewAccounts(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "status", required = false) String status, Model model) {
        logger.info("Received request to load accounts.");
        if (StringUtils.isNotEmpty(search)) {
            model.addAttribute("accounts", searchService.searchAccounts(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("accounts", accountsService.loadAccounts(status));
        }
        return "mfc-list-accounts";
    }

    @Breadcrumb
    @Action(value = ACCOUNTS_VIEW_PAGE)
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    public String viewAccount(@PathVariable Long id, Model model) {
        Account account = accountsService.loadAccount(id);
        model.addAttribute("account", account);
        model.addAttribute(BreadcrumbInterceptor.TITLE_ATTRIBUTE, "View account ( " + account.getLogin() + " )");
        return "c-view-accounts";
    }

    @Action(value = ACCOUNTS_DELETE, changeType = ACCOUNTS, changeMode = UPDATE)
    @RequestMapping(value = "/accounts/{id}/delete", method = RequestMethod.GET)
    public String deleteAccount(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Account account = accountsService.deleteAccount(id);
        redirectAttributes.addFlashAttribute(Message.MESSAGE_ATTRIBUTE,
                new Message(account.getLogin(), MessageType.TEXT_WITH_URL, MessageStatus.DANGER,
                        "/accounts/" + account.getId(), "Account ", " successfully deleted!"));
        return "redirect:/accounts";
    }

    @Action(value = ACCOUNTS_BLOCK, changeType = ACCOUNTS, changeMode = UPDATE)
    @RequestMapping(value = "/accounts/{id}/block", method = RequestMethod.GET)
    public String blockAccount(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Account account = accountsService.blockAccount(id);
        redirectAttributes.addFlashAttribute(Message.MESSAGE_ATTRIBUTE,
               new Message("Account " + account.getLogin() + " successfully blocked!",
                       MessageType.TEXT, MessageStatus.WARNING));
        return "redirect:/accounts/" + id;
    }

    @Action(value = ACCOUNTS_ACTIVATE, changeType = ACCOUNTS, changeMode = UPDATE)
    @RequestMapping(value = "/accounts/{id}/activate", method = RequestMethod.GET)
    public String activateAccount(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Account account = accountsService.activateAccount(id);
        redirectAttributes.addFlashAttribute(Message.MESSAGE_ATTRIBUTE,
                new Message("Account " + account.getLogin() + " successfully activated!",
                        MessageType.TEXT, MessageStatus.SUCCESS));
        return "redirect:/accounts/" + id;
    }

    @Action(value = ACCOUNTS_UNBLOCK, changeType = ACCOUNTS, changeMode = UPDATE)
    @RequestMapping(value = "/accounts/{id}/unblock", method = RequestMethod.GET)
    public String unBlockAccount(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Account account = accountsService.activateAccount(id);
        redirectAttributes.addFlashAttribute(Message.MESSAGE_ATTRIBUTE,
                new Message("Account " + account.getLogin() + " successfully unblocked!", MessageType.TEXT,
                        MessageStatus.WARNING));
        return "redirect:/accounts/" + id;
    }

    @Breadcrumb
    @Action(value = ACCOUNTS_EDIT_PAGE)
    @RequestMapping(value = "/accounts/{id}/edit", method = RequestMethod.GET)
    public String editAccount(@PathVariable Long id, Model model) {
        Account account = accountsService.loadAccount(id);
        model.addAttribute("account", account);
        model.addAttribute("edit", true);
        model.addAttribute("formAction", "/accounts/" + id);
        model.addAttribute("defaultPasswordLength", generatedPasswordDefaultLength);
        model.addAttribute(BreadcrumbInterceptor.TITLE_ATTRIBUTE, "Edit account ( " + account.getLogin() + " )");
        return "c-edit-accounts";
    }

    @Breadcrumb(label = "Add account")
    @Action(value = ACCOUNTS_ADD_PAGE)
    @RequestMapping(value = "/accounts/new", method = RequestMethod.GET)
    public String addAccount(Model model) {
        Account account = new Account();
        model.addAttribute("edit", false);
        model.addAttribute("account", account);
        model.addAttribute("formAction", "/accounts");
        model.addAttribute("defaultPasswordLength", generatedPasswordDefaultLength);
        return "c-edit-accounts";
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public String createAccount(@Valid Account account, BindingResult result, Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formAction", "/accounts");
            model.addAttribute("defaultPasswordLength", generatedPasswordDefaultLength);
            return "c-edit-accounts";
        }
        String login = account.getLogin();
        Account existsAccount = accountsService.loadAccount(login);
        if (existsAccount != null) {
            model.addAttribute("edit", false);
            model.addAttribute("formAction", "/accounts");
            model.addAttribute("defaultPasswordLength", generatedPasswordDefaultLength);
            model.addAttribute("existsAccount", existsAccount);
            return "c-edit-accounts";
        }
        Long id = accountsService.createAccount(account);
        redirectAttributes.addFlashAttribute(Message.MESSAGE_ATTRIBUTE,
               new Message("Account " + account.getLogin() + " successfully created!",
                       MessageType.TEXT, MessageStatus.SUCCESS));
        return "redirect:/accounts/" + id;
    }

    @Action(value = ACCOUNTS_UPDATE, changeType = ACCOUNTS, changeMode = UPDATE)
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.POST)
    public String updateAccount(@Valid Account account, BindingResult result, @PathVariable Long id, Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("edit", true);
            model.addAttribute("formAction", "/accounts/" + id);
            model.addAttribute("defaultPasswordLength", generatedPasswordDefaultLength);
            return "c-edit-accounts";
        }
        accountsService.updateAccount(id, account);
        redirectAttributes.addFlashAttribute(Message.MESSAGE_ATTRIBUTE,
                new Message("Account " + account.getLogin() + " successfully updated!",
                        MessageType.TEXT, MessageStatus.SUCCESS));
        return "redirect:/accounts/" + id;
    }
}
