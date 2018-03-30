(function($){
    var MyMenu = function(ele,opt){
        this.$element = ele,
            this.defaults = {
                'url':''
            },
            this.options = $.extend({},this.defaults,opt);
    }

    MyMenu.prototype={
        compositeMenuTree:function (menuItems,pElem) {
            var ulBox = $('ul',pElem);
            for(var i=0,j=menuItems.length;i<j;i++){
                var menuItem = menuItems[i];
                if(menuItem.hasChild){
                    var _liItem = $('<li class="dropdown"><a href="'+menuItem.url+'" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-fw fa-folder"></i> '+menuItem.name+' <span class="caret"></span></a><ul class="dropdown-menu" role="menu"></ul></li>');
                    this.compositeMenuTree(menuItem.children,_liItem);
                    ulBox.append(_liItem);
                }else{
                    var _liItem = $('<li><a href="'+menuItem.url+'"><i class="fa fa-fw fa-file"></i>'+menuItem.name+'</a></li>');
                    ulBox.append(_liItem);
                }
            }
        },
        load: function(){
            var _self = this;
            $.ajax({
               url:this.options.url,
               dataType:'json',
                type:'GET',
                contentType:'application/json; charset=utf-8',
                success:function(data){
                   var menuItems = JSON.stringify(data);
                   console.log(menuItems);
                   _self.compositeMenuTree(data,$(_self.$element));
                }
            });
        }
    }

    $.fn.mymenu=function(options){
        var menu = new MyMenu(this,options);
        menu.load();
    }
})(jQuery);