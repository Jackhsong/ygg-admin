<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>换吧网络-后台管理</title>
    <link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
    <script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
    <script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
    <script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
    <script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <style type="text/css">
        textarea{
            resize:none;
        }
        .fieldName{
            width: 100px;
            display:inline-block;
            font-size:14px;
        }
        .fieldName1{
            width: 200px;
            display:inline-block;
            font-size:14px;
        }
        .fieldName2{
            width: 120px;display:inline-block;font-size:14px;
        }
        .pointSpan{
            width:70px;display:inline-block;
        }

        input[type='text']{
            width:250px;
        }

    </style>
</head>
<body class="easyui-layout">

<div data-options="region:'center',title:'大转盘信息'  " style="padding:5px;">
    <form id="saveFortuneWheel"  method="post" action="">
        <fieldset>
            <input type="hidden" value="${fortuneWheelId!0}" id="id" name="id" />
            <legend>分享信息</legend>
            <span class="fieldName">分享图：</span><input id="shareImage"  name="shareImage"  value="${ shareImage!''}"/> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> &nbsp;&nbsp;&nbsp;&nbsp;
            <span class="fieldName">分享主标题：</span><input type="text" id="shareMainTitle" name="shareMainTitle" value="${shareMainTitle!''}" />&nbsp;&nbsp;&nbsp;&nbsp;
            <span class="fieldName">分享副标题：</span><input type="text"  id="shareSecondTitle" name="shareSecondTitle" value="${shareSecondTitle!''}" />
            <hr/>
            <br/>

            <legend>基本信息</legend>
            <span class="fieldName">活动名称：</span><input type="text" id="name" name="name" value="${name!''}" />&nbsp;&nbsp;&nbsp;&nbsp;
            <span class="fieldName">活动时间：</span><input value="${startTime!''}" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'})"/>
            &nbsp;至&nbsp;<input value="${endTime!''}" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"/>&nbsp;&nbsp;&nbsp;&nbsp;
            <span class="fieldName">背景色：</span><input type="text" name="backgroundColor" id="backgroundColor" value="${backgroundColor!''}"/> <br/><br/>
            <span class="fieldName">状态：</span><input type="radio"  class="isAvailable" name="isAvailable" id="isAvailable1" value="1" <#if isAvailable?exists && (isAvailable==1)>checked</#if>/>是&nbsp;&nbsp;&nbsp;
            <input type="radio" name="isAvailable" class="isAvailable" id="isAvailable0" value="0" <#if isAvailable?exists && (isAvailable==0)>checked</#if>/>否<br/><br/>
            <span class="fieldName1">转盘上部图片(可不填,可多张)：</span><#if topPics?exists && (topPics?size > 0)>
                                        <#list topPics as pic>
                                            <div class="topPicsDiv" style=""><input type="text" class="topPics" name="topPics" value="${pic}"> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> | <span class=" btn btn-success btn-xs" onclick="addLine(this, 'topPics')">添加</span>  | <span class=" btn btn-danger btn-xs" onclick="return removeLine(this, 'topPicsDiv')">删除</span> <br/></div>
                                        </#list>
                                    <#else>
                                            <div class="topPicsDiv" style=""><input type="text" class="topPics" name="topPics" value=""> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> | <span class=" btn btn-success btn-xs" onclick="addLine(this, 'topPics')">添加</span>  | <span class=" btn btn-danger btn-xs" onclick="return removeLine(this, 'topPicsDiv')">删除</span> <br/></div>
                                    </#if>
                                    </br>
            <span class="fieldName1">转盘下部图片(可不填,可多张)： </span><#if bottomPics?exists && (bottomPics?size > 0)>
                                        <#list bottomPics as pic>
                                            <div class="bottomPicsDiv" style=""><input type="text" class="bottomPics" name="bottomPics" value="${pic}"> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> | <span class=" btn btn-success btn-xs" onclick="return addLine(this, 'bottomPics')">添加</span>  | <span class=" btn btn-danger btn-xs" onclick="return removeLine(this, 'bottomPicsDiv')">删除</span> <br/></div>
                                        </#list>
                                    <#else>
                                            <div class="bottomPicsDiv" style=""><input type="text" class="bottomPics" name="bottomPics" value=""> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> | <span class=" btn btn-success btn-xs" onclick=" return addLine(this, 'bottomPics')">添加</span>  | <span class=" btn btn-danger btn-xs" onclick="return removeLine(this, 'bottomPicsDiv')" >删除</span> <br/></div>
                                    </#if>
            <hr/>

            <legend>大转盘信息</legend>
            <span class="fieldName">大转盘类型：</span><input type="radio" class="type" name="type" id="type1" value="1" <#if type?exists && (type==1)>checked</#if>/>扣积分抽奖&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;<input type="radio" name="type" class="type" id="type2" value="2" <#if type?exists && (type==2)>checked</#if>/>限次数抽奖<br/><br/>

            <span class="fieldName2">每次扣除积分：</span><input  type="number" id="pointUsed" name="pointUsed" value="${pointUsed!''}" style="width:400px;"/>  &nbsp;&nbsp;字体颜色
            <input type="text" id="pointFontColor" name="pointFontColor" value="${pointFontColor!''}">&nbsp;&nbsp; <div style="display:inline-block">图标 <input type="text" name="pointIcon" id="pointIcon" value="${pointIcon!''}"> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> </div>  <br/><br/>
            <span class="fieldName2">每天可抽奖次数：</span><input  type="number" id="timesLimit" name="timesLimit" value="${timesLimit!''}" style="width:400px;"/> &nbsp;&nbsp;  字体颜色
            <input type="text" id="timesFontColor" name="timesFontColor" value="${timesFontColor!''}"> &nbsp;&nbsp;<div style="display:inline-block">图标 <input type="text" name="timesIcon" id="timesIcon"  value="${timesIcon!''}"> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> </div> <br/><br/>
            <span class="fieldName2">当前拥有积分：</span>字体颜色
            <input type="text" id="ownPointFontColor" name="ownPointFontColor" value="${ownPointFontColor!''}" > &nbsp;&nbsp; <div style="display:inline-block">图标 <input type="text" name="ownPointFontIcon" id="ownPointFontIcon"  value="${ownPointFontIcon!''}" ><a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a></div>   <br/><br/>
            <span class="fieldName">大转盘背景图：</span>
            <#if backgroundPics?exists &&  (backgroundPics?size > 0)>
                <#list backgroundPics as pic>
                    <div class="backgroundPicsDiv"><input type="text" id="backgroundPicsId" class="backgroundPics" name="backgroundPics" value="${pic}"><a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> | <span class=" btn btn-success btn-xs" onclick=" return addLine(this, 'backgroundPics')">添加</span>  | <span class=" btn btn-danger btn-xs" onclick="return removeLine(this, 'backgroundPicsDiv')" >删除</span> <br/></div>
                </#list>
            <#else>
                <div class="backgroundPicsDiv"><input type="text" id="backgroundPicsId" class="backgroundPics" name="backgroundPics" value=""><a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> | <span class=" btn btn-success btn-xs" onclick=" return addLine(this, 'backgroundPics')">添加</span>  | <span class=" btn btn-danger btn-xs" onclick="return removeLine(this, 'backgroundPicsDiv')" >删除</span> <br/></div>
            </#if>
            <span class="fieldName">转盘轮廓图：</span><input type="text" id="outlinePic" name="outlinePic" value="<#if outlinePic?exists>${outlinePic}</#if>"> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> <br/><br/>
            <span class="fieldName">转盘图片：</span><input type="text" id="turntablePic" name="turntablePic" value="<#if turntablePic?exists>${turntablePic} </#if>"> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
            坐标： X<input type="number" id="turntableCoordinateX" name="turntableCoordinateX" value="<#if turntableCoordinateX?exists>${turntableCoordinateX}</#if>">
            Y <input type="number" id="turntableCoordinateY" name="turntableCoordinateY" value="<#if turntableCoordinateY?exists>${turntableCoordinateY}</#if>">  <br/><br/>
            <span class="fieldName">指针图片：</span><input type="text" id="pointerPic" name="pointerPic" value="<#if pointerPic?exists>${pointerPic}</#if>"> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
            坐标： X <input type="number" id="pointerCoordinateX" name="pointerCoordinateX" value="<#if pointerCoordinateX?exists>${pointerCoordinateX}</#if>">
            Y <input type="number" id="pointerCoordinateY" name="pointerCoordinateY" value="<#if pointerCoordinateY?exists>${pointerCoordinateY}</#if>">
            <hr/>
            <br/>
            <legend>弹窗信息</legend>
            <span class="fieldName">弹窗背景图：</span><input id="dialogBackgroundImg"  name="dialogBackgroundImg"  value="${ dialogBackgroundImg!''}"/> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> &nbsp;&nbsp;&nbsp;&nbsp;
            <span class="fieldName">按钮背景色：</span><input type="text" id="dialogBackgroundColor" name="dialogBackgroundColor" value="${dialogBackgroundColor!''}" />&nbsp;&nbsp;&nbsp;&nbsp;
            <span class="fieldName">按钮字体颜色：</span><input type="text" id="dialogFontColor" name="dialogFontColor" value="${dialogFontColor!''}" />&nbsp;&nbsp;&nbsp;&nbsp;
            <hr/>
            <br/>

            <legend>抽奖次数不足限制</legend>
            <span class="fieldName">弹窗背景图：</span><input id="timesDialogBackgroundImg"  name="timesDialogBackgroundImg"  value="${ timesDialogBackgroundImg!''}"/> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> &nbsp;&nbsp;&nbsp;&nbsp;
            <span class="fieldName">小图片：</span><input id="timesDialogSmallImg"  name="timesDialogSmallImg"  value="${ timesDialogSmallImg!''}"/> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> &nbsp;&nbsp;&nbsp;&nbsp;
            <br/><span class="fieldName">随机提示文案：</span>
            <#if timesDialogTips?exists &&  (timesDialogTips?size > 0)>
                <#list timesDialogTips as tip>
                    <div class="timesDialogTipsDiv"><input type="text"  class="timesDialogTips" name="timesDialogTips" value="${tip}"><span class=" btn btn-success btn-xs" onclick=" return addLine(this, 'timesDialogTips')">添加</span>  | <span class=" btn btn-danger btn-xs" onclick="return removeLine(this, 'timesDialogTipsDiv')" >删除</span> <br/></div>
                </#list>
            <#else>
                <div class="timesDialogTipsDiv"><input type="text" class="timesDialogTips" name="timesDialogTips" value=""> <span class=" btn btn-success btn-xs" onclick=" return addLine(this, 'timesDialogTips')">添加</span>  | <span class=" btn btn-danger btn-xs" onclick="return removeLine(this, 'timesDialogTipsDiv')" >删除</span> <br/></div>
            </#if>
            <hr/>
            <br/>

            <legend>积分不足限制</legend>
            <span class="fieldName">弹窗背景图：</span><input id="pointDialogBackgroundImg"  name="pointDialogBackgroundImg"  value="${ pointDialogBackgroundImg!''}"/> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> &nbsp;&nbsp;&nbsp;&nbsp;
            <span class="fieldName">小图片：</span><input id="pointDialogSmallImg"  name="pointDialogSmallImg"  value="${ pointDialogSmallImg!''}"/> <a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a> &nbsp;&nbsp;&nbsp;&nbsp;
            <br/><span class="fieldName">随机提示文案：</span>
            <#if pointDialogTips?exists &&  (pointDialogTips?size > 0)>
                <#list pointDialogTips as tip>
                    <div class="pointDialogTipsDiv"><input type="text"  class="pointDialogTips" name="pointDialogTips" value="${tip}"><span class=" btn btn-success btn-xs" onclick=" return addLine(this, 'pointDialogTips')">添加</span>  | <span class=" btn btn-danger btn-xs" onclick="return removeLine(this, 'pointDialogTipsDiv')" >删除</span> <br/></div>
                </#list>
            <#else>
                <div class="pointDialogTipsDiv"><input type="text" class="pointDialogTips" name="pointDialogTips" value=""> <span class=" btn btn-success btn-xs" onclick=" return addLine(this, 'pointDialogTips')">添加</span>  | <span class=" btn btn-danger btn-xs" onclick="return removeLine(this, 'pointDialogTipsDiv')" >删除</span> <br/></div>
            </#if>
            <hr/>
            <br/>
            <input style="width: 150px" type="button" id="saveButton" class="btn btn-primary" value="保存"/>

        </fieldset>
    </form>
            <legend>关联商品</legend>
            <div data-options="region:'center'" >
                <!--数据表格-->
                <table id="s_data" style=""></table>

            </div>
            </hr>


