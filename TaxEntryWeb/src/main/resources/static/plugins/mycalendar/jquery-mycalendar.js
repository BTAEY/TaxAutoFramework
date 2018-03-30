(function($){
    var MyCalendar = function(ele,opt){
        this.$element = ele,
            this.defaults = {
                'color':'#6ac13c',
                'async':false,
                'url':'',
                'rootpath':''
            },
            this.options = $.extend({},this.defaults,opt)
    }

    var my_date = new Date();

    MyCalendar.prototype={
        my_date : my_date,
        my_year : my_date.getFullYear(),
        my_month : my_date.getMonth(),
        my_day : my_date.getDate(),
        month_olympic : [31,29,31,30,31,30,31,31,30,31,30,31],
        month_normal : [31,28,31,30,31,30,31,31,30,31,30,31],
        month_name : ["January","Febrary","March","April","May","June","July","Auguest","September","October","November","December"],
        init: function(){
            $(this.$element).addClass('mycalendar');
            this.$element.append(this.buildCalHeader());
            this.$element.append(this.buildCalBody());
            this.loadingImg = document.createElement('img');
            $(this.loadingImg).addClass('loading').attr('src',this.options.rootpath+'/plugins/mycalendar/images/ajax_loader_1.gif').css('display','none');
            this.$element.append(this.loadingImg);
            $(document.body).append(this.buildModal());
            var _self = this;
            $("#prev").click(function(evt){
                evt.preventDefault();
                _self.prev();
            });
            $("#next").click(function(evt){
                evt.preventDefault();
                _self.next();
            });
        },
        buildCalHeader: function(){
            var titleDiv = "<div class='title'>"+
                "<h1 style='color:"+this.options.color+"' id='calendar-title'>Month</h1>"+
                "<h2 style='color:"+this.options.color+"' id='calendar-year'>Year</h2>"+
                "<a href='javascript:void(0);' title='prev' id='prev'></a>"+
                "<a href='javascript:void(0);' title='next' id='next'></a>"+
                "</div>";
            return titleDiv;
        },
        buildCalBody: function(){
            var bodyDiv = "<div class='mycalendar-body'>"+
                "<div class='lightgrey header-list clearfix'>"+
                "<ul>"+
                "<li>MON</li>"+
                "<li>TUE</li>"+
                "<li>WED</li>"+
                "<li>THU</li>"+
                "<li>FRI</li>"+
                "<li>SAT</li>"+
                "<li>SUN</li>"+
                "</ul>"+
                "</div>"+
                "<div class='darkgrey body-list clearfix'>"+
                "<ul id='days'></ul>"+
                "</div></div>";
            return bodyDiv;
        },
        buildModal:function(){
            var modalDiv = "<div class='modal fade' id='calModal' tabindex='-1' role='dialog' aria-labelledby='calModalLabel'>"+
                "<div class='modal-dialog' role='document'>"+
                "<div class='modal-content'>"+
                "<div class='modal-header'>"+
                "<button type='button' class='close' data-dismiss='modal' aria-label='Close'><span aria-hidden='true'>&times;</span></button>"+
                "<h4 class='modal-title' id='calModalLabel'>Calendar Schedule Details</h4>"+
                "</div>"+
                "<div class='modal-body'></div>"+
                "<div class='modal-footer'>"+
                "<button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>"+
                "</div></div></div></div>";
            return modalDiv;
        },
        prev: function(){
            this.my_month--;
            if(this.my_month<0){
                this.my_year--;
                this.my_month=11;
            }
            this.showCalendar();
        },
        next: function(){
            this.my_month++;
            if(this.my_month>11){
                this.my_year++;
                this.my_month = 0;
            }
            this.showCalendar();
        },
        dayStart: function(month,year){
            var tmpDate = new Date(year,month,1);
            return tmpDate.getDay();
        },
        daysMonth: function(month,year){
            if(this.isLeapYear(year)){
                return (this.month_olympic[month]);
            }else{
                return (this.month_normal[month]);
            }
        },
        isLeapYear: function(year){
            if((year%4 == 0 && year%100 != 0) || (year%400 == 0)){
                return true;
            }
            return false;
        },
        showCalendar: function(){
            var _self = this;
            if(this.options.async){
                $.when(this.asyncLoad()).done(function(data){
                    _self.refreshDate(data);
                });
            }else{
                this.refreshDate();
            }
        },
        showCalendarDetail: function(triggerElem){
            var scheduleInfoList = triggerElem.data("schedule-list");
            if(this.checkNullObj(scheduleInfoList)){
                scheduleInfoList = {};
            }else{
                var tmpStr = JSON.stringify(scheduleInfoList);
                scheduleInfoList = JSON.parse(tmpStr);
            }

            $('#calModal').on('show.bs.modal',function(event){
                console.count(JSON.stringify(scheduleInfoList));
                var modal = $(this);
                var detailUl = document.createElement('ul');
                var ulObj = $(detailUl);
                ulObj.addClass('schedule-details-list');
                for(var i=0,l=scheduleInfoList.length;i<l;i++){
                    var detailLi = document.createElement('li');
                    var liObj = $(detailLi);
                    liObj.text(scheduleInfoList[i].info);
                    $(detailUl).append(liObj);
                }
                modal.find(".modal-body").append(ulObj);
                modal.off('show.bs.modal');
            });
            $('#calModal').on('hidden.bs.modal', function () {
                var modal = $(this);
                modal.data('bs.modal',null);
                modal.find(".modal-body").children().remove();
            });
            $('#calModal').modal({
                width:400,
                height:800
            });

        },
        refreshDate: function(){
            var _self = this;
            var schedule = this.checkNullObj(arguments[0]) ? {}: arguments[0];
            console.log("schedule info in refreshDate: "+JSON.stringify(schedule));
            var str = "";
            var totalDay = this.daysMonth(this.my_month,this.my_year);
            var firstDay = this.dayStart(this.my_month, this.my_year); //获取该月第一天是星期几
            var todayClass;
            for(var i=1;i<firstDay;i++){
                str += "<li></li>"; //为起始日之前的日期创建空白节点
            }
            for(var i=1;i<=totalDay;i++){
                var scheduleInfoList = schedule[i.toString()];
                if((i<this.my_day && this.my_year==this.my_date.getFullYear() && this.my_month==this.my_date.getMonth()) ||
                    this.my_year<this.my_date.getFullYear() ||
                    (this.my_year==this.my_date.getFullYear() && this.my_month<this.my_date.getMonth())){
                    //当该日期在今天之前时，以浅灰色字体显示
                    todayClass = " lightgrey";
                }else if(i==this.my_day && this.my_year==this.my_date.getFullYear() && this.my_month==this.my_date.getMonth()){
                    todayClass = " circle";//当天日期以绿色背景突出显示
                }else{
                    todayClass = " darkgrey";//当该日期在今天之后时，以深灰字体显示
                }
                var dayDivId = "day"+i;
                str += "<li><div id='"+dayDivId+"' data-schedule-list='"+JSON.stringify(scheduleInfoList)+"' class='showday "+todayClass+"'>"+i+"</div>"; // 创建日期节点
                $(document).on('click','#'+dayDivId,function(evt){
                    _self.showCalendarDetail($(this));
                });
                if(this.checkNullObj(scheduleInfoList)){
                    str += "</li>";
                }else{
                    str += "<div class='schedulePanel'>"
                    for(var j=0,k=scheduleInfoList.length;j<k;j++){
                        str += "<span class='scheduleItemPanel'>"+scheduleInfoList[j].info.slice(0,12)+"</span>";
                    }
                    str += "</div></li>";
                }
            }
            $("#days").html(str);
            $("#calendar-title").html(this.month_name[this.my_month]);
            $("#calendar-year").html(this.my_year);

        },
        checkNullObj: function(obj){
            if(typeof(obj) === "undefined"){
                return true;
            }
            return Object.keys(obj).length === 0;
        },
        asyncLoad: function(){
            var defer = $.Deferred();
            var _self = this;
            var forwardUrl = this.options.rootpath+"/"+this.options.url+"?year="+this.my_year+"&month="+(this.my_month+1);
            $.ajax({
                url:forwardUrl,
                dataType:'json',
                type:'get',
                contentType : 'application/json; charset=utf-8',
                beforeSend:function(){
                    $(_self.loadingImg).show();
                },
                success:function(data){
                    defer.resolve(data);
                    $(_self.loadingImg).hide();
                },
                error:function(errorInfo){
                    console.log("error: "+errorInfo);
                },
                statusCode:{
                    404: function(){
                        console.log("page not found");
                    }
                }
            });
            return defer.promise();
        }
    }

    $.fn.mycalendar=function(options){
        var calendar = new MyCalendar(this,options);
        calendar.init();
        calendar.showCalendar();
    }
})(jQuery);
