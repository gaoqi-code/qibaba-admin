<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<table class="table table-border table-bordered table-bg">
    <thead>
    <tr>
        <th scope="col" colspan="9">用户留言列表</th>
    </tr>
    <tr class="text-c">
        <%--<th width="25"><input type="checkbox" name="" value=""></th>--%>
        <%--<th width="40">ID</th>--%>
        <th>联系电话</th>
        <th>所属类别</th>
        <th>描述</th>
        <th>添加时间</th>
        <th>删除</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${needs}" var="need" >
        <tr class="text-c">
                <%--<td><input type="checkbox" value="1" name=""></td>--%>
                <%--<td>1</td>--%>
            <td>${need.mobile}</td>
            <td>${need.cName}</td>
            <td>${need.needDesc}</td>
            <td> <fmt:formatDate value="${need.addTime}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" /></td>
            <td><a userneedid="${need.id}" class="delneed" href="javascript:;"><i class="Hui-iconfont">&#xe6e2;</i></a></td>
        </tr>
    </c:forEach>
    <c:if test="${empty needs}">
        <tr>
            <td colspan="6" style="text-align: center;">暂无需求！</td>
        </tr>
    </c:if>
    </tbody>
</table>

   <input type="hidden" id="totalPages" value="${paging.totalPages}"/>
   <input type="hidden" id="currentPage" value="${paging.currentPage}"/>
<script type="text/javascript">

</script> 