<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<form method="post" action="<c:url value="/communigate/import"/>" enctype="multipart/form-data" class="form-horizontal" role="form">
    <h3>Communigate import</h3>
    <c:if test="${error}">
        <div class="form-group">
            <div class="col-md-5">
                <div class="alert alert-danger" style="margin-bottom: 0;">Importing error!</div>
            </div>
        </div>
    </c:if>
    <div class="form-group">
        <div class="col-md-5">
            <input type="file" name="file"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-5">
            <input class="btn btn-primary" type="submit" value="Import"/>
        </div>
    </div>
</form>
<c:if test="${not error and not empty result}">
    <table>
        <tr>
            <td valign="top">
                <table class="table table-hover table-condensed">
                    <tr>
                        <td>
                                <span class="text-success">
                                    Successfully added <c:out value="${result.addedCount}"/> communigate items.
                                </span>
                        </td>
                    </tr>
                    <c:forEach items="${result.added}" var="added">
                        <tr>
                            <td>
                                    <span class="text-success">
                                        Communigate domain with domain name
                                        <a href="/communigate/${added.id}" target="_blank">
                                            <c:out value="${added.domainName}"/>
                                        </a>
                                        successfully added!
                                    </span>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
            <td valign="top">
                <c:if test="${not empty result.exists}">
                    <table class="table table-hover table-condensed">
                        <tr>
                            <td>
                                    <span class="text-error">
                                        Already exists and ignored <c:out value="${result.existsCount}"/> communigate items.
                                    </span>
                            </td>
                        </tr>
                        <c:forEach items="${result.exists}" var="exists">
                            <tr>
                                <td>
                                        <span class="text-error">
                                            Communigate Domain with
                                            <a href="/domain/communigate/${exists.id}" target="_blank">
                                                domainName <c:out value="${exists.domainName}"/> and tryPrefix <c:out
                                                    value="${exists.tryPrefix}"/>
                                            </a>
                                            already exists!
                                        </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>
            </td>
        </tr>
    </table>

</c:if>