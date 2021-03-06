<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/${application.version}/css/bootstrap/${application.theme}/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/${application.version}/css/bootstrap/${application.theme}/${application.theme}.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/${application.version}/css/datepicker.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/${application.version}/css/datepicker3.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/${application.version}/css/tablesorter.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/${application.version}/css/ipstore.css"/>">
    <title>IPStore</title>
</head>
<body>
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button data-target=".navbar-collapse" data-toggle="collapse" class="navbar-toggle" type="button">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="<c:url value="/"/>" class="navbar-brand">
                <span class="glyphicon glyphicon-globe"></span> IPStore
            </a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-left">
                <security:authorize access="hasRole('ROLE_ROOT')">
                    <li class="dropdown ${application.checkMenuActive('MANAGEMENT')}">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Management <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li class="dropdown-header">Actions</li>
                            <li><a href="<c:url value="/actions"/>">Actions</a></li>
                            <li><a href="<c:url value="/changes"/>">Changes</a></li>
                            <li class="dropdown-header">Users</li>
                            <li><a href="<c:url value="/users"/>">Users list</a></li>
                            <li><a href="<c:url value="/users/new"/>">Add user</a></li>
                            <li class="dropdown-header">Miscellaneous</li>
                            <li><a href="<c:url value="/monitoring"/>">Monitoring</a></li>
                            <li><a id="rebuild_index" href="<c:url value="/rebuild"/>">Rebuild lucene index</a></li>
                        </ul>
                    </li>
                </security:authorize>
                <li class="dropdown">
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">Tools <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="http://works.ztel.ru/" target="_blank">TRY</a></li>
                        <li><a href="http://zabbix.ztel.ru/zabbix/iptable/" target="_blank">Zabbix IP</a></li>
                        <li><a href="http://wiki.ztel.ru/" target="_blank">Wiki</a></li>
                        <li><a href="http://lexx.ztel.ru/" target="_blank">Lexx</a></li>
                        <li><a href="https://easy.step7.ru" target="_blank">Easy</a></li>
                    </ul>
                </li>
                <li class="${application.checkMenuActive('CONTACT')}"><a href="<c:url value="/contact"/>">Contact</a></li>
                <li class="dropdown ${application.checkMenuActive('USER_SETTINGS')}">
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                        <span class="glyphicon glyphicon-log-out"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="dropdown-header">You signed as <security:authentication property="principal.username"/></li>
                        <li><a href="<c:url value="/changepassword"/>">Change password</a></li>
                        <li><a href="<c:url value="/changeuserinfo"/>">Settings</a></li>
                        <li><a href="<c:url value="/j_spring_security_logout"/>">Logout</a></li>
                    </ul>
                </li>
            </ul>
            <tiles:insertAttribute ignore="true" name="menu-form"/>
        </div><!--/.nav-collapse -->
    </div>
