package ipstore.bootstrap;

import ipstore.service.Principal;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import ipstore.controller.accounts.AccountStatus;
import ipstore.controller.communigate.CommunigateStatus;
import ipstore.controller.equipment.PasswordStatus;
import ipstore.controller.equipment.Status;
import ipstore.controller.equipment.TelnetStatus;
import ipstore.controller.users.UserStatus;
import ipstore.menu.Menu;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Here will be javadoc
 *
 * @author ravenstar
 * @since 4.0, 2/19/14
 */
@Component("application")
@Scope("singleton")
public class ApplicationBean {

    private static final Map<Object, BootstrapClass> bootstrapStyleClasses = Collections.unmodifiableMap(
            new HashMap<Object, BootstrapClass>() {
                {
                    put(PasswordStatus.NEED_UPDATE, BootstrapClass.DANGER);
                    put(PasswordStatus.NEW, BootstrapClass.SUCCESS);
                    put(PasswordStatus.OLD, BootstrapClass.WARNING);
                    put(Status.DELETED, BootstrapClass.DEFAULT);
                    put(Status.ACTIVE_NO_EXPIRED, BootstrapClass.WARNING);
                    put(TelnetStatus.OK, BootstrapClass.SUCCESS);
                    put(TelnetStatus.WARNING, BootstrapClass.DANGER);
                    put(TelnetStatus.TIMEOUT, BootstrapClass.WARNING);
                    put(TelnetStatus.IGNORED, BootstrapClass.DEFAULT);
                    put(AccountStatus.NORMAL, BootstrapClass.SUCCESS);
                    put(AccountStatus.WARNING, BootstrapClass.DANGER);
                    put(AccountStatus.DELETED, BootstrapClass.DEFAULT);
                    put(CommunigateStatus.NORMAL, BootstrapClass.SUCCESS);
                    put(CommunigateStatus.BLOCKED, BootstrapClass.DANGER);
                    put(CommunigateStatus.DELETED, BootstrapClass.DEFAULT);
                    put(UserStatus.ACTIVE, BootstrapClass.SUCCESS);
                    put(UserStatus.BLOCKED, BootstrapClass.DEFAULT);
                }
            });

    private
    @Value("${application.version}")
    String version;

    public String checkMenuActive(String menu) {
        Menu checkMenu = Menu.valueOf(menu);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (checkMenu.checkMapping((String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE))) {
            return "active";
        }
        return "";
    }

    public String labelClass(Object status) {
        if (status != null) {
            BootstrapClass styleClass = bootstrapStyleClasses.get(status);
            if (styleClass != null) {
                return styleClass.getLabelClass();
            }
        }
        return StringUtils.EMPTY;
    }

    public String alertClass(Object status) {
        if (status != null) {
            BootstrapClass styleClass = bootstrapStyleClasses.get(status);
            if (styleClass != null) {
                return styleClass.getAlertClass();
            }
        }
        return StringUtils.EMPTY;
    }

    public String textClass(Object status) {
        if (status != null) {
            BootstrapClass styleClass = bootstrapStyleClasses.get(status);
            if (styleClass != null) {
                return styleClass.getTextClass();
            }
        }
        return StringUtils.EMPTY;
    }

    public String rowClass(Object status) {
        if (status != null) {
            BootstrapClass styleClass = bootstrapStyleClasses.get(status);
            if (styleClass != null) {
                return styleClass.getRowClass();
            }
        }
        return StringUtils.EMPTY;
    }

    public String getVersion() {
        return version;
    }

    public String getTheme() {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getTheme();
    }
}
