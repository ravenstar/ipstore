<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>VoIPStore</title>
    <link rel="shortcut icon" href="<c:url value="/assets/ico/favicon.png"/>">
    <link href="<c:url value="/assets/css/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/assets/css/bootstrap-responsive.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/datepicker.css"/>" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/js/jquery-1.8.3.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/assets/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/bootstrap-datepicker.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/actions.js"/>"></script>
    <style type="text/css">
        .table tbody tr.info td {
            background-color: #ffffff;
        }

        .nw {
            white-space: nowrap;
        }

        body {
            padding-top: 60px;
        }
    </style>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="<c:url value="/ipstore/equipment" />">VoIPStore</a>
            <div class="nav-collapse collapse">
                <ul class="nav">
                    <li><a href="<c:url value="/ipstore/equipment" />">Home</a></li>
                    <security:authorize access="hasRole('ROLE_ROOT')">
                        <li><a href="<c:url value="/monitoring"/>">Monitoring</a></li>
                    </security:authorize>
                    <security:authorize access="hasRole('ROLE_ROOT')">
                        <li class="active"><a href="<c:url value="/ipstore/actions"/>">Actions</a></li>
                    </security:authorize>
                    <li><a href="<c:url value="/j_spring_security_logout" />">Logout</a></li>
                </ul>
                <form:form commandName="actionFilterForm" class="navbar-form pull-right" action="/ipstore/actions">
                    <form:input id="from" path="from" cssClass="input-small" placeholder="From"/>
                    <form:input id="to" path="to" cssClass="input-small" placeholder="To"/>
                    <form:input id="ip" path="ip" cssClass="input-small" placeholder="Ip"/>
                    <form:input id="host" path="host" cssClass="input-small" placeholder="Host"/>
                    <button type="submit" class="btn btn-primary">Search</button>
                </form:form>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <table class="table table-hover table-condensed">
        <thead>
        <tr>
            <th>Date</th>
            <th class="nw">Ip address</th>
            <th>Host</th>
            <th>Type</th>
            <th>Request URL</th>
            <th>User Agent</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${actions}" var="action">
            <tr class="info">
                <td class="nw"><fmt:formatDate value="${action.actionTimestamp}" type="both"
                                               pattern="dd.MM.yyyy HH:mm:ss"/></td>
                <td><c:out value="${action.ip}"/></td>
                <td><c:out value="${action.host}"/></td>
                <td><c:out value="${action.type}"/></td>
                <td><c:out value="${action.requestURL}"/></td>
                <td><c:out value="${action.userAgent}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>