</div>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <security:authorize access="hasRole('ROLE_EQUIPMENT_VIEW')">
                <ul class="nav nav-pills nav-stacked">
                        <li class="${application.checkMenuActive('EQUIPMENTS')}">
                            <a href="<c:url value="/equipment"/>">
                                <span class="glyphicon glyphicon-th-list"></span> Equipments
                            </a>
                        </li>
                    <security:authorize access="hasRole('ROLE_EQUIPMENT_EDIT')">
                        <li class="${application.checkMenuActive('ADD_EQUIPMENT')}">
                            <a href="<c:url value="/equipment/new"/>">
                                <span class="glyphicon glyphicon-plus"></span> Add equipment
                            </a>
                        </li>
                    </security:authorize>
                    <security:authorize access="hasRole('ROLE_ROOT')">
                        <li class="${application.checkMenuActive('IMPORT_EQUIPMENTS')}">
                            <a href="<c:url value="/equipment/import"/>">
                                <span class="glyphicon glyphicon-upload"></span> Import equipment
                            </a>
                        </li>
                        <li>
                            <a href="<c:url value="/equipment/export"/>">
                                <span class="glyphicon glyphicon-download"></span> Export equipment
                            </a>
                        </li>
                    </security:authorize>
                </ul>
            </security:authorize>
            <security:authorize access="hasRole('ROLE_ACCOUNT_VIEW')">
                <ul class="nav nav-pills nav-stacked">
                    <li class="${application.checkMenuActive('ACCOUNTS')}">
                        <a href="<c:url value="/accounts"/>">
                            <span class="glyphicon glyphicon-th-list"></span> Accounts
                        </a>
                    </li>
                <security:authorize access="hasRole('ROLE_ACCOUNT_EDIT')">
                    <li class="${application.checkMenuActive('ADD_ACCOUNT')}">
                        <a href="<c:url value="/accounts/new"/>">
                            <span class="glyphicon glyphicon-plus"></span> Add account
                        </a>
                    </li>
                </security:authorize>
                <security:authorize access="hasRole('ROLE_ROOT')">
                    <li class="${application.checkMenuActive('IMPORT_ACCOUNTS')}">
                        <a href="<c:url value="/accounts/import"/>">
                            <span class="glyphicon glyphicon-upload"></span> Import accounts
                        </a>
                    </li>
                    <li>
                        <a href="<c:url value="/accounts/export"/>">
                            <span class="glyphicon glyphicon-download"></span> Export accounts
                        </a>
                    </li>
                </security:authorize>
                </ul>
            </security:authorize>
            <security:authorize access="hasRole('ROLE_COMMUNIGATE_VIEW')">
                <ul class="nav nav-pills nav-stacked">
                        <li class="${application.checkMenuActive('DOMAINS')}">
                            <a href="<c:url value="/communigate"/>">
                                <span class="glyphicon glyphicon-th-list"></span> Domains
                            </a>
                        </li>
                    <security:authorize access="hasRole('ROLE_COMMUNIGATE_EDIT')">
                        <li class="${application.checkMenuActive('ADD_DOMAIN')}">
                            <a href="<c:url value="/communigate/new"/>">
                                <span class="glyphicon glyphicon-plus"></span> Add domain
                            </a>
                        </li>
                    </security:authorize>
                    <security:authorize access="hasRole('ROLE_ROOT')">
                        <li class="${application.checkMenuActive('IMPORT_DOMAIN')}">
                            <a href="<c:url value="/communigate/import"/>">
                                <span class="glyphicon glyphicon-upload"></span> Import domains
                            </a>
                        </li>
                        <li>
                            <a href="<c:url value="/communigate/export"/>">
                                <span class="glyphicon glyphicon-download"></span> Export domains
                            </a>
                        </li>
                    </security:authorize>
                </ul>
            </security:authorize>
            <security:authorize access="hasRole('ROLE_CAPACITY_VIEW')">
                <ul class="nav nav-pills nav-stacked">

                    <li class="${application.checkMenuActive('CAPACITY')}">
                        <a href="<c:url value="/capacity"/>">
                            <span class="glyphicon glyphicon-th-list"></span> Phone capacity
                        </a>
                    </li>

                    <security:authorize access="hasRole('ROLE_CAPACITY_EDIT')">
                        <li class="${application.checkMenuActive('ADD_CAPACITY')}">
                            <a href="<c:url value="/capacity/new"/>">
                                <span class="glyphicon glyphicon-plus"></span> Add capacity
                            </a>
                        </li>
                    </security:authorize>
                </ul>
            </security:authorize>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <c:if test="${not empty sessionScope['BREADCRUMB']}">
                <ol class="breadcrumb">
                    <c:forEach items="${sessionScope['BREADCRUMB']}" var="item" varStatus="index">
                        <c:choose>
                            <c:when test="${item.current}">
                                <li class="active"><c:out value="${item.label}"/></li>
                            </c:when>
                            <c:otherwise>
                                <li>
                                    <a href="${item.requestURI}"><c:out value="${item.label}"/></a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </ol>
            </c:if>
            <c:if test="${not empty requestScope['UI_MESSAGE']}">
                <div class="${requestScope['UI_MESSAGE'].messageStatus.cssClass}">
                    <c:choose>
                        <c:when test="${requestScope['UI_MESSAGE'].messageType eq 'TEXT_WITH_URL'}">
                            <c:out value="${requestScope['UI_MESSAGE'].leftText}"/>
                            <a href="<c:url value="${requestScope['UI_MESSAGE'].url}"/>" class="alert-link">
                                <c:out value="${requestScope['UI_MESSAGE'].text}"/>
                            </a>
                            <c:out value="${requestScope['UI_MESSAGE'].rightText}"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${requestScope['UI_MESSAGE'].text}"/>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            <tiles:insertAttribute name="content"/>
        </div>
    </div>
</div>
<script type="text/javascript" src="<c:url value="/resources/${application.version}/js/jquery.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/${application.version}/js/bootstrap.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/${application.version}/js/bootstrap-datepicker.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/${application.version}/js/bootbox.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/${application.version}/js/tablesorter.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/${application.version}/js/tablesorter.widgets.min.js"/>"></script>
<script type="text/javascript">
    //<![CDATA[
    $(function () {
        window['contextPath'] = '${pageContext.request.contextPath}';
    });
    //]]>
</script>
<tiles:insertAttribute ignore="true" name="additional-scripts"/>
<script type="text/javascript" src="<c:url value="/resources/${application.version}/js/ipstore.js"/>"></script>
</body>
</html>