</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/>    <br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>


<div id="pointPrizeDiv" class="easyui-dialog" icon="icon-save" align="left"style="padding: 35px; width: 520px; height: 500px;">
    <form id="pointPrizeForm" method="post">
        <br/>
        <input type="hidden" value="${fortuneWheelId!0}" id="fortuneWheelId" name="fortuneWheelId" />
        <input type="hidden" value="" id="prizeId" name="id" />
        <span class="pointSpan">类型：</span>
        <div style="display:inline-block" id="pointType0Div" ><input type="radio" class="pointType" name="type" id="pointType0" value="0"    />谢谢惠顾</div>
        <div style="display:inline-block" id="pointType1Div"><input type="radio" class="pointType" name="type" id="pointType1" value="1"   />固定&nbsp;&nbsp;&nbsp;&nbsp;</div>
        <div style="display:inline-block" id="pointType2Div"><input type="radio" class="pointType" name="type" id="pointType2" value="2"  />随机</div>
        <div style="display:inline-block" id="pointType3Div"><input type="radio" class="pointType" name="type" id="pointType3" value="3"  />优惠券</div>
        <br/>
        <span class="pointSpan">是否可用：</span><input type="radio" class="pointPrizeStatus" name="isAvailable" id="pointIsAvailable0" value="0" />不可用
        &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" class="pointPrizeStatus" name="isAvailable" id="pointIsAvailable1" value="1"  />可用
        <br/><br/>
        <div id="couponPrizeDiv" ><span class="pointSpan">优惠券ID：</span><input type="number" id="couponPrizeID" name="couponId" /> </div>
        <div id="fixPoint" style="display:none"> <span class="pointSpan">积分数量：</span><input type="number" id="pointAmount" name="pointAmount" /></div>
        <div id="randomPoint" style="display:none"><span class="pointSpan">积分数量: </span><input type="number" id="minPoint" name="minPoint" />
            &nbsp;到&nbsp; <input type="number" id="maxPoint" name="maxPoint"></div>
        </br>
        <div ><span class="pointSpan">奖品总数：</span> <input type="number" id="prizeAmount" name="prizeAmount" /> </div><br/>
        <div ><span class="pointSpan">中奖概率：</span> <input type="number" id="pointProbability" name="probability" /> </div><br/>
        <div ><span class="pointSpan">顺序：</span> <input type="number" id="pointSequence" name="sequence" /> </div><br/>
        <div ><span class="pointSpan">中奖提示：</span> <input type="text" id="pointTip" name="tip" /> </div><br/>
        <div ><span class="pointSpan">中奖图片：</span> <div style="display:inline-block" ><input type="text" id="pointPic" class="pic" name="pic" value="">&nbsp;&nbsp;<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>  <br/></div></div>
        <div ><span class="pointSpan">字体颜色：</span> <input type="text" id="pFontColor" name="fontColor" /> </div><br/>
    </form>
