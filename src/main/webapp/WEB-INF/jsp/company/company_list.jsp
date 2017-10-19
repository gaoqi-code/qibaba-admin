<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE HTML>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/global.jsp" %>
	<base href="<%=basePath%>">
	<meta charset="utf-8">
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta http-equiv="Cache-Control" content="no-siteapp" />
	<link rel="Bookmark" href="/favicon.ico" >
	<link rel="Shortcut Icon" href="/favicon.ico" />
	<![endif]-->
	<jsp:include page="../common/static.jsp"></jsp:include>
	<![endif]-->
	<title>公司管理</title>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 公司管理 <span class="c-gray en">&gt;</span> 公司列表 <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="page-container">
	<div class="text-c">
		<form class="layui-form" action="" id="needForm">
		<div class="layui-form-item">
			<div class="layui-inline">
			<label class="layui-form-label">公司名称</label>
			<div class="layui-input-inline">
				<input id="companyName"  placeholder="请输入待查询的公司名称" class="layui-input"/>
			</div>
			</div>
			<button type="button" class="btn btn-success" name="" id="search" ><i class="Hui-iconfont">&#xe665;</i>搜索</button>
		</div>
		<%--<div class="layui-input-block">--%>
		<%--<button class="layui-btn" lay-submit="" lay-filter="demo1" >搜索</button>--%>
		<%--</div>--%>
		</form>
	</div>
	<div id="dataMsg"></div>
	<div id="companyPager"></div>
</div>
<!--_footer 作为公共模版分离出去-->
<jsp:include page="../common/static-js.jsp"></jsp:include>
<!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript">


    $(function () {
    	paging();
    })
    
    function paging(){
        layui.use(['form','laypage', 'layer'], function(){
            var laypage = layui.laypage
                ,layer = layui.layer;

            var form = layui.form()
                ,layedit = layui.layedit
                ,laydate = layui.laydate;
            //以下将以jquery.ajax为例，演示一个异步分页
            var pageSize = 5;
            function paging(curr){
				var statusSearch = $("#statusSearch").val();
				statusSearch = statusSearch == -1 ? "" : statusSearch;
                $.ajax({
                    type: "POST",
                    url: "/company/companyListData.html",
                    data: {
                        currentPage :curr || 1,
                        pageSize : pageSize
                    },
                    success: function(data){
                        $("#dataMsg").html(data);
                        var totalPages = $("#totalPages").val();
                        //显示分页
                        laypage({
                            cont: 'companyPager', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
                            pages: totalPages, //通过后台拿到的总页数
                            curr: curr || 1, //当前页
                            groups: 5 ,//连续显示分页数
                            jump: function(obj, first){ //触发分页后的回调
                                if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
                                    paging(obj.curr);
                                }
                            }
                        });
                    }
                });
            };
            //运行
            paging();
           /*  $("#search").click(function () {
                paging();
            }); */
        });
    }


	/*管理员-编辑*/
	function admin_edit(title,url,id,w,h){
		//layer_show(title,url,w,h);
		if (title == null || title == '') {
			title=false;
		};
		if (url == null || url == '') {
			url="404.html";
		};
		if (w == null || w == '') {
			w=800;
		};
		if (h == null || h == '') {
			h=($(window).height() - 50);
		};
		layer.open({
		      type: id,
		      title: title,
		      shadeClose: false,
		      shade: 0.4,
		      maxmin: true, //开启最大化最小化按钮
		      area: [w+'px', h+'px'],
		      content: url,
		      end:function(){
		    	  paging();
		      }
		 });
	}
    
    
</script>

<script type="text/javascript">
$(function(){
	queryCompanyByName();
	//sendUpdateCompanyPage();
});
/**
 * 通过公司名称查询公司
 * ——李文辉
 */
function queryCompanyByName(){
	$("#search").on("click",function(){
		var companyname= $("#companyName").val().replace(/\s+/g,"");
		if(companyname.length>0){
			
			layui.use(['form','laypage', 'layer'], function(){
	            var laypage = layui.laypage
	                ,layer = layui.layer;

	            var form = layui.form()
	                ,layedit = layui.layedit
	                ,laydate = layui.laydate;
	            //以下将以jquery.ajax为例，演示一个异步分页
	            var pageSize = 5;
	            paging();
	            function paging(curr){
	                $.ajax({
	                    type: "POST",
	                    url: "/company/queryCompanyByName.html",
	                    data: {
	                        currentPage :curr || 1,
	                        pageSize : pageSize,
	                        companyName:companyname
	                    },
	                    success: function(data){
	                        $("#dataMsg").html(data);
	                        var totalPages = $("#totalPages").val();
	                        //显示分页
	                        laypage({
	                            cont: 'companyPager', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
	                            pages: totalPages, //通过后台拿到的总页数
	                            curr: curr || 1, //当前页
	                            groups: 5 ,//连续显示分页数
	                            jump: function(obj, first){ //触发分页后的回调
	                                if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
	                                    paging(obj.curr);
	                                }
	                            }
	                        });
	                    }
	                });
	            };
	        });
			/* $.ajax({
                type: "POST",
                url:'/company/queryCompanyByName.html',
                data: {
                    companyName:companyname
                },
                success: function(data){
                    $("#dataMsg").html(data);
                    $("#companyPager").html("");
                }
            }); */
          }else{
        	  paging();
		}
	});
}

/**
 *通过公司id修改公司信息 
 *——李文辉
 */
/* function sendUpdateCompanyPage(){
	$(document).on("click",".updatecompany",function(){
		var id=$(this).attr("companyid");
		alert(id);
	});
}
 */
</script>
</body>
</html>