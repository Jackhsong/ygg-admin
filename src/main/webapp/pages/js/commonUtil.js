/* 定义一个全局变量 */
var sy = $.extend({}, sy);
/* 将form表单内的元素序列化为对象，扩展Jquery的一个方法 */
sy.serializeObject = function(form) {
	var o = {};
	$.each(form.serializeArray(), function(index) {
		if (o[this['name']]) {
			o[this['name']] = o[this['name']] + "," + this['value'];
		} else {
			o[this['name']] = this['value'];
		}
	});
	return o;
};



/**
 * 根据名称获取多个相同name的对象值
 */
function getValueByName(name){
    var returnVal = "";
    var allInputs = document.getElementsByName(name);
    for(var i=0; i<allInputs.length; i++){
    	returnVal = returnVal + allInputs[i].value + ";";
    }
    if(returnVal.endWith(";")){
        returnVal = returnVal.substring(0, returnVal.length - 1);
    }
    return returnVal;
}

function getValueByNameSep(name, sep){
    var returnVal = "";
    var allInputs = document.getElementsByName(name);
    for(var i=0; i<allInputs.length; i++){
    	returnVal = returnVal + allInputs[i].value + sep;
    }
    if(returnVal.endWith(sep)){
        returnVal = returnVal.substring(0, returnVal.length - sep.length);
    }
    return returnVal;
}

function formatterdate(val, row) {
	if (val != null) {
		var date = new Date(val);
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var day = date.getDate();
		var hour = date.getHours();
		var minutes = date.getMinutes();
		var seconds = date.getSeconds();
		if (month < 10)
			month = '0' + month;
		if (day < 10)
			day = '0' + day;
		if (hour < 10)
			hour = '0' + hour;
		if (minutes < 10)
			minutes = '0' + minutes;
		if (seconds < 10)
			seconds = '0' + seconds;
		return year + '-' + month + '-' + day + ' ' + hour + ':' + minutes
				+ ':' + seconds;
	}
}

function formatterdateYMD(val, row) {
	if (val != null) {
		var date = new Date(val);
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var day = date.getDate();
		if (month < 10)
			month = '0' + month;
		if (day < 10)
			day = '0' + day;
		return year + '-' + month + '-' + day;
	}
}

function yesNoStr(val, row) {
	if (val != null) {
		if(val == "0")
			return "否";
		else 
			return "是";
	}
}

function imgFormatter(value,row,index){
	if('' != value && null != value)
		value = '<img style="width:60px; height:60px" src="' + value + '">';
	return  value;
}

function formatYYMMDD(date){
	var y = date.getFullYear();  
    var m = date.getMonth()+1;  
    var d = date.getDate();  
    return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);  
}

//将日期类型转换成字符串型格式 yyyy-MM-dd hh:mm
function changeTimeToString(curDate){
	if(curDate == null || curDate.length == 0){
		return "";
	}
	if(curDate.length > 19){
		curDate = curDate.substr(0,19);
		var regEx = new RegExp("\\-","gi");
		curDate=curDate.replace(regEx,"/");
	}
	var DateIn = new Date(curDate);
    var Year = 0 ;
    var Month = 0 ;
    var Day = 0 ;
    var Hour = 0 ;
    var Minute = 0 ;
    var Second = 0 ;
    var CurrentDate = "" ;
    // 初始化时间
    Year       = DateIn.getYear() + 1900;
    Month      = DateIn.getMonth()+ 1 ;
    Day        = DateIn.getDate();
    Hour       = DateIn.getHours();
    Minute     = DateIn.getMinutes();
    Second     = DateIn.getSeconds();
    CurrentDate = Year + "-" ;
    if (Month >= 10) {
        CurrentDate = CurrentDate + Month + "-" ;
    } else {
        CurrentDate = CurrentDate + "0" + Month + "-" ;
    }

    if (Day >= 10){
        CurrentDate = CurrentDate + Day ;
    } else {
    	CurrentDate = CurrentDate + "0" + Day ;
    }
    if (Hour >= 10){
    	CurrentDate = CurrentDate + " " + Hour ;
    } else {
        CurrentDate = CurrentDate + " 0" + Hour ;
    }

    if (Minute >= 10){
        CurrentDate = CurrentDate + ":" + Minute ;
    } else {
        CurrentDate = CurrentDate + ":0" + Minute ;
    }
    if (Second >= 10){
        CurrentDate = CurrentDate + ":" + Second ;
    } else {
        CurrentDate = CurrentDate + ":0" + Second ;
    }
    return CurrentDate ;
}

function addTab(title, href){
	var jq = top.jQuery;    
	var content = '<iframe scrolling="auto" frameborder="0"  src="'+href+'" style="width:100%;height:100%;"></iframe>';  
    if (jq("#mainTabs").tabs('exists', title)){  
        jq("#mainTabs").tabs('close', title); 
    }  
    jq("#mainTabs").tabs('add',{    
        title:title,    
        content:content,    
        closable:true    
    });    
}



//日期加指定天数
function addDate(date, days) {
	var d = new Date(Date.parse(date));
	d.setDate(d.getDate() + days);
	var m = d.getMonth() + 1;
	var dd = d.getDate();
	if (m < 10) {
		m = "0" + m;
	}
	if (dd < 10) {
		dd = "0" + dd;
	}
	return d.getFullYear() + "-" + m + "-" + dd;
}

//为jquery扩展了这个方法了之后我们就可以通过如下方法来获取url某个参数的值了
(function($){
	$.getUrlParam
	 = function(name)
	{
	var reg
	 = new RegExp("(^|&)"+
	 name +"=([^&]*)(&|$)");
	var r
	 = window.location.search.substr(1).match(reg);
	if (r!=null) return unescape(r[2]); return null;
	}
	})(jQuery);