</div>


<script type="text/javascript">

    function addLine(obj , inputId){
        var tmp = $(obj).parent().clone();
        tmp.find("."+inputId).val("");
        $(obj).parent().after(tmp);
        return false;
    }

    function removeLine(obj, divId) {
        if($("."+divId).size() > 1){
            $(obj).parent().remove();
        }
        return false;
    }

//    function addTopPic(obj){
//        var tmp = $(obj).parent().clone();
//        tmp.find(".topPics").val("");
//        $(obj).parent().after(tmp);
//        return false;
//    }

//    function removeTopPic(obj){
//        if($(".topPicsDiv").size() > 1){
//            $(obj).parent().remove();
//        }
//        return false;
//    }

//    function addBottomPic(obj){
//        var tmp = $(obj).parent().clone();
//        tmp.find(".bottomPics").val("");
//        $(obj).parent().after(tmp);
//        return false;
//    }

//    function removeBottomPic(obj){
//        if($(".bottomPicsDiv").size() > 1){
//            $(obj).parent().remove();
//        }
//        return false;
//    }

//    function addBackgroundPic(obj){
//        var tmp = $(obj).parent().clone();
//        tmp.find(".backgroundPics").val("");
//        $(obj).parent().after(tmp);
//        return false;
//    }

//    function addTimesDialogTip(obj){
//        var tmp = $(obj).parent().clone();
//        tmp.find(".timesDialogTips").val("");
//        $(obj).parent().after(tmp);
//        return false;
//    }

