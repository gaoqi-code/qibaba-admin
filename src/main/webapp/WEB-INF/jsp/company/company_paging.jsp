<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<table class="table table-border table-bordered table-bg">
    <thead>
    <tr>
        <th scope="col" colspan="10">公司列表</th>
    </tr>
    <tr class="text-c">
        <th>公司名称</th>
        <th>加入时间</th>
        <th>公司地址</th>
        <th>顾问数量</th>
        <th>编辑</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${companys}" var="company" >
    <tr class="text-c">
        <%--<td><input type="checkbox" value="1" name=""></td>--%>
        <%--<td>1</td>--%>
            <td>${company.companyName}</td>
            <td> <fmt:formatDate value="${company.addTime}"   pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" /></td>
    		<td>${company.address}</td>
    		<td>${company.membercount}</td>
    		<td><a title="编辑" href="javascript:;" onclick="admin_edit('编辑','/company/toSetting/${company.id }.html',2,'800','500')" class="ml-5" style="text-decoration:none">
                <i class="Hui-iconfont">&#xe6df;</i></a>
    		</td>
    </tr>
    </c:forEach>
    <c:if test="${empty companys}">
        <tr>
            <td colspan="5" style="text-align: center;">暂无公司！</td>
        </tr>
    </c:if>
    </tbody>
</table>

   <input type="hidden" id="totalPages" value="${paging.totalPages}"/>
   <input type="hidden" id="currentPage" value="${paging.currentPage}"/>
<script type="text/javascript">

</script> 