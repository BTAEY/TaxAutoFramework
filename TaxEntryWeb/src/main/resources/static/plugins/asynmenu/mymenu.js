(function($){
    $.addMenu = function(t,p){
        if(t.menu)
            return false; //return if already exist menu
        p = $.extend({
            url:false,
            target:'main',
            dataType:'json',
            method:'POST',
            params:[],
            userId:0,            //get all modules by userId
            initParentId:0,      //get first level module by initParentId
            parentId:0,			// filter modules by parentId
            basepath:false,
            autoload:true
        },p);
        $(t).show();
        var g = {
            /**
             * populate menu data , get latest data
             */
            populate:function(){
                if(!p.url){
                    return false;
                }
                if($.browser.opera){ // browser compatible
                    $(t).css('visibility','hidden');
                }
//				alert(typeof p.userId);
//				alert(typeof p.parentId);
                if(typeof p.userId == "string"){
                    p.userId = parseInt(p.userId);
                }
                var param = [{
                    name:'userId',
                    value:p.userId
                }, {
                    name:'parentId',
                    value:p.initParentId
                }, {
                    name:'url',
                    value:p.url
                }];
                if(p.params.length){
                    for(var pi=0;pi < p.params.length;pi++){
                        param[param.length] = p.params[pi];
                    }
                }
                $.ajax({
                    type:p.method,
                    url:p.url,
                    data:param,
                    dataType:p.dataType,
                    beforeSend:function(){
                        $(g.busyDiv).show();
                    },
                    success: function(data){
                        $(g.busyDiv).hide();
                        g.addData(data);
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown){
                        try {
                            if (p.onError)
                                p.onError(XMLHttpRequest, textStatus, errorThrown);
                        }
                        catch (e) {
                        }
                    }
                });
            },
            addData:function(data){ //parse data
                if(p.dataType == 'json'){
                    var _ul = document.createElement('ul');
                    $(_ul).addClass('asynmenu_ul');
                    $.each(data,function(i,value){
                        var _this = $(this);
                        var _li = document.createElement('li');
                        $(_li).addClass('asynmenu_li');
                        var _collaspeIcon = document.createElement('span');
                        $(_collaspeIcon).addClass('collapse_close');
                        var _a = document.createElement('a');
                        $(_a).addClass('menuParent').attr({
                            href:p.basepath+value.url,
                            target:p.target
                        }).text(value.name);
                        $(_li).append(_a).append(_collaspeIcon);
                        $(_ul).append(_li);
                        if(value.url == ""){
                            $(_a).click(function(event){
                                event.preventDefault();
                            });
                            // click event for each li
                            $(_li).click(function(event){
                                var _this = $(this);
                                p.parentId = value.id;
                                if($('div',_this).length == 0){
                                    g.populateSubData(_this);
                                }else{
                                    if($(g.subDiv,_this).is(":visible")){
                                        $(g.subDiv,_this).hide();
                                        $('span',_this).removeClass('collapse_open').addClass('collapse_close');
                                    }else{
                                        $(g.subDiv,_this).show();
                                        $('span',_this).removeClass('collapse_close').addClass('collapse_open');
                                    }
                                }
                            });
                        }
                    });
                    $(g.gDiv).append(_ul);
                }
            },
            populateSubData: function(item){
                if($.browser.opera){ // browser compatible
                    $(t).css('visibility','hidden');
                }
                if(typeof p.userId == "string"){
                    p.userId = parseInt(p.userId);
                }
                var param = [{
                    name:'userId',
                    value:p.userId
                }, {
                    name:'parentId',
                    value:p.parentId
                }, {
                    name:'url',
                    value:p.url
                }];
                if(p.params.length){
                    for(var pi=0;pi < p.params.length;pi++){
                        param[param.length] = p.params[pi];
                    }
                }
                $.ajax({
                    type:p.method,
                    url:p.url,
                    data:param,
                    dataType:p.dataType,
                    success: function(data){
                        g.addSubData(data,item);
                        $(g.subDiv,item).show();
                        $('span',item).removeClass('collapse_close').addClass('collapse_open');
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown){
                        try {
                            if (p.onError)
                                p.onError(XMLHttpRequest, textStatus, errorThrown);
                        }
                        catch (e) {
                        }
                    }
                });
            },
            addSubData: function(data,item){
                if(p.dataType == 'json'){
                    var _ul = document.createElement('ul');
                    $(_ul).addClass('asynmenu_ul');
                    $.each(data,function(i,value){
                        var _this = $(this);
                        var _li = document.createElement('li');
//						$(_li).addClass('asynmenu_li');
                        var _a = document.createElement('a');
                        $(_a).addClass('menuParent').attr({
                            href:p.basepath+value.url,
                            target:p.target
                        }).text(value.name);
                        $(_li).append(_a);
                        $(_ul).append(_li);

                        // sub item click event, stop progagation
                        $(_li).click(function(event){
                            event.stopPropagation();
                        });
                        $(_a).click(function(event){
                            event.stopPropagation();
                        });
                    });
                    $(g.subDiv).append(_ul);
                    $(item).append(g.subDiv);
                }
            }
        };
        // start of init Divs
        g.gDiv = document.createElement('div');   // crate global container
        g.subDiv = document.createElement('div');//create chilren menu panel
        g.busyDiv = document.createElement('div');
        $(g.busyDiv).addClass("waitingData");
        $(t).append(g.busyDiv);
        $(g.gDiv).addClass('asynmenu');
        // end of init Divs
        t.p = p;
        t.menu = g;
        // append gDiv to menu object
        $(t).append(g.gDiv);
//		$(g.gDiv).append(t);

        if(p.url && p.autoload){
            g.populate();
        }
        return t;
    };// end of $.addMenu

    var docloaded = false;
    $(document).ready(function(){
        docloaded = true;
    });
    $.fn.menulist = function(p){
        return this.each(function(){
            if(!docloaded){
                var t = this;
                $(document).ready(function(){
                    $.addMenu(t,p);
                });
            }else{
                $.addMenu(this,p);
            }
        });
    }; // end of $.fn.menulist

    $.fn.menuReload = function(p){
        return this.each(function(){
            if(this.menu && this.p.url){
                this.menu.populate();
            }
        });
    }; // end of $.fn.menuReload
})(jQuery);