//    function removeBackgroundPic(obj){
//        if($(".backgroundPicsDiv").size() > 1){
//            $(obj).parent().remove();
//        }
//        return false;
//    }

//    function removeTimesDialogTip(obj) {
//        if($(".timesDialogTipsDiv").size() > 1){
//            $(obj).parent().remove();
//        }
//        return false;
//    }

    $(".pointType").click(function(){
       if($(this).val() == 1){
           $("#randomPoint").hide();
            $("#fixPoint").show();
       }
       if($(this).val() == 2){
            $("#fixPoint").hide();
           $("#randomPoint").show();
       }
    });

    function cleanPrizeAddDiv(){
        $("#prizeId").val('');
        $(".pointType:checked").prop("checked", false);
        $("#pointType0Div").hide();
        $("#pointType1Div").hide();
        $("#pointType2Div").hide();
        $("#pointType3Div").hide();
        $("#couponPrizeDiv").hide();
        $("#randomPoint").hide();
        $("#fixPoint").hide();
//        $("#pointIsAvailable1").attr("checked",false);
//        $("#pointIsAvailable0").attr("checked",false);
        $(".pointPrizeStatus:checked").prop("checked",false);
        $("#couponPrizeID").val('');
        $("#pointAmount").val('');
        $("#minPoint").val('');
        $("#maxPoint").val('');
        $("#prizeAmount").val('');
        $("#pointProbability").val('');
        $("#pointSequence").val('');
        $("#pointTip").val('');
        $("#pointPic").val('');
        $("#pFontColor").val('');
    }

    function cleanPointAddDiv(){
        cleanPrizeAddDiv();
        $("#pointType1Div").show();
        $("#pointType2Div").show();
        $("#pointType1").prop("checked", true);
        $("#pointIsAvailable1").prop("checked", true);
        $("#randomPoint").hide();
        $("#fixPoint").show();
    }

    function cleanXIEXIEAddDiv(){
        cleanPrizeAddDiv();
        $("#pointType0Div").show();
        $("#pointType0").prop("checked", true);
        $("#pointIsAvailable1").prop("checked", true);
    }

    function cleanCouponAddDiv(){
        cleanPrizeAddDiv();
        $("#pointType3Div").show();
        $("#couponPrizeDiv").show();
        $("#pointType3").prop("checked", true);
        $("#pointIsAvailable1").prop("checked", true);
    }

    function checkAddPrize() {
        var fortuneWheelId =  $("#fortuneWheelId").val();
        var type = $(".pointType:checked").val();
        var couponId = $("#couponPrizeID").val();
        var minPoint = $("#minPoint").val();
        var maxPoint = $("#maxPoint").val();
        var pointAmount = $("#pointAmount").val();
        var prizeAmount = $("#prizeAmount").val();
        var probability = $("#pointProbability").val();
        var isAvailable = $(".pointPrizeStatus:checked").val();
        var tip = $("#pointTip").val();
        var pic = $("#pointPic").val();
        var fontColor =  $("#pFontColor").val();
        var sequence =  $("#pointSequence").val();
        if (prizeAmount == '' || Number(prizeAmount) < 0) {
            $.messager.alert('info', "奖品总数必填大于等于0",'info'); return false;
        }
        if (probability == '' || Number(probability) < 0) {
            $.messager.alert('info', "中奖概率必填大于等于0",'info'); return false;
        }
        if (isAvailable == '') {
            $.messager.alert('info', "是否可用必选",'info'); return false;
        }
        if (tip == '') {
            $.messager.alert('info', "提示必填",'info'); return false;
        }
        if (pic == '') {
            $.messager.alert('info', "中奖图片必填",'info'); return false;
        }
        if (fontColor == '') {
            $.messager.alert('info', "中奖字体颜色必填",'info'); return false;
        }
        if (sequence =='' || Number(sequence) < 0) {
            $.messager.alert('info', "排序必填 大于0",'info'); return false;
        }


        if(type == 0) { //谢谢 惠顾
        } else if( type == 1) { //固定积分
            if (pointAmount == '' || Number(pointAmount) < 0){
                    $.messager.alert('info', "固定积分必填大于0",'info'); return false;
            }
        } else if( type == 2) { // 随机积分
                if(minPoint == '' || Number(minPoint) < 0){
                    $.messager.alert('info', "随机最低积分必填大于0",'info'); return false;
                }
                if(maxPoint == '' || Number(maxPoint) < 0){
                    $.messager.alert('info', "随机最高积分必填大于0",'info'); return false;
                }
                if (Number(minPoint) > Number(maxPoint)) {
                    $.messager.alert('info', "随机最低积分必须小于最高积分",'info'); return false;
                }
        } else if( type == 3) { // 优惠券
            if (couponId == ''){
                $.messager.alert('info', "优惠券Id必填",'info'); return false;
            }
        } else {
                 $.messager.alert('info', "奖品类型未知",'info'); return false;
        }
        return true;
    }

    function editPrize(index) {
        var arr = $("#s_data").datagrid("getData");
        var row = arr.rows[index];
        var id = row.id;
        var type = row.type;
        var isAvailable = row.isAvailable;
        if (type == 0) {
            cleanXIEXIEAddDiv();
        } else if (type == 1 || type == 2) {
            cleanPointAddDiv();
            if (type == 1){
                $("#randomPoint").hide();
                $("#fixPoint").show();
            } else {
                $("#fixPoint").hide();
                $("#randomPoint").show();
            }
        } else if (type == 3) {
            cleanCouponAddDiv();
        } else {
            return;
        }
        $("#prizeId").val(id);
        $("#couponPrizeID").val(row.couponId);
        $("#minPoint").val(row.minPoint);
        $("#maxPoint").val(row.maxPoint);
        $("#pointAmount").val(row.pointAmount);
        $("#prizeAmount").val(row.prizeAmount);
        $("#pointProbability").val(row.probability);
        $("#pointTip").val(row.tip);
        $("#pointPic").val(row.pic);
        $("#pFontColor").val(row.fontColor);
        $("#pointSequence").val(row.sequence);
        $('.pointType[value='+type+']').prop("checked", true);
        $('.pointPrizeStatus[value='+isAvailable+']').prop("checked",true);
        $("#pointPrizeDiv").dialog("open");
    }

    function changePrizeAvailable(id, status){
        var a ='不可用';
        if(status == 1){
            a = "可用";
        }
        $.messager.confirm("确认", "确定"+a+"吗?", function (r) {
            if (r) {
                $.ajax({
                    url: '${rc.contextPath}/fortuneWheel/updatePrizeAvailable',
                    data:{
                       id: id,
                        isAvailable : status
                    },
                    type:'post',
                    success: function(data) {
                        $.messager.progress('close');
                        if (data.status == 1) {
                            $.messager.alert("提示", "成功", "info");
                            $("#s_data").datagrid("reload");
                        } else {
                            $.messager.alert("提示", data.msg, "error");
                        }
                    },
                    error:function(data, textStatus){
                        $.messager.progress('close');
                    }
                });
            }
        });
    }

    $(function() {

        $('#picDia').dialog({
            title: '又拍图片上传窗口',
            collapsible: true,
            closed: true,
            modal: true,
            draggable:true
        });
        $('#pointPrizeDiv').dialog({
            title: '添加积分',
            collapsible: true,
            closed: true,
            modal: true,
            draggable:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $('#pointPrizeForm').form('submit',{
                        url:"${rc.contextPath}/fortuneWheel/savePrize",
                        onSubmit: function(){
                            return checkAddPrize();
                        },
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                cleanPrizeAddDiv();
                                $('#pointPrizeDiv').dialog('close');
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                    $('#s_data').datagrid('reload');
                                });
                            } else{
                                $.messager.alert('响应信息',data.message,'error',function(){
                                });
                            }
                        }
                    })
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    cleanPrizeAddDiv();
                    $('#pointPrizeDiv').dialog('close');
                }
            }]
        });


        $("#saveButton").click(function(){
            var id =$("#id").val();
            var name = $.trim($("#name").val());
            var isAvailable =$(".isAvailable:checked").val();
            var startTime =$("#startTime").val();
            var endTime =$("#endTime").val();
            var shareImage =$.trim($("#shareImage").val());
            var shareMainTitle =$.trim($("#shareMainTitle").val());
            var shareSecondTitle =$.trim($("#shareSecondTitle").val());
            var backgroundColor =$.trim($("#backgroundColor").val());
            var type =Number($(".type:checked").val());
            var pointUsed =$("#pointUsed").val();
            var pointFontColor =$.trim($("#pointFontColor").val());
            var pointIcon =$.trim($("#pointIcon").val());
            var timesLimit = $("#timesLimit").val();
            var timesFontColor =$.trim($("#timesFontColor").val());
            var timesIcon =$.trim($("#timesIcon").val());
            var ownPointFontColor =$.trim($("#ownPointFontColor").val());
            var ownPointFontIcon =$.trim($("#ownPointFontIcon").val());

            var topPics=[];
            $(".topPics").each(function(){
                var v = $.trim($(this).val());
                if( v != '') {
                    topPics.push(v);
                }
            });
            var bottomPics = [];
            $(".bottomPics").each(function(){
                var v = $.trim($(this).val());
                if( v != '') {
                    bottomPics.push(v);
                }
            });
            var backgroundPics =[];
            $(".backgroundPics").each(function(){
                var v = $.trim($(this).val());
                if( v != '') {
                    backgroundPics.push(v);
                }
            });

            var outlinePic =$("#outlinePic").val();
            var turntablePic =$("#turntablePic").val();
            var pointerPic =$("#pointerPic").val();
            var turntableCoordinateX =$("#turntableCoordinateX").val();
            var turntableCoordinateY =$("#turntableCoordinateY").val();
            var pointerCoordinateX =$("#pointerCoordinateX").val();
            var pointerCoordinateY =$("#pointerCoordinateY").val();

            if(name == '') {
                $.messager.alert("提示","活动名称必填","info"); return;
            } else if (startTime == '' || endTime == ''){
                $.messager.alert("提示","活动时间必填","info"); return;
            } else if(backgroundColor == ''){
                $.messager.alert("提示","背景色必填","info"); return;
            } else  if(isAvailable == ''){
                $.messager.alert("提示","状态必填","info"); return;
            } else if(type != 1 && type != 2) {
                $.messager.alert("提示","大转盘类型必填","info"); return;
            }  else if(backgroundPics.length == 0) {
                    $.messager.alert("提示","大转盘背景必填","info"); return;
            } else if(outlinePic ==''){
                    $.messager.alert("提示","轮廓盘背景必填","info");return;
            } else  if( turntablePic == '') {
                    $.messager.alert("提示","转盘图必填","info");return;
            } else if(pointerPic == ''){
                    $.messager.alert("提示","指针图必填","info");return;
            } else if( turntableCoordinateX == '' || turntableCoordinateY== '' ){
                    $.messager.alert("提示","转盘坐标必填","info");return;
            } else if(pointerCoordinateX == '' || pointerCoordinateY == '' ){
                    $.messager.alert("提示","指针坐标必填","info");return;
            }
            if(type ==1 ) {
                if(pointUsed == '' || Number(pointUsed) <= 0) {
                    $.messager.alert("提示","每次使用积分大于0必填","info");return ;
                }
                if(pointFontColor == ''){
                    $.messager.alert("提示","扣除积分字体颜色必填","info");return ;
                }
                if (pointIcon == ''){
                    $.messager.alert("提示","扣除积分图标必填","info");return ;
                }
                if(ownPointFontColor == ''){
                    $.messager.alert("提示","现有积分字体颜色必填","info");return ;
                }
                if (ownPointFontIcon == ''){
                    $.messager.alert("提示","现有积分图标必填","info");return ;
                }
            }
            if (type == 2){
                if (timesLimit == '' ){
                    $.messager.alert("提示","每天抽奖次数必填","info");return ;
                }
                if(timesFontColor == ''){
                    $.messager.alert("提示","抽奖次数字体颜色必填","info");return ;
                }
                if(timesIcon == '') {
                    $.messager.alert("提示","抽奖次数图标必填","info");return ;
                }
            }
            $.ajax({
                url: '${rc.contextPath}/fortuneWheel/editOrSave',
                data:($("#saveFortuneWheel").serialize()),
                type:'post',
                success: function(data) {
                    $.messager.progress('close');
                    if (data.status == 1) {
                        $.messager.alert("提示", "成功", "info");
                    } else {
                        $.messager.alert("提示", data.msg, "error");
                    }
                },
                error:function(data, textStatus){
                    $.messager.progress('close');
                }
            });
        });

        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/fortuneWheel/prizeJsonInfo',
            loadMsg:'正在装载数据...',
            queryParams:{
                fortuneWheelId: ${fortuneWheelId!0}
            },
            fitColumns:true,
            remoteSort: true,
            columns:[[
                {field:'id',    title:'序号', width:15, align:'center'},
                {field:'hidden1',    title:'关联商品', width:65, align:'center',
                    formatter: function (value, row, index) {
                        return  row.map.info;
                    }
                },
                {field:'prizeAmount',    title:'商品数量', width:25, align:'center'},
                {field:'probability',    title:'中奖概率', width:25, align:'center'},
                {field:'pic',     title:'中奖图片',  width:40,  align:'center',
                    formatter: function (value, row, index) {
                        return  row.map.picImg;
                    }
                },
                {field:'tip',     title:'中奖提示',  width:40,   align:'center'},
                {field:'fontColor',     title:'字体颜色',  width:25,   align:'center'},
                {field:'sequence',     title:'排序',  width:25,   align:'center'},
                {field:'isAvailable',     title:'状态',  width:25,   align:'center',
                    formatter: function (value, row, index) {
                        return  value == 0 ? "不可用" : "可用";
                    }
                },
                {field: 'hidden', title: '操作', width: 40, align: 'center',
                    formatter: function (value, row, index) {
                        var v1 = '<a href="javascript:;" onclick="editPrize(' + index + ')">编辑</a>';
                        var v3 = '';
                        if (row.isAvailable == 1) {
                            v3 = ' | <a href="javascript:;" onclick="changePrizeAvailable(' + row.id + ', 0)">停用</a>';
                        } else {
                            v3 = '| <a href="javascript:;" onclick="changePrizeAvailable(' + row.id + ', 1)">启用</a>';
                        }
                        return v1 + v3 ;
                    }
                }
            ]],
            toolbar:[{
                id:'_add1',
                text:'新增优惠券',
                iconCls:'icon-add',
                handler:function(){
                    cleanCouponAddDiv();
                    $("#pointPrizeDiv").dialog("open");

                }},
                {
                id:'_add2',
                text:'新增积分',
                iconCls:'icon-add',
                handler:function(){
                    cleanPointAddDiv();
                    $("#pointPrizeDiv").dialog("open");
                }},
                {
                id:'_add3',
                text:'新增未中奖',
                iconCls:'icon-add',
                handler:function(){
                    cleanXIEXIEAddDiv();
                    $("#pointPrizeDiv").dialog("open");
                }},
                {
                    id:'_refresh',
                    text:'刷新',
                    iconCls:'icon-refresh',
                    handler:function(){
                        $('#s_data').datagrid('reload');
                    }},
            ],
            pagination:false,
            onLoadSuccess:function(){
                $("#s_data").datagrid('clearSelections');
            }
        });

    });


    var $obj;
    function picDialogOpen(obj) {
        $obj = $(obj).prev();
        $("#picDia").dialog("open");
        $("#yun_div").css('display','none');
    }
    function picDialogClose() {
        $("#picDia").dialog("close");
    }
    function picUpload() {
        $('#picForm').form('submit',{
            url:"${rc.contextPath}/pic/fileUpLoad",
            success:function(data){
                var res = eval("("+data+")")
                if(res.status == 1){
                    $.messager.alert('响应信息',"上传成功...",'info',function(){
                        $("#picDia").dialog("close");
                        if($obj != null && $obj!='' && $obj !=undefined) {
                            $obj.val(res.url);
                            $("#picFile").val("");
                        }
                        return
                    });
                } else{
                    $.messager.alert('响应信息',res.msg,'error',function(){
                        return ;
                    });
                }
            }
        })
    }

</script>


</body>
</html>